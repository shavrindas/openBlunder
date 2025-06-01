package fighting.my.way.controller;

import fighting.my.way.entity.User;
import fighting.my.way.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return user == null ? "login/index_guest" : "login/index_home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
    	return userService.login(username, password)
    		    .map(user -> {
    		        session.setAttribute("user", user);
    		        return "redirect:/";
    		    })
    		    .orElseGet(() -> {
    		        model.addAttribute("error", "로그인 실패");
    		        return "login/login";
    		    });
    }


    @GetMapping("/signup")
    public String signupForm() {
        return "login/signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email,
                         Model model) {
        boolean success = userService.register(username, password, email);
        if (!success) {
            model.addAttribute("error", "이미 존재하는 사용자입니다.");
            return "login/signup";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
