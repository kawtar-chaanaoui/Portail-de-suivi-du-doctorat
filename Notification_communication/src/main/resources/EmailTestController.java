package ma.emsi.notification_communication.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ma.emsi.notification_communication.services.EmailService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping("/test")
    public String sendTestEmail(@RequestParam String to) {
        emailService.sendTestEmail(to);
        return "Email envoyé à " + to;
    }
}