document.addEventListener("DOMContentLoaded", async function () {
    // 댓글 섹션에서 이벤트 위임
    const commentSection = document.querySelector(".comment-section");

    const hasTokens = await checkTokens();

    // 이벤트 위임 방식으로 "댓글 작성" 버튼 처리
    commentSection.addEventListener("click", function (event) {
        // 클릭된 요소가 "댓글 작성" 버튼인지 확인
        if (event.target.classList.contains("btn-outline-primary") && event.target.textContent.trim() === "대댓글 작성") {
            const button = event.target;

            // 해당 댓글의 postId와 commentId 가져오기
            const commentContainer = button.closest(".recommend-container").parentElement;
            const postId = commentContainer.querySelector(".sub-comment-form-container").dataset.postId;
            const commentId = commentContainer.querySelector(".sub-comment-form-container").dataset.commentId;

            // 해당 댓글 아래의 sub-comment-form-container 찾기
            const subCommentContainer = commentContainer.querySelector(".sub-comment-form-container");

            // 기존 폼이 존재하면 폼 삭제 (토글 동작)
            if (subCommentContainer.innerHTML.trim() !== "") {
                subCommentContainer.innerHTML = "";
                return;
            }

            // 대댓글 폼 생성 및 추가
            if (hasTokens) {
                subCommentContainer.innerHTML = `
                <form class="comment-form" action="/posts/${postId}/comments" method="post">
                    <!-- hidden 필드로 commentId 전송 -->
                    <input type="hidden" name="parentCommentId" value="${commentId}"/>
    
                    <div class="form-group">
                        <textarea class="form-control" name="content" rows="3" placeholder="대댓글을 작성하세요..." required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">댓글 등록</button>
                </form>
                `;
            } else {
                subCommentContainer.innerHTML = `
                    <p>댓글을 작성하려면 <a href="/auth/login">로그인</a>하세요.</p>
                `;
            }

        }
    });
});
