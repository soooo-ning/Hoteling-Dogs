package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.DogInfo;
import com.hoteling.project.domain.entity.User;

@Repository
public interface DogInfoRepository extends JpaRepository<DogInfo, Long> {
    // 회원이 소유한 강아지 정보를 조회
    List<DogInfo> findByOwner(User owner);
    
}