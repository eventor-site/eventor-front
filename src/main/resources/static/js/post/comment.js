document.addEventListener('DOMContentLoaded', async () => {
    let commentFormContainer = document.querySelector('.comment-form-container');
    const postId = commentFormContainer.dataset.postId; // Thymeleaf에서 렌더링된 data-post-id 값을 가져옴

    const isComment = await hasTokens();

    // 댓글 작성 폼 업데이트
    if (isComment) {
        commentFormContainer.innerHTML = `
            <form class="comment-form" id="commentForm" action="/posts/${postId}/comments" method="post">
                <div class="mb-2">
                    <textarea class="form-control" name="content" id="commentContent" rows="3" 
                        placeholder="댓글을 작성하세요..." maxlength="500" required></textarea>
                    <small id="charCount" class="text-muted">0 / 500</small>
                </div>
                <button type="submit" class="btn btn-primary">댓글 등록</button>
            </form>
        `;

        // 댓글 글자 수 제한 기능 추가
        const commentContent = document.getElementById('commentContent');
        const charCount = document.getElementById('charCount');

        commentContent.addEventListener('input', () => {
            const currentLength = commentContent.value.length;
            charCount.textContent = `${currentLength} / 500`;

            if (currentLength > 500) {
                charCount.style.color = 'red';
            } else {
                charCount.style.color = 'black';
            }
        });
    } else {
        commentFormContainer.innerHTML = `
            <p>댓글을 작성하려면 <a href="/auth/login">로그인</a>하세요.</p>
        `;
    }
});
