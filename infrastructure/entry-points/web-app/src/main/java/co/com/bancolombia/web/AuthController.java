package co.com.bancolombia.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @Value("${reCAPTCHA.web-key}")
    private String recaptchaWebKey;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("captchaSiteKey", recaptchaWebKey);
        return "login";
    }


    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
