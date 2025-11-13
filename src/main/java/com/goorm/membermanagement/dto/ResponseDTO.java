package com.goorm.membermanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private String status;  // success / fail
    private String message;
    private Object data;
}