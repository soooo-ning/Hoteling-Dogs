function cancelEdit() {
    // 수정 페이지에서 취소 버튼 클릭 시 리디렉션할 URL
    window.location.href = '/security-login/info/modify';
}

$(document).ready(function () {
    // 전역 함수로 정의
    window.sendNumber = function () {
        const email = $("#uEmail").val();
        if (!email) {
            alert("이메일 주소를 입력해 주세요");
            return;
        }

        $("#verification-section").css("display", "block");
        $.ajax({
            url: "/security-login/send-email",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify({"mail": email}),
            dataType: "json",
            success: function (response) {
                let message = response.message;
                alert(message);
            },
            error: function (xhr, status, error) {
                let errorMessage = `오류가 발생했습니다: ${status} ${xhr.status} ${xhr.statusText}. 서버 응답: ${xhr.responseText}`;
                alert(errorMessage);
            }
        });
    }

    window.confirmNumber = function () {
        const email = $("#uEmail").val();
        const number = $("#emailCode").val();

        if (!email || !number) {
            alert("이메일과 인증 코드를 모두 입력해 주세요.");
            return;
        }

        $.ajax({
            url: "/security-login/verify",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify({"mail": email, "verifyCode": number}),
            dataType: "json",
            success: function (response) {
                let message = response.message;
                alert(message);
            },
            error: function (xhr, status, error) {
                let errorMessage = `오류가 발생했습니다: ${status} ${xhr.status} ${xhr.statusText}. 서버 응답: ${xhr.responseText}`;
                alert(errorMessage);
            }
        });
    }
});
