document.getElementById('image').addEventListener('change', function(event) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onload = function(e) {
        const contentDiv = document.getElementById('content');
        const imgElement = document.createElement('img');
        imgElement.src = e.target.result;
        contentDiv.appendChild(imgElement);
    };
    reader.readAsDataURL(file);
});

document.getElementById('title_image').addEventListener('change', function(event) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onload = function(e) {
        const titleImage = document.createElement('img');
        titleImage.src = e.target.result;
        titleImage.style.maxWidth = '100%'; // 타이틀 이미지를 조정
        const contentDiv = document.getElementById('content');
        };
    reader.readAsDataURL(file);
});

$(function(){
	$("#submitBtn").click(function(e){
		e.preventDefault();
		console.log("폼 제출 시");
		
		var formData = new FormData($("#event_form")[0]);
		console.log(formData);
		var inputFile = $("input[name='image']");
		var files = inputFile[0].files;
		console.log(files);
		
		if (files.length > 0) {
            for (var i = 0; i < files.length; i++) {
                formData.append("image", files[i]);
            }
        }
		
		var inputFileTitle = $("input[name='title_image']");
		var filesTitle = inputFileTitle[0].files;
		console.log(filesTitle);
		
		if (filesTitle.length > 0) {
            for (var i = 0; i < filesTitle.length; i++) {
                formData.append("title_image", filesTitle[i]);
            }
        }
		
		console.log(formData);
		
		$.ajax({
            url: '/submitEvent', 
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            success: function(result) {
                alert("등록되었습니다.");
				// 관리자냐 아니냐에 따라 리뷰 영역을 보여줄지 말지 골라야 하는데.. 여기는 일반사용자만 하니까 ...
                window.location.href = '/eventlist';
            },
            error: function(xhr, status, error) {
                console.error("등록 실패: ", error);
                alert("등록 중 오류가 발생했습니다.");
            }
        });
    });    
});
