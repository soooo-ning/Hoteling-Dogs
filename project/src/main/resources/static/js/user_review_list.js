const rowsPerPage = 10; // 페이지당 표시할 행 수
const table = document.getElementById('reviewTable');
const rows = $("#reviewTable").find("tbody>tr");
const pagination = document.getElementById('pagination');
const totalPages = Math.ceil(rows.length / rowsPerPage);

var imgChange = 0;

function displayPage(page) {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    // 모든 행 숨기기
    for (let i = 0; i < rows.length; i++) {
        rows[i].style.display = 'none';
    }

    // 현재 페이지에 해당하는 행 표시
    for (let i = start; i < end && i < rows.length; i++) {
        rows[i].style.display = '';
        $(rows[i]).find("td:first").text(i + 1);
    }
}

function createPagination() {
    pagination.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const a = document.createElement('a');
        a.textContent = i;
        a.href = '#';
        a.onclick = function(event) {
            event.preventDefault();
            displayPage(i);
        };
        pagination.appendChild(a);
    }
}

// 기존에 작성된 리뷰 행 클릭 시 Layer 데이터 출력을 위한 클릭 func
// 리뷰 테이블에서 행 클릭 시 이벤트
$("#reviewTable").find("tbody>tr").off("click").on("click", function(e){
    e.preventDefault();
    imgChange = 0;
    $("#reviewModalCenter").find('input[name="review_star"]').prop("disabled", true);
    $("#content").prop("disabled", true);
    $("#image").prop("disabled", true);
    $("#imgChg").val("0");

    let review_id = $(this).attr("id");
    let review_star = $("#" + review_id + "_review_star").val();
    let review_content = $("#" + review_id + "_review_content").val();
    let review_file = $("#" + review_id + "_review_file").find("img");

    $("#review_id").val(review_id);
    $("#star" + review_star).prop("checked", true);
    $("#content").val(review_content);

    // 기존 미리보기 이미지 복제하여 표시
    $("#review_file").html(review_file.clone());

    $("#editButton").removeClass("hide");
    $("#saveButton, #deleteButton").addClass("hide");

    $("#reviewModalCenter").modal('show');
    modalCloseBtn();
});

// 이미지 첨부 파일 변경 시, 미리보기 업데이트
// 리뷰 테이블에서 행 클릭 시 이벤트
$("#reviewTable").find("tbody>tr").off("click").on("click", function(e){
    e.preventDefault();
    imgChange = 0;
    $("#reviewModalCenter").find('input[name="review_star"]').prop("disabled", true);
    $("#content").prop("disabled", true);
    $("#image").prop("disabled", true);
    $("#imgChg").val("0");

    let review_id = $(this).attr("id");
    let review_star = $("#" + review_id + "_review_star").val();
    let review_content = $("#" + review_id + "_review_content").val();
    let review_file = $("#" + review_id + "_review_file").find("img");

    $("#review_id").val(review_id);
    $("#star" + review_star).prop("checked", true);
    $("#content").val(review_content);

    // 기존 미리보기 이미지 복제하여 표시 (라운드 모서리 적용)
    if (review_file.length > 0) {
        $("#review_file").html(review_file.clone().css({
            "max-width": "100%", 
            "height": "auto", 
            "border-radius": "10px", 
            "object-fit": "cover"
        }));
    } else {
        $("#review_file").empty();  // 이미지가 없을 경우 미리보기 초기화
    }

    $("#editButton").removeClass("hide");
    $("#saveButton, #deleteButton").addClass("hide");

    $("#reviewModalCenter").modal('show');
    modalCloseBtn();
});

// 이미지 첨부 파일 변경 시, 미리보기 업데이트
// 리뷰 테이블에서 행 클릭 시 이벤트
$("#reviewTable").find("tbody>tr").off("click").on("click", function(e){
    e.preventDefault();
    imgChange = 0;
    $("#reviewModalCenter").find('input[name="review_star"]').prop("disabled", true);
    $("#content").prop("disabled", true);
    $("#image").prop("disabled", true);
    $("#imgChg").val("0");

    let review_id = $(this).attr("id");
    let review_star = $("#" + review_id + "_review_star").val();
    let review_content = $("#" + review_id + "_review_content").val();
    let review_file = $("#" + review_id + "_review_file").find("img");

    $("#review_id").val(review_id);
    $("#star" + review_star).prop("checked", true);
    $("#content").val(review_content);

    // 기존 미리보기 이미지 복제하여 표시 (라운드 모서리 적용 및 크기 고정)
    if (review_file.length > 0) {
        $("#review_file").html(review_file.clone().css({
            "width": "150px", // 고정 너비
            "height": "150px", // 고정 높이
            "border-radius": "10px", 
            "object-fit": "cover"
        }));
    } else {
        $("#review_file").empty();  // 이미지가 없을 경우 미리보기 초기화
    }

    $("#editButton").removeClass("hide");
    $("#saveButton, #deleteButton").addClass("hide");

    $("#reviewModalCenter").modal('show');
    modalCloseBtn();
});

// 이미지 첨부 파일 변경 시, 미리보기 업데이트
document.getElementById('image').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const reviewImage = document.createElement('img');
            reviewImage.src = e.target.result;

            // 스타일 추가: 이미지 크기 고정 및 라운드 모서리 적용
            $(reviewImage).css({
                "width": "150px", // 고정 너비
                "height": "150px", // 고정 높이
                "border-radius": "10px", 
                "object-fit": "cover"
            });

            // 기존 이미지 제거 후 새 이미지 추가
            $("#review_file").empty(); 
            $("#review_file").append(reviewImage);

            imgChange = 1;
            $("#imgChg").val("1");
        };
        reader.readAsDataURL(file);
    } else {
        // 파일이 없을 경우 미리보기 영역 초기화
        $("#review_file").empty();
        imgChange = 0;
        $("#imgChg").val("0");
    }
});

// 모달 내의 버튼 동작 처리
function modalCloseBtn(){
    $("#reviewModalCloseTop, #closeButton").off("click").on("click", function(e) {
        e.preventDefault();
        $("#reviewModalCenter").modal('hide');
        resetModal();
    });

    $("#editButton").off("click").on("click", function(e) {
        e.preventDefault();
        $("#editButton").addClass("hide");
        $("#saveButton, #deleteButton").removeClass("hide");

        // 수정 버튼을 눌렀을 때만 필드 활성화
        $("#reviewModalCenter").find('input[name="review_star"], #content').prop("disabled", false);
        $("#image").prop("disabled", false); // 이미지 변경 활성화
    });

    $("#saveButton").off("click").on("click", function(e) {
        e.preventDefault();
        var formData = new FormData($("#reviews")[0]);
        var inputFile = $("input[name='image']");
        var files = inputFile[0].files;

        if (files.length > 0) {
            for (var i = 0; i < files.length; i++) {
                formData.append("image", files[i]);
            }
        }
        formData.append("imgChg", imgChange);

        $.ajax({
            url: '/updateReview',
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            success: function(result) {
                alert("업데이트가 완료되었습니다.");
                window.location.href = '/userReviewList';
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
            $.ajax({
                url: '/deleteReview',
                processData: false,
                contentType: false,
                data: new FormData($("#reviews")[0]),
                type: 'POST',
                success: function(result) {
                    alert("리뷰가 삭제되었습니다.");
                    window.location.href = '/userReviewList';
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
}

function resetModal() {
    $("#reviews")[0].reset();
    $("#review_file").empty();
    $("#reviewModalCenter").find('input[name="review_star"]').prop("disabled", true);
    $("#content").prop("disabled", true);
    $("#image").prop("disabled", true);
}

$(document).ready(function() {
    createPagination();
    displayPage(1);
});
