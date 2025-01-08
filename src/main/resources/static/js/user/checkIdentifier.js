document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('identifier').addEventListener('blur', function () {
        const identifier = this.value.trim();
        const feedbackElement = document.getElementById('identifierFeedback');

        if (!identifier) {
            feedbackElement.textContent = '아이디를 입력해주세요.';
            feedbackElement.className = "mismatch";
            return;
        }

        const formData = new FormData();
        formData.append('identifier', identifier);

        fetch('/users/signUp/checkIdentifier', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.text())
            .then(message => {
                if (message === '사용 가능한 아이디 입니다.') {
                    feedbackElement.textContent = message;
                    feedbackElement.className = "match";
                } else {
                    feedbackElement.textContent = message;
                    feedbackElement.className = "mismatch";
                }
            })
            .catch(error => {
                feedbackElement.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                feedbackElement.style.setProperty('color', 'red', 'important');
                console.error(error);
            });
    });
});