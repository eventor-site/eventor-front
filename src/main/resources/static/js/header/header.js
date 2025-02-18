document.addEventListener('DOMContentLoaded', async () => {
    const isDisplayed = await hasTokens();
    const loginButton = document.getElementById("login");
    const logoutButton = document.getElementById("logout");
    const myPageLink = document.getElementById("myPageLink");

    if (isDisplayed) {
        // 로그인한 경우
        loginButton.style.display = "none";
        logoutButton.style.display = "inline-block";
        myPageLink.style.display = 'inline-block';    // 마이페이지 링크 표시
    } else {
        // 로그인하지 않은 경우
        loginButton.style.display = "inline-block";
        logoutButton.style.display = "none";
        myPageLink.style.display = 'none';            // 마이페이지 링크 숨김
    }


    document.getElementById('logout')?.addEventListener('click', event => {
        event.preventDefault();

        console.log("로그아웃 버튼 클릭");

        fetch('/auth/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
        }).then(response => {
            if (response.ok) {
                window.location.replace('https://www.eventor.store/main');
            }
        }).catch(error => {
            console.error('로그아웃 중 오류 발생: ', error);
        });
    });

    document.getElementById('login')?.addEventListener('click', event => {
        event.preventDefault();
        window.location.href = '/auth/login';
    });

});

const hasTokens = async () => {
    try {
        const response = await fetch('/auth/hasTokens', {
            method: 'GET'
        });

        if (response.ok) {
            return await response.json();
        } else {
            console.error('토큰 여부 확인 중 오류 발생');
            return false;
        }
    } catch (error) {
        console.error('Error:', error);
        return false;
    }
}