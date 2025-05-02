package co.com.bancolombia.web;

import co.com.bancolombia.model.User;
import co.com.bancolombia.usecase.RegisterUserUseCase;
import co.com.bancolombia.web.security.model.RegisterUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterUserUseCase registerUserUseCase;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new RegisterUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterUserDto userDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                true
        );

        registerUserUseCase.register(user);
        return "redirect:/login?registered";
    }
}
