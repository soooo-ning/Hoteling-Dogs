document.addEventListener('DOMContentLoaded', function () {
    const agreeAllCheckbox = document.getElementById('agree-all');
    const agreePersonalCheckbox = document.getElementById('agree-personal');
    const agreeConfirmCheckbox = document.getElementById('agree-confirm');
    const paymentButton = document.querySelector('.payment-button.submit');
    const paymentOptions = document.querySelectorAll('.payment-options .payment-button');
    const genderButtons = document.querySelectorAll('.content-options .content-button');
    const tellNumberInput = document.getElementById('tell-number');
    const IMP = window.IMP;
    IMP.init("imp34612723"); // 아임포트 가맹점 식별코드
    const reservationId = 'TEMP_' + new Date().getTime();


    const requiredFields = [
        { id: 'name', name: '이름' },
        { id: 'tell-number', name: '연락처' },
        { id: 'dog-name', name: '강아지 이름' },
        { id: 'age', name: '나이' },
        { id: 'weight', name: '무게' },
        { id: 'breed', name: '견종' }
    ];

    

    // 연락처 입력 필드 이벤트 리스너
    tellNumberInput.addEventListener('input', function (e) {
        let input = e.target.value.replace(/\D/g, '').substring(0, 11);
        let formattedInput = '';

        if (input.length > 3) {
            formattedInput += input.substr(0, 3) + '-';
            if (input.length > 7) {
                formattedInput += input.substr(3, 4) + '-' + input.substr(7);
            } else {
                formattedInput += input.substr(3);
            }
        } else {
            formattedInput = input;
        }

        e.target.value = formattedInput;
        hideFieldError(e.target);
    });

    // 모든 입력 필드에 이벤트 리스너 추가
    requiredFields.forEach(field => {
        const element = document.getElementById(field.id);
        if (element) {
            element.addEventListener('input', function() {
                hideFieldError(this);
            });
        }
    });

    const formFields = [
        'name',
        'tell-number',
        'dog-name',
        'age',
        'weight',
        'breed',
        'special-requests'
    ];

    // 각 입력 필드에 이벤트 리스너 추가
    formFields.forEach((fieldId, index) => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('keydown', function(event) {
                if (event.key === 'Enter') {
                    event.preventDefault(); // 폼 제출 방지
                    const nextField = document.getElementById(formFields[index + 1]);
                    if (nextField) {
                        nextField.focus();
                    } else {
                        // 마지막 필드에서 엔터를 누르면 결제 버튼으로 포커스 이동
                        const paymentButton = document.querySelector('.payment-button.submit');
                        if (paymentButton) {
                            paymentButton.focus();
                        }
                    }
                }
            });
        }
    });

    // 성별 버튼 이벤트 리스너
    genderButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            genderButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            hideError('gender-error');
        });
    });

    // 결제 수단 선택 이벤트 리스너
    paymentOptions.forEach(option => {
        option.addEventListener('click', function (e) {
            e.preventDefault();
            paymentOptions.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            hideError('payment-method-error');
        });
    });

    // 체크박스 이벤트 리스너
    [agreePersonalCheckbox, agreeConfirmCheckbox].forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            updateCheckboxStyles();
            updateAgreeAllCheckbox();
            hideCheckboxError(this);
        });
    });
    
    agreeAllCheckbox.addEventListener('change', function () {
        const isChecked = this.checked;
        [agreePersonalCheckbox, agreeConfirmCheckbox].forEach(checkbox => {
            checkbox.checked = isChecked;
            updateCheckboxStyles();
            hideCheckboxError(checkbox);
        });
    });
    
    function updateAgreeAllCheckbox() {
        agreeAllCheckbox.checked = agreePersonalCheckbox.checked && agreeConfirmCheckbox.checked;
        updateCheckboxStyles();
    }
    
    function hideCheckboxError(checkbox) {
        const errorId = checkbox.id === 'agree-personal' ? 'agree-personal-error' : 'agree-confirm-error';
        hideError(errorId);
    }

    // 체크박스 스타일 업데이트 함수
    function updateCheckboxStyles() {
        [agreeAllCheckbox, agreePersonalCheckbox, agreeConfirmCheckbox].forEach(checkbox => {
            checkbox.style.backgroundColor = checkbox.checked ? '#149c3bad' : '';
            checkbox.style.borderColor = checkbox.checked ? '#149c3bad' : '#ccc';
        });
    }

    // 예약 금액 계산 (페이지 매개변수에서 값을 가져옴)
    const urlParams = new URLSearchParams(window.location.search);
    const hotelId = urlParams.get('hotelId');
    const startDate = urlParams.get('startDate');
    const endDate = urlParams.get('endDate');
    const dogType = urlParams.get('dogType');
    
    if (hotelId && startDate && endDate && dogType) {
        fetchReservationAmount(hotelId, startDate, endDate, dogType);
    }

    let totalPrice = 0; // totalPrice 변수 추가

    function fetchReservationAmount(hotelId, startDate, endDate, dogType) {
        fetch(`/reservation/calculate?hotelId=${hotelId}&startDate=${startDate}&endDate=${endDate}&dogType=${dogType}`)
            .then(response => response.json())
            .then(data => {
                totalPrice = data; // totalPrice 저장
                const totalPriceElements = document.querySelectorAll('.total span:last-child, #paymentButton');
                totalPriceElements.forEach(element => {
                    if (element.id === 'paymentButton') {
                        element.textContent = `${data} 원 결제하기`;
                    } else {
                        element.textContent = `${data} 원`;
                    }
                });
            })
            .catch(error => console.error('Error:', error));
    }

    // 결제하기 버튼 이벤트 리스너
    paymentButton.addEventListener('click', function (e) {
        e.preventDefault();
        console.log('Payment button clicked');
        if (validateForm()) {
            console.log('Form is valid, proceeding with payment');
            processPayment();
        } else {
            console.log('Form is invalid, showing errors');
            showErrors();
        }
    });
    
    function showErrors() {
        console.log("Showing form errors");
        // 여기에 각 필드의 오류를 표시하는 로직을 구현하세요
    }

    function showErrors() {
        console.log("Showing form errors");
        // 여기에 각 필드의 오류를 표시하는 로직을 구현하세요
    }

    // processPayment 함수 수정
    function processPayment() {
        console.log('Processing payment...');
        
        // 호텔 이름 가져오기
        const hotelNameElement = document.getElementById('hotel-name');
        const hotelName = hotelNameElement ? hotelNameElement.textContent : '호텔 이름 없음';
    
        // 필요한 데이터 수집
        const name = document.getElementById('name').value;
        const tellNumber = document.getElementById('tell-number').value;
        const dogName = document.getElementById('dog-name').value;
        const age = document.getElementById('age').value;
        const weight = document.getElementById('weight').value;
        const breed = document.getElementById('breed').value;
        const gender = document.querySelector('.content-options .content-button.active').getAttribute('value');
        const paymentMethod = document.querySelector('.payment-options .payment-button.active').textContent;
        
        // URL에서 예약 정보 가져오기
        const urlParams = new URLSearchParams(window.location.search);
        const hotelId = urlParams.get('hotelId');
        const startDate = urlParams.get('startDate');
        const endDate = urlParams.get('endDate');
        const dogType = urlParams.get('dogType');
    
        // totalPrice 가져오기
        const totalPriceElement = document.querySelector('.total span:last-child');
        const totalPrice = totalPriceElement ? parseInt(totalPriceElement.textContent.replace(/[^0-9]/g, '')) : 0;
    
        const pgProvider = getPaymentGateway(paymentMethod);
    
        IMP.request_pay({
            pg: pgProvider,
            pay_method: getPaymentMethod(paymentMethod),
            merchant_uid: reservationId,
            name: `${hotelName} 예약`,
            amount: totalPrice,
            buyer_email: 'buyer@example.com',
            buyer_name: name,
            buyer_tel: tellNumber,
        }, function (rsp) {
            if (rsp.success) {
                // 결제 성공 시 서버에 데이터 전송
                sendReservationData(rsp.imp_uid, reservationId, {
                    hotelId, startDate, endDate, dogType, name, tellNumber,
                    dogName, dogAge: age, dogWeight: weight, dogBreed: breed,
                    dogGender: gender, paymentMethod, totalPrice
                });
            } else {
                alert('결제에 실패하였습니다. 에러 내용: ' + rsp.error_msg);
            }
        });
    }


    function sendReservationData(impUid, reservationId, data) {
        // URL에서 파라미터 가져오기
        const urlParams = new URLSearchParams(window.location.search);
        const hotelId = urlParams.get('hotelId');
        const startDate = urlParams.get('startDate');
        const endDate = urlParams.get('endDate');
        const dogType = urlParams.get('dogType');
    
        const requestBody = {
            reservationName: data.name,
            reservationTel: data.tellNumber,
            dogName: data.dogName,
            dogAge: parseInt(data.dogAge),
            dogWeight: parseFloat(data.dogWeight),
            dogGender: data.dogGender,
            dogBreed: data.dogBreed,
            specialRequests: data.specialRequests || "",
            agreedPersonal: true,
            impUid: impUid,
            reservationId: reservationId,
            totalAmount: parseFloat(data.totalPrice)
        };
    
        console.log('Sending reservation data:', requestBody);
    
        // CSRF 토큰 가져오기
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
        fetch(`/reservation?hotelId=${hotelId}&startDate=${startDate}&endDate=${endDate}&dogType=${dogType}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(requestBody),
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Server error');
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Reservation created:', data);
            window.location.href = `/payment/complete`;
        })
        .catch(error => {
            console.error('Error:', error);
            alert('예약 처리 중 오류가 발생했습니다: ' + error.message);
        });
    }

    function getPaymentGateway(paymentMethod) {
        switch(paymentMethod) {
            case '카카오페이':
                return 'kakaopay';
            case '토스페이':
                return 'tosspay';
            default:
                return 'html5_inicis';
        }
    }

    function getPaymentMethod(paymentMethod) {
        switch(paymentMethod) {
            case '카카오페이':
                return 'card';
            case '토스페이':
                return 'card';
            default:
                return 'card';
        }
    }

    // 폼 유효성 검사 함수
    function validateForm() {
        let isValid = true;

        requiredFields.forEach(field => {
            const element = document.getElementById(field.id);
            if (element && element.value.trim() === '') {
                isValid = false;
                showFieldError(element, `${field.name}을(를) 입력해주세요.`);
            } else if (field.id === 'tell-number' && !isValidPhoneNumber(element.value)) {
                isValid = false;
                showFieldError(element, '올바른 전화번호 형식이 아닙니다.');
            }
        });

        if (!document.querySelector('.content-options .content-button.active')) {
            isValid = false;
            showError('gender-error', '성별을 선택해주세요.');
        }

        if (!document.querySelector('.payment-options .payment-button.active')) {
            isValid = false;
            showError('payment-method-error', '결제 수단을 선택해주세요.');
        }

        if (!agreePersonalCheckbox.checked) {
            isValid = false;
            showError('agree-personal-error', '필수 동의 항목에 체크해주세요.');
        }

        if (!agreeConfirmCheckbox.checked) {
            isValid = false;
            showError('agree-confirm-error', '필수 동의 항목에 체크해주세요.');
        }

        return isValid;
    }

    // 전화번호 유효성 검사 함수
    function isValidPhoneNumber(phone) {
        return /^010-\d{4}-\d{4}$/.test(phone);
    }

    // 필드 에러 표시 함수
    function showFieldError(element, message) {
        element.classList.add('error');
        const errorElement = document.getElementById(`${element.id}-error`);
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }
    }

    // 필드 에러 숨기기 함수
    function hideFieldError(element) {
        element.classList.remove('error');
        const errorElement = document.getElementById(`${element.id}-error`);
        if (errorElement) {
            errorElement.style.display = 'none';
        }
    }

    // 일반 에러 표시 함수
    function showError(errorId, message) {
        const errorElement = document.getElementById(errorId);
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }
    }

    // 일반 에러 숨기기 함수
    function hideError(errorId) {
        const errorElement = document.getElementById(errorId);
        if (errorElement) {
            errorElement.style.display = 'none';
        }
    }
});