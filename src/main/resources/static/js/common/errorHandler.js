// 원래 fetch 를 저장
const originalFetch = window.fetch;

window.fetch = async (...args) => {
    let responseBody;
    try {
        const response = await originalFetch(...args);

        if (!response.ok && response.status !== 0) {
            // 응답 본문을 한 번만 읽어서 저장
            responseBody = await response.text();

            if (response.status === 401) {
                alert("🔒" + responseBody);
                await fetch("/auth/logout", {method: "POST"})
                window.location.href = "/auth/login";

            }
            // 다른 에러 처리
            else {
                alert(`❌ 요청 실패 (${response.status}): ${responseBody}`);
            }
        }

        return response;
    } catch (error) {
        console.error("⚠️ 네트워크 오류 발생:", error);
        throw error;
    }
};