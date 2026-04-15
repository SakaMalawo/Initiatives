let currentUser = null;

function checkAuth() {
    const token = localStorage.getItem('token');
    if (token) {
        api.setToken(token);
        return true;
    }
    return false;
}

function getCurrentUserId() {
    const token = localStorage.getItem('token');
    if (!token) return null;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return parseInt(payload.sub);
    } catch (e) {
        return null;
    }
}

async function login(email, password) {
    try {
        const response = await api.post('/auth/login', { email, password });
        api.setToken(response.token);
        currentUser = response;
        localStorage.setItem('user', JSON.stringify(response));
        window.location.href = 'initiatives.html';
    } catch (error) {
        alert('Erreur de connexion');
    }
}

async function register(data) {
    try {
        const response = await api.post('/auth/register', data);
        api.setToken(response.token);
        currentUser = response;
        localStorage.setItem('user', JSON.stringify(response));
        window.location.href = 'initiatives.html';
    } catch (error) {
        alert('Erreur lors de l\'inscription');
    }
}

function logout() {
    api.clearToken();
    localStorage.removeItem('user');
    currentUser = null;
    window.location.href = 'login.html';
}

function updateAuthUI() {
    const userId = getCurrentUserId();
    const userMenu = document.getElementById('userMenu');
    const authButtons = document.getElementById('authButtons');

    if (userId && userMenu && authButtons) {
        userMenu.classList.remove('hidden');
        authButtons.classList.add('hidden');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    updateAuthUI();
});
