package com.blunder.open.controller;

import com.blunder.open.entity.User;
import com.blunder.open.service.ChessRoomService;
import com.blunder.open.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChessRoomService roomService;
    
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "Basic/SignupPage";
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user, Model model) {
        boolean success = userService.registerUser(user);
        if (!success) {
            model.addAttribute("error", "Username or Email already exists.");
            return "Basic/SignupPage";
        }
        return "redirect:/guest";
    }
    

    @GetMapping("/login")
    public String showLoginForm() {
        return "Basic/LoginPage";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        User user = userService.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "Basic/LoginPage";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            roomService.clearRoomsForUser(userId);
        }
        session.invalidate();
        return "redirect:/";
    }
    


    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", userService.findById(user.getId()));
        return "PersonalProfile/ProfilePage";
    }

    @GetMapping("/edit")
    public String showEditProfileForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", userService.findById(user.getId()));
        model.addAttribute("profileImages", List.of("1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png","10.png"));
        return "PersonalProfile/EditProfilePage";
    }

    @PostMapping("/edit")
    public String handleEditProfile(@ModelAttribute User updatedUser, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        updatedUser.setId(sessionUser.getId());  // 아이디 고정
        userService.updateUserProfile(updatedUser);
        session.setAttribute("user", userService.findById(sessionUser.getId()));
        return "redirect:/profile";
    }

    
}
