// document.getElementById('category-button').addEventListener('click', function () {
//     document.querySelector('.category-button').classList.toggle('active');
// });

// 마우스 이벤트로 하위 카테고리 표시
document.querySelectorAll('.category-item').forEach(item => {
    item.addEventListener('mouseenter', function () {
        const subCategory = this.querySelector('.sub-category');
        if (subCategory) {
            subCategory.style.display = 'block';
        }
    });

    item.addEventListener('mouseleave', function () {
        const subCategory = this.querySelector('.sub-category');
        if (subCategory) {
            subCategory.style.display = 'none';
        }
    });
});
