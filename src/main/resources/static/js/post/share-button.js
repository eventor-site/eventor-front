function shareX() {
    var sendText = "이벤터"; // 전달할 텍스트
    var sendUrl = window.location.href; // 전달할 URL
    window.open("https://twitter.com/intent/tweet?text=" + encodeURIComponent(sendText) + "&url=" + encodeURIComponent(sendUrl));
}

function shareFacebook() {
    var sendUrl = window.location.href; // 전달할 URL
    window.open("http://www.facebook.com/sharer/sharer.php?u=" + encodeURIComponent(sendUrl));
}

document.addEventListener("DOMContentLoaded", function () {
    const postTitle = /*[[${post.title}]]*/ '제목';
    const postContent = /*[[${post.content}]]*/ '내용';
    const thumbnailUrl = /*[[${thumbnailUrl}]]*/ '썸네일';

    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = postContent;
    const plainTextContent = tempDiv.textContent || tempDiv.innerText || '';
    const shortDescription = plainTextContent.substring(0, 100) + '...';

    if (!Kakao.isInitialized()) {
        Kakao.init('e3949b86d8bbaedada3bb7fbdb5e6715');
    }

    Kakao.Link.createDefaultButton({
        container: '#btnKakao', // 클릭 대상 ID
        objectType: 'feed',
        content: {
            title: postTitle,
            description: shortDescription,
            imageUrl: thumbnailUrl,
            link: {
                mobileWebUrl: window.location.href,
                webUrl: window.location.href
            }
        },
        buttons: [
            {
                title: '게시글 보러가기',
                link: {
                    mobileWebUrl: window.location.href,
                    webUrl: window.location.href
                }
            }
        ]
    });

    const copyBtn = document.getElementById("copyBtn");

    if (copyBtn) {
        copyBtn.addEventListener("click", function () {
            const url = window.location.href;

            // 클립보드에 복사
            navigator.clipboard.writeText(url).then(() => {
                alertMessage("링크가 복사되었습니다!");
            }).catch((err) => {
                alertMessage("복사에 실패했습니다.");
            });
        });
    }
});
