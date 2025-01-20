$(document).ready(function() {
    // 전역 함수로 정의
    window.sendNumber = function() {
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
            data: JSON.stringify({ "mail": email }),
            dataType: "json",
            success: function(response) {
                let message = response.message;
                alert(message);
            },
            error: function(xhr, status, error) {
                let errorMessage = `오류가 발생했습니다: ${status} ${xhr.status} ${xhr.statusText}. 서버 응답: ${xhr.responseText}`;
                alert(errorMessage);
            }
        });
    }

    window.confirmNumber = function() {
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
            data: JSON.stringify({ "mail": email, "verifyCode": number }),
            dataType: "json",
            success: function(response) {
                let message = response.message;
                alert(message);
            },
            error: function(xhr, status, error) {
                let errorMessage = `오류가 발생했습니다: ${status} ${xhr.status} ${xhr.statusText}. 서버 응답: ${xhr.responseText}`;
                alert(errorMessage);
            }
        });
    }

    // 강아지 정보 추가 기능
    document.getElementById('addDogInfoButton').addEventListener('click', function() {
        var container = document.getElementById('dog-info-container');
        var dogInfoIndex = container.children.length + 1;
        var dogInfoDiv = document.createElement('div');
        dogInfoDiv.className = 'dog-info-item';
        dogInfoDiv.innerHTML = `
            <h4>강아지 ${dogInfoIndex}</h4>
            <input type="hidden" name="dogInfos[${dogInfoIndex - 1}].id" />
            <div class="form-section">
                <label class="form-label">이름</label>
                <input type="text" class="form-input" name="dogInfos[${dogInfoIndex - 1}].dogName" />
            </div>
            <div class="form-section">
                <label class="form-label">성별</label>
                <select class="form-input" name="dogInfos[${dogInfoIndex - 1}].dogGender">
                    <option value="MALE">남</option>
                    <option value="FEMALE">여</option>
                </select>
            </div>
            <div class="form-section">
                <label class="form-label">생년월일</label>
                <input type="date" class="form-input" name="dogInfos[${dogInfoIndex - 1}].dogBirth" />
            </div>
            <div class="form-section">
                <label class="form-label">중성화 여부</label>
                <input type="checkbox" name="dogInfos[${dogInfoIndex - 1}].neutered"/>
                <span>중성화된 경우에만 체크해주세요</span>
            </div>
            <div class="form-section">
                <label class="form-label">추가정보</label>
                <textarea class="form-input" name="dogInfos[${dogInfoIndex - 1}].additionalInfo"></textarea>
            </div>
            <span class="remove-dog-info text-danger" onclick="removeDogInfo(this)">삭제</span>
            <hr />
        `;

        // 강아지 정보 추가 폼을 회원가입 버튼 위에 추가
        var submitButton = document.querySelector('.text-center .btn-submit');
        container.appendChild(dogInfoDiv);
    });

    // 강아지 정보 삭제 기능
    window.removeDogInfo = function(element) {
        var container = document.getElementById('dog-info-container');
        container.removeChild(element.parentElement);
    }
});
