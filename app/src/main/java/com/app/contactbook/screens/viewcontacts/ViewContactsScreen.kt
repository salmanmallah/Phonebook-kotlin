package com.app.contactbook.screens.viewcontacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.contactbook.ui.viewmodel.ContactViewModel
import com.app.contactbook.data.entity.ContactEntity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.app.contactbook.utils.IntentHelper
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ViewContactsScreen(navController: NavController, viewModel: ContactViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var showActionMenu by remember { mutableStateOf<ContactEntity?>(null) }
    
    // Get context for intents
    val context = LocalContext.current
    
    // Get real contacts from ViewModel
    val contacts by viewModel.contacts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Section
            HeaderSection()
            
            // Real contact count
            Text(
                text = "${contacts.size} contacts in your phonebook",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            SearchBarSection(
                searchQuery = searchQuery,
                onSearchQueryChange = { 
                    searchQuery = it
                    viewModel.searchContacts(it)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Contacts List
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ContactsListSection(
                    contacts = contacts,
                    onContactClick = { contact ->
                        showActionMenu = contact
                    },
                    viewModel = viewModel
                )
            }
        }

        // FAB at bottom end with padding like system contacts app
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp, end = 30.dp), // More padding for floating look
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("add_contact") },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.Add, "Add contact")
            }
        }
    }
    
    // Action Menu Bottom Sheet
    if (showActionMenu != null) {
        ContactActionMenu(
            contact = showActionMenu!!,
            navController = navController,
            onDismiss = { showActionMenu = null },
            onCall = { 
                try {
                    // Try direct call first
                    IntentHelper.makePhoneCall(context, showActionMenu!!.phone)
                } catch (e: Exception) {
                    // Fallback to dialer if call fails
                    IntentHelper.openDialer(context, showActionMenu!!.phone)
                }
                showActionMenu = null
            },
            onSMS = { 
                IntentHelper.openSMSApp(context, showActionMenu!!.phone)
                showActionMenu = null
            },
            onEmail = { 
                if (showActionMenu!!.email.isNotEmpty()) {
                    IntentHelper.sendEmail(context, showActionMenu!!.email)
                }
                showActionMenu = null
            }
        )
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Contacts",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Real contact count will be shown in main function
    }
}

@Composable
fun SearchBarSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Search contacts...") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ContactsListSection(
    contacts: List<ContactEntity>,
    onContactClick: (ContactEntity) -> Unit,
    viewModel: ContactViewModel
) {
    if (contacts.isEmpty()) {
        EmptyContactsState()
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(contacts) { contact ->
                ContactItem(
                    contact = contact,
                    onClick = { onContactClick(contact) },
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: ContactEntity,
    onClick: () -> Unit,
    viewModel: ContactViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contact Avatar
            ContactAvatar(contact = contact)
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Contact Details
            Box(
                modifier = Modifier.weight(1f)
            ) {
                ContactDetails(contact = contact)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Action Icons
            ContactActionIcons(contact = contact, viewModel = viewModel)
        }
    }
}

@Composable
fun ContactAvatar(contact: ContactEntity) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = contact.name.take(1).uppercase(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ContactDetails(contact: ContactEntity) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = contact.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (contact.isFavorite) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = contact.phone,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        if (contact.email.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = contact.email,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ContactActionIcons(contact: ContactEntity, viewModel: ContactViewModel) {
    val context = LocalContext.current
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = { 
                try {
                    IntentHelper.makePhoneCall(context, contact.phone)
                } catch (e: Exception) {
                    IntentHelper.openDialer(context, contact.phone)
                }
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Call,
                contentDescription = "Call",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        IconButton(
            onClick = { 
                IntentHelper.openSMSApp(context, contact.phone)
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Sms,
                contentDescription = "SMS",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        // Favorite Toggle
        IconButton(
            onClick = { 
                viewModel.toggleFavorite(contact)
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (contact.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = "Toggle Favorite",
                modifier = Modifier.size(18.dp),
                tint = if (contact.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyContactsState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PersonOff,
                contentDescription = "No contacts",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "No contacts found",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Try adding some contacts or check your search query",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactActionMenu(
    contact: ContactEntity,
    navController: NavController,
    onDismiss: () -> Unit,
    onCall: () -> Unit,
    onSMS: () -> Unit,
    onEmail: () -> Unit
) {
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Contact Info Header
            ContactAvatar(contact = contact)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = contact.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = contact.phone,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            ActionButton(
                text = "Call",
                icon = Icons.Filled.Call,
                onClick = {
                    onCall()
                    onDismiss()
                },
                containerColor = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ActionButton(
                text = "Send SMS",
                icon = Icons.Filled.Sms,
                onClick = {
                    onSMS()
                    onDismiss()
                },
                containerColor = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ActionButton(
                text = "Send Email",
                icon = Icons.Filled.Email,
                onClick = {
                    onEmail()
                    onDismiss()
                },
                containerColor = MaterialTheme.colorScheme.tertiary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Share Contact Button
            ActionButton(
                text = "Share Contact",
                icon = Icons.Filled.Share,
                onClick = {
                    IntentHelper.shareContact(
                        context, 
                        contact.name, 
                        contact.phone, 
                        contact.email
                    )
                    onDismiss()
                },
                containerColor = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Edit Contact Button
            ActionButton(
                text = "Edit Contact",
                icon = Icons.Filled.Edit,
                onClick = {
                    navController.navigate("edit_contact/${contact.id}")
                    onDismiss()
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Cancel Button
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 8.dp)
        )
        Text(text)
    }
}
