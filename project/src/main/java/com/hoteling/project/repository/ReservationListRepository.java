package com.hoteling.project.repository;

import com.hoteling.project.domain.entity.ReservationEntity;
import com.hoteling.project.domain.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationListRepository extends JpaRepository<ReservationEntity,Long> {
	 // 사용자 ID와 상태를 기반으로 예약 찾기
    List<ReservationEntity> findByUser_UserIdAndStatus(Long userId, ReservationStatus status);
    
    // 상태 기반으로 예약 찾기 (네이티브 쿼리)
    @Query(value = "SELECT r FROM reservations r WHERE r.status = :status ORDER BY r.start_date", nativeQuery = true)
    List<ReservationEntity> findByStatus(@Param("status") ReservationStatus status);

    // 사용자 ID와 상태를 기반으로 예약을 찾는 쿼리 (JPQL)
    @Query("SELECT r FROM ReservationEntity r WHERE r.user.userId = :userId AND r.status = :status ORDER BY r.startDate")
    List<ReservationEntity> findByUserIdAndStatusOrdered(@Param("userId") Long userId, @Param("status") ReservationStatus status);
}
