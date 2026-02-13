# Hoteling-Dogs

강아지 호텔 예약 플랫폼 팀프로젝트 소스코드 백업입니다.

<br>

![쉼, 독 _ 멍멍 펫 호텔](https://github.com/user-attachments/assets/4c139ef5-02ed-4834-9ea3-10acde1742f8)

> **Notice**
> 이 레포지토리는 팀프로젝트 소스코드 아카이브입니다.
> 외부 API 키 만료(Iamport, 카카오맵) 및 DB 환경 미포함으로 현재 실행되지 않습니다.

<br>

## 🗓️ Project Period

2024.08 - 2024.09 (5주) | 4인 풀스택 팀 프로젝트

<br>

## 💻 Tech Stack

<img src="https://img.shields.io/badge/java-FF7800?style=flat-square&logo=java&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=flat-square&logo=javascript&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white"/> &nbsp;

<br>

| 분류              | 기술                                             |
| ---------------- | ----------------------------------------------- |
| **Backend**      | Spring Boot 3.3.4, JPA, Gradle, Java 17         |
| **Frontend**     | Thymeleaf, JavaScript                           |
| **Auth**         | Spring Security, JWT, OAuth 2.0 (Google, Kakao) |
| **Database**     | MySQL, Redis                                    |
| **External API** | Iamport (결제), Gmail SMTP, 카카오맵               |

<br>

## 📁 Project Structure

```
com.hoteling.project/
├── controller/     # API 엔드포인트
├── service/        # 비즈니스 로직
├── repository/     # 데이터 접근 (JPA)
├── domain/
│   ├── entity/     # JPA 엔티티
│   └── dto/        # 요청/응답 DTO
├── config/         # Security, Redis 설정
└── exception/      # 예외 처리
```
<br>

## 📃 Output

<details>
  <summary>ERD</summary>
  <img width="2440" height="1287" alt="erd" src="https://github.com/user-attachments/assets/6f1d9a8b-1002-438e-86dd-d7cb57e45b98" />
</details>

<details>
  <summary>IA</summary>
  <img width="2440" height="1287" alt="erd" src="https://github.com/user-attachments/assets/50d4bfa4-9cf6-43fa-8da4-35e8963ba945" />
</details>

<details>
  <summary>wireframe</summary>
  <img width="2440" height="1287" alt="erd" src="https://github.com/user-attachments/assets/4e5e5086-83df-4fc5-9d5a-0fa688984326" />
</details>

<br>

## 👨‍💻 My Role

### 담당 기능

<table border="1" cellspacing="0" cellpadding="8">
  <thead>
    <tr>
      <th>구분</th>
      <th>기능</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td rowspan="5">호텔 상세 페이지</td>
      <td>호텔 정보 조회 (이미지, 지도 위치, 리뷰, Q&amp;A)</td>
    </tr>
    <tr>
      <td>이미지 슬라이더</td>
    </tr>
    <tr>
      <td>날짜 선택 및 강아지 유형 선택</td>
    </tr>
    <tr>
      <td>객실 가용성 체크</td>
    </tr>
    <tr>
      <td>카카오맵 API 연동</td>
    </tr>
    <tr>
      <td rowspan="4">예약 페이지</td>
      <td>예약자 정보 입력 폼</td>
    </tr>
    <tr>
      <td>반려견 정보 입력 (이름, 성별, 나이, 무게, 견종)</td>
    </tr>
    <tr>
      <td>유효성 검사 (프론트)</td>
    </tr>
    <tr>
      <td>예약 확인 정보 표시</td>
    </tr>
    <tr>
      <td rowspan="3">결제 시스템</td>
      <td>아임포트(Iamport) API 연동</td>
    </tr>
    <tr>
      <td>카카오페이/토스페이 결제 수단</td>
    </tr>
    <tr>
      <td>결제 전 동의 체크</td>
    </tr>
    <tr>
      <td rowspan="1">결제 완료 페이지</td>
      <td>예약 완료 안내</td>
    </tr>
  </tbody>
</table>

<br>

### 주요 구현 코드

### 1. 객실 가용성 체크 API

> [HotelController.java](project/src/main/java/com/hoteling/project/controller/HotelController.java)

날짜와 강아지 타입에 따른 예약 가능 객실 조회:

```java
@GetMapping("/hotel/availability")
@ResponseBody
public ResponseEntity<?> checkAvailability(
        @RequestParam("hotelId") Long hotelId,
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("endDate") LocalDate endDate,
        @RequestParam("dogType") DogType dogType) {

    List<HotelRoomEntity> availableRooms =
        hotelListService.findAvailableRooms(hotelId, startDate, endDate, dogType);

    Map<String, Object> response = new HashMap<>();
    response.put("code", "SU");
    response.put("availableRoom", availableRooms.stream()
            .map(room -> {
                Map<String, Object> roomData = new HashMap<>();
                roomData.put("roomId", room.getRoomId());
                roomData.put("availableRooms", room.getAvailableRooms());
                roomData.put("price", room.getPrice());
                return roomData;
            })
            .collect(Collectors.toList()));

    return ResponseEntity.ok(response);
}
```

### 2. 예약 생성 API

> [ReservationController.java](project/src/main/java/com/hoteling/project/controller/ReservationController.java)

예약 정보 검증 및 생성:

```java
@PostMapping
public ResponseEntity<?> createReservation(
        @RequestParam("hotelId") Long hotelId,
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam("dogType") DogType dogType,
        @RequestBody ReservationRequestDto reservationForm,
        Authentication authentication) {

    String userId = authentication.getName();

    ResponseEntity<? super ResponseDto> response = reservationService.createReservation(
            userId, hotelId, startDate, endDate, dogType, reservationForm);

    if (response.getStatusCode().is2xxSuccessful()) {
        return ResponseEntity.ok(response.getBody());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("예약 생성 중 오류가 발생했습니다.");
}
```

### 3. 결제 처리 API

> [PaymentController.java](project/src/main/java/com/hoteling/project/controller/PaymentController.java)

아임포트 결제 검증 및 처리:

```java
@PostMapping("/process")
public ResponseEntity<?> processPayment(
        @RequestBody PaymentRequestDto requestDto,
        Authentication auth) {

    String userId = auth.getName();
    ResponseEntity<? super PaymentResponseDto> response =
        paymentService.processPayment(requestDto);

    if (response.getStatusCode().is2xxSuccessful()) {
        return ResponseEntity.ok(response.getBody());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("결제 처리 중 오류가 발생했습니다.");
}
```

<br>

## 💡 What I Learned

이 프로젝트에서 배운 것

| 영역                | 배운 점                                                   |
| ------------------- | --------------------------------------------------------- |
| **외부 API 연동**   | 카카오맵, 아임포트 결제 API 문서 분석 및 인증 플로우 이해 |
| **예약 시스템**     | 날짜 기반 가용성 체크, 금액 계산 로직 설계                |

<br>

## 🛠️ Points to Improve

아쉬웠던 점 → 이후 개선 방향

| 문제점                         | 원인                    | 개선 방향                                                    |
| ------------------------------ | ----------------------- | ------------------------------------------------------------ |
| 컨트롤러에 비즈니스 로직 혼재  | 계층 분리 개념 부족     | Service 계층으로 로직 분리, DTO 패턴 적용                    |
| 환경 설정 파일에 민감정보 노출 | 환경변수 관리 미숙      | `.env` 파일 분리, Spring Profiles 활용                       |
| 예약-결제 플로우 분리          | 트랜잭션 설계 경험 부족 | `@Transactional`로 원자적 처리, 상태 관리(PENDING→CONFIRMED) |
| 프론트엔드 코드 구조화 부족    | JS 모듈화 개념 부족     | 기능별 파일 분리, 모듈 패턴 적용                             |
