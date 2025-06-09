package com.blunder.open.controller;

import com.blunder.open.entity.Post;
import com.blunder.open.entity.User;
import com.blunder.open.service.CommentService;
import com.blunder.open.service.PostService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/posts")
public class PostController {

    private static final int PAGE_SIZE = 50;

    @Autowired
    private PostService postService;
    
    @Autowired
    private CommentService commentService;
    

    // 게시글 리스트 - 최신순 페이지네이션
    @GetMapping
    public String listPosts(@RequestParam(defaultValue = "0") int page,
                            Model model,
                            HttpSession session) {
        Page<Post> postsPage = postService.listPosts(page, PAGE_SIZE);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("currentPage", page);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);  // 💡 여기가 중요

        return "Post/ViewPosts";
    }


    // 글 작성 폼
    @GetMapping("/create")
    public String showCreateForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";  // 로그인 필요
        }
        model.addAttribute("post", new Post());
        return "Post/CreatePost";
    }

    // 글 작성 처리
    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        post.setUser(user);
        postService.createPost(post);
        return "redirect:/posts";
    }

    // 글 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Post post = postService.getPost(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getUser().getId().equals(user.getId())) {
            return "redirect:/posts/" + id;  // 권한 없음
        }

        model.addAttribute("post", post);
        return "Post/EditPost";
    }

    // 글 수정 처리
    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable Integer id, @ModelAttribute Post post, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            post.setId(id);  // path variable의 id 적용
            postService.updatePost(post, user);
            return "redirect:/posts/" + id;
        } catch (IllegalAccessException e) {
            model.addAttribute("error", e.getMessage());
            return "Post/EditPost";
        }
    }

    // 글 삭제 처리
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            postService.deletePost(id, user);
        } catch (IllegalAccessException e) {
        }
        return "redirect:/posts";
    }
    
    
    @GetMapping("/posts/{id}/edit")
    public String showEditPostPage(@PathVariable Integer id, Model model, HttpSession session) {
    	User user = (User) session.getAttribute("user");
    	if (user == null) {
    	return "redirect:/login";
    	}
    	Post post = postService.findById(id);
    	if (!post.getUser().getId().equals(user.getId())) {
    	    return "redirect:/posts/" + id;
    	}

    	model.addAttribute("post", post);
    	return "Post/EditPost";
    }
    
    
    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // 로그인 필요
        }

        postService.incrementLikeCount(id);
        return "redirect:/posts/" + id;
    }


    @GetMapping("/{id}")
    public String viewPost(@PathVariable Integer id, Model model, HttpSession session) {
        Post post = postService.getPost(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getCommentsForPost(post));

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "Post/ViewPostDetail";
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Integer id,
                             @RequestParam String content,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Post post = postService.getPost(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        commentService.addComment(user, post, content);
        return "redirect:/posts/" + id;
    }

    
    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Integer postId,
                                @PathVariable Integer commentId,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            commentService.deleteComment(commentId, user);
        } catch (IllegalAccessException e) {
        }
        return "redirect:/posts/" + postId;
    }
    
}
