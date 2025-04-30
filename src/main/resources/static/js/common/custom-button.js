document.addEventListener("DOMContentLoaded", function () {
    const scrollTopBtn = document.getElementById("scrollToTopBtn");
    const scrollBottomBtn = document.getElementById("scrollToBottomBtn");

    const shareBtns = document.getElementById("shareBtns");
    const shareBtn = document.getElementById("postDetailView") ? document.getElementById("shareBtn") : null;

    // 스크롤 이벤트
    window.addEventListener("scroll", function () {
        const scrollY = window.scrollY;
        const windowHeight = window.innerHeight;
        const documentHeight = document.documentElement.scrollHeight;

        const isAtBottom = scrollY + windowHeight >= documentHeight - 5; // 오차 보정
        const shouldShow = scrollY > 100 && !isAtBottom;

        scrollTopBtn.classList.toggle("show", shouldShow);
        scrollBottomBtn.classList.toggle("show", shouldShow);

        if (shareBtn != null) {
            shareBtn.classList.toggle("show", shouldShow);
        }

        if (shareBtns.classList.contains("show") && !shouldShow) {
            shareBtns.classList.remove("show");
        }

    });

    // 스크롤 이동
    scrollTopBtn.addEventListener("click", function () {
        window.scrollTo({top: 0, behavior: "smooth"});
    });

    scrollBottomBtn.addEventListener("click", function () {
        window.scrollTo({top: document.body.scrollHeight, behavior: "smooth"});
    });

    if (shareBtn != null) {
        shareBtn.addEventListener("click", function (e) {
            e.stopPropagation(); // 버튼 클릭 시 이벤트 전파 방지
            shareBtns.classList.toggle("show");
        });

        document.addEventListener("click", function (e) {
            if (shareBtns.classList.contains("show")) {
                shareBtns.classList.remove("show");
            }
        });

        const btnKakao = document.getElementById("btnKakao");
        if (btnKakao) {
            btnKakao.addEventListener("click", function () {
                // 카카오 링크 동작 전에 공유 리스트를 닫음
                shareBtns.classList.remove("show");
            });
        }
    }

});
