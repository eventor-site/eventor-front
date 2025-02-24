<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

document.addEventListener("DOMContentLoaded", function () {
    let tempPostId = document.getElementById("tempPostId")?.value;

    if (tempPostId) {
        let modal = document.getElementById("tempPostModal");
        modal.style.display = "block";

        document.getElementById("continue-writing").addEventListener("click", function () {
            window.location.href = `/posts/${tempPostId}/update`;
        });

        document.getElementById("discard-temp").addEventListener("click", async function () {
            const result = await Swal.fire({
                title: "임시 게시물 삭제",
                text: "정말 삭제하시겠습니까?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#3085d6",
                confirmButtonText: "삭제",
                cancelButtonText: "취소"
            });

            if (result.isConfirmed) {
                try {
                    await fetch(`/posts/temp/${tempPostId}`, {method: "DELETE"});
                    await fetch(`/images/temp/${tempPostId}`, {method: "DELETE"});

                    Swal.fire({
                        title: "삭제 완료",
                        text: "임시 게시물이 삭제되었습니다.",
                        icon: "success",
                        confirmButtonText: "확인"
                    }).then(() => {
                        window.location.href = "/posts/create";
                    });
                } catch (error) {
                    Swal.fire({
                        title: "오류 발생",
                        text: "임시 게시물 삭제 중 오류가 발생했습니다.",
                        icon: "error",
                        confirmButtonText: "확인"
                    });
                }
            }
        });
    }
});