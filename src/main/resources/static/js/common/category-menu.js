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

document.querySelectorAll('.eatery-category-item').forEach(item => {
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
