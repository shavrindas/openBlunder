package com.blunder.open.repository;

import com.blunder.open.entity.Comment;
import com.blunder.open.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}