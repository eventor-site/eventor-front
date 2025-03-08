// 알림 박스를 설정하는 함수입니다.
// URL 과 HTTP 메서드, 성공 및 실패 메시지를 인수로 받아서 버튼 클릭 시 알림 박스에 메시지를 표시합니다.
function setupAlertBox(button) {
    // button 요소에서 데이터 속성 읽기
    const url = button.dataset.url;
    const method = button.dataset.method;

    fetch(url, {
        method: method,
        headers: {"X-Ajax-Request": "true"}
    })
        .then(response => response.text())
        .then(data => {
            // 알림 박스 설정
            const alertBox = document.getElementById('alertBox');
            const alertMessage = document.getElementById('alertMessage');

            alertMessage.innerText = data;
            alertBox.classList.add('show');

            // 5초 후 알림 박스 숨기기
            setTimeout(() => {
                alertBox.classList.remove('show');
            }, 5000);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}