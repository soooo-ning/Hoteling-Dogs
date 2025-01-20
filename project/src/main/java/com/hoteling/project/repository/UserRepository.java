package com.hoteling.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 주어진 로그인 ID를 갖는 객체가 존재하는지 확인
    boolean existsByuId(String uId);
    
    // 주어진 로그인 ID를 갖는 객체 반환
    User findByuId(String uId);
  
    // 이메일로 회원을 조회하는 메서드
    @Query("SELECT u FROM User u WHERE LOWER(u.uEmail) = LOWER(:uEmail)")
    Optional<User> findByuEmailIgnoreCase(@Param("uEmail") String uEmail);
    
    // 이메일로 사용자가 존재하는지 확인하는 메서드
    boolean existsByuEmail(String uEmail);  

    // 주어진 로그인 ID에 해당하는 사용자의 userId 반환
    default Long findUserIdByUId(String uId) {
        return findByuId(uId).getUserId();
        

    }
}
