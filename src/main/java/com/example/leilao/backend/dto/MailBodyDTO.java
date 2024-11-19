package com.example.leilao.backend.dto;

import lombok.Builder;

@Builder
public record MailBodyDTO(String to, String subject, String text) {

}
