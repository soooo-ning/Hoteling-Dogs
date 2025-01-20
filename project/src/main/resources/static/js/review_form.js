$(function(){
    // 리뷰 작성 폼에서 제출 버튼 클릭 시
    $("#uploadBtn").click(function(e){
        e.preventDefault();
        console.log("폼 제출 시");

        var formData = new FormData($("#reviews")[0]);
        var inputFile = $("input[name='image']");
        var files = inputFile[0].files;
        console.log(files);

        if (files.length > 0) {
            for (var i = 0; i < files.length; i++) {
                formData.append("image", files[i]);
            }
        }

        $.ajax({
            url: '/submitReview',
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            success: function(result) {
                alert("리뷰가 작성되었습니다.");
                window.location.href = '/reviewList';
            },
            error: function(xhr, status, error) {
                console.error("업로드 실패: ", error);
                alert("업로드 중 오류가 발생했습니다.");
            }
        });
    });
	
	
	/* ↓↓첨부파일 등록 시 미리보기 추가 ↓↓*/
   
	 // 이미지 첨부파일 미리보기 기능 추가
    $("input[name='image']").change(function(e) {
        const inputFile = e.target.files[0];
        const reader = new FileReader();

        // 이미지가 선택된 경우
        if (inputFile) {
            reader.onload = function(event) {
                const imgPreview = $('<img />', {
                    src: event.target.result,
                    alt: '미리보기 이미지'
                });

                // 이미지 스타일 설정 (고정 크기 및 라운드 모서리 적용)
                imgPreview.css({
                    "width": "150px", // 너비 고정
                    "height": "150px", // 높이 고정
                    "border-radius": "10px", // 모서리 둥글게
                    "object-fit": "cover" // 이미지를 컨테이너에 맞춤
                });

                // 기존 미리보기 이미지 제거 후 새로 추가
                $("#imagePreview").empty().append(imgPreview);
            };

            // 파일을 읽어 미리보기
            reader.readAsDataURL(inputFile);
        } else {
            // 파일이 없을 경우 미리보기 영역 초기화
            $("#imagePreview").empty();
        }
    });
});
