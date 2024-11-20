// TOAST UI Editor
document.addEventListener('DOMContentLoaded', function () {
    const editor = new toastui.Editor({
        el: document.querySelector('#toastUi'),
        height: '500px',
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical',
        initialValue: document.querySelector('#content').value
    });

    const form = document.getElementById('form');
    form.addEventListener('submit', function () {
        const content = document.getElementById('content');
        content.value = editor.getHTML();
    });
});