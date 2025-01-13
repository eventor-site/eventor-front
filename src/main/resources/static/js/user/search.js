$(document).ready(function () {
    // 상태 유형 검색 기능
    $('#identifier').on('input', function () {
        const query = $(this).val();
        if (query.length > 0) { // 한 글자 이상 입력 시 검색
            $.ajax({
                url: '/users/search',
                type: 'GET',
                data: {keyword: query},
                success: function (data) {
                    $('#idResults').html(''); // 기존 검색 결과 초기화
                    data.forEach(function (user) {
                        $('#idResults').append(
                            '<div class="list-group-item list-group-item-action" data-name="' + user.identifier + '">' + user.identifier + '</div>');
                    });

                    // 결과 클릭 시 처리
                    $('.list-group-item').on('click', function () {
                        const identifier = $(this).data('name');
                        $('#identifier').val(identifier); // 선택된 상태 유형 설정
                        $('#idResults').html(''); // 결과 초기화
                    });
                }
            });
        } else {
            $('#idResults').html(''); // 입력이 없으면 결과 초기화
        }
    });
});