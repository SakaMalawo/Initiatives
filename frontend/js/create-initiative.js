// Check authentication
if (!localStorage.getItem('token')) {
    window.location.href = 'login.html';
}

async function loadCategories() {
    try {
        const categories = await api.get('/categories');
        const select = document.getElementById('category');
        if (select) {
            categories.forEach(cat => {
                const option = document.createElement('option');
                option.value = cat.id;
                option.textContent = cat.name;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

async function loadZones() {
    try {
        const zones = await api.get('/zones');
        const select = document.getElementById('zone');
        if (select) {
            zones.forEach(zone => {
                const option = document.createElement('option');
                option.value = zone.id;
                option.textContent = zone.name;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading zones:', error);
    }
}

document.getElementById('initiativeForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    const data = {
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        categoryId: parseInt(document.getElementById('category').value),
        zoneId: document.getElementById('zone').value ? parseInt(document.getElementById('zone').value) : null,
        priority: document.getElementById('priority').value
    };

    try {
        await api.post('/initiatives', data);
        window.location.href = 'initiatives.html';
    } catch (error) {
        alert('Erreur lors de la création de l\'initiative');
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    loadZones();
});
