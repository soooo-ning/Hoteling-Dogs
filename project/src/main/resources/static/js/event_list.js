$(function(){
    var imgChange = 0;
    var imgChangeTitle = 0;

    $("#userBtn").off("click").on("click", function(e) {
        e.preventDefault();
        $("#userType").val("1");
    });

    $("#adminBtn").off("click").on("click", function(e) {
        e.preventDefault();
        $("#userType").val("2");
    });

    function readPageMove(){
        var formData = new FormData($("#event_move_form")[0]);
        $("#event_move_form").submit();
    }

    // 기존에 작성된 리뷰 행 클릭 시 Layer 데이터 출력을 위한 클릭 func
    $("#eventArea").find('div[id$="_divArea"]').on("click", function(e){
        e.preventDefault();
        imgChange = 0;
        imgChangeTitle = 0;
        if($("#userType").val() === "1"){
            $("#event_move_form").find("#event_id").val($(this).attr("id").split("_")[0]);
            readPageMove();
            return ;
        }

        $("#content").prop("disabled", true);
        $("#title").prop("disabled", true);
        $("#image").prop("disabled", true);
        $("#title_image").prop("disabled", true);
        $("#event_title_file_view").removeClass("hide");
        $("#event_title_file_view").attr("style", "height:100px; overflow:auto;");
        $("#imgChg").val("0");

        let event_id = $(this).attr("id").split("_")[0];
        let event_title = $("#" + event_id + "_event_title").text();
        let event_file = $("#" + event_id + "_event_file");
        let event_title_file = $("#" + event_id + "_event_title_file");

        $("#eventModalCenter").find("#event_id").val(event_id);
        $("#title").val(event_title);
        $("#content").html(event_file.clone());
        $("#content").find("#" + event_id + "_event_file").removeClass("hide");
        $("#event_title_file_view").html(event_title_file.clone()); // 이미지 복제하여 표시
        
        $("#editButton").removeClass("hide");
        $("#saveButton, #deleteButton").addClass("hide");

        $("#eventModalCenter").modal('show');
        modalCloseBtn();
    });

    // 모달 안의 버튼 액션    
    function modalCloseBtn(){
        $("#eventModalCloseTop, #closeButton").off("click").on("click", function(e) {
            e.preventDefault();
            $("#eventModalCenter").modal('hide');
            resetModal();
        });

        $("#editButton").off("click").on("click", function(e) {
            e.preventDefault();
            $("#editButton").addClass("hide");
            $("#saveButton, #deleteButton").removeClass("hide");

            // 수정 버튼을 눌렀을 때만 필드 활성화
            $("#image").prop("disabled", false); // 이미지 변경도 활성화
            $("#title").prop("disabled", false);
            $("#title_image").prop("disabled", false);
        });

        $("#saveButton").off("click").on("click", function(e) {
            e.preventDefault();
            var formData = new FormData($("#events")[0]);

            if(imgChange > 0){
                var inputFile = $("input[name='image']");
                var files = inputFile[0].files;
				
                if (files.length > 0) {
                    for (var i = 0; i < files.length; i++) {
                        formData.append("image", files[i]);
                    }
                }
            }

            if(imgChangeTitle > 0){
                var inputFileTitle = $("input[name='title_image']");
                var filesTitle = inputFileTitle[0].files;
				console.log(filesTitle);
                if (filesTitle.length > 0) {
                    for (var i = 0; i < filesTitle.length; i++) {
                        formData.append("title_image", filesTitle[i]);
                    }
                }
            }

            formData.append("imgChg", imgChange);
            formData.append("imgChgTitle", imgChangeTitle);

            $.ajax({
                url: '/updateEvent', 
                processData: false,
                contentType: false,
                data: formData,
                type: 'POST',
                success: function(result) {
                    alert("업데이트가 완료되었습니다.");
                    window.location.href = '/eventlist';
                },
                error: function(xhr, status, error) {
                    console.error("업로드 실패: ", error);
                    alert("업로드 중 오류가 발생했습니다.");
                }
            });
        });

        $("#deleteButton").off("click").on("click", function(e) {
            e.preventDefault();
            if (confirm("정말로 삭제하시겠습니까?")) {
                var formData = new FormData($("#events")[0]);
                formData.append("imgChg", imgChange);
                formData.append("imgChgTitle", imgChangeTitle);
                $.ajax({
                    url: '/deleteEvent',
                    processData: false,
                    contentType: false,
                    data: formData,
                    type: 'POST',
                    success: function(result) {
                        alert("이벤트가 삭제되었습니다.");
                        window.location.href = '/eventlist';
                    },
                    error: function(xhr, status, error) {
                        console.error("삭제 실패: ", error);
                        alert("삭제 중 오류가 발생했습니다.");
                    }
                });
            }
        });

        $("input[name='image']").change(function() {
            imgChange = 1;
            $("#imgChg").val("1");
        });
		
		
		/* ↓↓타이틀 첨부파일 미리보기 수정 ↓↓*/
       
		
		 // *** 타이틀 이미지 변경 이벤트 수정 ***
        $("input[name='title_image']").change(function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const titleImage = document.createElement('img');
                    titleImage.src = e.target.result;
                    titleImage.style.maxWidth = '100%'; // 타이틀 이미지를 조정
                    $("#event_title_file_view").empty();
                    $("#event_title_file_view").css("height", "auto"); // 높이 자동 조정
                    $("#event_title_file_view").removeClass("hide"); // 미리보기 표시
                    $("#event_title_file_view").append(titleImage);
                    imgChangeTitle = 1;
                    $("#imgChgTitle").val("1");
                };
                reader.readAsDataURL(file);
            } else {
                // 파일이 없을 경우 미리보기 영역 초기화
                $("#event_title_file_view").empty();
                imgChangeTitle = 0;
                $("#imgChgTitle").val("0");
            }
        });

        $("#image, #title_image").on('click', function(e) {
            if ($(this).prop('disabled')) {
                e.preventDefault();
                e.stopImmediatePropagation();
            }
        });

        document.getElementById('image').addEventListener('change', function(event) {
            $("#content").empty();
            const file = event.target.files[0];
            const reader = new FileReader();
            reader.onload = function(e) {
                const contentDiv = document.getElementById('content');
                const imgElement = document.createElement('img');
                imgElement.src = e.target.result;
                contentDiv.appendChild(imgElement);
                imgChange = 1;
                $("#imgChg").val("1");
            };
            reader.readAsDataURL(file);
        });

		document.getElementById('title_image').addEventListener('change', function(event) {
		    const file = event.target.files[0];
		    if (file) {
		        const reader = new FileReader();
		        reader.onload = function(e) {
		            const titleImage = document.createElement('img');
		            titleImage.src = e.target.result;
		            titleImage.style.maxWidth = '100%'; // 이미지의 너비를 100%로 맞춤
		            titleImage.style.display = 'block'; // 이미지 블록 형태로 표시

		            // 기존 스타일을 유지하면서 스크롤을 추가하는 방식
		            $("#event_title_file_view").empty(); // 기존 내용 제거
		            $("#event_title_file_view").css({
		                "height": "100px",       // 기존 박스 높이 유지
		                "overflow": "auto",      // 스크롤 활성화
		                "border": "1px solid #ccc", // 테두리 유지 (필요시)
		                "padding": "5px"        // 내부 여백
		            });
		            $("#event_title_file_view").removeClass("hide"); // 박스 표시
		            $("#event_title_file_view").append(titleImage);  // 새 이미지 추가

		            imgChangeTitle = 1;
		            $("#imgChgTitle").val("1");
		        };
		        reader.readAsDataURL(file);
		    } else {
		        // 파일이 없을 경우 미리보기 영역 초기화
		        $("#event_title_file_view").empty();
		        imgChangeTitle = 0;
		        $("#imgChgTitle").val("0");
		    }
		});
    }

    function resetModal() {
        $("#events")[0].reset();
        $("#event_title_file_view").empty();
        $("#event_title_file_view").removeClass("hide");
        $("#content").prop("disabled", true);
        $("#image").prop("disabled", true);
        $("#title_image").prop("disabled", true);
    }
});
