package com.app.contactbook.data.database

import androidx.room.*
import com.app.contactbook.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    
    // Get all contacts
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>
    
    // Get favorite contacts
    @Query("SELECT * FROM contacts WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteContacts(): Flow<List<ContactEntity>>
    
    // Search contacts
    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchContacts(query: String): Flow<List<ContactEntity>>
    
    // Get contact by ID
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): ContactEntity?
    
    // Insert contact
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity): Long
    
    // Update contact
    @Update
    suspend fun updateContact(contact: ContactEntity)
    
    // Delete contact
    @Delete
    suspend fun deleteContact(contact: ContactEntity)
    
    // Delete contact by ID
    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Int)
    
    // Toggle favorite status
    @Query("UPDATE contacts SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean, updatedAt: Long = System.currentTimeMillis())
    
    // Get total contact count
    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int
}
