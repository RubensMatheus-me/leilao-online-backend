package com.example.leilao.backend.service;

import com.example.leilao.backend.dto.MailBodyDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service

public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendSimpleEmail(MailBodyDTO mailBodyDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBodyDTO.to());
        message.setSubject(mailBodyDTO.subject());
        message.setText(mailBodyDTO.text());
        mailSender.send(message);
    }

    public void sendTemplateEmail(String to, String subject, Integer verificationCode)
            throws MessagingException {

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);

        String processedTemplate = templateEngine.process("index", context);


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(processedTemplate, true);

        mailSender.send(message);
    }
}