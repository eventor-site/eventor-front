function toggleImageLinks() {
    var imageLinksDiv = document.getElementById('image-links');
    var currentDisplay = imageLinksDiv.style.display;

    // 이미지 링크 영역의 표시/숨김 상태를 토글
    if (currentDisplay === 'none' || currentDisplay === '') {
        imageLinksDiv.style.display = 'flex';  // 링크를 보여줌
    } else {
        imageLinksDiv.style.display = 'none';  // 링크를 숨김
    }
}