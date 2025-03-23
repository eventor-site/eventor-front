document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById("email");
    const certifyCodeInput = document.getElementById("certifyCode");
    const certifyButton = document.getElementById("certifyCodeButton");
    const recoverButton = document.getElementById("recoverPasswordButton");
    const form = document.getElementById("recoverPasswordForm");

    certifyButton.addEventListener("click", async () => {
        await certifyEmailCodeRecoverPassword(emailInput, certifyCodeInput, "비밀번호 찾기");
    });

    recoverButton.addEventListener("click", function () {
        recoverButton.disabled = true;
        form.submit(); // 폼 제출
    });
});

function updateSignupButtonState() {
    const recoverPasswordButton = document.getElementById('recoverPasswordButton');

    if (isEmailCertified) {
        recoverPasswordButton.removeAttribute('disabled');
    } else {
        recoverPasswordButton.setAttribute('disabled', 'true');
    }
}

const certifyEmailCodeRecoverPassword = async (emailInput, certifyCodeInput, type) => {
    const isSuccess = await certifyEmailCode(emailInput, certifyCodeInput, type);

    if (isSuccess) {
        emailInput.setAttribute('readonly', 'true');
        certifyCodeInput.setAttribute('readonly', 'true');
        isEmailCertified = true;
    } else {
        isEmailCertified = false;
    }
    updateSignupButtonState();
}