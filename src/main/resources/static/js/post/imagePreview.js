document.getElementById('files').addEventListener('change', function () {
    const previewContainer = document.getElementById('imagePreviewContainer');
    const fileCountElement = document.getElementById('fileCount');
    const totalSizeElement = document.getElementById('totalSize');

    previewContainer.innerHTML = ""; // 기존 미리보기 초기화
    fileCountElement.textContent = ""; // 파일 개수 초기화
    totalSizeElement.textContent = ""; // 총 용량 초기화

    const files = Array.from(this.files);

    if (files.length === 0) return; // 파일이 없으면 아무것도 하지 않음

    let totalSize = 0;
    files.forEach((file, index) => {
        if (file.type.startsWith('image/')) {
            totalSize += file.size;

            const reader = new FileReader();
            reader.onload = function (e) {
                const container = document.createElement('div');
                container.style.position = 'relative';
                container.style.width = '100px';
                container.style.height = '100px';
                container.style.overflow = 'hidden';
                container.style.border = '1px solid #ddd';
                container.style.borderRadius = '4px';
                container.style.boxShadow = '0 2px 4px rgba(0, 0, 0, 0.1)';
                container.style.cursor = 'pointer';
                container.style.display = 'flex';
                container.style.alignItems = 'center';
                container.style.justifyContent = 'center';

                const img = document.createElement('img');
                img.src = e.target.result;
                img.style.width = '100%';
                img.style.height = '100%';
                img.style.objectFit = 'cover'; // 정사각형 크기에 맞춤
                img.style.display = 'block';

                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.checked = true; // 기본적으로 선택된 상태
                checkbox.style.position = 'absolute';
                checkbox.style.top = '5px';
                checkbox.style.left = '5px';
                checkbox.style.zIndex = '10';

                // 파일 이름 출력
                const fileName = document.createElement('span');
                fileName.textContent = file.name;
                fileName.style.position = 'absolute';
                fileName.style.bottom = '0';
                fileName.style.left = '0';
                fileName.style.right = '0';
                fileName.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
                fileName.style.color = '#fff';
                fileName.style.padding = '2px';
                fileName.style.fontSize = '12px';
                fileName.style.textAlign = 'center';
                fileName.style.visibility = 'hidden'; // 기본적으로 숨김

                // 마우스 오버 시 파일 이름 표시
                container.addEventListener('mouseenter', function () {
                    fileName.style.visibility = 'visible';
                });

                // 마우스 떠날 때 파일 이름 숨김
                container.addEventListener('mouseleave', function () {
                    fileName.style.visibility = 'hidden';
                });

                // 이미지 클릭 시 체크박스 상태 변경
                container.addEventListener('click', function (event) {
                    if (event.target !== checkbox) {
                        checkbox.checked = !checkbox.checked; // 상태 토글
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

    // 파일 개수 및 용량 업데이트 (이미지 선택된 경우에만 표시)
    fileCountElement.textContent = files.length;
    totalSizeElement.textContent = (totalSize / (1024 * 1024)).toFixed(2); // MB 단위
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

    if (selectedFiles.length === 0) {
        alert('업로드할 이미지를 선택하세요.');
        e.preventDefault(); // 폼 제출 취소
        return;
    }

    // 선택된 파일을 새로 구성
    const dataTransfer = new DataTransfer();
    selectedFiles.forEach(file => dataTransfer.items.add(file));
    input.files = dataTransfer.files;
});
