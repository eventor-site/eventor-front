document.addEventListener('DOMContentLoaded', async () => {
    let commentFormContainer = document.querySelector('.comment-form-container');

    const hasTokens = await checkTokens();

    // 댓글 작성 폼 업데이트
    if (hasTokens) {
        commentFormContainer.innerHTML = `
            <form class="comment-form" id="commentForm" action="/posts/{postId}/comments" method="post">
                <div class="form-group">
                    <textarea class="form-control" name="content" rows="3" placeholder="댓글을 작성하세요..." required></textarea>
                </div>
                <button type="submit" class="btn btn-primary">댓글 등록</button>
            </form>
        `;
        document.getElementById('commentForm').addEventListener('submit', async (event) => {
            event.preventDefault();
            const content = event.target.elements['content'].value;

            try {
                const response = await fetch('/posts/{postId}/comments', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({content}),
                });

                if (response.ok) {
                    location.reload(); // 댓글 등록 후 페이지 새로고침
                } else {
                    console.error('댓글 등록 중 오류 발생');
                }
            } catch (error) {
                console.error('Error:', error);
            }
        });
    } else {
        commentFormContainer.innerHTML = `
            <p>댓글을 작성하려면 <a href="/auth/login">로그인</a>하세요.</p>
        `;
    }
});