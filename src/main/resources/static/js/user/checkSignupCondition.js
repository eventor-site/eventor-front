// 회원가입 버튼 활성화 여부를 확인하는 함수
function updateSignupButtonState() {
    const signupButton = document.getElementById('signupButton');
    if (isIdentifierValid && isPasswordMatch && isNicknameValid && isEmailCertified) {
        signupButton.removeAttribute('disabled');
    } else {
        signupButton.setAttribute('disabled', 'true');
    }
}