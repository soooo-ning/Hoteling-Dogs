package com.hoteling.project.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dog_info")
public class DogInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Long dogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "dog_name", nullable = false)
    private String dogName;

    @Enumerated(EnumType.STRING)
    @Column(name = "dog_gender", nullable = false)
    private DogGender dogGender;

    @Column(name = "dog_birth")
    private LocalDate dogBirth;

    @Column(name = "neutered")
    private Boolean neutered;

    @Lob
    @Column(name = "additional_info")
    private String additionalInfo;

    public enum DogGender {
    	MALE, FEMALE
    }
}
