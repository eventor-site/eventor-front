// 각 조건의 상태를 추적하는 변수
let isPasswordMatch = false;

document.addEventListener("DOMContentLoaded", function () {
    const newPassword = document.getElementById("newPassword");
    const confirmPassword = document.getElementById("confirmPassword");
    const feedback = document.getElementById("passwordFeedback");

    function checkPasswordMatch() {
        if (confirmPassword.value === "") {
            feedback.textContent = "";
            feedback.className = ""; // 초기화
            isPasswordMatch = false;
            return;
        }

        if (newPassword.value === confirmPassword.value) {
            feedback.textContent = "비밀번호가 일치합니다.";
            feedback.className = "match";
            isPasswordMatch = true;
        } else {
            feedback.textContent = "비밀번호가 일치하지 않습니다.";
            feedback.className = "mismatch";
            isPasswordMatch = false;
        }
        updateSignupButtonState();
    }

    // 입력 시마다 확인
    newPassword.addEventListener("input", checkPasswordMatch);
    confirmPassword.addEventListener("input", checkPasswordMatch);
});