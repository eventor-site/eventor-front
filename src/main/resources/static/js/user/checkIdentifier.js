// 각 조건의 상태를 추적하는 변수
let isIdentifierValid = false;

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('email').addEventListener('blur', function () {
        const email = this.value.trim();
        const feedbackElement = document.getElementById('identifierFeedback');

        if (!email) {
            feedbackElement.textContent = '아이디를 입력해주세요.';
            feedbackElement.className = "mismatch";
            isIdentifierValid = false;
            updateSignupButtonState();
            updateCertifyButtonState();
            return;
        }

        // 이메일 형식 검사를 위한 정규표현식
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            feedbackElement.textContent = '유효한 이메일 형식이 아닙니다.';
            feedbackElement.className = "mismatch";
            isIdentifierValid = false;
            updateSignupButtonState();
            updateCertifyButtonState();
            return;
        }

        const formData = new FormData();
        formData.append('identifier', email);

        fetch('/users/signup/checkIdentifier', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.text())
            .then(message => {
                if (message === '사용 가능한 아이디 입니다.') {
                    feedbackElement.textContent = message;
                    feedbackElement.className = "match";
                    isIdentifierValid = true;
                } else {
                    feedbackElement.textContent = message;
                    feedbackElement.className = "mismatch";
                    isIdentifierValid = false;
                }
                updateSignupButtonState();
                updateCertifyButtonState();
            })
            .catch(error => {
                feedbackElement.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                feedbackElement.style.setProperty('color', 'red', 'important');
                isIdentifierValid = false;
                updateSignupButtonState();
                updateCertifyButtonState();
                console.error(error);
            });
    });
});