package com.meetsipdrink.board.service;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostComment;
import com.meetsipdrink.board.repository.PostCommentRepository;
import com.meetsipdrink.board.repository.PostRepository;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.service.MemberService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PostCommentService {
    private final PostService postService;
    private final MemberService memberService;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    public PostCommentService(PostService postService,
                              MemberService memberService,
                              PostCommentRepository postCommentRepository, PostRepository postRepository) {
        this.postService = postService;
        this.memberService = memberService;
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
    }

    public PostComment createPostComment(PostComment postComment) throws IllegalArgumentException {
        Post post = postService.findVerifiedPost(postComment.getPost().getPostId());
        Member member = memberService.findVerifiedMember(postComment.getMember().getMemberId());

        postComment.setPost(post);
        postComment.setMember(member);

        if (postComment.getParentComment() != null) {
            // 부모 댓글이 있는 경우, 부모 댓글을 검증하여 설정
            PostComment parentComment = postCommentRepository.findById(postComment.getParentComment().getPostCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
            postComment.setParentComment(parentComment);
        } else {
            // 부모 댓글이 없을 경우, 최상위 댓글로 설정
            postComment.setParentComment(null);
        }

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return postCommentRepository.save(postComment);
    }

    public PostComment updatePostComment(long postCommentId, PostComment postComment, long memberId) {
        PostComment findPostComment = findVerifiedPostComment(postCommentId);

        if (findPostComment.getMember().getMemberId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.BOARD_UNAUTHORIZED_ACTION);
        }

        Optional.ofNullable(postComment.getContent())
                .ifPresent(content -> findPostComment.setContent(content));
        return postCommentRepository.save(findPostComment);
    }

    public List<PostComment> findPostComments() {
        return postCommentRepository.findAll();
    }

    public void deletePostComment(long postCommentId, long memberId) {
        PostComment findPostComment = findVerifiedPostComment(postCommentId);

        if (findPostComment.getMember().getMemberId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.BOARD_UNAUTHORIZED_ACTION);
        }

        Post post = findPostComment.getPost();
        int commentCountToRemove = countComments(findPostComment);
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

    private int countComments(PostComment postComment) {
        // 댓글 본인 + 자식 댓글 수를 재귀적으로 계산
        int count = 1; // 본인 댓글을 1로 시작
        for (PostComment reply : postComment.getReplies()) {
            count += countComments(reply); // 자식 댓글들에 대해서도 재귀적으로 카운트
        }
        return count;
    }
}
