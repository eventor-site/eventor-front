document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById("email");
    const certifyCodeInput = document.getElementById("certifyCode");
    const certifyButton = document.getElementById("certifyCodeButton");

    certifyButton.addEventListener("click", async () => {
        await certifyEmailCodeSignup(emailInput, certifyCodeInput, "회원가입");
    });
});

// 회원가입 버튼 활성화 여부를 확인하는 함수
function updateSignupButtonState() {
    const signupButton = document.getElementById('signupButton');

    if (isIdentifierValid && isPasswordValid && isPasswordMatch && isNicknameValid && isEmailCertified) {
        signupButton.removeAttribute('disabled');
    } else {
        signupButton.setAttribute('disabled', 'true');
    }
}

function updateCertifyButtonState() {
    if (isIdentifierValid) {
        certifyButton.removeAttribute('disabled');
    } else {
        certifyButton.setAttribute('disabled', 'true');
    }
}

const certifyEmailCodeSignup = async (emailInput, certifyCodeInput, type) => {
    const isSuccess = await certifyEmailCode(emailInput, certifyCodeInput, type)

    if (isSuccess) {
        emailInput.setAttribute('readonly', 'true');
        certifyCodeInput.setAttribute('readonly', 'true');
        isEmailCertified = true;
    } else {
        isEmailCertified = false;
    }

    updateSignupButtonState();
}
