$(document).ready(function() {
    $("#submitBtn").click(function(e) {
        e.preventDefault(); // 기본 폼 제출 방지

        // 폼 요소를 사용하여 FormData 객체 생성
        var formData = new FormData(document.getElementById("hotel_form"));

        // 가격 필드에서 쉼표 제거
        const priceField = document.getElementById('pricePerNight');
        priceField.value = priceField.value.replace(/,/g, ''); // 쉼표 제거

        // 수정된 가격 값을 FormData에 설정
        formData.set('pricePerNight', priceField.value);
        
        // 1차 및 2차 분류 선택값을 location 필드에 설정
        const primaryLocation = $('#primary-location').val();
        const secondaryLocation = $('#secondary-location').val();
        const locationValue = primaryLocation + (secondaryLocation ? " - " + secondaryLocation : "");
        formData.set('location', locationValue); // FormData에 location 추가

        // AJAX 요청을 통해 폼 데이터 제출
        $.ajax({
            url: '/hotelSubmit', // 요청을 보낼 URL
            type: 'POST',
            data: formData,
            processData: false, // jQuery가 데이터를 자동으로 쿼리 문자열로 변환하지 않도록 설정
            contentType: false, // jQuery가 콘텐츠 유형을 설정하지 않도록 설정
            success: function(response) {
                alert("호텔 등록이 완료되었습니다."); // 성공 메시지 표시
                window.location.href = '/hotelList'; // 호텔 목록 페이지로 리다이렉트
            },
            error: function(xhr, status, error) {
                console.error("등록 실패: ", error);
                alert("등록 중 오류가 발생했습니다. " + xhr.responseText); // 오류 메시지 표시
            }
        });
    });

    // 2차 분류 옵션
    const secondaryLocationOptions = {
        "서울": [],
        "경기": ["수원", "성남", "의정부", "안양", "부천", "광명", "평택", "동두천", "안산", "고양", "과천", "구리", "남양주", "오산", "시흥", "군포", "의왕", "하남", "용인", "파주", "이천", "안성", "김포", "화성", "광주", "양주", "포천", "여주"],
        "강원": ["춘천", "원주", "강릉", "동해", "태백", "속초", "삼척"],
        "충북": ["청주", "충주", "제천"],
        "충남": ["천안", "공주", "보령", "아산", "서산", "논산", "계룡", "당진"],
        "전북": ["전주", "군산", "익산", "정읍", "남원", "김제"],
        "전남": ["목포", "여수", "순천", "나주", "광양"],
        "경북": ["포항", "경주", "김천", "안동", "구미", "영주", "영천", "상주", "문경", "경산"],
        "경남": ["창원", "진주", "통영", "사천", "김해", "밀양", "거제", "양산"],
        "제주": ["제주", "서귀포"],
        "부산": [],
        "대구": [],
        "인천": [],
        "대전": [],
        "광주": [],
        "울산": [],
        "세종": []
    };

    // 1차 분류 선택 시 2차 분류 초기화
    const primaryLocationSelect = document.getElementById("primary-location");
    const secondaryLocationSelect = document.getElementById("secondary-location");

    primaryLocationSelect.addEventListener("change", function () {
        const selectedPrimaryLocation = this.value;

        // 2차 분류 초기화
        secondaryLocationSelect.innerHTML = '<option value="">2차 분류를 선택하세요</option>';

        // 2차 분류 옵션 추가
        if (selectedPrimaryLocation && secondaryLocationOptions[selectedPrimaryLocation]) {
            secondaryLocationOptions[selectedPrimaryLocation].forEach(function (location) {
                const option = document.createElement("option");
                option.value = location;
                option.textContent = location;
                secondaryLocationSelect.appendChild(option);
            });
        }
    });

    // 가격 포맷 함수
    function formatPrice(input) {
        // 입력된 값에서 숫자만 추출
        let value = input.value.replace(/[^0-9]/g, '');

        // 숫자를 쉼표로 구분하여 포맷
        if (value) {
            value = Number(value).toLocaleString(); // 숫자를 쉼표로 구분하여 포맷
        }

        // 포맷된 값을 입력 필드에 설정
        input.value = value;
    }

    // 폼 제출 시 쉼표 제거
    document.querySelector('form').addEventListener('submit', function() {
        const priceField = document.getElementById('pricePerNight');
        priceField.value = priceField.value.replace(/,/g, ''); // 쉼표 제거
        console.log(priceField.value); 
    });
});
