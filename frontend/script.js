// API Base URL - Update this to match your backend URL
const API_URL = 'http://localhost:3000/api/users';

// DOM Elements
const addUserForm = document.getElementById('addUserForm');
const updateUserForm = document.getElementById('updateUserForm');
const usersTableBody = document.getElementById('usersTableBody');
const loadingSpinner = document.getElementById('loadingSpinner');
const errorMessage = document.getElementById('errorMessage');
const successMessage = document.getElementById('successMessage');
const noUsersMessage = document.getElementById('noUsersMessage');
const updateModal = document.getElementById('updateModal');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const clearSearchBtn = document.getElementById('clearSearchBtn');
const refreshBtn = document.getElementById('refreshBtn');
const cancelBtn = document.getElementById('cancelBtn');
const submitBtn = document.getElementById('submitBtn');

let allUsers = [];
let isEditMode = false;
let editingUserId = null;

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
    setupEventListeners();
});

// Setup Event Listeners
function setupEventListeners() {
    addUserForm.addEventListener('submit', handleAddUser);
    updateUserForm.addEventListener('submit', handleUpdateUser);
    searchBtn.addEventListener('click', handleSearch);
    clearSearchBtn.addEventListener('click', handleClearSearch);
    refreshBtn.addEventListener('click', loadUsers);
    
    // Modal close buttons
    const closeButtons = document.querySelectorAll('.close, .close-modal');
    closeButtons.forEach(btn => {
        btn.addEventListener('click', closeModal);
    });

    // Close modal when clicking outside
    window.addEventListener('click', (e) => {
        if (e.target === updateModal) {
            closeModal();
        }
    });

    cancelBtn.addEventListener('click', cancelEdit);
}

// Show/Hide Loading Spinner
function showLoading(show) {
    loadingSpinner.style.display = show ? 'block' : 'none';
}

// Show Error Message
function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
    setTimeout(() => {
        errorMessage.style.display = 'none';
    }, 5000);
}

// Show Success Message
function showSuccess(message) {
    successMessage.textContent = message;
    successMessage.style.display = 'block';
    setTimeout(() => {
        successMessage.style.display = 'none';
    }, 3000);
}

// Load all users
async function loadUsers() {
    showLoading(true);
    try {
        const response = await fetch(`${API_URL}/getUsers`);
        if (!response.ok) {
            throw new Error('Failed to fetch users');
        }
        allUsers = await response.json();
        displayUsers(allUsers);
    } catch (error) {
        showError('Error loading users: ' + error.message);
        allUsers = [];
        displayUsers([]);
    } finally {
        showLoading(false);
    }
}

// Display users in table
function displayUsers(users) {
    usersTableBody.innerHTML = '';
    
    if (users.length === 0) {
        noUsersMessage.style.display = 'block';
        document.getElementById('usersTable').style.display = 'none';
        return;
    }

    noUsersMessage.style.display = 'none';
    document.getElementById('usersTable').style.display = 'table';

    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${escapeHtml(user.name)}</td>
            <td>${escapeHtml(user.email)}</td>
            <td>${user.age}</td>
            <td>${escapeHtml(user.address)}</td>
            <td>${new Date(user.createdAt).toLocaleDateString()}</td>
            <td class="action-buttons">
                <button class="btn btn-success" onclick="editUser('${user._id}')">Edit</button>
                <button class="btn btn-danger" onclick="deleteUser('${user._id}')">Delete</button>
            </td>
        `;
        usersTableBody.appendChild(row);
    });
}

// Handle Add User Form Submit
async function handleAddUser(e) {
    e.preventDefault();
    
    const userData = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        age: parseInt(document.getElementById('age').value),
        address: document.getElementById('address').value
    };

    if (isEditMode && editingUserId) {
        // Update existing user
        await updateUserInline(editingUserId, userData);
    } else {
        // Add new user
        await addUser(userData);
    }
}

// Add new user
async function addUser(userData) {
    showLoading(true);
    try {
        const response = await fetch(`${API_URL}/addUsers`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Failed to add user');
        }

        showSuccess('User added successfully!');
        addUserForm.reset();
        loadUsers();
    } catch (error) {
        showError('Error adding user: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Edit user (inline)
async function editUser(userId) {
    const user = allUsers.find(u => u._id === userId);
    if (!user) return;

    // Fill form with user data
    document.getElementById('name').value = user.name;
    document.getElementById('email').value = user.email;
    document.getElementById('age').value = user.age;
    document.getElementById('address').value = user.address;

    // Change form to edit mode
    isEditMode = true;
    editingUserId = userId;
    submitBtn.textContent = 'Update User';
    cancelBtn.style.display = 'inline-block';

    // Scroll to form
    document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });
}

// Cancel edit mode
function cancelEdit() {
    isEditMode = false;
    editingUserId = null;
    submitBtn.textContent = 'Add User';
    cancelBtn.style.display = 'none';
    addUserForm.reset();
}

// Update user inline
async function updateUserInline(userId, userData) {
    showLoading(true);
    try {
        const response = await fetch(`${API_URL}/updateUser/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Failed to update user');
        }

        showSuccess('User updated successfully!');
        cancelEdit();
        loadUsers();
    } catch (error) {
        showError('Error updating user: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Handle Update User Form Submit (Modal)
async function handleUpdateUser(e) {
    e.preventDefault();
    
    const userId = document.getElementById('updateUserId').value;
    const userData = {
        name: document.getElementById('updateName').value,
        email: document.getElementById('updateEmail').value,
        age: parseInt(document.getElementById('updateAge').value),
        address: document.getElementById('updateAddress').value
    };

    showLoading(true);
    try {
        const response = await fetch(`${API_URL}/updateUser/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Failed to update user');
        }

        showSuccess('User updated successfully!');
        closeModal();
        loadUsers();
    } catch (error) {
        showError('Error updating user: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Delete user
async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) {
        return;
    }

    showLoading(true);
    try {
        const response = await fetch(`${API_URL}/deleteUser/${userId}`, {
            method: 'DELETE'
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Failed to delete user');
        }

        showSuccess('User deleted successfully!');
        loadUsers();
    } catch (error) {
        showError('Error deleting user: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Search functionality
function handleSearch() {
    const searchTerm = searchInput.value.toLowerCase().trim();
    
    if (!searchTerm) {
        displayUsers(allUsers);
        return;
    }

    const filteredUsers = allUsers.filter(user => 
        user.name.toLowerCase().includes(searchTerm) ||
        user.email.toLowerCase().includes(searchTerm) ||
        user.address.toLowerCase().includes(searchTerm)
    );

    displayUsers(filteredUsers);
}

// Clear search
function handleClearSearch() {
    searchInput.value = '';
    displayUsers(allUsers);
}

// Open modal
function openModal(userId) {
    const user = allUsers.find(u => u._id === userId);
    if (!user) return;

    document.getElementById('updateUserId').value = user._id;
    document.getElementById('updateName').value = user.name;
    document.getElementById('updateEmail').value = user.email;
    document.getElementById('updateAge').value = user.age;
    document.getElementById('updateAddress').value = user.address;

    updateModal.style.display = 'block';
}

// Close modal
function closeModal() {
    updateModal.style.display = 'none';
    updateUserForm.reset();
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Add search on Enter key
searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        handleSearch();
    }
});
