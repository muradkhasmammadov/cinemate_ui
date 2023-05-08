package com.esiproject2023.emailservice.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    @Id
    private Long id;
    private String user_email;
    private String title;
    private String subject;
    private String content;
}
