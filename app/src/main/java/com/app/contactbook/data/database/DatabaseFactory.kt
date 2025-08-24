package com.app.contactbook.data.database

import android.content.Context
import com.app.contactbook.data.repository.ContactRepository
import com.app.contactbook.ui.viewmodel.ContactViewModel

object DatabaseFactory {
    
    // Database instance
    private var database: ContactDatabase? = null
    
    // Repository instance
    private var repository: ContactRepository? = null
    
    // ViewModel instance
    private var viewModel: ContactViewModel? = null
    
    // Initialize database
    fun initializeDatabase(context: Context) {
        if (database == null) {
            database = ContactDatabase.getDatabase(context)
        }
    }
    
    // Get repository
    fun getRepository(context: Context): ContactRepository {
        if (repository == null) {
            initializeDatabase(context)
            val dao = database!!.contactDao()
            repository = ContactRepository(dao)
        }
        return repository!!
    }
    
    // Get ViewModel
    fun getViewModel(context: Context): ContactViewModel {
        if (viewModel == null) {
            val repo = getRepository(context)
            viewModel = ContactViewModel(repo)
        }
        return viewModel!!
    }
    
    // Clear instances (for testing or memory management)
    fun clearInstances() {
        viewModel = null
        repository = null
        database = null
    }
}
