document.addEventListener('DOMContentLoaded', async () => {
    const isDisplayed = await hasTokens();
    const loginButton = document.getElementById("login");
    const logoutButton = document.getElementById("logout");
    const myPageLink = document.getElementById("myPageLink");

    fetchTopKeywords();

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

function fetchTopKeywords() {
    fetch('/searches/topKeywords')
        .then(response => response.text())
        .then(html => {
            $('#topKeywords').replaceWith(html);

            $('.topKeywords-slider').slick({
                vertical: true,
                slidesToShow: 1,
                slidesToScroll: 1,
                infinite: true,
                autoplay: true,
                swipeToSlide: true,
                arrows: false,
                autoplaySpeed: 1000,
            });
        })
        .catch(error => {
            console.error('인기 검색어 가져오기 실패:', error);
        });
}
