// src/main/java/co/com/bancolombia/web/security/CaptchaValidationFilter.java
package co.com.bancolombia.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class CaptchaValidationFilter extends OncePerRequestFilter {

    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final String secretKey;

    public CaptchaValidationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String captchaResponse = request.getParameter("g-recaptcha-response");
            if (captchaResponse == null || captchaResponse.isEmpty() || !verifyCaptcha(captchaResponse)) {
                request.setAttribute("captchaError", true);
                response.sendRedirect("/login?error"); // Redirect with error parameter
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean verifyCaptcha(String responseToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "secret=" + secretKey + "&response=" + responseToken;
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> resp = restTemplate.exchange(RECAPTCHA_URL, HttpMethod.POST, requestEntity, Map.class);
        return Boolean.TRUE.equals(resp.getBody().get("success"));
    }
}