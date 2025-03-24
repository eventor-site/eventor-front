function endTypeLimitSelection(checkbox) {
    const checkboxes = document.getElementsByName('endType');
    if (checkbox.checked) {
        // 다른 체크박스들 체크 해제
        checkboxes.forEach((item) => {
            if (item !== checkbox) {
                item.checked = false;
            }
        });
    }
}