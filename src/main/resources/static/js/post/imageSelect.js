// 전체 선택 버튼 클릭 시
document.getElementById('selectAllBtn').addEventListener('click', function () {
    const checkboxes = document.querySelectorAll('.imageCheckbox');
    checkboxes.forEach(checkbox => checkbox.checked = true);
});

// 전체 삭제 버튼 클릭 시
document.getElementById('deselectAllBtn').addEventListener('click', function () {
    const checkboxes = document.querySelectorAll('.imageCheckbox');
    checkboxes.forEach(checkbox => checkbox.checked = false);
});


