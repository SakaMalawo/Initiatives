let currentInitiativeId = null;

async function loadInitiative() {
    const urlParams = new URLSearchParams(window.location.search);
    currentInitiativeId = urlParams.get('id');

    if (!currentInitiativeId) {
        window.location.href = 'initiatives.html';
        return;
    }

    try {
        const initiative = await api.get(`/initiatives/${currentInitiativeId}`);
        renderInitiative(initiative);
        loadComments();
    } catch (error) {
        console.error('Error loading initiative:', error);
    }
}

function renderInitiative(initiative) {
    const container = document.getElementById('initiativeDetail');
    const statusColors = {
        'PROPOSED': 'bg-yellow-500/10 text-yellow-400 border-yellow-500/20',
        'VALIDATED': 'bg-green-500/10 text-green-400 border-green-500/20',
        'IN_PROGRESS': 'bg-blue-500/10 text-blue-400 border-blue-500/20',
        'COMPLETED': 'bg-purple-500/10 text-purple-400 border-purple-500/20',
        'REJECTED': 'bg-red-500/10 text-red-400 border-red-500/20'
    };

    const statusLabels = {
        'PROPOSED': 'Proposée',
        'VALIDATED': 'Validée',
        'IN_PROGRESS': 'En cours',
        'COMPLETED': 'Réalisée',
        'REJECTED': 'Rejetée'
    };

    container.innerHTML = `
        <div class="glass p-8 mb-8">
            <div class="flex items-center justify-between mb-6">
                <span class="px-4 py-2 rounded-lg text-sm font-bold ${statusColors[initiative.status]}">
                    ${statusLabels[initiative.status]}
                </span>
                <span class="text-gray-400 text-sm">${new Date(initiative.createdAt).toLocaleDateString()}</span>
            </div>
            <h1 class="text-4xl font-bold text-white mb-4">${initiative.title}</h1>
            <div class="flex items-center space-x-4 mb-6">
                <div class="w-12 h-12 rounded-full bg-gradient-to-br from-blue-500 to-purple-500"></div>
                <div>
                    <p class="font-bold text-white">${initiative.authorName}</p>
                    <p class="text-gray-400 text-sm">Auteur</p>
                </div>
            </div>
            <div class="prose prose-invert max-w-none">
                <p class="text-gray-300 text-lg leading-relaxed whitespace-pre-line">${initiative.description}</p>
            </div>
            <div class="flex items-center justify-between mt-8 pt-6 border-t border-white/10">
                <div class="flex items-center space-x-6">
                    <button onclick="vote('UP')" class="flex items-center space-x-2 text-gray-400 hover:text-green-400 transition-colors btn-animate ${initiative.hasVoted && initiative.userVoteType === 'UP' ? 'text-green-400' : ''}">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.704a2 2 0 011.94 2.415l-1.61 8a2 2 0 01-1.94 1.585H8V10l4.293-4.293a1 1 0 011.414 0L14 10z"></path></svg>
                        <span class="font-bold">${initiative.soutienCount}</span>
                    </button>
                    <button onclick="vote('DOWN')" class="flex items-center space-x-2 text-gray-400 hover:text-red-400 transition-colors btn-animate ${initiative.hasVoted && initiative.userVoteType === 'DOWN' ? 'text-red-400' : ''}">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14h4.704a2 2 0 001.94-2.415l-1.61-8a2 2 0 00-1.94-1.585H8v10l4.293 4.293a1 1 0 001.414 0L10 14z"></path></svg>
                        <span class="font-bold">${initiative.oppositionCount}</span>
                    </button>
                </div>
                <button onclick="shareInitiative()" class="text-gray-400 hover:text-white btn-animate">
                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"></path></svg>
                </button>
            </div>
        </div>
    `;

    const userId = getCurrentUserId();
    if (userId) {
        document.getElementById('commentForm').classList.remove('hidden');
    }
}

async function vote(type) {
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    try {
        await api.post('/votes', { initiativeId: currentInitiativeId, type });
        loadInitiative();
    } catch (error) {
        alert('Erreur lors du vote');
    }
}

async function loadComments() {
    try {
        const comments = await api.get(`/comments/initiative/${currentInitiativeId}`);
        const container = document.getElementById('commentsList');
        container.innerHTML = '';

        comments.forEach(comment => {
            const div = document.createElement('div');
            div.className = 'bg-white/5 rounded-xl p-4';
            div.innerHTML = `
                <div class="flex items-center space-x-3 mb-2">
                    <div class="w-8 h-8 rounded-full bg-gradient-to-br from-blue-500 to-purple-500"></div>
                    <div>
                        <p class="font-bold text-white text-sm">${comment.authorName}</p>
                        <p class="text-gray-500 text-xs">${new Date(comment.createdAt).toLocaleDateString()}</p>
                    </div>
                </div>
                <p class="text-gray-300">${comment.content}</p>
            `;
            container.appendChild(div);
        });
    } catch (error) {
        console.error('Error loading comments:', error);
    }
}

async function addComment() {
    const content = document.getElementById('commentContent').value;
    if (!content.trim()) return;

    try {
        await api.post('/comments', { initiativeId: currentInitiativeId, content });
        document.getElementById('commentContent').value = '';
        loadComments();
    } catch (error) {
        alert('Erreur lors de l\'ajout du commentaire');
    }
}

function shareInitiative() {
    const url = window.location.href;
    navigator.clipboard.writeText(url);
    alert('Lien copié dans le presse-papier');
}

document.addEventListener('DOMContentLoaded', () => {
    loadInitiative();
});
