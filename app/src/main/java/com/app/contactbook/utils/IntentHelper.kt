package com.app.contactbook.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import androidx.core.content.ContextCompat.startActivity
import com.app.contactbook.utils.PermissionHelper

object IntentHelper {
    
    // Make phone call
    fun makePhoneCall(context: Context, phoneNumber: String) {
        // Check permission first
        if (!PermissionHelper.hasCallPermission(context)) {
            // If no permission, open dialer instead
            openDialer(context, phoneNumber)
            return
        }
        
        try {
            // First try ACTION_CALL (direct call)
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            
            // Check if we can make direct call
            if (callIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(callIntent)
            } else {
                // Fallback to ACTION_DIAL (dialer)
                openDialer(context, phoneNumber)
            }
        } catch (e: Exception) {
            // Handle permission denied or other errors
            e.printStackTrace()
            
            // Fallback to dialer if call fails
            openDialer(context, phoneNumber)
        }
    }
    
    // Send SMS
    fun sendSMS(context: Context, phoneNumber: String, message: String = "") {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            // Handle permission denied or other errors
            e.printStackTrace()
        }
    }
    
    // Alternative call method (dialer only)
    fun openDialer(context: Context, phoneNumber: String) {
        try {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")
            dialIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(dialIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Open SMS app
    fun openSMSApp(context: Context, phoneNumber: String, message: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:$phoneNumber")
            intent.putExtra("sms_body", message)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Send Email
    fun sendEmail(context: Context, email: String, subject: String = "", body: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$email")
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Open contact details
    fun openContactDetails(context: Context, contactId: Long) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.withAppendedPath(
                Uri.parse("content://com.android.contacts/contacts"),
                contactId.toString()
            )
            intent.data = uri
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Share contact
    fun shareContact(context: Context, name: String, phone: String, email: String) {
        try {
            val shareText = buildString {
                append("Name: $name\n")
                append("Phone: $phone\n")
                if (email.isNotEmpty()) {
                    append("Email: $email")
                }
            }
            
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact: $name")
            context.startActivity(Intent.createChooser(intent, "Share Contact"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
