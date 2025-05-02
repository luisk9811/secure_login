package co.com.bancolombia.web;

import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterUserUseCase registerUserUseCase;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        registerUserUseCase.register(user);
        model.addAttribute("registered", true);
        return "redirect:/login?registered";
    }
}
