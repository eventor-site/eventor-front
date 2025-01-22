document.querySelectorAll('.category-item').forEach(item => {
    item.addEventListener('mouseenter', function () {
        const subCategory = this.nextElementSibling;
        if (subCategory && subCategory.classList.contains('sub-category')) {
            // 하위 카테고리 위치를 동적으로 설정하지 않아도
            // `position: absolute`와 `top: 100%`, `left: 0`으로 이미 아래에 표시됨
            subCategory.style.display = 'block';
        }
    });

    item.addEventListener('mouseleave', function () {
        const subCategory = this.nextElementSibling;
        if (subCategory && subCategory.classList.contains('sub-category')) {
            subCategory.style.display = 'none';
        }
    });
});