// Check authentication
if (!localStorage.getItem('token')) {
    window.location.href = 'login.html';
}

async function loadDashboard() {
    try {
        const stats = await api.get('/dashboard/stats');
        
        document.getElementById('totalInitiatives').textContent = stats.totalInitiatives;
        document.getElementById('totalUsers').textContent = stats.totalUsers;
        document.getElementById('totalVotes').textContent = stats.totalVotes;
        document.getElementById('totalComments').textContent = stats.totalComments;

        // Top initiatives
        const topContainer = document.getElementById('topInitiatives');
        topContainer.innerHTML = '';
        stats.topInitiatives.slice(0, 5).forEach(initiative => {
            const div = document.createElement('div');
            div.className = 'bg-white/5 rounded-xl p-4 flex items-center justify-between';
            div.innerHTML = `
                <div>
                    <p class="font-bold text-white">${initiative.title}</p>
                    <p class="text-gray-400 text-sm">${initiative.authorName}</p>
                </div>
                <div class="text-right">
                    <p class="text-blue-400 font-bold">${initiative.voteCount} votes</p>
                    <p class="text-gray-500 text-sm">${initiative.commentCount} commentaires</p>
                </div>
            `;
            topContainer.appendChild(div);
        });

        // Status stats
        const statusContainer = document.getElementById('statusStats');
        statusContainer.innerHTML = '';
        const statusLabels = {
            'PROPOSED': 'Proposées',
            'VALIDATED': 'Validées',
            'IN_PROGRESS': 'En cours',
            'COMPLETED': 'Réalisées',
            'REJECTED': 'Rejetées'
        };
        Object.entries(stats.initiativesByStatus).forEach(([status, count]) => {
            const div = document.createElement('div');
            div.className = 'bg-white/5 rounded-xl p-4 flex items-center justify-between';
            div.innerHTML = `
                <span class="text-gray-300">${statusLabels[status]}</span>
                <span class="text-white font-bold">${count}</span>
            `;
            statusContainer.appendChild(div);
        });

        // Category stats
        const categoryContainer = document.getElementById('categoryStats');
        categoryContainer.innerHTML = '';
        Object.entries(stats.initiativesByCategory).forEach(([category, count]) => {
            const div = document.createElement('div');
            div.className = 'bg-white/5 rounded-xl p-4 flex items-center justify-between';
            div.innerHTML = `
                <span class="text-gray-300">${category}</span>
                <span class="text-white font-bold">${count}</span>
            `;
            categoryContainer.appendChild(div);
        });
    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadDashboard();
});
