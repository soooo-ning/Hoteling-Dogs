package com.hoteling.project.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users_modify")

public class UserModify {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_info_id")
    private Long userInfoId;
	
	@Column(name = "u_changed_at", updatable = false)
    private LocalDateTime uChangedAt;
	
	@Column(name = "u_col_name")
    private String uColName;
	
	@Column(name = "u_old_value")
    private String uOldValue;
	
	@Column(name = "u_new_value")
    private String uNewValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User modifyUser;
	
	 @PrePersist
	    public void prePersist() {
	        if (uChangedAt == null) {
	            uChangedAt = LocalDateTime.now();
	        }
	    }
	
}
