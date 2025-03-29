document.addEventListener("DOMContentLoaded", function () {
    const videos = document.querySelectorAll("video");
    let currentPlaying = null; // 현재 재생 중인 비디오

    // 페이지 로드 시 음소거 상태 불러오기
    const isMuted = localStorage.getItem("videoMuted") === "true";
    videos.forEach(video => {
        video.muted = isMuted;
    });

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            const video = entry.target;
            const videoId = video.dataset.id || video.src; // 각 비디오를 구별할 수 있는 값

            if (entry.isIntersecting) {
                // 저장된 재생 위치 불러오기
                const lastTime = localStorage.getItem(`videoTime-${videoId}`);
                if (lastTime) {
                    video.currentTime = parseFloat(lastTime);
                }

                // 현재 재생 중인 비디오가 있고, 새로운 비디오와 다르면 정지
                if (currentPlaying && currentPlaying !== video) {
                    currentPlaying.pause();
                }

                // 새로운 비디오 재생 (play()는 promise이므로 catch 추가)
                video.play().catch(error => console.warn("비디오 재생 중단됨:", error));

                currentPlaying = video;
            } else {
                // 현재 재생 중인 비디오가 화면에서 사라지면 정지 (위치 저장)
                if (video === currentPlaying) {
                    localStorage.setItem(`videoTime-${videoId}`, video.currentTime);
                    video.pause();
                }
            }
        });
    }, {threshold: 0.5}); // 비디오의 50% 이상이 화면에 보이면 재생

    // 모든 비디오 요소에 Observer 적용
    videos.forEach(video => observer.observe(video));

    // 이벤트 리스너 추가 (음소거 설정 동기화)
    videos.forEach(video => {
        video.addEventListener("volumechange", function () {
            if (!video.muted) {
                // 사용자가 음소거 해제하면 모든 비디오 음소거 해제
                localStorage.setItem("videoMuted", "false");
                videos.forEach(v => v.muted = false);
            } else {
                localStorage.setItem("videoMuted", "true");
                videos.forEach(v => v.muted = true);
            }
        });
    });

    // 페이지를 벗어나면 모든 비디오의 재생 시간 기록 삭제
    window.addEventListener("beforeunload", function () {
        videos.forEach(video => {
            const videoId = video.dataset.id || video.src;
            localStorage.removeItem(`videoTime-${videoId}`);
        });
    });
});
