// 1. 사이드바 오른쪽 요소 가져오기
const sidebarRight = document.querySelector('.sidebar-right');

// 2. MutationObserver 정의
const observer = new MutationObserver((mutationsList, observer) => {
    for (const mutation of mutationsList) {
        if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
            if (sidebarRight.classList.contains('show')) {
                // show 클래스가 추가되었으면 => 사이드바 열림
                fetchRecentPosts();
            }
        }
    }
});

// 3. observer 시작 (class 속성 감시)
observer.observe(sidebarRight, {attributes: true});


// 최근 본 게시물 가져오기
function fetchRecentPosts() {
    fetch('/users/me/postViews')
        .then(response => response.text())
        .then(html => {
            $('#postViews').replaceWith(html);
        })
        .catch(error => {
            console.error('최근 본 게시물 가져오기 실패:', error);
        });
}
