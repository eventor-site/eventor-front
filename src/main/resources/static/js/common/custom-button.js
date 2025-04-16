document.addEventListener("DOMContentLoaded", function () {
    const scrollBtn = document.createElement("button");
    scrollBtn.id = "scrollToTopBtn";
    scrollBtn.title = "맨 위로";
    scrollBtn.innerHTML = `<i class="fa-solid fa-arrow-up"></i>`;
    document.body.appendChild(scrollBtn);

    // 스타일 삽입
    const style = document.createElement("style");
    style.innerHTML = `
        #scrollToTopBtn {
            position: fixed;
            bottom: 30px;
            right: 30px;
            z-index: 1000;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            font-size: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            opacity: 0;
            visibility: hidden;
            cursor: pointer;
            box-shadow: 0 4px 6px rgba(0,0,0,0.3);
            transition: opacity 0.4s ease, visibility 0.4s ease;
        }

        #scrollToTopBtn:hover {
            background-color: #0056b3;
        }

        #scrollToTopBtn.show {
            opacity: 1;
            visibility: visible;
        }
    `;
    document.head.appendChild(style);

    // 스크롤 이벤트 감지
    window.addEventListener("scroll", function () {
        if (window.scrollY > 100) {
            scrollBtn.classList.add("show");
        } else {
            scrollBtn.classList.remove("show");
        }
    });

    // 버튼 클릭 → 부드럽게 최상단 이동
    scrollBtn.addEventListener("click", function () {
        window.scrollTo({ top: 0, behavior: "smooth" });
    });
});
