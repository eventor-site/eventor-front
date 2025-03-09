// 부모 창에서 호출되는 함수
function openOAuthWindow(provider) {
    let authUrl = "/auth/oauth2/authorization/" + provider;
    window.open(authUrl, "_blank", "width=800,height=1000");
}