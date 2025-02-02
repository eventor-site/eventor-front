function favorite(button) {
    // button 요소에서 데이터 속성 읽기
    const url = button.dataset.url;
    const method = button.dataset.method;

    const icon = document.getElementById('favoriteIcon'); // id로 아이콘 요소 찾기
    const isFavorite = icon.style.color === 'red'; // 현재 좋아요 상태 체크 (빨간색이면 true)

    fetch(url, {method: method})
        .then(response => response.text()) // 응답 데이터를 텍스트로 변환
        .then(message => {
            // 알림 박스 설정
            const alertBox = document.getElementById('alertBox');
            const alertMessage = document.getElementById('alertMessage');

            alertMessage.innerText = message;
            alertBox.classList.add('show');

            // 응답에 따라 색깔 변경
            if (isFavorite) {
                // 좋아요가 되어있으면 색을 회색으로 변경 (비활성화)
                icon.style.color = 'grey';
            } else {
                // 좋아요가 되어있지 않으면 색을 빨간색으로 변경 (활성화)
                icon.style.color = 'red';
            }

            // 5초 후 알림 박스 숨기기
            setTimeout(() => {
                alertBox.classList.remove('show');
            }, 5000);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
