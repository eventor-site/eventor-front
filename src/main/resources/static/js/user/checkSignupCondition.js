// 회원가입 버튼 활성화 여부를 확인하는 함수
function updateSignupButtonState() {
    const signupButton = document.getElementById('signupButton');
    const certifyButton = document.getElementById('certify-button');
    
    certifyButton.disabled = !isIdentifierValid;

    if (isIdentifierValid && isPasswordMatch && isNicknameValid && isEmailCertified) {
        signupButton.removeAttribute('disabled');
    } else {
        signupButton.setAttribute('disabled', 'true');
    }
}