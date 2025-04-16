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
        const modalHtml = `
    <style>
      .oauth-circle-img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        object-fit: cover;
        margin: 10px;
      }
      .oauth-buttons {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
        margin-top: 20px;
      }
    </style>
    
        <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content p-3">
              <div class="modal-header">
                <h5 class="modal-title text-center fw-bold w-100" id="loginModalLabel">EVENTOR</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <form action="/auth/login" method="post">
                  <div class="mb-2">
                    <label for="identifier">아이디</label>
                    <input type="text" class="form-control" name="identifier" id="identifier" required>
                  </div>
                  <div class="mb-2">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" name="password" id="password" required>
                  </div>
                  <button type="submit" class="btn btn-primary w-100">로그인</button>
                  
                  <div class="oauth-buttons">
                    <a href="javascript:void(0);" onclick="openOAuthWindow('kakao')">
                        <img class="oauth-circle-img" alt="kakao login" src="/image/oauth/kakao_rectangular_login.png">
                    </a>
                    <a href="javascript:void(0);" onclick="openOAuthWindow('naver')">
                        <img class="oauth-circle-img" alt="naver login" src="/image/oauth/naver_circle_login.png">
                    </a>
                    <a href="javascript:void(0);" onclick="openOAuthWindow('google')">
                        <img class="oauth-circle-img" alt="google login" src="/image/oauth/google_circle_login.png">
                    </a>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>`;

        document.body.insertAdjacentHTML("beforeend", modalHtml);
        const modal = new bootstrap.Modal(document.getElementById('loginModal'));
        modal.show();
    }
});

// 부모 창에서 호출되는 함수
function openOAuthWindow(provider) {
    let authUrl = "/auth/oauth2/authorization/" + provider;
    window.open(authUrl, "_blank", "width=800,height=1000");
}