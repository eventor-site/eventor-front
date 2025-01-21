document.addEventListener("DOMContentLoaded", function () {
    const hash = window.location.hash;

    // 해시가 있는지 확인하고, '#' 제거한 후 안전한 선택자로 사용
    if (hash) {
        const cleanHash = hash.substring(1); // '#' 제거

        // 숫자나 특수문자가 포함되어 있으면 안전하게 escape 처리
        const commentElement = document.querySelector(`#${CSS.escape(cleanHash)}`);

        if (commentElement) {
            // 해당 댓글로 부드럽게 스크롤
            commentElement.scrollIntoView({behavior: "smooth", block: "center"});
            // 하이라이트 적용
            commentElement.classList.add("highlight");

            // 5초 후에 fade-out 클래스를 추가하여 하이라이트가 점차 옅어지도록 설정
            setTimeout(function () {
                commentElement.classList.add("fade-out");
            }, 5000);

            // 10초 후에 완전히 하이라이트 제거
            setTimeout(function () {
                commentElement.classList.remove("highlight", "fade-out");
            }, 10000);
        }
    }
});