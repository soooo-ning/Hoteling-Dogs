# Hoteling-Dogs

강아지 호텔 예약 플랫폼 팀프로젝트 소스코드 백업입니다. <br>
실제 배포용 X 로컬작업

<br>

![쉼, 독 _ 멍멍 펫 호텔](https://github.com/user-attachments/assets/4c139ef5-02ed-4834-9ea3-10acde1742f8)

<br>

## 🗓️ Project Period
2024.08 - 2024.09 (5주)

<br>

## 💻 Tech Stack

<img src="https://img.shields.io/badge/java-FF7800?style=flat-square&logo=java&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=flat-square&logo=javascript&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white"/> &nbsp;
<img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white"/> &nbsp;
<br>

> **Backend**: Spring Boot, JPA, Gradle <br>
> **Frontend**: Thymeleaf <br>
> **Authentication**: Spring Security, JWT, OAuth 2.0 (Google, Kakao) <br>
> **Database**: MySQL, Redis <br>
> **External API**: Iamport (Payment), Gmail SMTP <br>

<br>

## 📃 Output

<img width="2440" height="1287" alt="erd" src="https://github.com/user-attachments/assets/6f1d9a8b-1002-438e-86dd-d7cb57e45b98" /> <br>

![IA](https://github.com/user-attachments/assets/50d4bfa4-9cf6-43fa-8da4-35e8963ba945)

![예약 상세 페이지](https://github.com/user-attachments/assets/4e5e5086-83df-4fc5-9d5a-0fa688984326)

<br>

## 👨‍💻 My Role

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

## 💡 What I Learned

### 외부 API 연동

- 카카오맵 API, 아임포트 결제 API 연동 경험 <br>
- API 문서 분석, 인증 플로우 이해에 많이 도움이 되었다 

<br>

### 예약 및 결제 로직 구현

ReservationService
```
@Service
public class ReservationService {

    @Transactional
    public Reservation createReservation(ReservationRequest req) {
        // 1. 호텔/객실 유효성 체크
        Hotel hotel = hotelRepository.findById(req.getHotelId())
            .orElseThrow(() -> new NotFoundException("호텔 없음"));

        // 2. 예약 가능 여부 확인
        if (!roomAvailabilityService.isAvailable(hotel, req.getDate())) {
            throw new IllegalStateException("예약 불가");
        }

        // 3. 예약 정보 저장
        Reservation reservation = Reservation.of(req, hotel);
        reservationRepository.save(reservation);

        // 4. 결제 단계로 진행
        paymentService.processPayment(reservation);

        return reservation;
    }
}
```

<br>


아임포트 SDK 활용 → 결제 성공 시 서버 검증 및 예약 상태 업데이트.

PaymentService
```
@Service
public class PaymentService {

    public void processPayment(Reservation reservation) {
        // 1. 결제 요청 생성
        Payment payment = Payment.of(reservation);

        // 2. 외부 PG사 API 호출 (ex. KakaoPay, TossPayments)
        PaymentResult result = paymentGateway.request(payment);

        // 3. 결과 처리
        if (result.isSuccess()) {
            reservation.markPaid();
        } else {
            throw new PaymentFailedException("결제 실패");
        }
    }
}
```

<br>

## 🛠️ Points to Improve

### 리팩토링 필요

- 컨트롤러에 비즈니스 로직 혼재, JS 함수 파일하나에 통으로 관리 → 프로젝트 이후에 유지보수 어려울 것 같다고 깨닫게 됨
- application.yml에 API 키, DB 비밀번호 등 민감정보가 노출됨
- 호텔 상세페이지에서 선택한 날짜에 맞는 금액으로 변경되는 코드 작성 안함.. 하드코딩 되어있는거 발견
- 상세페이지에서 선택한 날짜값 파라미터가 -1씩 되어 데이터 넘김
- 프론트 스크롤 위치에 선택 메뉴 변경되는 부분이 적용 범위가 다르게 됨
- 민감한 예약 정보(사용자ID, 호텔ID, 날짜 등)를 개별 파라미터로 전달하고 있음 POST Body로만 전송하도록 리팩토링이 필요
- 추가로 예약 서비스 코드에서 중복 setter 들 정리
- 예외처리 구체적으로 분류하기

### 코드 개선

- 예약-결제 플로우를 수정하는게 좋을 것 같다
- 둘을 별개의 로직으로 분리하지 않고, 트랜잭션으로 묶는게 더 효율적인듯

> 1. 호텔 상세 → 날짜/강아지 선택 (기존과 동일)
> 2. 예약 페이지 → 정보 입력 (기존과 동일)
> 3. 결제 버튼 클릭 시:
>    - 서버에 임시 예약 생성 (PENDING 상태, DB 저장 안 함 or PENDING으로 저장) -> 메모리에 저장해도 좋을 듯
>    - 가용성 재확인
>    - 15분 정도 시간제한
> 4. 아임포트 결제창 띄움
> 5. 결제 성공 시:
>    @Transactional
>    - 예약 생성 (PENDING)
>    - 결제 검증
>    - 예약 상태 CONFIRMED로 변경
>    - 객실 재고 차감
> 6. 결제 실패 시:
>    - 예약 생성 안 함
>    - 또는 PENDING 상태였다면 취소
