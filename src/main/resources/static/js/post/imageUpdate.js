document.addEventListener("DOMContentLoaded", function () {
    const categoryName = document.getElementById("categoryName").value; // 카테고리 이름 가져오기
    const maxTotalSize = 10 * 1024 * 1024; // 10MB 제한
    let currentSize = 0;
    let thumbnailSize = 0;
    let deletedSize = 0;
    let newSize = 0;
    let totalSize = 0;

    // 기존 이미지 크기 계산
    document.querySelectorAll(".image-size").forEach(span => {
        currentSize += parseInt(span.getAttribute("data-size"));
    });

    // 기존 용량 표시 반영
    document.getElementById("totalSize").textContent = (currentSize / (1024 * 1024)).toFixed(2);

    function updateSizeDisplay() {
        // 용량 초과 체크
        if ((currentSize - deletedSize + newSize + thumbnailSize) > maxTotalSize) {
            alert("총 업로드 용량(10MB)을 초과했습니다. 기존 이미지를 삭제하거나 파일 크기를 줄여주세요.");
            return false; // 용량 초과 시 false 반환
        } else {
            totalSize = (currentSize - deletedSize + newSize + thumbnailSize) / (1024 * 1024);
            document.getElementById("totalSize").textContent = totalSize.toFixed(2);

            return true; // 용량이 허용 범위 내에 있을 경우 true 반환
        }
    }

    // 기존 이미지 삭제 체크박스 이벤트
    document.querySelectorAll(".deleteImageCheckbox").forEach(checkbox => {
        checkbox.addEventListener("change", function (event) {
            event.stopPropagation(); // 부모 이벤트 버블링 방지

            let imageSize = parseInt(this.closest(".image-preview").querySelector(".image-size").getAttribute("data-size"));
            if (this.checked) {
                deletedSize -= imageSize;
            } else {
                deletedSize += imageSize;
            }

            if (!updateSizeDisplay()) {
                this.checked = !this.checked; // 용량 초과 시 원래 상태로 복구
                deletedSize -= this.checked ? imageSize : -imageSize;
            }
        });
    });

    // 기존 이미지 클릭 시 체크박스 상태 변경
    document.querySelectorAll(".image-preview").forEach(imagePreview => {
        imagePreview.addEventListener("click", function (event) {
            // 체크박스를 직접 클릭한 경우에는 실행하지 않음
            if (event.target.classList.contains("deleteImageCheckbox")) return;

            const checkbox = this.querySelector('.deleteImageCheckbox');
            checkbox.checked = !checkbox.checked; // 상태 반전

            let imageSize = parseInt(this.querySelector(".image-size").getAttribute("data-size"));
            if (checkbox.checked) {
                deletedSize -= imageSize;
            } else {
                deletedSize += imageSize;
            }

            if (!updateSizeDisplay()) {
                checkbox.checked = !checkbox.checked; // 용량 초과 시 원래 상태로 복구
                deletedSize -= checkbox.checked ? imageSize : -imageSize;
            }
        });
    });

    if (categoryName !== '자유' && categoryName !== '공지' && categoryName !== '맛집' && categoryName !== '핫딜') {
        // 썸네일 파일 업로드 처리
        document.getElementById("thumbnail").addEventListener("change", function () {
            const previewContainer = document.getElementById("thumbnailPreviewContainer");
            previewContainer.innerHTML = ""; // 기존 썸네일 초기화

            let file = this.files.length > 0 ? this.files[0] : null; // 파일이 없으면 null 설정

            if (file === null) {
                // 파일이 없으면 미리보기 및 용량 계산 초기화
                previewContainer.innerHTML = ""; // 기존 미리보기 초기화
                thumbnailSize = 0; // 새 이미지 크기 초기화
                updateSizeDisplay(); // 용량 표시 업데이트
                return; // 파일이 없으면 아무것도 하지 않음
            }

            // 파일 크기 합산을 먼저 계산
            if (file.type.startsWith('image/')) {
                thumbnailSize += file.size;
            }

            // 용량 초과 체크 (추가 전에 검사)
            if (!updateSizeDisplay()) {
                this.value = ""; // 파일 선택 취소
                thumbnailSize = 0;
                previewContainer.innerHTML = ""; // 기존 미리보기 초기화
                updateSizeDisplay()
                return;
            }

            thumbnailSize = 0;
            previewContainer.innerHTML = ""; // 기존 미리보기 초기화


            if (file.type.startsWith('image/')) {
                thumbnailSize += file.size;

                const reader = new FileReader();
                reader.onload = function (e) {
                    const container = document.createElement('div');
                    container.classList.add('image-preview');  // 기존 스타일 클래스 추가

                    const img = document.createElement('img');
                    img.src = e.target.result;

                    const checkbox = document.createElement('input');
                    checkbox.classList.add('newImageCheckbox')
                    checkbox.type = 'checkbox';
                    checkbox.checked = true; // 기본적으로 선택된 상태

                    // 파일 이름 출력
                    const fileName = document.createElement('span');
                    fileName.textContent = file.name;
                    fileName.classList.add('image-name'); // 기존 클래스 적용

                    // 체크박스 상태 변경 시 용량 갱신
                    checkbox.addEventListener("change", function () {
                        thumbnailSize += checkbox.checked ? file.size : -file.size;
                        if (!updateSizeDisplay()) {
                            checkbox.checked = !checkbox.checked; // 상태 되돌림
                            thumbnailSize += checkbox.checked ? file.size : -file.size; // 용량 되돌림
                        }
                    });

                    // 이미지 클릭 시 체크박스 상태 변경
                    container.addEventListener('click', function (event) {
                        if (event.target !== checkbox) {
                            checkbox.checked = !checkbox.checked; // 상태 토글
                            thumbnailSize += checkbox.checked ? file.size : -file.size; // 용량 업데이트
                            if (!updateSizeDisplay()) {
                                checkbox.checked = !checkbox.checked; // 상태 되돌림
                                thumbnailSize += checkbox.checked ? file.size : -file.size; // 용량 되돌림
                            }
                        }
                    });

                    container.appendChild(img);
                    container.appendChild(checkbox);
                    container.appendChild(fileName);
                    previewContainer.appendChild(container);
                };
                reader.readAsDataURL(file);
            }

            updateSizeDisplay();
        });
        
    }

    // 폼 제출 시 선택된 파일만 업로드
    document.getElementById('form').addEventListener('submit', function (e) {
        const input = document.getElementById('thumbnail');
        const checkbox = document.querySelector('#thumbnailPreviewContainer input[type="checkbox"]');

        // 체크박스가 없거나 체크되지 않은 경우 → 파일 제거
        if (!checkbox || !checkbox.checked) {
            input.value = ""; // 파일 선택 해제
        }

        this.submit(); // 폼 제출
    });

    // 새 이미지 업로드 시 미리보기 및 체크박스 추가
    document.getElementById("files").addEventListener("change", function () {
        const previewContainer = document.getElementById('imagePreviewContainer');

        let files = this.files.length > 0 ? Array.from(this.files) : null; // 파일이 없으면 null 로 설정

        if (files === null) {
            // 파일이 없으면 미리보기 및 용량 계산 초기화
            previewContainer.innerHTML = ""; // 기존 미리보기 초기화
            newSize = 0; // 새 이미지 크기 초기화
            updateSizeDisplay(); // 용량 표시 업데이트
            return; // 파일이 없으면 아무것도 하지 않음
        }

        // 파일 크기 합산을 먼저 계산
        files.forEach(file => {
            if (file.type.startsWith('image/')) {
                newSize += file.size;
            }
        });

        // 용량 초과 체크 (추가 전에 검사)
        if (!updateSizeDisplay()) {
            this.value = ""; // 파일 선택 취소
            newSize = 0;
            previewContainer.innerHTML = ""; // 기존 미리보기 초기화
            updateSizeDisplay()
            return;
        }

        newSize = 0;
        previewContainer.innerHTML = ""; // 기존 미리보기 초기화

        files.forEach((file, index) => {
            if (file.type.startsWith('image/')) {
                newSize += file.size;

                const reader = new FileReader();
                reader.onload = function (e) {
                    const container = document.createElement('div');
                    container.classList.add('image-preview');  // 기존 스타일 클래스 추가

                    const img = document.createElement('img');
                    img.src = e.target.result;

                    const checkbox = document.createElement('input');
                    checkbox.classList.add('newImageCheckbox')
                    checkbox.type = 'checkbox';
                    checkbox.checked = true; // 기본적으로 선택된 상태

                    // 파일 이름 출력
                    const fileName = document.createElement('span');
                    fileName.textContent = file.name;
                    fileName.classList.add('image-name'); // 기존 클래스 적용

                    // 체크박스 상태 변경 시 용량 갱신
                    checkbox.addEventListener("change", function () {
                        newSize += checkbox.checked ? file.size : -file.size;
                        if (!updateSizeDisplay()) {
                            checkbox.checked = !checkbox.checked; // 상태 되돌림
                            newSize += checkbox.checked ? file.size : -file.size; // 용량 되돌림
                        }
                    });

                    // 이미지 클릭 시 체크박스 상태 변경
                    container.addEventListener('click', function (event) {
                        if (event.target !== checkbox) {
                            checkbox.checked = !checkbox.checked; // 상태 토글
                            newSize += checkbox.checked ? file.size : -file.size; // 용량 업데이트
                            if (!updateSizeDisplay()) {
                                checkbox.checked = !checkbox.checked; // 상태 되돌림
                                newSize += checkbox.checked ? file.size : -file.size; // 용량 되돌림
                            }
                        }
                    });

                    container.appendChild(img);
                    container.appendChild(checkbox);
                    container.appendChild(fileName);
                    previewContainer.appendChild(container);
                };
                reader.readAsDataURL(file);
            }
        });

        updateSizeDisplay();
    });
});

// 폼 제출 시 선택된 파일만 업로드
document.getElementById('form').addEventListener('submit', function (e) {
    const input = document.getElementById('files');
    const files = Array.from(input.files);

    const selectedFiles = [];
    const containers = document.querySelectorAll('#imagePreviewContainer > div');

    containers.forEach((container, index) => {
        const checkbox = container.querySelector('input[type="checkbox"]');
        if (checkbox.checked) {
            selectedFiles.push(files[index]);
        }
    });

    // 선택된 파일을 새로 구성
    const dataTransfer = new DataTransfer();
    selectedFiles.forEach(file => dataTransfer.items.add(file));
    input.files = dataTransfer.files;


    const deleteImageIds = [];
    document.querySelectorAll('.deleteImageCheckbox').forEach(checkbox => {
        if (!checkbox.checked) { // 체크되지 않은 체크박스를 찾음
            deleteImageIds.push(checkbox.value); // 체크되지 않은 이미지의 ID값을 리스트에 추가
        }
    });

    // 삭제할 이미지 아이디들을 폼에 추가
    if (deleteImageIds.length > 0) {
        const deleteInput = document.createElement('input');
        deleteInput.type = 'hidden';
        deleteInput.name = 'deleteImageIds'; // 같은 name으로 삭제할 이미지 ID 전송
        deleteInput.value = deleteImageIds.join(','); // ID값들을 콤마로 구분하여 전달
        this.appendChild(deleteInput);
    }
});
