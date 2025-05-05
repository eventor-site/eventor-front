function setCookie(name, value, hours) {
    const date = new Date();
    date.setTime(date.getTime() + (hours * 60 * 60 * 1000));
    document.cookie = `${name}=${value}; expires=${date.toUTCString()}; path=/`;
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}


function showAdModal() {
    fetch("/advertisement/eventModal")
        .then(response => response.text())
        .then(html => {
            document.body.insertAdjacentHTML("beforeend", html);

            const modalEl = document.getElementById("eventModal");
            const eventModal = new bootstrap.Modal(modalEl);
            eventModal.show();

            // 이벤트 바인딩
            modalEl.querySelector("#dismissTodayBtn")?.addEventListener("click", function () {
                setCookie("hideAdToday", "true", 24);
                eventModal.hide();
            });
        })
        .catch(err => {
            console.error("광고 모달 로딩 실패:", err);
        });
}

document.addEventListener("DOMContentLoaded", function () {
    if (!getCookie("hideAdToday")) {
        showAdModal();
    }
});