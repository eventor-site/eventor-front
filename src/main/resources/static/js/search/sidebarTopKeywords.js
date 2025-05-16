function fetchSidebarTopKeywords() {
    fetch('/searches/topKeywords/sidebar')
        .then(response => response.text())
        .then(html => {
            $('#sidebarTopKeywords').replaceWith(html);
        })
        .catch(error => {
            console.error('인기 검색어 가져오기 실패:', error);
        });
}
