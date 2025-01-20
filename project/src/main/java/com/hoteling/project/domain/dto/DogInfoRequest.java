package com.hoteling.project.domain.dto;

import java.time.LocalDate;

import com.hoteling.project.domain.entity.DogInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogInfoRequest {
    private String dogName;
    private DogInfo.DogGender dogGender;
    private LocalDate dogBirth;
    private Boolean neutered;
    private String additionalInfo;
    
}
