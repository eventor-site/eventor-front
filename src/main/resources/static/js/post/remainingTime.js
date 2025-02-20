const categoryName = document.getElementById("categoryName").getAttribute("data-category-name");

function updateRemainingTime() {

    if (categoryName !== "자유" && categoryName !== "공지" && categoryName !== "맛집" && categoryName !== "핫딜") {
        const endTimeElement = document.getElementById('endTime');
        const remainingTimeElement = document.getElementById('remainingTime');
        const endTime = new Date(endTimeElement.getAttribute('data-end-time'));

        // 현재 시간 가져오기
        const now = new Date();

        // 남은 시간 계산
        const timeDiff = endTime - now;

        if (timeDiff <= 0) {
            remainingTimeElement.textContent = "이벤트가 종료되었습니다.";
            return;
        }

        // 계산된 시간 -> 일, 시간, 분, 초로 변환
        const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

        // 남은 시간 표시
        remainingTimeElement.textContent =
            `${days}일 ${hours}시간 ${minutes}분 ${seconds}초`;
    }

}

if (categoryName !== "자유" && categoryName !== "공지" && categoryName !== "맛집" && categoryName !== "핫딜") {
    // 초기 계산 및 1초마다 업데이트
    updateRemainingTime();
    setInterval(updateRemainingTime, 1000);
}

