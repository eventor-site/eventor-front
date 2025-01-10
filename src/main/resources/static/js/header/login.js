const login = async () => {
    const email = document.getElementById('loginButton').value

    const response = await fetch(
        `/auth/login`,
        {method: 'POST'}
    );

    const message = await response.text();
    alert(message);
}