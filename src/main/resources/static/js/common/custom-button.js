document.addEventListener("DOMContentLoaded", function () {
    // 버튼 생성
    const scrollTopBtn = document.createElement("button");
    scrollTopBtn.id = "scrollToTopBtn";
    scrollTopBtn.title = "맨 위로";
    scrollTopBtn.innerHTML = `<i class="fa-solid fa-arrow-up"></i>`;

    const scrollBottomBtn = document.createElement("button");
    scrollBottomBtn.id = "scrollToBottomBtn";
    scrollBottomBtn.title = "맨 아래로";
    scrollBottomBtn.innerHTML = `<i class="fa-solid fa-arrow-down"></i>`;

    // 추가
    document.body.appendChild(scrollTopBtn);
    document.body.appendChild(scrollBottomBtn);

    // 스크롤 이벤트
    window.addEventListener("scroll", function () {
        const shouldShow = window.scrollY > 100;
        scrollTopBtn.classList.toggle("show", shouldShow);
        scrollBottomBtn.classList.toggle("show", shouldShow);
    });

    // 스크롤 이동
    scrollTopBtn.addEventListener("click", function () {
        window.scrollTo({top: 0, behavior: "smooth"});
    });

    scrollBottomBtn.addEventListener("click", function () {
        window.scrollTo({top: document.body.scrollHeight, behavior: "smooth"});
    });
});
