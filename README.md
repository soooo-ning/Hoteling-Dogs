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

### 1. 예약 및 결제 시스템

> [ReservationController.java](project/src/main/java/com/hoteling/project/controller/ReservationController.java) | [PaymentController.java](project/src/main/java/com/hoteling/project/controller/PaymentController.java)

- 날짜, 강아지 크기별 객실 예약 API 개발
- 아임포트(I'mport) 결제 시스템 연동 (카카오페이, 토스페이)
- 예약-결제 프로세스 통합 관리
- 예약 정보 실시간 검증 및 유효성 확인

### 2. 객실 가용성 관리

> [HotelController.java](project/src/main/java/com/hoteling/project/controller/HotelController.java) | [ReservationServiceImplement.java](project/src/main/java/com/hoteling/project/service/implement/ReservationServiceImplement.java)

- 날짜 및 반려견 유형별 객실 가용성 확인 API
- 실시간 객실 재고 관리 시스템
- 예약 확정 시 객실 가용성 자동 업데이트

### 3. 호텔 정보 시스템

> [HotelController.java](project/src/main/java/com/hoteling/project/controller/HotelController.java)

- 호텔 상세 정보 제공 API
- 카카오맵 API 연동을 통한 위치 정보 제공
- 이미지 슬라이더, 리뷰, Q&A 통합 조회

<br>

## 🔍 Trouble Shooting

### 1. 다중 날짜 예약 시 금액 계산 오류

예약 페이지에서 사용자가 다중 날짜를 예약할 때 금액 계산이 부정확하게 이루어지는 문제가 발생했습니다.

| 문제 | 원인 | 상세 |
| --- | --- | --- |
| 다중 날짜 계산 오류 | 날짜 계산 로직 오류 | 체크아웃 날짜를 포함하여 숙박 일수를 계산하면서 실제보다 1박 더 많이 청구됨 |
| 객실 가격 중복 계산 | 가격 합산 로직 미흡 | 객실 가격 합산 시 중복 계산되어 클라이언트와 서버 간 금액 불일치 발생 |

<br>

**Solution**

> [ReservationServiceImplement.java](project/src/main/java/com/hoteling/project/service/implement/ReservationServiceImplement.java) - calculateReservationAmount

`ChronoUnit.DAYS.between()`을 활용한 정확한 숙박 일수 계산 및 스트림 API를 활용한 객실 가격 합산 로직 최적화

<br>

### 2. 결제 완료 후 서버 데이터 전송 오류

결제 완료 후 서버로 데이터 전송 과정에서 다음과 같은 기술적 문제가 발생했습니다.

| 문제 | 원인 | 상세 |
| --- | --- | --- |
| CSRF 토큰 인증 실패 | AJAX 요청 시 토큰 누락 | 결제 완료 후 서버로 데이터 전송 과정에서 CSRF 토큰이 헤더에 포함되지 않아 403 에러 발생 |
| 결제 금액 검증 미흡 | 원자적 처리 부재 | 결제 성공 시 예약 상태 업데이트가 누락되어 동일 예약건에 중복 결제가 가능하게 되는 문제 발생 |

<br>

**Solution**

> [reservation.js](project/src/main/resources/static/js/reservation.js) - CSRF 토큰 처리

CSRF 토큰을 meta 태그에서 추출하여 AJAX 요청 헤더에 포함

> [PaymentServiceImplement.java](project/src/main/java/com/hoteling/project/service/implement/PaymentServiceImplement.java) - processPayment

결제 금액 검증 로직 추가 및 `@Transactional`을 통한 예약-결제 상태 동기화

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
