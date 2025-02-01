document.addEventListener('DOMContentLoaded', async () => {
    let profileContainer = document.querySelector('.profile-container');

    const roles = await getRoles();

    const adminPageLink = document.getElementById("adminPageLink");
    const myPageLink = document.getElementById("myPageLink");

    if (roles.includes("admin")) {
        // 로그인한 경우
        profileContainer.innerHTML = '<button class="btn btn-sm btn-outline-primary" id="logout">로그아웃</button>';
        adminPageLink.style.display = 'inline-block';  // 관리자페이지 링크 표시
        myPageLink.style.display = 'inline-block';    // 마이페이지 링크 표시
    } else if (roles.includes("member")) {
        profileContainer.innerHTML = '<button class="btn btn-sm btn-outline-primary" id="logout">로그아웃</button>';
        myPageLink.style.display = 'inline-block';    // 마이페이지 링크 표시
    } else {
        // 로그인하지 않은 경우
        profileContainer.innerHTML = '<button class="btn btn-sm btn-outline-primary" id="login">로그인</button>';
        adminPageLink.style.display = 'none';         // 관리자페이지 링크 숨김
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
                window.location.href = '/main';
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

const getRoles = async () => {
    try {
        const response = await fetch('/users/me/Roles', {
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