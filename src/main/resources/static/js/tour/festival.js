const postsPerPage = 10;
let currentPage = 0;

function showPage(page) {
    const allCards = document.querySelectorAll(".post-card");
    allCards.forEach((card, index) => {
        const start = page * postsPerPage;
        const end = start + postsPerPage;
        card.style.display = (index >= start && index < end) ? "block" : "none";
    });

    document.getElementById("currentPage").innerText = page + 1;
}

document.querySelector(".prev-btn").addEventListener("click", () => {
    if (currentPage > 0) {
        currentPage--;
        showPage(currentPage);
        window.scrollTo(0, 0);
    }
});

document.querySelector(".next-btn").addEventListener("click", () => {
    const totalCards = document.querySelectorAll(".post-card").length;
    const maxPage = Math.floor((totalCards - 1) / postsPerPage);
    if (currentPage < maxPage) {
        currentPage++;
        showPage(currentPage);
        window.scrollTo(0, 0);
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const totalCards = document.querySelectorAll(".post-card").length;
    document.getElementById("totalPages").innerText = Math.ceil(totalCards / postsPerPage);
    showPage(currentPage);
});