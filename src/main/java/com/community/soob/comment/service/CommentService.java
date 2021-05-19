package com.community.soob.comment.service;

import com.community.soob.account.domain.Account;
import com.community.soob.comment.domain.Comment;
import com.community.soob.comment.domain.CommentRepository;
import com.community.soob.comment.exception.CommentNotFoundException;
import com.community.soob.heart.service.HeartService;
import com.community.soob.post.domain.Post;
import com.community.soob.post.domain.PostRepository;
import com.community.soob.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final HeartService heartService;

    @Transactional
    public void saveComment(Account account, long postId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Comment comment = Comment.builder()
                .post(post)
                .author(account)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    public Page<Comment> getComments(long postId, Pageable pageable) {
        pageable = PageRequest.of((pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1), 10);
        return commentRepository.findAllByPostId(postId, pageable);
    }

    public Comment getComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Transactional
    public void updateComment(long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(long commentId) {
        Long heartCount = heartService.getHeartCountForComment(commentId);
        if (heartCount != null && heartCount != 0) {
            heartService.deleteAllHeartForComment(commentId);
        }
        commentRepository.deleteById(commentId);
    }

    public boolean isAuthorMatched(Account account, long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        return account.getId().equals(comment.getAuthor().getId());
    }

    public Long getCommentCountForPost(long postId) {
        return commentRepository.countByPostId(postId);
    }

    public void deleteAllCommentForPost(long postId) {
        commentRepository.deleteAllByPostId(postId);
    }
}
