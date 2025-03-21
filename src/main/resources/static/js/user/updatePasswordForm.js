// 회원가입 버튼 활성화 여부를 확인하는 함수
function updateUpdatePasswordButtonState() {
    const updatePasswordButton = document.getElementById('updatePasswordButton');

    if (isPasswordValid && isPasswordMatch) {
        updatePasswordButton.removeAttribute('disabled');
    } else {
        updatePasswordButton.setAttribute('disabled', 'true');
    }
}
