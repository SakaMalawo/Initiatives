function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.add('hidden'));
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('bg-blue-600');
        btn.classList.add('text-gray-400');
    });
    
    document.getElementById(`${tabName}Tab`).classList.remove('hidden');
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('bg-blue-600');
    document.querySelector(`[data-tab="${tabName}"]`).classList.remove('text-gray-400');
}

async function loadAdminInitiatives() {
    try {
        const initiatives = await api.get('/initiatives');
        const container = document.getElementById('adminInitiatives');
        container.innerHTML = '';

        initiatives.forEach(initiative => {
            const div = document.createElement('div');
            div.className = 'bg-white/5 rounded-xl p-4';
            div.innerHTML = `
                <div class="flex items-center justify-between mb-2">
                    <h3 class="font-bold text-white">${initiative.title}</h3>
                    <span class="text-gray-400 text-sm">${initiative.status}</span>
                </div>
                <p class="text-gray-400 text-sm mb-3">${initiative.description.substring(0, 100)}...</p>
                <div class="flex space-x-2">
                    <button onclick="changeStatus(${initiative.id}, 'VALIDATED')" class="bg-green-600 hover:bg-green-500 text-white px-3 py-1 rounded-lg text-sm btn-animate">Valider</button>
                    <button onclick="changeStatus(${initiative.id}, 'REJECTED')" class="bg-red-600 hover:bg-red-500 text-white px-3 py-1 rounded-lg text-sm btn-animate">Rejeter</button>
                    <button onclick="changeStatus(${initiative.id}, 'IN_PROGRESS')" class="bg-blue-600 hover:bg-blue-500 text-white px-3 py-1 rounded-lg text-sm btn-animate">En cours</button>
                    <button onclick="changeStatus(${initiative.id}, 'COMPLETED')" class="bg-purple-600 hover:bg-purple-500 text-white px-3 py-1 rounded-lg text-sm btn-animate">Terminer</button>
                </div>
            `;
            container.appendChild(div);
        });
    } catch (error) {
        console.error('Error loading initiatives:', error);
    }
}

async function changeStatus(id, status) {
    try {
        await api.put(`/initiatives/${id}/status/${status}`);
        loadAdminInitiatives();
    } catch (error) {
        alert('Erreur lors du changement de statut');
    }
}

async function loadAdminComments() {
    try {
        const comments = await api.get('/comments/unmoderated');
        const container = document.getElementById('adminComments');
        container.innerHTML = '';

        if (comments.length === 0) {
            container.innerHTML = '<p class="text-gray-400">Aucun commentaire à modérer</p>';
            return;
        }

        comments.forEach(comment => {
            const div = document.createElement('div');
            div.className = 'bg-white/5 rounded-xl p-4';
            div.innerHTML = `
                <div class="flex items-center justify-between mb-2">
                    <span class="font-bold text-white">${comment.authorName}</span>
                    <span class="text-gray-400 text-sm">${new Date(comment.createdAt).toLocaleDateString()}</span>
                </div>
                <p class="text-gray-300 mb-3">${comment.content}</p>
                <div class="flex space-x-2">
                    <button onclick="moderateComment(${comment.id})" class="bg-yellow-600 hover:bg-yellow-500 text-white px-3 py-1 rounded-lg text-sm btn-animate">Modérer</button>
                    <button onclick="deleteComment(${comment.id})" class="bg-red-600 hover:bg-red-500 text-white px-3 py-1 rounded-lg text-sm btn-animate">Supprimer</button>
                </div>
            `;
            container.appendChild(div);
        });
    } catch (error) {
        console.error('Error loading comments:', error);
    }
}

async function moderateComment(id) {
    try {
        await api.put(`/comments/${id}/moderate`);
        loadAdminComments();
    } catch (error) {
        alert('Erreur lors de la modération');
    }
}

async function deleteComment(id) {
    if (!confirm('Supprimer ce commentaire ?')) return;
    try {
        await api.delete(`/comments/${id}`);
        loadAdminComments();
    } catch (error) {
        alert('Erreur lors de la suppression');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadAdminInitiatives();
    loadAdminComments();
});
