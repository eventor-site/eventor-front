let isNicknameValid = false // 닉네임 유효성 확인
let isEditingEmail = true; // 이메일 수정 상태 추적
isEmailCertified = true;

// 수정 버튼 활성화 여부를 확인하는 함수
function updateButtonState() {
    const updateButton = document.getElementById('updateButton');
    // 이메일 인증과 닉네임 유효성 조건 모두 만족해야 활성화
    if (isEmailCertified && isNicknameValid) {
        updateButton.removeAttribute('disabled');
    } else {
        updateButton.setAttribute('disabled', 'true');
    }
}

// 이메일 변경 버튼 동작
const toggleEmailEdit = () => {
    const emailInput = document.getElementById('email');
    const codeInput = document.getElementById('certifyCode');
    const updateButton = document.getElementById('updateButton');

    isEmailCertified = false; // 이메일 인증 초기화
    updateButton.setAttribute('disabled', 'true'); // 수정 버튼 비활성화

    if (!isEditingEmail) {
        // '변경' 버튼 클릭 시 수정 가능 상태로 변경
        emailInput.removeAttribute('readonly'); // 이메일 입력 가능
        emailInput.focus(); // 입력칸 포커스
        certifyButton.textContent = '인증번호'; // 버튼 이름 변경
        codeInput.setAttribute('required', 'true'); // 인증번호 필수 입력 설정
        isEditingEmail = true; // 상태 업데이트

    } else {
        // '인증번호' 상태 → 인증 요청 실행
        sendEmail(); // 이메일 인증번호 요청
        showInputSignUpCode();
    }
};

document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById("email");
    const certifyCodeInput = document.getElementById("certifyCode");
    const certifyButton = document.getElementById("certifyCodeButton");

    certifyButton.addEventListener("click", async () => {
        await certifyEmailCodeUpdate(emailInput, certifyCodeInput, "이메일 수정");
    });
});


const certifyEmailCodeUpdate = async (emailInput, certifyCodeInput, type) => {
    const isSuccess = await certifyEmailCode(emailInput, certifyCodeInput, type);

    if (isSuccess) {
        emailInput.setAttribute('readonly', 'true'); // 이메일 수정 불가
        certifyCodeInput.setAttribute('readonly', 'true'); // 인증번호 수정 불가
        certifyCodeInput.removeAttribute('required'); // 인증번호 필수 해제
        isEmailCertified = true; // 이메일 인증 완료
    }

    updateButtonState(); // 수정 버튼 상태 갱신
}

// 닉네임 유효성 검사
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('nickname').addEventListener('blur', function () {
        const nickname = this.value.trim();
        const feedbackElement = document.getElementById('nicknameFeedback');

        // 글자 수 체크 (1~10자 제한)
        if (nickname.length < 1 || nickname.length > 10) {
            feedbackElement.textContent = '10자 이하로 입력해주세요.';
            feedbackElement.className = "mismatch";
            isNicknameValid = false;
            updateButtonState();
            return;
        }

        if (!nickname) {
            feedbackElement.textContent = '닉네임을 입력해주세요.';
            feedbackElement.className = "mismatch";
            isNicknameValid = false;
            updateButtonState();
            return;
        }

        const formData = new FormData();
        formData.append('nickname', nickname);

        fetch('/users/me/checkNickname', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.text())
            .then(message => {
                if (message === '사용 가능한 닉네임 입니다.') {
                    feedbackElement.textContent = message;
                    feedbackElement.className = "match";
                    isNicknameValid = true;
                } else {
                    feedbackElement.textContent = message;
                    feedbackElement.className = "mismatch";
                    isNicknameValid = false;
                }
                updateButtonState();
            })
            .catch(error => {
                feedbackElement.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                feedbackElement.style.setProperty('color', 'red', 'important');
                isNicknameValid = false;
                updateButtonState();
                console.error(error);
            });
    });
});

// 이메일 인증번호 입력칸을 표시하는 함수 (필요 시 호출)
function showInputSignUpCode() {
    document.getElementById('verify-code-row').style.display = 'block';
}
