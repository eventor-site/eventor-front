function toggleSidebar(elementId) {
    document.getElementById(elementId).classList.toggle('show');
    document.getElementById('overlay').classList.toggle('show');
}

function closeSidebar(elementId) {
    document.getElementById(elementId).classList.remove('show');

    document.getElementById('overlay').classList.remove('show');
}

function closeSidebarByOverlay() {
    document.querySelectorAll('.sidebar.show').forEach(sidebar => sidebar.classList.remove('show'));
    document.querySelectorAll('.sidebar-left').forEach(sb => sb.classList.remove('show-left'));
    document.querySelectorAll('.sidebar-right').forEach(sb => sb.classList.remove('show-right'));
    document.getElementById('overlay').classList.remove('show');
}