document.addEventListener('DOMContentLoaded', async () => {
    let commentFormContainer = document.querySelector('.comment-form-container');
    const postId = commentFormContainer.dataset.postId; // Thymeleaf에서 렌더링된 data-post-id 값을 가져옴

    const isComment = await hasTokens();

    // 댓글 작성 폼 업데이트
    if (isComment) {
        commentFormContainer.innerHTML = `
            <form class="comment-form" id="commentForm" action="/posts/${postId}/comments" method="post">
                <div class="form-group">
                    <textarea class="form-control" name="content" rows="3" placeholder="댓글을 작성하세요..." required></textarea>
                </div>
                <button type="submit" class="btn btn-primary">댓글 등록</button>
            </form>
        `;
    } else {
        commentFormContainer.innerHTML = `
            <p>댓글을 작성하려면 <a href="/auth/login">로그인</a>하세요.</p>
        `;
    }
});
