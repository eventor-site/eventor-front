$(document).ready(function () {
    // 상태 유형 검색 기능
    $('#statusTypeName').on('input', function () {
        const query = $(this).val();
        if (query.length > 0) { // 한 글자 이상 입력 시 검색
            $.ajax({
                url: '/statusTypes/search',
                type: 'GET',
                data: {keyword: query},
                success: function (data) {
                    $('#statusTypeResults').html(''); // 기존 검색 결과 초기화
                    data.forEach(function (statusType) {
                        $('#statusTypeResults').append(
                            '<div class="list-group-item list-group-item-action" data-name="' + statusType.name + '">' + statusType.name + '</div>');
                    });

                    // 결과 클릭 시 처리
                    $('.list-group-item').on('click', function () {
                        const statusTypeName = $(this).data('name');
                        $('#statusTypeName').val(statusTypeName); // 선택된 상태 유형 설정
                        $('#statusTypeResults').html(''); // 결과 초기화
                    });
                }
            });
        } else {
            $('#statusTypeResults').html(''); // 입력이 없으면 결과 초기화
        }
    });
});