package com.hoteling.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoteling.project.domain.entity.ReservationEntity;
import com.hoteling.project.domain.entity.ReservationStatus;
import com.hoteling.project.repository.ReservationListRepository;

@Service
public class ReservationListService {
    @Autowired
    private ReservationListRepository reservationListRepository;

    //이용예정
    public List<ReservationEntity> findUpcomingReservations(String userId){
        return reservationListRepository.findByUser_UserIdAndStatus(Long.valueOf(userId), ReservationStatus.valueOf("expect"));
    }
    
    //취소된 예약
    public List<ReservationEntity> findCanceledReservations(String userId){
        return reservationListRepository.findByUser_UserIdAndStatus(Long.valueOf(userId), ReservationStatus.valueOf("canceled"));
    }

    //이용 완료된 예약
    public List<ReservationEntity> findCompletedReservations(String userId){
        return reservationListRepository.findByUser_UserIdAndStatus(Long.valueOf(userId), ReservationStatus.valueOf("completed"));
    }

}
