const API_BASE_URL = 'http://localhost:8080/api';

class ApiClient {
    constructor() {
        this.token = localStorage.getItem('token');
    }

    setToken(token) {
        this.token = token;
        localStorage.setItem('token', token);
    }

    clearToken() {
        this.token = null;
        localStorage.removeItem('token');
    }

    getHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        };
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }
        return headers;
    }

    async get(endpoint) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'GET',
            headers: this.getHeaders()
        });
        if (!response.ok) throw new Error('API Error');
        return response.json();
    }

    async post(endpoint, data) {
        console.log('API POST:', endpoint, data);
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: this.getHeaders(),
            body: JSON.stringify(data)
        });
        console.log('API response status:', response.status);
        if (!response.ok) {
            const errorText = await response.text();
            console.error('API error response:', errorText);
            throw new Error(`API Error ${response.status}: ${errorText}`);
        }
        return response.json();
    }

    async put(endpoint, data) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: this.getHeaders(),
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error('API Error');
        return response.json();
    }

    async delete(endpoint) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE',
            headers: this.getHeaders()
        });
        if (!response.ok) throw new Error('API Error');
        return response;
    }
}

const api = new ApiClient();
