package com.app.contactbook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.contactbook.data.entity.ContactEntity
import com.app.contactbook.data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel() {
    
    // State for contacts
    private val _contacts = MutableStateFlow<List<ContactEntity>>(emptyList())
    val contacts: StateFlow<List<ContactEntity>> = _contacts.asStateFlow()
    
    // State for search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // State for loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // State for error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadContacts()
    }
    
    // Load all contacts
    fun loadContacts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.getAllContacts().collect { contacts ->
                    _contacts.value = contacts
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
                _isLoading.value = false
            }
        }
    }
    
    // Search contacts
    fun searchContacts(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            loadContacts()
        } else {
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    repository.searchContacts(query).collect { contacts ->
                        _contacts.value = contacts
                        _isLoading.value = false
                    }
                } catch (e: Exception) {
                    _error.value = e.message ?: "Search failed"
                    _isLoading.value = false
                }
            }
        }
    }
    
    // Add new contact
    fun addContact(name: String, phone: String, email: String) {
        viewModelScope.launch {
            try {
                val contact = ContactEntity(
                    name = name.trim(),
                    phone = phone.trim(),
                    email = email.trim()
                )
                val id = repository.insertContact(contact)
                if (id > 0) {
                    loadContacts() // Reload contacts
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add contact"
            }
        }
    }
    
    // Update contact
    fun updateContact(contact: ContactEntity) {
        viewModelScope.launch {
            try {
                val updatedContact = contact.copy(updatedAt = System.currentTimeMillis())
                repository.updateContact(updatedContact)
                loadContacts() // Reload contacts
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update contact"
            }
        }
    }
    
    // ...existing code...
    
    // Toggle favorite status
    fun toggleFavorite(contact: ContactEntity) {
        viewModelScope.launch {
            try {
                val updatedContact = contact.copy(
                    isFavorite = !contact.isFavorite,
                    updatedAt = System.currentTimeMillis()
                )
                repository.updateContact(updatedContact)
                loadContacts() // Reload contacts
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to toggle favorite"
            }
        }
    }
    
    // Delete contact
    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            try {
                repository.deleteContact(contact)
                loadContacts() // Reload contacts
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete contact"
            }
        }
    }
    
    // ...existing code...
    
    // Clear error
    fun clearError() {
        _error.value = null
    }
    
    // Get contact count
    fun getContactCount(): Int = _contacts.value.size
}
