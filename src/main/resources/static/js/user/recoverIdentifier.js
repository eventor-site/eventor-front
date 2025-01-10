const recoverIdentifier = async () => {
    const email = document.getElementById('email').value

    const response = await fetch(
        `/users/recover/identifier?email=${encodeURI(email)}`,
        {method: 'POST'}
    );

    const message = await response.text();

    alert(message); // 알림 메시지 출력
    window.location.href = '/auth/login'; // 리다이렉트

}
