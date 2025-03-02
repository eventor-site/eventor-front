document.addEventListener("DOMContentLoaded", function () {
    const categoryInput = document.getElementById("categoryName");
    const categorySelect = document.getElementById("categorySelect");

    // 드롭다운에서 선택 시 입력 필드에 반영
    categorySelect.addEventListener("change", function () {
        const selectedOption = categorySelect.options[categorySelect.selectedIndex];
        categoryInput.value = selectedOption.text;
        categorySelect.value = "";
    });

    $(document).ready(function () {
        // 카테고리 검색 기능
        $('#categoryName').on('input', function () {
            const query = $(this).val();
            if (query.length > 0) { // 한 글자 이상 입력 시 검색
                $.ajax({
                    url: '/categories/search',
                    type: 'GET',
                    data: {keyword: query},
                    success: function (data) {
                        $('#categoryResults').html(''); // 기존 검색 결과 초기화
                        data.forEach(function (category) {
                            $('#categoryResults').append(
                                '<div class="list-group-item list-group-item-action" data-name="' + category.name + '">' + category.name + '</div>'
                            );
                        });

                        // 결과 클릭 시 처리
                        $('.list-group-item').on('click', function () {
                            const categoryName = $(this).data('name');
                            $('#categoryName').val(categoryName); // 선택된 상태 유형 설정
                            $('#categoryResults').html(''); // 결과 초기화
                        });
                    }
                });
            } else {
                $('#categoryResults').html(''); // 입력이 없으면 결과 초기화
            }
        });
    });


});