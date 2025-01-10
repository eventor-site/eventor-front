// 신고 버튼 및 팝업 관리
document.querySelectorAll('.report-button').forEach((button) => {
    const popup = button.nextElementSibling;

    button.addEventListener('click', (event) => {
        const isVisible = popup.style.display === 'block';
        document.querySelectorAll('.report-popup').forEach(popup => popup.style.display = 'none'); // 다른 팝업 숨기기
        popup.style.display = isVisible ? 'none' : 'block';

        // 팝업 위치 설정
        const rect = button.getBoundingClientRect();
        popup.style.top = `${rect.bottom + window.scrollY + 5}px`;
        popup.style.left = `${rect.left + window.scrollX}px`;
    });
});

// 신고 유형 전송 함수
function submitReport(button) {
    const reportType = button.dataset.reportType; // 신고 유형 가져오기
    const popup = button.closest('.report-popup');
    const url = popup.previousElementSibling.dataset.url; // URL 가져오기
    const method = popup.previousElementSibling.dataset.method; // HTTP 메서드 가져오기

    fetch(`${url}?reportTypeName=${reportType}`, {
        method: method
    })
        .then(response => response.text())
        .then(message => {
            // 알림 표시
            const alertBox = document.getElementById('alertBox');
            const alertMessage = document.getElementById('alertMessage');
            alertMessage.innerText = message;
            alertBox.classList.add('show');

            // 5초 후 알림 숨김
            setTimeout(() => {
                alertBox.classList.remove('show');
            }, 5000);

            // 팝업 숨김
            popup.style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// 팝업 외부 클릭 시 닫기
document.addEventListener('click', (event) => {
    document.querySelectorAll('.report-popup').forEach(popup => {
        if (!popup.contains(event.target) && !popup.previousElementSibling.contains(event.target)) {
            popup.style.display = 'none';
        }
    });
});
