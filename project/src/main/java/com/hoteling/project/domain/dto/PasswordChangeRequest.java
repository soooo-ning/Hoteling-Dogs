package com.hoteling.project.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequest {
    private String currentUPassword;
    private String newUPassword;
    private String confirmUPassword;
}