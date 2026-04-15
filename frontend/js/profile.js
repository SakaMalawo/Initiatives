async function loadProfile() {
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    try {
        const profile = await api.get(`/auth/profile/${userId}`);
        
        document.getElementById('firstName').value = profile.firstName || '';
        document.getElementById('lastName').value = profile.lastName || '';
        document.getElementById('email').value = profile.email || '';
        document.getElementById('city').value = profile.city || '';
        document.getElementById('postalCode').value = profile.postalCode || '';
        document.getElementById('phone').value = profile.phoneNumber || '';
    } catch (error) {
        console.error('Error loading profile:', error);
    }
}

document.getElementById('profileForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    const data = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        city: document.getElementById('city').value,
        postalCode: document.getElementById('postalCode').value,
        phone: document.getElementById('phone').value
    };

    const password = document.getElementById('password').value;
    if (password) {
        data.password = password;
    }

    try {
        await api.put(`/auth/profile/${userId}`, data);
        alert('Profil mis à jour avec succès');
        document.getElementById('password').value = '';
    } catch (error) {
        alert('Erreur lors de la mise à jour du profil');
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadProfile();
});
