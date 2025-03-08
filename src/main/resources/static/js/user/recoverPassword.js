const recoverPassword = async () => {
    const identifier = document.getElementById('identifier').value

    const response = await fetch(
        `/users/recover/password?identifier=${encodeURI(identifier)}`,
        {method: 'POST'}
    );

    const message = await response.text();

    // alert(message); // 알림 메시지 출력
    alertMessage(message)
    // window.location.href = '/auth/login'; // 리다이렉트
}
