document.addEventListener('DOMContentLoaded', async function () {
    let postId = document.getElementById("postId").value; // 포스트 이름

    const MAX_SIZE = 10 * 1024 * 1024; // 최대 용량 10MB

    const totalSizeElement = document.getElementById('totalSize');
    const categoryName = document.getElementById("categoryName").value; // 카테고리 이름 가져오기
    const categoryType = document.getElementById("categoryType").value; // 카테고리 유형 가져오기

    previewImageEvent();
    InitializeTotalSize(totalSizeElement);  // 초기 totalSize 설정

    function formatSize(sizeInBytes) {
        const sizeInMB = sizeInBytes / (1024 * 1024); // MB 단위로 변환
        if (sizeInMB >= 1) {
            return `${sizeInMB.toFixed(2)}MB`;
        } else {
            const sizeInKB = sizeInBytes / 1024; // KB 단위로 변환
            if (sizeInKB >= 1) {
                return `${sizeInKB.toFixed(2)}KB`;
            } else {
                return `${sizeInBytes}B`; // 1KB 미만은 바이트 단위로 출력
            }
        }
    }

    function InitializeTotalSize(totalSizeElement) {
        const totalSizeText = totalSizeElement.textContent.trim(); // `totalSize` 값 가져오기

        // totalSize 값이 문자열로 되어 있을 수 있으므로 파싱해서 숫자로 처리
        const totalSize = parseFloat(totalSizeText);

        if (!isNaN(totalSize)) {
            imageSelector(totalSize);
            totalSizeElement.textContent = formatSize(totalSize);  // 변환된 용량 출력
        } else {
            console.error("Invalid total size value");
        }
    }

    // 현재 업로드된 용량
    async function getTotalSize() {
        const totalSizeText = totalSizeElement.textContent.trim();

        // MB, KB, B 단위로 분리하여 값 추출
        let sizeInBytes = 0;

        if (totalSizeText.includes('MB')) {
            const sizeInMB = parseFloat(totalSizeText.replace('MB', '').trim());
            sizeInBytes = sizeInMB * 1024 * 1024; // MB -> 바이트
        } else if (totalSizeText.includes('KB')) {
            const sizeInKB = parseFloat(totalSizeText.replace('KB', '').trim());
            sizeInBytes = sizeInKB * 1024; // KB -> 바이트
        } else if (totalSizeText.includes('B')) {
            sizeInBytes = parseFloat(totalSizeText.replace('B', '').trim()); // 바이트
        }

        return sizeInBytes; // 바이트 단위로 반환
    }


    const editor = new toastui.Editor({
        el: document.querySelector('#toastUi'),
        height: '500px',
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical',
        initialValue: document.querySelector('#content').value,
        hooks: {
            addImageBlobHook: async (blob, callback) => {
                if (!postId) {
                    await createTempPost();
                }

                // if (await getTotalSize() + blob.size > MAX_SIZE) {
                //     alert("업로드 용량이 10MB를 초과할 수 없습니다.");
                //     return;
                // }

                const file = new File([blob], `pasted-image.png`, {type: blob.type});
                const uploadedImages = await uploadImage(file, postId, categoryName, false, true);
                callback(uploadedImages[uploadedImages.length - 1].url, '업로드된 이미지');

                updatePreview(uploadedImages);
                updateTotalSize(uploadedImages);

                // 이미지 URL 이 본문에 반영된 후, 서버로 업데이트 요청
                await updatePostWithImage();
            }
        },
        customHTMLRenderer: {
            htmlBlock: {
                iframe(node) {
                    const iframeSrc = node.attrs.src;

                    // iframe src 가 허용된 도메인에 해당하는지 확인
                    const allowedDomains = ['https://www.google.com/maps', 'https://www.youtube.com', 'https://www.facebook.com', 'https://www.instagram.com', 'https://x.com'];
                    if (allowedDomains.some(domain => iframeSrc.startsWith(domain))) {
                        return [
                            {
                                type: 'openTag',
                                tagName: 'iframe',
                                outerNewLine: true,
                                attributes: node.attrs // iframe 태그 속성
                            },
                            {type: 'html', content: node.childrenHTML}, // iframe 안의 HTML 콘텐츠
                            {type: 'closeTag', tagName: 'iframe', outerNewLine: true}
                        ];
                    }

                    return ''; // 허용되지 않은 iframe은 렌더링하지 않음
                }
            }
        }
    });

    // 게시물 업데이트 요청
    async function updatePostWithImage() {
        // editor 의 내용에 이미지를 포함시킴
        document.getElementById('content').value = editor.getHTML();

        // 폼 데이터 준비
        const form = document.getElementById('form');
        const formData = new FormData(form);
        formData.append("isTemp", "true");

        await fetch(`/posts/${postId}`, {
            method: 'PUT',
            headers: {"X-Ajax-Request": "true"},
            body: formData
        });
    }

    const form = document.getElementById('form');
    form.addEventListener('submit', function () {
        document.getElementById('content').value = editor.getHTML();
    });

    if (categoryType === '이벤트') {
        document.getElementById('thumbnail').addEventListener('change', async function (event) {
            let files = event.target.files;
            if (!files.length) return;

            if (!postId) {
                await createTempPost();
            }

            // // 파일 크기 체크 및 용량 계산
            // for (const file of files) {
            //     if (await getTotalSize() + file.size > MAX_SIZE) {
            //         alert("업로드 용량이 10MB를 초과할 수 없습니다.");
            //         return;
            //     }
            // }

            for (const file of files) {
                const uploadedImages = await uploadImage(file, postId, categoryName, true, false);
                updatePreview(uploadedImages);
                updateTotalSize(uploadedImages);
            }

        });
    }

    document.getElementById('files').addEventListener('change', async function (event) {
        const files = event.target.files;
        if (!files.length) return;

        if (!postId) {
            await createTempPost();
        }

        // // 파일 크기 체크 및 용량 계산
        // for (const file of files) {
        //     if (await getTotalSize() + file.size > MAX_SIZE) {
        //         alert("업로드 용량이 10MB를 초과할 수 없습니다.");
        //         return;
        //     }
        // }

        for (const file of files) {
            const uploadedImages = await uploadImage(file, postId, categoryName, false, false);
            updatePreview(uploadedImages);
            updateTotalSize(uploadedImages);
        }
    });

    // 🛠️ **임시 게시물 생성 함수**
    window.createTempPost = async function createTempPost() {
        const formData = new FormData();
        formData.append("categoryName", document.getElementById('categoryName').value);
        formData.append("title", document.getElementById('title').value);
        formData.append("content", editor.getHTML());
        formData.append("isTemp", "true")

        if (categoryName === '핫딜') {
            formData.append("link", document.getElementById('hotDealLink')?.value || null);
            formData.append("shoppingMall", document.getElementById('shoppingMall')?.value || null);
            formData.append("productName", document.getElementById('productName')?.value || null);
            formData.append("price", document.getElementById('price')?.value || null);
        }

        if (categoryType === '이벤트') {
            formData.append("startTime", document.getElementById('startTime')?.value || "");
            formData.append("endTime", document.getElementById('endTime')?.value || "");
            formData.append("link", document.getElementById('eventLink')?.value || null);
        }

        let url = postId ? '/posts/' + postId : '/posts';
        let method = postId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {"X-Ajax-Request": "true"},
            body: formData
        })

        const json = await response.json();

        if (!postId) {
            postId = json.data.postId;
        }

        alertMessage(json.message)
    }

    // 🛠️ **단일 이미지 업로드 함수**
    async function uploadImage(file, postId, categoryName, isThumbnail, isPasted) {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("postId", postId);
        formData.append("categoryName", categoryName);
        formData.append("isThumbnail", isThumbnail);
        formData.append("isPasted", isPasted);

        const response = await fetch('/images/upload', {
            method: 'POST',
            headers: {"X-Ajax-Request": "true"},
            body: formData
        });
        return await response.json(); // 업로드된 이미지 정보 리스트 반환
    }

    const deleteImageBtn = document.getElementById('deleteImageBtn');

    // 이미지 삭제 버튼 클릭 시
    deleteImageBtn.addEventListener('click', async function () {
        // 선택된 이미지 ID들 가져오기
        const selectedImageIds = [];
        document.querySelectorAll('.imageCheckbox:checked').forEach(checkbox => {
            selectedImageIds.push(checkbox.getAttribute('data-image-id'));
        });

        if (selectedImageIds.length > 0) {
            // 서버로 삭제 요청 보내기
            const updatedImages = await deleteSelectedImages(selectedImageIds);
            updatePreview(updatedImages);
            updateTotalSize(updatedImages);
        } else {
            alert('삭제할 이미지를 선택하세요.');
        }
    });


    // 선택된 이미지 삭제 요청
    async function deleteSelectedImages(imageIds) {
        const response = await fetch('/images', { // 서버의 이미지 삭제 엔드포인트
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                "X-Ajax-Request": "true"
            },
            body: JSON.stringify({
                postId: postId,
                categoryName: categoryName,
                imageIds: imageIds
            })
        });

        return await response.json();

    }

    // 🖼️ **업로드된 이미지 미리보기 추가 및 체크박스 클릭 시 상태 변경**
    function updatePreview(images) {
        let previewContainer = document.getElementById('imagePreviewContainer');
        previewContainer.innerHTML = '';

        if (categoryType === '이벤트') {
            previewContainer = document.getElementById('thumbnailPreviewContainer');
            // 기존 미리보기 이미지 초기화
            previewContainer.innerHTML = '';
        }

        // 새로운 이미지 미리보기 컨테이너 생성
        images.forEach(image => {
            let previewContainer = document.getElementById('imagePreviewContainer');

            if (categoryType === '이벤트' && image.isThumbnail) {
                previewContainer = document.getElementById('thumbnailPreviewContainer');
            }

            const ImageContainer = document.createElement('div');
            ImageContainer.classList.add('image-preview');  // 기존 클래스 사용

            // 이미지 또는 동영상 요소 생성
            let tagName = image.type === "image" ? "img" : "video";
            let imgElement = document.createElement(tagName);
            imgElement.src = image.url;
            imgElement.classList.add('image-preview');  // 기존 클래스 사용

            // 파일 이름 표시 요소 생성
            const fileNameElement = document.createElement('span');
            fileNameElement.textContent = image.originalName; // 파일 이름을 설정
            fileNameElement.classList.add('image-name');

            // 새로운 이미지 체크박스 요소 생성 (필요시)
            const imageCheckbox = document.createElement('input');
            imageCheckbox.type = 'checkbox';
            imageCheckbox.classList.add('imageCheckbox');
            imageCheckbox.setAttribute('data-image-id', image.imageId);

            // 요소들을 미리보기 컨테이너에 추가
            ImageContainer.appendChild(imgElement);
            ImageContainer.appendChild(fileNameElement);
            ImageContainer.appendChild(imageCheckbox);

            // 컨테이너에 미리보기 추가
            previewContainer.appendChild(ImageContainer);

            // 체크박스 클릭 시 체크박스 상태 변경
            imageCheckbox.addEventListener("click", function () {
                imageCheckbox.checked = !imageCheckbox.checked; // 상태 되돌림
            });

            // 이미지 클릭 시 체크박스 상태 변경
            ImageContainer.addEventListener('click', function (event) {
                imageCheckbox.checked = !imageCheckbox.checked; // 상태 토글
            });
        })

    }

    // 📏 **총 업로드된 이미지 용량 업데이트**
    function updateTotalSize(uploadedImages) {
        let size = 0;
        uploadedImages.forEach(image => {
            size += image.size;
        })

        totalSizeElement.textContent = formatSize(size); // MB 단위로 표시
        imageSelector(size)
    }

    // FormData 로 폼 데이터를 보내는 예시
    document.getElementById('form').addEventListener('submit', async function (event) {
        event.preventDefault();

        let formData = new FormData(this);
        let url = postId ? '/posts/' + postId : '/posts';
        let method = postId ? 'PUT' : 'POST';
        formData.append("isTemp", "false");

        const response = await fetch(url, {
            method: method,
            headers: {"X-Ajax-Request": "true"},
            body: formData
        })

        const json = await response.json();

        if (!postId) {
            postId = json.data.postId;
        }

        setupAlertMessage(json.message)
        window.location.href = `/posts/` + postId;

    });

    function previewImageEvent() {
        let previewContainer = document.getElementById('imagePreviewContainer');

        // 모든 이미지 컨테이너에 대해 체크박스 상태 변경 처리
        previewContainer.querySelectorAll('.image-preview').forEach(ImageContainer => {
            const imageCheckbox = ImageContainer.querySelector('.imageCheckbox');

            ImageContainer.addEventListener('click', function () {
                imageCheckbox.checked = !imageCheckbox.checked; // 상태 토글
            });
        });

        if (categoryType === '이벤트') {
            previewContainer = document.getElementById('thumbnailPreviewContainer');

            // 모든 이미지 컨테이너에 대해 체크박스 상태 변경 처리
            previewContainer.querySelectorAll('.image-preview').forEach(ImageContainer => {
                const imageCheckbox = ImageContainer.querySelector('.imageCheckbox');

                ImageContainer.addEventListener('click', function () {
                    imageCheckbox.checked = !imageCheckbox.checked; // 상태 토글
                });
            });
        }

    }

    function imageSelector(size) {
        // 이미지가 하나 이상 업로드되었으면 버튼들을 보이도록 설정
        const imageSelectBox = document.getElementById("image-select-box");
        if (size > 0) {
            imageSelectBox.style.display = "block";  // 버튼들이 포함된 div 보이기
        } else {
            imageSelectBox.style.display = "none";   // 버튼들이 포함된 div 숨기기
        }
    }
});

