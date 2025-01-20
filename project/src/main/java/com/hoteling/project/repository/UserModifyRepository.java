package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.User;
import com.hoteling.project.domain.entity.UserModify;

@Repository
public interface UserModifyRepository extends JpaRepository<UserModify, Long> {
	
	// 특정 사용자에 대한 수정 기록 조회
    List<UserModify> findByModifyUser(User modifyUser);

    // 특정 사용자 ID에 대한 수정 기록 조회
    List<UserModify> findByModifyUser_UserId(Long userId);
}
