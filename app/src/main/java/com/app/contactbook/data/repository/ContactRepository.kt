package com.app.contactbook.data.repository

import com.app.contactbook.data.database.ContactDao
import com.app.contactbook.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

class ContactRepository(
    private val contactDao: ContactDao
) {
    
    // Get all contacts
    fun getAllContacts(): Flow<List<ContactEntity>> = contactDao.getAllContacts()
    
    // Get favorite contacts
    fun getFavoriteContacts(): Flow<List<ContactEntity>> = contactDao.getFavoriteContacts()
    
    // Search contacts
    fun searchContacts(query: String): Flow<List<ContactEntity>> = contactDao.searchContacts(query)
    
    // Get contact by ID
    suspend fun getContactById(id: Int): ContactEntity? = contactDao.getContactById(id)
    
    // Insert contact
    suspend fun insertContact(contact: ContactEntity): Long = contactDao.insertContact(contact)
    
    // Update contact
    suspend fun updateContact(contact: ContactEntity) = contactDao.updateContact(contact)
    
    // Delete contact
    suspend fun deleteContact(contact: ContactEntity) = contactDao.deleteContact(contact)
    
    // Delete contact by ID
    suspend fun deleteContactById(id: Int) = contactDao.deleteContactById(id)
    
    // Toggle favorite status
    suspend fun toggleFavoriteStatus(id: Int, isFavorite: Boolean) = 
        contactDao.updateFavoriteStatus(id, isFavorite)
    
    // Get contact count
    suspend fun getContactCount(): Int = contactDao.getContactCount()
}
