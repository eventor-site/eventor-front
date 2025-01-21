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
                            '<div class="list-group-item list-group-item-action" data-name="' + user.identifier + '">' + user.identifier + '</div>'
                        );
                    });

                    // 결과 클릭 시 처리
                    $('.list-group-item').on('click', function () {
                        const identifier = $(this).data('name');
                        $('#identifier').val(identifier); // 선택된 회원 ID 설정
                        $('#idResults').html(''); // 검색 결과 초기화
                        fetchUserStopHistory(identifier); // 회원 정지 이력 가져오기
                    });
                }
            });
        } else {
            $('#idResults').html(''); // 입력이 없으면 결과 초기화
        }
    });

    // 회원 정지 이력 가져오기
    function fetchUserStopHistory(identifier) {
        $.ajax({
            url: '/userStops/users',
            type: 'GET',
            data: {identifier: identifier},
            success: function (data) {
                const tbody = $('table tbody'); // 테이블의 tbody 선택
                tbody.html(''); // 기존 데이터 초기화

                if (data.length > 0) {
                    data.forEach(function (item) {
                        tbody.append(`
                            <tr>
                                <td>${item.reportTypeName}</td>
                                <td>${item.reportCount}</td>
                            </tr>
                        `);
                    });
                } else {
                    tbody.append('<tr><td colspan="5">회원 정지 내역이 없습니다.</td></tr>');
                }
            },
            error: function () {
                alert('회원 정지 이력을 가져오는 데 실패했습니다.');
            }
        });
    }
});
