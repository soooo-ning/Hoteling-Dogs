// 찜하기 기능을 위한 함수
function toggleWishlist(button) {
    const hotelId = parseInt(button.getAttribute('data-hotel-id'), 10);
    const heartIcon = button.querySelector('.fa-heart'); // 하트 아이콘 선택
    const isWished = heartIcon.classList.toggle('fa-solid'); // fa-solid 추가/제거
	
	// 로그인 여부 확인
   const userIdElement = document.getElementById('uId');
   const userId = userIdElement ? userIdElement.value : null;

   if (!userId) {
       alert("로그인이 필요합니다.");
       return; // 로그인하지 않았을 때 함수 종료
   }

    if (isWished) {
        heartIcon.classList.add('heart-filled'); // 찜했을 때 빨간색으로 변경
        heartIcon.classList.remove('fa-regular'); // 비어 있는 하트 제거
        addToWishlist(hotelId); // 찜하기 요청
    } else {
        heartIcon.classList.remove('heart-filled'); // 색상 초기화
        heartIcon.classList.add('fa-regular'); // 비어 있는 하트 추가
        removeFromWishlist(hotelId); // 찜 해제 요청
    }
}

// 찜 목록에 호텔 추가
function addToWishlist(hotelId) {
const userIdElement = document.getElementById('uId');
    const userId = userIdElement ? userIdElement.value : null;

    if (!userId) {
        alert("로그인이 필요합니다.");
        return; // 함수 종료
    }
	
    $.post('/wishlist/add', { hotelId: hotelId })
        .done(function (response) {
            alert("찜 목록에 추가되었습니다.");
            console.log("Add Response:", response);
        })
        .fail(function (xhr) {
            alert("찜 목록 추가 실패: " + xhr.responseText);
            console.error("Add Error:", xhr);
        });
}

// 찜 목록에서 호텔 제거
function removeFromWishlist(hotelId) {
    $.post('/wishlist/remove', { hotelId: hotelId })
        .done(function (response) {
            alert("찜 목록에서 제거되었습니다.");
            console.log("Remove Response:", response);
        })
        .fail(function (xhr) {
            alert("찜 목록 제거 실패: " + xhr.responseText);
            console.error("Remove Error:", xhr);
        });
}
// 페이지 로드 시 필터 초기화
window.onload = function() {
	const filterDropdown = document.getElementById('filterDropdown');
    const params = new URLSearchParams(window.location.search);
    const filter = params.get("filter") || 'category'; // URL에서 필터 값을 가져오고 기본값 설정
    filterDropdown.value = filter; // 드롭다운 값 설정
};

// 필터
function applyFilter() {
    var filter = document.getElementById("filterDropdown").value; // 필터 값 가져오기
    // 현재 URL의 파라미터를 모두 가져옴
    var params = new URLSearchParams(window.location.search);
    
    // 필터 값이 선택된 경우에만 추가
    if (filter) {
        params.set("filter", filter);
    }

    // 필터와 기존 검색어가 포함된 URL로 이동
    window.location.href = `/hotelList?${params.toString()}`;
}