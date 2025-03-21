// 각 조건의 상태를 추적하는 변수
let isEmailCertified = false;
// 인증 버튼과 관련된 요소 선택
const certifyButton = document.getElementById('certifyButton');

// 타이머 변수
let certifyTimer = null; // 인증 버튼 타이머

const sendEmail = async () => {
    const email = document.getElementById('email').value
    const type = document.getElementById('type').value
    let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/; // 이메일 정규식

    if (!emailRegex.test(email)) {
        alert("유효한 이메일 형식이 아닙니다.");
        return;
    }

    // 인증 버튼 비활성화 및 1분 카운트다운
    disableCertifyButton(60);

    const formData = new FormData();
    formData.append('email', email);
    formData.append('type', type);

    const response = await fetch('/users/signup/sendEmail', {
        method: 'POST',
        body: formData,
    })

    const json = await response.json();
    const message = json.message;
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

const certifyEmailCode = async (emailInput, certifyCodeInput, type) => {
    const email = emailInput.value;
    const certifyCode = certifyCodeInput.value;

    const formData = new FormData();
    formData.append('email', email);
    formData.append('type', type);
    formData.append('certifyCode', certifyCode);

    const response = await fetch('/users/signup/certifyEmail', {
        method: 'POST',
        body: formData,
    })

    const json = await response.json();
    const message = json.message;
    alert(message)

    return json.data;
}
