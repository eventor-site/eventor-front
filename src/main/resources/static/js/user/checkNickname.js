// 각 조건의 상태를 추적하는 변수
let isNicknameValid = false;

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('nickname').addEventListener('blur', function () {
        const nickname = this.value.trim();
        const feedbackElement = document.getElementById('nicknameFeedback');

        if (!nickname) {
            feedbackElement.textContent = '닉네임을 입력해주세요.';
            feedbackElement.className = "mismatch";
            isNicknameValid = false;
            updateSignupButtonState();
            return;
        }

        const formData = new FormData();
        formData.append('nickname', nickname);

        fetch('/users/signup/checkNickname', {
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
                updateSignupButtonState();
            })
            .catch(error => {
                feedbackElement.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                feedbackElement.style.setProperty('color', 'red', 'important');
                isNicknameValid = false;
                updateSignupButtonState();
                console.error(error);
            });
    });
});