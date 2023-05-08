package com.esiproject2023.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    private String user_email;
    private String title;
    private String subject;
    private String content;
}
