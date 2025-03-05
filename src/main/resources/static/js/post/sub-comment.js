document.addEventListener("DOMContentLoaded", async function () {
    // 댓글 섹션에서 이벤트 위임
    const commentSection = document.querySelector(".comment-section");

    const isSubComment = await hasTokens();

    // 이벤트 위임 방식으로 "댓글 작성" 버튼 처리
    commentSection.addEventListener("click", function (event) {
        const button = event.target;

        // 해당 댓글의 postId와 commentId 가져오기
        const commentContainer = button.closest(".comment-container").parentElement;
        const postId = commentContainer.querySelector(".sub-comment-form-container").dataset.postId;
        const commentId = commentContainer.querySelector(".sub-comment-form-container").dataset.commentId;

        // 해당 댓글 아래의 sub-comment-form-container 찾기
        const subCommentContainer = commentContainer.querySelector(".sub-comment-form-container");

        // 클릭된 요소가 "대댓글 작성" 버튼인지 확인
        if (button.classList.contains("btn-outline-primary") && button.textContent.trim() === "대댓글 작성") {
            // 기존 폼이 존재하면 폼 삭제 (토글 동작)
            if (subCommentContainer.innerHTML.trim() !== "") {
                subCommentContainer.innerHTML = "";
                return;
            }

            // 대댓글 폼 생성 및 추가
            if (isSubComment) {
                subCommentContainer.innerHTML = `
                    <form class="comment-form" action="/posts/${postId}/comments" method="post">
                        <!-- hidden 필드로 commentId 전송 -->
                        <input type="hidden" name="parentCommentId" value="${commentId}"/>
        
                        <div class="form-group">
                            <textarea class="form-control comment-textarea" name="content" rows="3" 
                                placeholder="대댓글을 작성하세요..." maxlength="500" required></textarea>
                            <small class="char-count text-muted">0 / 500</small>
                        </div>
                        <button type="submit" class="btn btn-primary">댓글 등록</button>
                    </form>
                `;
                attachCharCountListener(subCommentContainer);
            } else {
                subCommentContainer.innerHTML = `<p>댓글을 작성하려면 <a href="/auth/login">로그인</a>하세요.</p>`;
            }

        } else if (button.classList.contains("btn-warning") && button.textContent.trim() === "수정") {
            // 기존 폼이 존재하면 폼 삭제 (토글 동작)
            if (subCommentContainer.innerHTML.trim() !== "") {
                subCommentContainer.innerHTML = "";
                return;
            }

            // 댓글 수정 폼 생성 및 추가
            if (isSubComment) {
                subCommentContainer.innerHTML = `
                    <form class="comment-form" action="/posts/${postId}/comments/${commentId}" method="post">
                        <input type="hidden" name="_method" value="PUT" />
                        <div class="form-group">
                            <textarea class="form-control comment-textarea" name="content" rows="3" 
                                placeholder="수정할 내용을 작성하세요..." maxlength="500" required></textarea>
                            <small class="char-count text-muted">0 / 500</small>
                        </div>
                        <button type="submit" class="btn btn-primary">댓글 수정</button>
                    </form>
                `;
                attachCharCountListener(subCommentContainer);
            } else {
                subCommentContainer.innerHTML = `<p>댓글을 수정하려면 <a href="/auth/login">로그인</a>하세요.</p>`;
            }
        }
    });

    // 글자 수 제한 기능 추가 함수
    function attachCharCountListener(container) {
        const textarea = container.querySelector(".comment-textarea");
        const charCount = container.querySelector(".char-count");

        textarea.addEventListener("input", () => {
            const currentLength = textarea.value.length;
            charCount.textContent = `${currentLength} / 500`;

            if (currentLength > 500) {
                charCount.style.color = "red";
            } else {
                charCount.style.color = "black";
            }
        });
    }
});
