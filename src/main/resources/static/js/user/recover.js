document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById("email");
    const certifyCodeInput = document.getElementById("certifyCode");
    const certifyButton = document.getElementById("certifyCodeButton");

    certifyButton.addEventListener("click", async () => {
        await certifyEmailCodeActive(emailInput, certifyCodeInput, "휴면계정 활성화");
    });
});

// 휴면해제 버튼 활성화 여부를 확인하는 함수
function updateActiveButtonState() {
    const signupButton = document.getElementById('activeButton');

    if (isEmailCertified) {
        signupButton.removeAttribute('disabled');
    } else {
        signupButton.setAttribute('disabled', 'true');
    }
}

const certifyEmailCodeActive = async (emailInput, certifyCodeInput, type) => {
    const isSuccess = await certifyEmailCode(emailInput, certifyCodeInput, type)

    if (isSuccess) {
        emailInput.setAttribute('readonly', 'true');
        certifyCodeInput.setAttribute('readonly', 'true');
        isEmailCertified = true;
    } else {
        isEmailCertified = false;
    }

    updateActiveButtonState();
}


document.addEventListener("DOMContentLoaded", async function () {

    const result = await Swal.fire({
        title: "휴면 계정",
        text: "로그인을 안한지 90일 이상이 지나 휴면계정으로 전환 되었습니다. 활성화를 시작하시겠습니까?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "네",
        cancelButtonText: "아니오",
        allowOutsideClick: false,
        allowEscapeKey: false,
        backdrop: true
    });

    if (!result.isConfirmed) {
        window.location.href = `/auth/login`;
    }
});