document.addEventListener('DOMContentLoaded', function () {
    const searchForms = document.querySelectorAll('.search-form');

    searchForms.forEach(function (form) {
        form.addEventListener('submit', function (e) {
            const keywordInput = form.querySelector('.search-input');
            const keyword = keywordInput.value.trim();

            if (keyword) {
                saveRecentKeyword(keyword);
            }
        });
    });
});

function saveRecentKeyword(keyword) {
    const storageKey = 'recentKeywords';
    let keywords = JSON.parse(localStorage.getItem(storageKey)) || [];

    keywords = keywords.filter(k => k !== keyword);  // 중복 제거
    keywords.unshift(keyword);                      // 제일 앞에 추가

    if (keywords.length > 10) {                      // 10개까지만
        keywords = keywords.slice(0, 10);
    }

    localStorage.setItem(storageKey, JSON.stringify(keywords));
}

function loadRecentKeywords() {
    const storageKey = 'recentKeywords';
    const keywords = JSON.parse(localStorage.getItem(storageKey)) || [];

    const container = document.getElementById('recentKeywords');
    container.innerHTML = ''; // 초기화

    keywords.forEach((keyword, index) => {
        const wrapper = document.createElement('div');
        wrapper.className = 'keyword-item-wrapper';

        const div = document.createElement('div');
        div.className = 'keyword-item';
        div.textContent = keyword;
        div.onclick = function () {
            const searchInput = document.querySelector('.search-input');
            searchInput.value = keyword;
            searchInput.closest('form').submit(); // 클릭시 바로 검색
        };

        const deleteButton = document.createElement('span');
        deleteButton.className = 'delete-button';
        deleteButton.innerHTML = '×';
        deleteButton.onclick = function (e) {
            e.stopPropagation(); // 키워드 클릭 이벤트 막기
            deleteRecentKeyword(index);
        };

        wrapper.appendChild(div);
        wrapper.appendChild(deleteButton);
        container.appendChild(wrapper);
    });

    updateClearAllButton();
}


function deleteRecentKeyword(index) {
    const storageKey = 'recentKeywords';
    let keywords = JSON.parse(localStorage.getItem(storageKey)) || [];

    keywords.splice(index, 1); // 해당 인덱스 삭제

    localStorage.setItem(storageKey, JSON.stringify(keywords));
    loadRecentKeywords(); // 다시 렌더링
}

function clearAllRecentKeywords() {
    const storageKey = 'recentKeywords';
    localStorage.removeItem(storageKey);
    loadRecentKeywords();
}

function updateClearAllButton() {
    const storageKey = 'recentKeywords';
    const keywords = JSON.parse(localStorage.getItem(storageKey)) || [];

    const clearButton = document.getElementById('clearAllKeywords');

    if (keywords.length > 0) {
        // 검색어가 있으면 전체 삭제 버튼을 표시
        clearButton.style.display = 'inline-block';
    } else {
        // 검색어가 없으면 버튼 숨기기
        clearButton.style.display = 'none';
    }
}