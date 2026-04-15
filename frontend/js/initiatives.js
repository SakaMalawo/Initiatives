async function loadCategories() {
    try {
        const categories = await api.get('/categories');
        const select = document.getElementById('categoryFilter');
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
        const select = document.getElementById('zoneFilter');
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

async function loadInitiatives() {
    const categoryId = document.getElementById('categoryFilter').value;
    const zoneId = document.getElementById('zoneFilter').value;
    const status = document.getElementById('statusFilter').value;

    let endpoint = '/initiatives';
    const params = [];
    if (categoryId) params.push(`category=${categoryId}`);
    if (zoneId) params.push(`zone=${zoneId}`);
    if (status) params.push(`status=${status}`);
    if (params.length > 0) endpoint += '?' + params.join('&');

    try {
        const initiatives = await api.get(endpoint);
        const grid = document.getElementById('initiativesGrid');
        grid.innerHTML = '';

        initiatives.forEach(initiative => {
            const card = createInitiativeCard(initiative);
            grid.appendChild(card);
        });
    } catch (error) {
        console.error('Error loading initiatives:', error);
    }
}

function createInitiativeCard(initiative) {
    const div = document.createElement('div');
    div.className = 'glass p-6 hover:border-blue-500/40 transition-all group cursor-pointer';
    div.onclick = () => window.location.href = `initiative-detail.html?id=${initiative.id}`;

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

    div.innerHTML = `
        <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-3">
                <div class="w-10 h-10 rounded-full bg-gradient-to-br from-blue-500 to-purple-500"></div>
                <div>
                    <p class="font-bold text-white">${initiative.authorName}</p>
                    <p class="text-xs text-gray-500">${new Date(initiative.createdAt).toLocaleDateString()}</p>
                </div>
            </div>
            <span class="px-3 py-1 rounded-lg text-xs font-bold ${statusColors[initiative.status]}">
                ${statusLabels[initiative.status]}
            </span>
        </div>
        <h3 class="text-xl font-bold mb-3 group-hover:text-blue-400 transition-colors">${initiative.title}</h3>
        <p class="text-gray-400 mb-4 line-clamp-3">${initiative.description}</p>
        <div class="flex items-center justify-between pt-4 border-t border-white/5">
            <div class="flex items-center space-x-4">
                <button class="flex items-center space-x-2 text-gray-400 hover:text-blue-400 transition-colors" onclick="event.stopPropagation()">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.704a2 2 0 011.94 2.415l-1.61 8a2 2 0 01-1.94 1.585H8V10l4.293-4.293a1 1 0 011.414 0L14 10z"></path></svg>
                    <span class="font-bold">${initiative.voteCount}</span>
                </button>
                <button class="flex items-center space-x-2 text-gray-400 hover:text-blue-400 transition-colors" onclick="event.stopPropagation()">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
                    <span class="font-bold">${initiative.commentCount}</span>
                </button>
            </div>
            <span class="text-gray-500 text-sm">${initiative.categoryName || 'Non catégorisé'}</span>
        </div>
    `;

    return div;
}

document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    loadZones();
    loadInitiatives();
});
