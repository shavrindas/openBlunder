package fighting.my.way.controller;

import fighting.my.way.entity.Post;
import fighting.my.way.entity.User;
import fighting.my.way.service.PostService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 목록 - 로그인한 사용자만 접근 가능
    @GetMapping
    public String listPosts(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("posts", postService.findAll());
        return "post/post_main";
    }

    // 상세
    @GetMapping("/{id}")
    public String postDetail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "post/post_detail";
    }

    // 작성 폼
    @GetMapping("/create")
    public String createForm(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        return "post/post_create";
    }

    // 작성 처리
    @PostMapping("/create")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        postService.createPost(title, content, user);
        return "redirect:/post";
    }

    // 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        // 권한 체크
        if (!post.getUser().getId().equals(user.getId())) {
            return "redirect:/post/" + id;
        }

        model.addAttribute("post", post);
        return "post/post_edit";
    }

    // 수정 처리
    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String content,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        // 권한 체크
        if (!post.getUser().getId().equals(user.getId())) {
            return "redirect:/post/" + id;
        }

        postService.updatePost(id, title, content);
        return "redirect:/post/" + id;
    }
}
