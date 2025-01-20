package com.hoteling.project.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hoteling.project.domain.dto.request.reservation.ReservationRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class ReservationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reservationId;

  @ManyToOne
  @JoinColumn(name = "hotel_id", nullable = false)
  private HotelEntity hotel;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private LocalDateTime reservationDate;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @ManyToOne
  @JoinColumn(name = "hotel_room_id", nullable = false)
  private HotelRoomEntity hotelRoom;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DogType dogType;

  @Column(nullable = false)
  private String reservationName;

  @Column(nullable = false)
  private String reservationTel;

  @Column(nullable = false)
  private String dogName;

  @Column(nullable = false)
  private Integer dogAge;

  @Column(nullable = false)
  private BigDecimal dogWeight;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender dogGender;

  @Column(nullable = false)
  private String dogBreed;

  @Column(columnDefinition = "TEXT")
  private String specialRequests;

  @Column(nullable = false)
  private Boolean agreedPersonal;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus paymentStatus;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReservationStatus status = ReservationStatus.PENDING;

  @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PaymentEntity> payments = new ArrayList<>(); // 결제내역과 일대다

  public ReservationEntity(ReservationRequestDto dto) {

    this.reservationDate = LocalDateTime.now();
    this.reservationName = dto.getReservationName();
    this.reservationTel = dto.getReservationTel();
    this.dogName = dto.getDogName();
    this.dogAge = dto.getDogAge();
    this.dogWeight = dto.getDogWeight();
    this.dogGender = dto.getDogGender();
    this.dogBreed = dto.getDogBreed();
    this.specialRequests = dto.getSpecialRequests();
    this.agreedPersonal = dto.getAgreedPersonal();

  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public Long getReservationId() {
    return reservationId;
  }

}
