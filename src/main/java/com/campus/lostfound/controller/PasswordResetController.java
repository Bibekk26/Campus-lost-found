package com.campus.lostfound.controller;

import com.campus.lostfound.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Validated
public class PasswordResetController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("resetForm", new ResetPasswordForm());
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(
            @Validated @ModelAttribute("resetForm") ResetPasswordForm resetForm,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (!resetForm.password().equals(resetForm.confirmPassword())) {
            result.rejectValue("confirmPassword", "match", "Passwords do not match.");
        }

        var user = userRepository.findByEmail(resetForm.email().toLowerCase());
        if (user.isEmpty()) {
            result.rejectValue("email", "missing", "No account exists with this email.");
        }

        if (result.hasErrors()) {
            return "auth/forgot-password";
        }

        var account = user.get();
        account.setPassword(passwordEncoder.encode(resetForm.password()));
        userRepository.save(account);
        redirectAttributes.addFlashAttribute("success", "Password changed. Please log in.");
        return "redirect:/login";
    }

    public static class ResetPasswordForm {
        @Email(message = "Enter a valid email.")
        @NotBlank(message = "Email is required.")
        private String email;

        @NotBlank(message = "Password is required.")
        @Size(min = 6, message = "Password must be at least 6 characters.")
        private String password;

        @NotBlank(message = "Please confirm your password.")
        private String confirmPassword;

        public String email() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String password() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String confirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}
