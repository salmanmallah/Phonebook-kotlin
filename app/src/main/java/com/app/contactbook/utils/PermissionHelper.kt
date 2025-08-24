package com.app.contactbook.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionHelper {
    
    // Check if call permission is granted
    fun hasCallPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    // Check if SMS permission is granted
    fun hasSmsPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    // Check if contacts permission is granted
    fun hasContactsPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    // Get all required permissions
    fun getRequiredPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS
        )
    }
    
    // Check if all permissions are granted
    fun hasAllPermissions(context: Context): Boolean {
        return hasCallPermission(context) && 
               hasSmsPermission(context) && 
               hasContactsPermission(context)
    }
}
