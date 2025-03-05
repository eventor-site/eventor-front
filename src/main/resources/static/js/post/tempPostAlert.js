document.addEventListener("DOMContentLoaded", async function () {
    let categoryName = document.getElementById("categoryName")?.value;
    let tempPostId = document.getElementById("postId")?.value;

    if (tempPostId) {
        const result = await Swal.fire({
            title: "임시 게시물",
            text: "작성 중인 게시물이 존재합니다. 이어서 작성하시겠습니까?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "네",
            cancelButtonText: "아니오",
            allowOutsideClick: false,
            allowEscapeKey: false,
            backdrop: true
        });

        if (result.isConfirmed) {
            // 이어서 작성
            window.location.href = `/posts/${tempPostId}/updateForm`;
        } else {
            // 임시 게시물 삭제
            try {
                await fetch(`/images/temp`, {method: "DELETE"});
                await fetch(`/posts/temp`, {method: "DELETE"});
                
                window.location.href = `/posts/createForm?categoryName=${categoryName}`;
            } catch (error) {
                await Swal.fire({
                    title: "오류 발생",
                    text: "임시 게시물 삭제 중 오류가 발생했습니다.",
                    icon: "error",
                    confirmButtonText: "확인",
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    backdrop: true
                });
                window.location.href = "/main";
            }
        }
    }
});