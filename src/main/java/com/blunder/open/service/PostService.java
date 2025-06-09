package com.blunder.open.service;


import com.blunder.open.entity.Post;
import com.blunder.open.entity.User;
import com.blunder.open.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Page<Post> listPosts(int page, int size) {
        return postRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }

    public Optional<Post> getPost(Integer id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Post post, User user) throws IllegalAccessException {
        Post existing = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new IllegalAccessException("No permission to update this post");
        }

        existing.setTitle(post.getTitle());
        existing.setContent(post.getContent());
        existing.setPgn(post.getPgn());
        return postRepository.save(existing);
    }

    public void deletePost(Integer id, User user) throws IllegalAccessException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalAccessException("No permission to delete this post");
        }

        postRepository.delete(post);
    }

    public Post findById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
    }

    
    public void incrementLikeCount(Integer postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }
    
    
    public List<Post> getTopLikedPostsWithinDays(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return postRepository.findTopPostsSince(since)
                             .stream()
                             .limit(10)
                             .toList();
    }

}
