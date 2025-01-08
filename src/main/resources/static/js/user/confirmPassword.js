document.addEventListener("DOMContentLoaded", function () {
    const newPassword = document.getElementById("newPassword");
    const confirmPassword = document.getElementById("confirmPassword");
    const feedback = document.getElementById("passwordFeedback");

    function checkPasswordMatch() {
        if (confirmPassword.value === "") {
            feedback.textContent = "";
            feedback.className = ""; // 초기화
            return;
        }

        if (newPassword.value === confirmPassword.value) {
            feedback.textContent = "비밀번호가 일치합니다.";
            feedback.className = "match";
        } else {
            feedback.textContent = "비밀번호가 일치하지 않습니다.";
            feedback.className = "mismatch";
        }
    }

    // 입력 시마다 확인
    newPassword.addEventListener("input", checkPasswordMatch);
    confirmPassword.addEventListener("input", checkPasswordMatch);
});