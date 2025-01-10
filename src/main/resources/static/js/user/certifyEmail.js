const sendEmail = async () => {
    const email = document.getElementById('email').value

    const response = await fetch(
        `/users/signup/sendEmail?email=${encodeURI(email)}`,
        {method: 'POST'}
    );

    const message = await response.text();

    if (message === email + '로 인증 번호를 전송했습니다.') {
        alert(message);
        showInputSignUpCode();
    } else {
        alert(message);
        document.getElementById('email').value = '';
    }
}

const showInputSignUpCode = () => {
    const codeInputRow = document.getElementById('verify-code-row');
    codeInputRow.style.display = '';
}

const certifySignUpCode = async () => {
    const emailInput = document.getElementById('email');
    const email = emailInput.value;
    const codeInput = document.getElementById('certify-code');
    const code = codeInput.value;
    const signupButton = document.getElementById('signupButton');
    const updateButton = document.getElementById('updateButton');

    const response = await fetch(`/users/signup/checkEmail?email=${encodeURI(email)}&certifyCode=${code}`);
    const message = await response.text();

    if (message === '인증되었습니다.') {
        alert(message);
        emailInput.setAttribute('readonly', 'true');
        codeInput.setAttribute('readonly', 'true');

        // 회원가입 버튼 활성화
        if (signupButton) {
            signupButton.removeAttribute('disabled');
        }

        // 수정폼일 경우 updateButton 활성화
        if (updateButton) {
            updateButton.removeAttribute('disabled');
        }
    } else {
        alert(message);
    }
}

let isEditingEmail = false; // 상태를 추적하는 변수

const toggleEmailEdit = () => {
    const emailInput = document.getElementById('email');
    const changeButton = document.getElementById('changeButton');
    const updateButton = document.getElementById('updateButton');

    if (!isEditingEmail) {
        // '변경' 상태 → 수정 가능 상태로 변경
        showInputSignUpCode();
        updateButton.setAttribute('disabled', 'true'); // 비활성화
        emailInput.removeAttribute('readonly'); // 입력 가능
        emailInput.focus(); // 입력칸에 포커스
        changeButton.textContent = '인증번호'; // 버튼 이름 변경
        isEditingEmail = true; // 상태 업데이트
    } else {
        // '인증번호' 상태 → 입력 비활성화 및 버튼 비활성화
        sendEmail(); // 인증번호 요청 함수 호출
    }
};
