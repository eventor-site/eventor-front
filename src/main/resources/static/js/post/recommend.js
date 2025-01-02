// function recommendPost() {
//     const postRecommendButton = document.querySelector('.postRecommendButton');
//     const postId = postRecommendButton.dataset.postId;
//
//     // 추천 요청
//     fetch(`/posts/${postId}/recommend`, {
//         method: 'PUT',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//     })
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Failed to recommend the post');
//             }
//             return response.json(); // 서버에서 변경된 추천수 반환
//         })
//         .then(updatedCount => {
//             // 추천수를 DOM에 반영
//             const recommendationCountElement = document.querySelector('.recommendation-count');
//             recommendationCountElement.textContent = updatedCount;
//         })
//         .catch(error => {
//             console.error('Error while recommending post:', error);
//         });
// }
