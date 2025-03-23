document.addEventListener("DOMContentLoaded", async function () {

    let errorElement = document.querySelector(".text-danger");
    let errorMessage;

    if (errorElement) {
        errorMessage = errorElement.getAttribute("data-error");
    }

    if (errorMessage === "탈퇴한 사용자입니다. 관리자에게 문의해 주세요.") {
        await Swal.fire({
            title: "탈퇴 계정",
            text: "탈퇴한 사용자입니다. 관리자에게 문의해 주세요.",
            icon: "warning",
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "확인",
            allowOutsideClick: false,
            allowEscapeKey: false,
            backdrop: true
        });

    }
});