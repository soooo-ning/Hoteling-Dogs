// 스크롤 이벤트 처리
window.onscroll = function() {
    const scrollUpBtn = document.getElementById("scrollUpIcon");
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        scrollUpBtn.style.display = "block"; // 스크롤이 50px 이상 내려가면 버튼 표시
    } else {
        scrollUpBtn.style.display = "none"; // 그렇지 않으면 버튼 숨기기
    }
};

// 클릭 이벤트 처리
document.getElementById("scrollUpIcon").onclick = function() {
    window.scrollTo({ top: 0, behavior: 'smooth' }); // 페이지 맨 위로 부드럽게 스크롤
};