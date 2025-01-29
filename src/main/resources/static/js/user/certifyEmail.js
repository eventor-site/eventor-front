// 각 조건의 상태를 추적하는 변수
let isEmailCertified = false;
// 인증 버튼과 관련된 요소 선택
const certifyButton = document.getElementById('certify-button');

// 타이머 변수
let certifyTimer = null; // 인증 버튼 타이머

const sendEmail = async () => {
    const identifier = document.getElementById('identifier').value
    let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/; // 이메일 정규식

    if (!emailRegex.test(identifier)) {
        alert("유효한 이메일 형식이 아닙니다.");
        return;
    }

    // 인증 버튼 비활성화 및 1분 카운트다운
    disableCertifyButton(60);

    const formData = new FormData();
    formData.append('email', identifier);

    const response = await fetch('/users/signup/sendEmail', {
        method: 'POST',
        body: formData,
    })

    const message = await response.text();
    alert(message);

}

// 인증 버튼 비활성화 및 카운트다운 함수
function disableCertifyButton(seconds) {
    certifyButton.disabled = true; // 버튼 비활성화
    let remainingTime = seconds;

    // 1초마다 카운트다운
    certifyTimer = setInterval(() => {
        certifyButton.textContent = `재전송 (${remainingTime}초)`;
        remainingTime--;

        // 카운트다운 종료
        if (remainingTime < 0) {
            clearInterval(certifyTimer);
            certifyButton.textContent = '인증번호';
            certifyButton.disabled = false; // 버튼 활성화
        }
    }, 1000);
}

const certifySignUpCode = async () => {
    const emailInput = document.getElementById('identifier');
    const email = emailInput.value;
    const codeInput = document.getElementById('certify-code');
    const code = codeInput.value;

    const response = await fetch(`/users/signup/checkEmail?email=${encodeURI(email)}&certifyCode=${code}`);
    const message = await response.text();

    if (message === '인증되었습니다.') {
        alert(message);
        emailInput.setAttribute('readonly', 'true');
        codeInput.setAttribute('readonly', 'true');
        isEmailCertified = true;
    } else {
        alert(message);
        isEmailCertified = false;
    }
    updateSignupButtonState();
}
