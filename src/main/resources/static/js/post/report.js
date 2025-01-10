const reportButton = document.getElementById('reportButton');
const reportPopup = document.getElementById('reportPopup');

// 신고 버튼 클릭 시 팝업 표시
reportButton.addEventListener('click', (event) => {
    const isVisible = reportPopup.style.display === 'block';
    reportPopup.style.display = isVisible ? 'none' : 'block';

    // 팝업 위치 설정
    const rect = reportButton.getBoundingClientRect();
    reportPopup.style.top = `${rect.bottom + window.scrollY + 5}px`;
    reportPopup.style.left = `${rect.left + window.scrollX}px`;
});

// 신고 유형 전송 함수
function submitReport(button) {
    const reportType = button.dataset.reportType; // 신고 유형 가져오기
    const url = reportButton.dataset.url; // URL 가져오기

    fetch(`${url}?reportTypeName=${reportType}`, {
        method: reportButton.dataset.method
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
            reportPopup.style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// 팝업 외부 클릭 시 닫기
document.addEventListener('click', (event) => {
    if (!reportPopup.contains(event.target) && event.target !== reportButton) {
        reportPopup.style.display = 'none';
    }
});
