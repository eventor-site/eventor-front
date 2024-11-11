$(document).ready(function () {
    // selectAll 체크박스가 클릭될 때마다 실행
    $('#selectAll').click(function () {
        // selectAll 체크박스 상태에 따라 모든 selectedFoodCheckbox 를 선택/해제
        $('.selectedFoodCheckbox').prop('checked', this.checked);
    });

    // 개별 체크박스가 클릭될 때마다 전체 선택 상태를 조정
    $('.selectedFoodCheckbox').click(function () {
        // 모든 개별 체크박스가 선택된 상태면 selectAll 체크박스도 선택
        if ($('.selectedFoodCheckbox:checked').length === $('.selectedFoodCheckbox').length) {
            $('#selectAll').prop('checked', true);
        } else {
            // 하나라도 선택이 해제되면 selectAll 체크박스를 해제
            $('#selectAll').prop('checked', false);
        }
    });
});