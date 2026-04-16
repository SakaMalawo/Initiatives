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
    console.log('Register function called with:', data);
    try {
        const response = await api.post('/auth/register', data);
        console.log('Register response:', response);
        api.setToken(response.token);
        currentUser = response;
        localStorage.setItem('user', JSON.stringify(response));
        window.location.href = 'initiatives.html';
    } catch (error) {
        console.error('Register error:', error);
        alert('Erreur lors de l\'inscription: ' + (error.message || 'Unknown error'));
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
    console.log('DOM loaded - auth.js initialized');
    checkAuth();
    updateAuthUI();

    // Register form handler
    const registerForm = document.getElementById('registerForm');
    console.log('Register form found:', registerForm);
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            console.log('Form submit triggered');
            e.preventDefault();
            const data = {
                firstName: document.getElementById('firstName').value,
                lastName: document.getElementById('lastName').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
                phone: document.getElementById('phone').value,
                city: document.getElementById('city').value,
                postalCode: document.getElementById('postalCode').value
            };
            console.log('Register data:', data);
            await register(data);
        });
    }

    // Login form handler
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            await login(email, password);
        });
    }
});
