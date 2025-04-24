package co.com.bancolombia.web.controller;

import co.com.bancolombia.hashutil.HashUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/hash")
public class HashController {

    @GetMapping
    public String showForm() {
        return "hash"; // Mismo nombre para GET y POST
    }

    @PostMapping
    public String hashWord(
            @RequestParam("palabra") String palabra,
            @RequestParam("algoritmo") String algoritmo,
            Model model) {

        String resultado = switch (algoritmo.toLowerCase()) {
            case "sha256" -> HashUtil.sha256(palabra);
            case "md5" -> HashUtil.md5(palabra);
            case "argon2id" -> HashUtil.argon2id(palabra);
            default -> "Algoritmo no soportado";
        };

        model.addAttribute("hash", resultado);
        model.addAttribute("algoritmo", algoritmo.toUpperCase());
        model.addAttribute("palabra", palabra);

        return "hash"; // Devuelve la misma vista
    }
}
