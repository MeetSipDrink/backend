package com.meetsipdrink.board.service;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostComment;
import com.meetsipdrink.board.repository.PostCommentRepository;
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

    public PostCommentService(PostService postService,
                              MemberService memberService,
                              PostCommentRepository postCommentRepository, PostRepository postRepository) {
        this.postService = postService;
        this.memberService = memberService;
        this.postCommentRepository = postCommentRepository;
    }

    public PostComment createPostComment(PostComment postComment) throws IllegalArgumentException {
        Post post = postService.findVerifiedPost(postComment.getPost().getPostId());
        Member member = memberService.findVerifiedMember(postComment.getMember().getMemberId());

        postComment.setPost(post);
        postComment.setMember(member);

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

    @Transactional(readOnly = true)
    public List<PostComment> findPostComments() {
        return postCommentRepository.findAll();
    }

    public void deletePostComment(long postCommentId, long memberId) {
        PostComment findPostComment = findVerifiedPostComment(postCommentId);

        if (findPostComment.getMember().getMemberId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.BOARD_UNAUTHORIZED_ACTION);
        }

        postCommentRepository.delete(findPostComment);
    }

    public PostComment findVerifiedPostComment(long postCommentId) {
        Optional<PostComment> optionalPostComment = postCommentRepository.findById(postCommentId);
        PostComment findPostComment = optionalPostComment.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_COMMENT_NOT_FOUND));
        return findPostComment;
    }
}
