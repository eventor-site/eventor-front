document.addEventListener("DOMContentLoaded", function () {
    const currentPath = window.location.pathname;

    // "/posts" 또는 "/" 경로에서만 작동
    if (!(currentPath === "/" || currentPath.startsWith("/posts"))) return;

    const cookies = document.cookie.split(";").map(c => c.trim());
    const countCookie = cookies.find(c => c.startsWith("guest-access-count="));
    const tokenCookie = cookies.find(c => c.startsWith("access-token="));

    if (!tokenCookie && countCookie) {
        const count = parseInt(countCookie.split("=")[1]);
        if (count >= 5) {
            // 모달 띄우기
            showLoginModal();
        }
    }

    function showLoginModal() {
        fetch("/auth/login/modal") // 이 경로는 실제 위치에 맞게 조정
            .then(response => response.text())
            .then(html => {
                document.body.insertAdjacentHTML("beforeend", html);
                const modal = new bootstrap.Modal(document.getElementById('loginModal'));
                modal.show();
            })
            .catch(err => {
                console.error("모달 HTML 로딩 실패:", err);
            });
    }
});

// 부모 창에서 호출되는 함수
function openOAuthWindow(provider) {
    let authUrl = "/auth/oauth2/authorization/" + provider;
    window.open(authUrl, "_blank", "width=800,height=1000");
}