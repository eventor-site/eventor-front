// 각 조건의 상태를 추적하는 변수
let isPasswordValid = false;
let isPasswordMatch = false;

document.getElementById('password').addEventListener('input', validatePassword);
document.getElementById('confirmPassword').addEventListener('input', checkPasswordMatch);

function validatePassword() {
    const password = document.getElementById('password').value;
    const passwordFeedback = document.getElementById('passwordFeedback');
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/;

    // 규칙 검사
    if (!passwordRegex.test(password)) {
        passwordFeedback.innerHTML = "비밀번호는 최소 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다. <br>사용 가능한 특수문자: !@#$%^&*()_+";
        passwordFeedback.className = "mismatch";
        isPasswordValid = false;
    } else {
        passwordFeedback.textContent = "사용 가능한 비밀번호 입니다.";
        passwordFeedback.className = "match";
        isPasswordValid = true;
    }
    checkPasswordMatch();
}

function checkPasswordMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const confirmPasswordFeedback = document.getElementById('confirmPasswordFeedback');

    if (password === confirmPassword) {
        confirmPasswordFeedback.textContent = "비밀번호가 일치합니다.";
        confirmPasswordFeedback.className = "match";
        isPasswordMatch = true;
    } else {
        confirmPasswordFeedback.textContent = "비밀번호가 일치하지 않습니다.";
        confirmPasswordFeedback.className = "mismatch";
        isPasswordMatch = false;
    }
    updateButtonState();

}

// ✅ 현재 폼에 맞는 버튼을 활성화하는 함수
function updateButtonState() {
    // 현재 실행 중인 폼을 구분하기 위해 form 요소를 가져옴
    const signupForm = document.getElementById('signupForm');  // 회원가입 폼
    const updatePasswordForm = document.getElementById('updatePasswordForm');    // 비밀번호 변경 폼

    if (signupForm) {
        updateSignupButtonState();
    } else if (updatePasswordForm) {
        updateUpdatePasswordButtonState();
    }
}