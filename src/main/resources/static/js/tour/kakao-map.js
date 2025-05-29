document.addEventListener('DOMContentLoaded', function () {
    const mapX = parseFloat(document.getElementById('mapX').textContent);
    const mapY = parseFloat(document.getElementById('mapY').textContent);

    if (!isNaN(mapX) && !isNaN(mapY)) {
        const container = document.getElementById('map');
        const options = {
            center: new kakao.maps.LatLng(mapY, mapX),
            level: 3
        };

        const map = new kakao.maps.Map(container, options);

        const markerPosition = new kakao.maps.LatLng(mapY, mapX);
        const marker = new kakao.maps.Marker({
            position: markerPosition
        });
        marker.setMap(map);
    } else {
        console.warn("유효하지 않은 좌표값입니다.", mapX, mapY);
    }
});