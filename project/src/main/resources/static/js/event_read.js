
document.addEventListener('DOMContentLoaded', () => {
    // 리뷰 데이터 예시 (실제 사용 시 서버에서 데이터를 받아오는 로직으로 대체해야 함)
    const reviewData = {
        title: "이벤트 제목",
        content: "이 이벤트에 대한 리뷰 내용이 여기에 들어갑니다. 리뷰 내용이 길어질 수 있으니 스크롤을 사용하여 모든 내용을 확인하세요.",
        imageUrl: "file:///C:/20240821_workspace/exm.jpg" // 로컬 파일 경로 또는 서버에서 제공되는 경로
    };

    // 리뷰 데이터 DOM에 삽입
    const reviewTitle = document.querySelector('.review-content h2');
    const reviewContent = document.querySelector('.review-content p');
    const reviewImage = document.querySelector('.review-content img');

    reviewTitle.textContent = reviewData.title;
    reviewContent.textContent = reviewData.content;
    reviewImage.src = reviewData.imageUrl;

    // 뒤로가기 버튼 클릭 시 동작
    const backButton = document.querySelector('.back-button');
    backButton.addEventListener('click', () => {
        history.back();
    });
});
