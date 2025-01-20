// 문서가 로드되면 실행
$(function() {
    // 날짜 선택기 초기화
    $('#dateRangePicker').daterangepicker({
        locale: {
            format: 'YYYY-MM-DD',
            applyLabel: '적용',
            cancelLabel: '취소',
            fromLabel: '시작',
            toLabel: '끝',
            weekLabel: '주',
            customRangeLabel: '사용자 정의',
            daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
            monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
            firstDay: 0
        },
        opens: 'center', // 드롭다운 위치 설정
        autoUpdateInput: true, // 사용자가 입력 필드에 날짜를 수동으로 입력할 수 있도록 설정
        startDate: moment().format('YYYY-MM-DD'), // 시작 날짜
        endDate: moment().add(1, 'days').format('YYYY-MM-DD') // 종료 날짜
    }, function(start, end) {
        // 선택된 날짜가 변경될 때 입력 필드에 날짜를 설정
        $('#dateRangePicker').val(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
    });
});


// 검색 리다이렉트 함수
function redirectToSearch() {
    const keyword = document.getElementById('searchInput').value.trim(); // 검색어
    let dogWeight1 = parseInt(document.getElementById('dogWeight1').value) || 0; // S 마리수
    let dogWeight2 = parseInt(document.getElementById('dogWeight2').value) || 0; // M 마리수
    let dogWeight3 = parseInt(document.getElementById('dogWeight3').value) || 0; // L 마리수
    const dateRange = document.getElementById('dateRangePicker').value;

    if (!keyword) {
        alert('검색어를 입력해주세요.'); // 검색어가 비어있으면 경고
        return false; // 리다이렉트 방지
    }
	
	if (dogWeight1 <= 0 && dogWeight2 <= 0 && dogWeight3 <= 0) {
        dogWeight1 = 1; // 기본값으로 스몰 1마리 설정
    } else {
        // 강아지 타입이 둘 이상 선택된 경우 나머지 수량을 0으로 설정
        if (dogWeight1 > 0 && (dogWeight2 > 0 || dogWeight3 > 0)) {
            dogWeight2 = 0; // 미디움 타입 수량을 0으로 설정
            dogWeight3 = 0; // 라지 타입 수량을 0으로 설정
        } else if (dogWeight2 > 0 && dogWeight3 > 0) {
            dogWeight1 = 0; // 스몰 타입 수량을 0으로 설정
        }
    }

    // 시작 날짜와 종료 날짜 처리
    let startDate, endDate;
    if (dateRange) {
        [startDate, endDate] = dateRange.split(' - ');
    } else {
        // 기본 날짜 설정
        startDate = new Date().toISOString().split('T')[0]; // 오늘 날짜
        endDate = new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().split('T')[0]; // 오늘 + 1일
    }

	// 검색 URL 형식에 맞춰서 리다이렉트 (기본 페이지는 0으로 설정)
    let url = `http://localhost:8080/hotelList?page=0&keyword=${encodeURIComponent(keyword)}&startDate=${startDate}&endDate=${endDate}`; 

    // 강아지 타입 설정
    if (dogWeight1 > 0) url += `&dogWeight1=${dogWeight1}`; // S 마리수 추가
    if (dogWeight2 > 0) url += `&dogWeight2=${dogWeight2}`; // M 마리수 추가
    if (dogWeight3 > 0) url += `&dogWeight3=${dogWeight3}`; // L 마리수 추가

    // 필터를 마지막에 추가
    url += `&filter=category`;

    // 검색 요청을 위해 URL로 리다이렉트
    window.location.href = url;

    return false; // 폼 제출을 방지
}

// dog hotel 누르면 서치바로 focus되게 함
function focusSearchBar() {
    const searchInput = document.getElementById('searchInput'); // 검색 입력 필드 ID
    searchInput.focus(); // 검색 입력 필드에 포커스
}

// 메뉴 드롭다운 

function toggleMenuDropdown() {
    console.log("메뉴 토글 함수 호출됨");
    event.stopPropagation(); // 이벤트 전파 방지
    const dropdown = document.getElementById('menuDropdownContent');
    dropdown.classList.toggle('show'); // show 클래스를 토글
}

// 드롭다운 토글 함수
function toggleDogDropdown() {
	console.log("토글 함수 호출됨");
	event.stopPropagation(); // 이벤트 전파 방지
	const dropdown = document.getElementById('dogDropdown');
	dropdown.classList.toggle('show'); // show 클래스를 토글
}

// 강아지 수 증가 기능
function increment(id) {
    const input = document.getElementById(id);
    input.value = parseInt(input.value) + 1; // 값 증가
}

// 강아지 수 감소 기능
function decrement(id) {
    const input = document.getElementById(id);
    if (input.value > 0) {
        input.value = parseInt(input.value) - 1; // 값 감소
    }
}

// 강아지 선택 적용 기능
function applyDogSelection() {
    const dogWeight1 = parseInt(document.getElementById('dogWeight1').value); // S 마리수
    const dogWeight2 = parseInt(document.getElementById('dogWeight2').value); // M 마리수
    const dogWeight3 = parseInt(document.getElementById('dogWeight3').value); // L 마리수

    // 총 강아지 수 계산
    const totalDogs = dogWeight1 + dogWeight2 + dogWeight3;

    // 강아지 버튼 텍스트 업데이트
    const dogButton = document.querySelector('.btn-outline-secondary');
    if (totalDogs > 0) {
        dogButton.textContent = `강아지 (S: ${dogWeight1}, M: ${dogWeight2}, L: ${dogWeight3})`; // 총 마리수 표시
    } else {
        dogButton.textContent = '강아지'; // 초기 상태로 설정
    }

    // 드롭다운 닫기
    toggleDogDropdown();
}

// 초기화 기능
function resetDogSelection() {
    document.getElementById('dogWeight1').value = 0;
    document.getElementById('dogWeight2').value = 0;
    document.getElementById('dogWeight3').value = 0;
    applyDogSelection(); // 버튼 텍스트도 초기화
}

// 드롭다운 외부 클릭 시 닫기
window.onclick = function(event) {
    const dropdown = document.getElementById('dogDropdown');
    const menuDropdownContent = document.getElementById('menuDropdownContent');

    // 클릭된 요소가 드롭다운이나 드롭다운의 자식 요소가 아닌 경우
    if (
        !dropdown.contains(event.target) &&
        !menuDropdownContent.contains(event.target) &&
        dropdown.classList.contains('show')
    ) {
        dropdown.classList.remove('show'); // show 클래스를 제거하여 드롭다운 닫기
    }

    // menuDropdownContent도 닫기
    if (
        !menuDropdownContent.contains(event.target) &&
        menuDropdownContent.classList.contains('show')
    ) {
        menuDropdownContent.classList.remove('show'); // show 클래스를 제거하여 드롭다운 닫기
    }
};

// 페이지 로드 시 부트스트랩 오프캔버스 초기화
document.addEventListener('DOMContentLoaded', function() {
    var offcanvasElementList = [].slice.call(document.querySelectorAll('.offcanvas'));
    var offcanvasList = offcanvasElementList.map(function(offcanvasEl) {
        return new bootstrap.Offcanvas(offcanvasEl); // 부트스트랩 오프캔버스 초기화
    });
});