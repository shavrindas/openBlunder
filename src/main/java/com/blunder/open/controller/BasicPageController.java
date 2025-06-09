package com.blunder.open.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.blunder.open.entity.Post;
import com.blunder.open.entity.User;
import com.blunder.open.service.PostService;

import jakarta.servlet.http.HttpSession;



@Controller
public class BasicPageController {
	
	
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String showWelcomePage() {
        return "Basic/WelcomePage";  
    }


	@GetMapping("/guest")
	public String showGuestIndex(Model model) {
	    List<Post> topPosts = postService.getTopLikedPostsWithinDays(3);
	    model.addAttribute("topPosts", topPosts);
	    return "Basic/GuestIndex";
	}
	
	@GetMapping("/home")
	public String showHomeIndex(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) return "redirect:/login"; 
	    session.setAttribute("userId", user.getId());

	    List<Post> topPosts = postService.getTopLikedPostsWithinDays(3);
	    model.addAttribute("user", user);
	    model.addAttribute("topPosts", topPosts);
	    return "Basic/HomeIndex";
	}
	
	
    @GetMapping("/quiz")
    public String showQuizPage(Model model) {
        return "ChessQuiz/ChessQuizPage"; 
    }

    @GetMapping("/practice")
    public String showPracticePage(Model model) {
        return "OpenPractice/OpeningPracticePage"; 
    }

}
