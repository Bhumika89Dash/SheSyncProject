package com.patientManage.notification_service.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EmailRequest {
    // sender , receiver, content to send ,subject
    private String recipientEmail;
    private String subject;
    private String body;



}
