package com.esiproject2023.emailservice.controller;

import com.esiproject2023.emailservice.dto.EmailDto;
import com.esiproject2023.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public EmailDto sendEmail(@RequestBody EmailDto emailDto) {
        return  emailService.sendEmail(emailDto);
    }
}
