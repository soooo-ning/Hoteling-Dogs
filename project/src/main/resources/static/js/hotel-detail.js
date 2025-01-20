document.addEventListener('DOMContentLoaded', function() {
    initializeImageSlider();
    initializeNavigation();
    initializeQAndA();
    initializeReservation();
    initializeOtherFeatures();
    loadKakaoMaps();
});

function loadKakaoMaps() {
    var script = document.createElement('script');
    script.src = 'https://dapi.kakao.com/v2/maps/sdk.js?appkey=96ae16d5665dad89ac649af6b1dbe0ba&libraries=services&autoload=false';
    script.onload = function() {
        kakao.maps.load(initializeMap);
    };
    document.head.appendChild(script);
}

function initializeImageSlider() {
    const slides = document.querySelectorAll('.slide');
    const prevBtn = document.querySelector('.prev');
    const nextBtn = document.querySelector('.next');
    let currentSlide = 0;

    function showSlide(n) {
        slides[currentSlide].classList.remove('active');
        currentSlide = (n + slides.length) % slides.length;
        slides[currentSlide].classList.add('active');
    }

    prevBtn.addEventListener('click', () => showSlide(currentSlide - 1));
    nextBtn.addEventListener('click', () => showSlide(currentSlide + 1));
}

function initializeNavigation() {
    const navBtns = document.querySelectorAll('.nav-btn');
    const sections = document.querySelectorAll('section');
    let isScrolling = false;

    navBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const targetId = this.dataset.target;
            const targetSection = document.getElementById(targetId);
            if (targetSection) {
                scrollToSection(targetSection);
            }
        });
    });

    function scrollToSection(targetSection) {
        isScrolling = true;
        const navHeight = document.querySelector('.navi').offsetHeight;
        const targetPosition = targetSection.getBoundingClientRect().top + window.pageYOffset - navHeight;
		
        window.scrollTo({
            top: targetPosition,
            behavior: 'smooth'
        });

        setTimeout(() => {
            isScrolling = false;
        }, 500);
    }

    function updateActiveButton() {
        if (isScrolling) return;

        const scrollPosition = window.scrollY;
        const navHeight = document.querySelector('.navi').offsetHeight;

		// 스크롤 위치가 맨 위일 때
	    if (scrollPosition === 0) {
	        navBtns.forEach(btn => btn.classList.remove('active'));
	        return; // 더 이상 진행하지 않음
	    }
		
        sections.forEach((section, index) => {
            const sectionTop = section.offsetTop - navHeight - 1;
            const sectionBottom = sectionTop + section.offsetHeight;

            if (scrollPosition >= sectionTop && scrollPosition < sectionBottom) {
                navBtns.forEach(btn => btn.classList.remove('active'));
                navBtns[index].classList.add('active');
            }
        });
    }

    window.addEventListener('scroll', debounce(updateActiveButton, 100));
}

function initializeQAndA() {
    const qaItems = document.querySelectorAll('.qa-item');
    const itemsPerPage = 3;
    const totalPages = Math.ceil(qaItems.length / itemsPerPage);
    const paginationContainer = document.querySelector('.pagination');
	
	if (qaItems.length === 0) {
        paginationContainer.style.display = 'none'; // Q&A가 없으면 pagination 숨김
        return;
    }

    qaItems.forEach(function(qaItem) {
        const toggleButton = qaItem.querySelector('.toggle-answer');
        const answerElement = qaItem.querySelector('.answer');

        if (!answerElement || answerElement.textContent.trim() === '') {
            toggleButton.style.display = 'none';
        } else {
            toggleButton.addEventListener('click', function(event) {
                event.preventDefault();
                answerElement.style.display = answerElement.style.display === 'none' ? 'block' : 'none';
                this.textContent = answerElement.style.display === 'none' ? '답변 보기' : '답변 숨기기';
            });
        }
    });

    createPagination();
    showPage(1);

    function createPagination() {
        paginationContainer.innerHTML = '';
        addPaginationButton('«', 'prev');
        for (let i = 1; i <= totalPages; i++) {
            addPaginationButton(i.toString(), i);
        }
        addPaginationButton('»', 'next');
        document.querySelector('.pagination a[data-page="1"]').classList.add('active');
    }

    function addPaginationButton(text, page) {
        const button = document.createElement('a');
        button.href = '#';
        button.textContent = text;
        button.classList.add('page-link');
        button.dataset.page = page;
        paginationContainer.appendChild(button);
    }

    function showPage(pageNumber) {
        const start = (pageNumber - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        qaItems.forEach((item, index) => {
            item.style.display = (index >= start && index < end) ? 'block' : 'none';
        });

        document.querySelectorAll('.pagination .page-link').forEach(link => {
            link.classList.toggle('active', link.dataset.page == pageNumber);
        });

        document.querySelector('.pagination .page-link[data-page="prev"]').classList.toggle('disabled', pageNumber === 1);
        document.querySelector('.pagination .page-link[data-page="next"]').classList.toggle('disabled', pageNumber === totalPages);
    }

    paginationContainer.addEventListener('click', function(event) {
        event.preventDefault();
        if (event.target.classList.contains('page-link')) {
            const page = event.target.dataset.page;
            let currentPage = parseInt(document.querySelector('.pagination .page-link.active').dataset.page);

            if (page === 'prev') {
                showPage(Math.max(1, currentPage - 1));
            } else if (page === 'next') {
                showPage(Math.min(totalPages, currentPage + 1));
            } else {
                showPage(parseInt(page));
            }
        }
    });
}

function initializeReservation() {
    // URL에서 파라미터 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const urlStartDate = urlParams.get('startDate');
    const urlEndDate = urlParams.get('endDate');
    const urlDogType = urlParams.get('dogType');

    const dateRangePicker = flatpickr("#dateRange", {
        mode: "range",
        minDate: "today",
        dateFormat: "Y-m-d",
        onChange: function(selectedDates) {
            if (selectedDates.length === 2) {
                checkAvailability();
            }
        }
    });

    // URL에 날짜 파라미터가 있으면 date-picker에 설정
    if (urlStartDate && urlEndDate) {
        dateRangePicker.setDate([urlStartDate, urlEndDate]);
    }

    const dogTypeInputs = document.querySelectorAll('input[name="dog-size"]');
    const reserveBtn = document.getElementById('reserveBtn');
    const errorMessageElement = document.createElement('div');
    errorMessageElement.className = 'error-message';
    document.querySelector('.reservation-summary').appendChild(errorMessageElement);

    dogTypeInputs.forEach(input => {
        input.addEventListener('change', checkAvailability);
    });

    // URL 파라미터로 강아지 유형 설정
    if (urlDogType) {
        const dogTypeInput = document.querySelector(`input[name="dog-size"][value="${urlDogType.toLowerCase()}"]`);
        if (dogTypeInput) {
            dogTypeInput.checked = true;
        }
    }

    function checkAvailability() {
        const selectedDates = dateRangePicker.selectedDates;
        const selectedDogType = document.querySelector('input[name="dog-size"]:checked');

        if (selectedDates.length === 2 && selectedDogType) {
            showLoading(true);
            clearErrorMessage();

            const startDate = selectedDates[0].toISOString().split('T')[0];
            const endDate = selectedDates[1].toISOString().split('T')[0];
            const dogType = selectedDogType.value.toUpperCase();
            const hotelId = window.location.pathname.split('/').pop();

            fetch(`/hotel/availability?hotelId=${hotelId}&startDate=${startDate}&endDate=${endDate}&dogType=${dogType}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Received data:', data);
                    handleAvailabilityResponse(data);
                })
                .catch(handleAvailabilityError)
                .finally(() => showLoading(false));
        } else {
            updateReservationStatus(false, 0);
            clearErrorMessage();
        }
    }
    
    function handleAvailabilityError(error) {
        console.error('Error checking availability:', error);
        updateReservationStatus(false, 0);
        showErrorMessage("서버와의 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요. 오류 메시지: " + error.message);
    }

    function handleAvailabilityResponse(data) {
        if (data.code === "SU") {
            const availableRoom = data.availableRoom[0];
            if (availableRoom && availableRoom.availableRooms > 0) {
                updateReservationStatus(true, availableRoom.totalPrice);
            } else {
                updateReservationStatus(false, 0);
                showErrorMessage("선택하신 날짜에 예약 가능한 방이 없습니다.");
            }
        } else {
            updateReservationStatus(false, 0);
            showErrorMessage(data.message || "가용성 확인 중 오류가 발생했습니다.");
        }
    }


    function updateReservationStatus(isAvailable, price) {
        if (reserveBtn) {
            reserveBtn.disabled = !isAvailable;
            reserveBtn.classList.toggle('active', isAvailable);
        }
        updatePrice(price);
    }

    function updatePrice(price) {
        const totalPriceElement = document.getElementById('totalPrice');
        if (totalPriceElement) {
            totalPriceElement.textContent = Number(price).toLocaleString();
        }
    }

    function showLoading(isLoading) {
        if (reserveBtn) {
            if (isLoading) {
                const loadingElement = document.createElement('span');
                loadingElement.className = 'loading';
                reserveBtn.prepend(loadingElement);
                reserveBtn.disabled = true;
            } else {
                const loadingElement = reserveBtn.querySelector('.loading');
                if (loadingElement) {
                    loadingElement.remove();
                }
            }
        }
    }

    function showErrorMessage(message) {
        errorMessageElement.textContent = message;
        errorMessageElement.style.display = 'block';
    }

    function clearErrorMessage() {
        errorMessageElement.textContent = '';
        errorMessageElement.style.display = 'none';
    }

    // 페이지 로드 시 자동으로 가용성 체크
    if (urlStartDate && urlEndDate && urlDogType) {
        // URL 파라미터가 있을 때 약간의 지연 후 체크 (DOM이 완전히 로드되도록)
        setTimeout(checkAvailability, 100);
    }

    // 예약 버튼 클릭 이벤트
    if (reserveBtn) {
        reserveBtn.addEventListener('click', function() {
            if (!this.disabled) {
                const selectedDates = dateRangePicker.selectedDates;
                const selectedDogType = document.querySelector('input[name="dog-size"]:checked');
                if (selectedDates.length === 2 && selectedDogType) {
                    const startDate = selectedDates[0].toISOString().split('T')[0];
                    const endDate = selectedDates[1].toISOString().split('T')[0];
                    const dogType = selectedDogType.value.toUpperCase();
                    const hotelId = window.location.pathname.split('/').pop();

                    window.location.href = `/reservation?hotelId=${hotelId}&startDate=${startDate}&endDate=${endDate}&dogType=${dogType}`;
                } else {
                    showErrorMessage("날짜와 강아지 유형을 모두 선택해주세요.");
                }
            }
        });
    }
}



function initializeOtherFeatures() {
    // 리뷰 전체 보기
    var reviewAllBtn = document.querySelector('.review-all');
    if (reviewAllBtn) {
        reviewAllBtn.addEventListener('click', function() {
            window.location.href = '/reviews';
        });
    }

    // 문의 버튼
    var inquiryBtn = document.getElementById('inquiryBtn');
    var qnaSection = document.getElementById('qna');
    if (inquiryBtn && qnaSection) {
        inquiryBtn.addEventListener('click', function() {
            var navHeight = document.querySelector('.navi').offsetHeight;
            var offset = qnaSection.offsetTop - navHeight - 20;
            window.scrollTo({
                top: offset,
                behavior: 'smooth'
            });
        });
    }

    // 찜하기 기능
    var wishlistBtn = document.getElementById('wishlistBtn');
    if (wishlistBtn) {
        wishlistBtn.addEventListener('click', function() {
            this.classList.toggle('active');
            this.textContent = this.classList.contains('active') ? '찜 취소' : '찜';
        });
    }

    // 공유 기능
    var shareBtn = document.getElementById('shareBtn');
    if (shareBtn) {
        shareBtn.setAttribute('data-clipboard-text', window.location.href);
        var clipboard = new ClipboardJS('#shareBtn');

        clipboard.on('success', function(e) {
            alert('URL이 클립보드에 복사되었습니다.');
            e.clearSelection();
        });

        clipboard.on('error', function(e) {
            alert('클립보드 복사에 실패했습니다.');
        });
    }
}

function initializeMap() {
    var mapContainer = document.getElementById('map');
    if (!mapContainer) {
        console.error('Map container not found');
        return;
    }
    
    // HTML 요소에서 호텔 위치 정보 가져오기
    var hotelLocation = mapContainer.getAttribute('data-location');
	console.log("호텔 위치 정보#####################: ", hotelLocation); // 위치 정보를 확인

    var mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 기본 위치는 임의로 설정 (예: 제주도)
        level: 3 // 확대 레벨
    };

    var map = new kakao.maps.Map(mapContainer, mapOption);
    var geocoder = new kakao.maps.services.Geocoder();

    // 호텔 위치를 주소로 검색하여 지도에 마커 추가
    geocoder.addressSearch(hotelLocation, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            var infowindow = new kakao.maps.InfoWindow({
                content: '<div style="width:150px;text-align:center;padding:6px 0;">' + hotelLocation + '</div>'
            });
            infowindow.open(map, marker);

            // 지도 중심을 검색된 좌표로 이동
            map.setCenter(coords);
        } else {
            console.error('Geocoding failed: ' + status);
        }
    });
}

// 리뷰 별점 관련 js
function updateStarRating(averageRating) {
    const stars = document.querySelectorAll('.stars .star');
    stars.forEach((star, index) => {
        if (index < averageRating) {
            star.classList.remove('far'); // 빈 별 클래스 제거
            star.classList.add('fas'); // 꽉 찬 별 클래스 추가
        } else {
            star.classList.remove('fas'); // 꽉 찬 별 클래스 제거
            star.classList.add('far'); // 빈 별 클래스 추가
        }
    });
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function setEventId(eventId) {
       document.getElementById('event_id').value = eventId; // 히든 필드에 이벤트 ID 설정
       document.getElementById('event_move_form').submit(); // 폼 제출
   }