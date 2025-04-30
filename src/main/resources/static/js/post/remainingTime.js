document.addEventListener("DOMContentLoaded", function () {
    const isEvent = document.getElementById("isEvent").getAttribute("isEvent") === 'true';

    if (isEvent) {
        // 초기 계산 및 1초마다 업데이트
        updateRemainingTime();
        setInterval(updateRemainingTime, 1000);
    }

    function updateRemainingTime() {
        const endTimeElement = document.getElementById('endTime');
        const remainingTimeElement = document.getElementById('remainingTime');
        const endTime = new Date(endTimeElement.getAttribute('endTime'));

        // 종료 시간이 없는 경우 (null)
        if (!endTimeElement.getAttribute('endTime')) {
            remainingTimeElement.textContent = "별도 공지시까지";
            return;
        }

        // 현재 시간 가져오기
        const now = new Date();

        // 남은 시간 계산
        const timeDiff = endTime - now;

        if (timeDiff <= 0) {
            remainingTimeElement.textContent = "이벤트 종료";
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

});