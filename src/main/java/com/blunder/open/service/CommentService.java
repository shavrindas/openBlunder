package com.blunder.open.service;

import com.blunder.open.entity.Comment;
import com.blunder.open.entity.Post;
import com.blunder.open.entity.User;
import com.blunder.open.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsForPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }

    public void addComment(User user, Post post, String content) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);
        commentRepository.save(comment);
    }
    
    
    public void deleteComment(Integer commentId, User user) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalAccessException("You do not have permission to delete this comment.");
        }

        commentRepository.delete(comment);
    }
}
