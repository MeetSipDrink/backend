package com.meetsipdrink.board.service;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostComment;
import com.meetsipdrink.board.repository.PostCommentRepository;
import com.meetsipdrink.board.repository.PostRepository;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.notification.service.FCMService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Transactional
@Service
public class PostCommentService {
    private final PostService postService;
    private final MemberService memberService;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final FCMService fcmService;

    public PostCommentService(PostService postService,
                              MemberService memberService,
                              PostCommentRepository postCommentRepository, PostRepository postRepository, FCMService fcmService) {
        this.postService = postService;
        this.memberService = memberService;
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
        this.fcmService = fcmService;
    }

    public PostComment createPostComment(PostComment postComment) throws IllegalArgumentException {
        Post post = postService.findVerifiedPost(postComment.getPost().getPostId());
        Member member = memberService.findMemberByEmail(postComment.getMember().getEmail());

        postComment.setPost(post);
        postComment.setMember(member);

        if (postComment.getParentComment() != null) {
            // 부모 댓글이 있는 경우, 부모 댓글을 검증하여 설정
            PostComment parentComment = postCommentRepository.findById(postComment.getParentComment().getPostCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

            // 부모 댓글이 동일한 게시물에 속하는지 확인
            if (parentComment.getPost().getPostId() != post.getPostId()) {
                throw new IllegalArgumentException("부모 댓글이 현재 게시물에 속하지 않습니다.");
            }

            postComment.setParentComment(parentComment);
            if (!postComment.getParentComment().getMember().getFcmToken().equals(postComment.getMember().getFcmToken())) {
                String fcmtoken = postComment.getParentComment().getMember().getFcmToken();
                String nickname = postComment.getMember().getNickname();
                fcmService.sendCommentNotification(fcmtoken, nickname, postComment.getContent());
            }
        } else {
            // 부모 댓글이 없을 경우, 최상위 댓글로 설정
            postComment.setParentComment(null);

            if (!postComment.getPost().getMember().getFcmToken().equals(postComment.getMember().getFcmToken())) {
                String fcmtoken = postComment.getPost().getMember().getFcmToken();
                String nickname = postComment.getMember().getNickname();
                fcmService.sendCommentNotification(fcmtoken, nickname, postComment.getContent());
            }
        }

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
        return postCommentRepository.save(postComment);
    }

    public PostComment updatePostComment(long postCommentId, PostComment postComment, String email) {
        PostComment findPostComment = findVerifiedPostComment(postCommentId);

        if (!Objects.equals(findPostComment.getMember().getEmail(), email)) {
            throw new BusinessLogicException(ExceptionCode.BOARD_UNAUTHORIZED_ACTION);
        }

        Optional.ofNullable(postComment.getContent())
                .ifPresent(content -> findPostComment.setContent(content));
        return postCommentRepository.save(findPostComment);
    }

    @Transactional(readOnly = true)
    public List<PostComment> findPostCommentsByPostId(long postId) {
        Post post = postService.findVerifiedPost(postId); // postId에 해당하는 Post 검증
        return postCommentRepository.findByPost(post); // PostCommentRepository에 해당하는 메서드 추가 필요
    }

    public void deletePostComment(long postCommentId, String email) {
        PostComment findPostComment = findVerifiedPostComment(postCommentId);

        if (!Objects.equals(findPostComment.getMember().getEmail(), email)) {
            throw new BusinessLogicException(ExceptionCode.BOARD_UNAUTHORIZED_ACTION);
        }

        Post post = findPostComment.getPost();
        int commentCountToRemove = countComments(findPostComment, new HashSet<Long>());
        postCommentRepository.delete(findPostComment);
        int newCommentCount = post.getCommentCount() - commentCountToRemove;
        post.setCommentCount(newCommentCount);
        postRepository.save(post);
    }

    public PostComment findVerifiedPostComment(long postCommentId) {
        Optional<PostComment> optionalPostComment = postCommentRepository.findById(postCommentId);
        PostComment findPostComment = optionalPostComment.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_COMMENT_NOT_FOUND));
        return findPostComment;
    }

    private int countComments(PostComment postComment, Set<Long> visited) {
        if (visited.contains(postComment.getPostCommentId())) {
            // 이미 방문한 댓글이면 카운트하지 않음
            return 0;
        }
        visited.add(postComment.getPostCommentId());

        int count = 1; // 현재 댓글 카운트
        for (PostComment reply : postComment.getReplies()) {
            count += countComments(reply, visited);
        }
        return count;
    }


}
