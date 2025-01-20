package com.hoteling.project.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "u_id", unique = true, nullable = false)
    private String uId;

    @Column(name = "u_pw", nullable = false)
    private String uPw;

    @Column(name = "u_name", nullable = false)
    private String uName;

    @Column(name = "u_ssn")
    private String uSsn;

    @Column(name = "u_phone_num")
    private String uPhoneNum;

    @Column(name = "u_email", unique = true, nullable = false)
    private String uEmail;

    @Column(name = "u_create_at", updatable = false)
    private LocalDateTime uCreateAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "u_role")
    private UserRole uRole = UserRole.USER;

    // OAuth2 로그인 공급자
    @Column(name = "provider")
    private String provider;

    // OAuth2 로그인 공급자 고유 ID
    @Column(name = "provider_id")
    private String providerId;
    
    // Email 인증 여부(이메일 인증이 됐는지 안 됐는지 True, False 로 체크)
    @Column(name = "email_status", nullable = false)
    private boolean emailStatus;
    
    @PrePersist
    public void prePersist() {
        if (uCreateAt == null) {
            uCreateAt = LocalDateTime.now();
        }
    }
    
    @Column(name = "u_wish")
    private String uWish;

}
