package com.app.contactbook.screens.viewcontacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.contactbook.data.entity.ContactEntity
import com.app.contactbook.ui.viewmodel.ContactViewModel
import com.app.contactbook.utils.IntentHelper
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun ViewContactsScreen(navController: NavController, viewModel: ContactViewModel) {
    // --- UI states ---
    var searchQuery by rememberSaveable { mutableStateOf("") }
    // sirf id store karo to prevent heavy object comparisons
    var selectedContactId by remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current
    val contacts by viewModel.contacts.collectAsState()     // keep as is; lifecycle variant optional
    val isLoading by viewModel.isLoading.collectAsState()

    // Debounce search: less recompositions under typing
    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(300)
            .collect { q -> viewModel.searchContacts(q) }
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // --- Screen ---
    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            HeaderSection()

            Text(
                text = "${contacts.size} contacts in your phonebook",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp)
            )

            SearchBarSection(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            Spacer(Modifier.height(8.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ContactsListSection(
                    contacts = contacts,
                    listState = listState,
                    onContactClick = { contact -> selectedContactId = contact.id },
                    onCall = { phone ->
                        try { IntentHelper.makePhoneCall(context, phone) }
                        catch (_: Exception) { IntentHelper.openDialer(context, phone) }
                    },
                    onSms = { phone -> IntentHelper.openSMSApp(context, phone) },
                    onToggleFav = { c -> viewModel.toggleFavorite(c) }
                )
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { navController.navigate("add_contact") },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add contact")
        }
    }

    // --- Bottom Sheet (cheap toggling via id) ---
    val selected = remember(selectedContactId, contacts) {
        contacts.firstOrNull { it.id == selectedContactId }
    }
    if (selected != null) {
        ContactActionMenu(
            contact = selected,
            navController = navController,
            onDismiss = { selectedContactId = null },
            onCall = {
                try { IntentHelper.makePhoneCall(context, selected.phone) }
                catch (_: Exception) { IntentHelper.openDialer(context, selected.phone) }
            },
            onSMS = { IntentHelper.openSMSApp(context, selected.phone) },
            onEmail = {
                if (selected.email.isNotEmpty()) {
                    IntentHelper.sendEmail(context, selected.email)
                }
            }
        )
    }
}

@Composable
fun HeaderSection() {
    Text(
        text = "All Contacts",
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp),
        textAlign = TextAlign.Center
    )
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
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ContactsListSection(
    contacts: List<ContactEntity>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onContactClick: (ContactEntity) -> Unit,
    onCall: (String) -> Unit,
    onSms: (String) -> Unit,
    onToggleFav: (ContactEntity) -> Unit
) {
    if (contacts.isEmpty()) {
        EmptyContactsState()
        return
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 88.dp), // avoid FAB overlap
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = contacts,
            key = { it.id },                 // stable key
            contentType = { "contact_item" } // helps recycler reuse
        ) { contact ->
            ContactItem(
                contact = contact,
                onClick = { onContactClick(contact) },
                onCall = onCall,
                onSms = onSms,
                onToggleFav = onToggleFav
            )
        }
    }
}

@Composable
fun ContactItem(
    contact: ContactEntity,
    onClick: () -> Unit,
    onCall: (String) -> Unit,
    onSms: (String) -> Unit,
    onToggleFav: (ContactEntity) -> Unit
) {
    // Light memoization for text pieces
    val initial by remember(contact.id, contact.name) {
        mutableStateOf(contact.name.take(1).uppercase())
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar (no extra Box layering)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contact.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (contact.isFavorite) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Favorite",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(Modifier.height(2.dp))

                Text(
                    text = contact.phone,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (contact.email.isNotEmpty()) {
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = contact.email,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            ContactActionIcons(
                contact = contact,
                onCall = onCall,
                onSms = onSms,
                onToggleFav = onToggleFav
            )
        }
    }
}

@Composable
fun ContactActionIcons(
    contact: ContactEntity,
    onCall: (String) -> Unit,
    onSms: (String) -> Unit,
    onToggleFav: (ContactEntity) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        IconButton(onClick = { onCall(contact.phone) }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Filled.Call, contentDescription = "Call", modifier = Modifier.size(18.dp))
        }
        IconButton(onClick = { onSms(contact.phone) }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Filled.Sms, contentDescription = "SMS", modifier = Modifier.size(18.dp))
        }
        IconButton(onClick = { onToggleFav(contact) }, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = if (contact.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = "Toggle Favorite",
                modifier = Modifier.size(18.dp),
                tint = if (contact.isFavorite) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyContactsState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PersonOff,
                contentDescription = "No contacts",
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No contacts found",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current // Fixed: Moved to Composable level

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar + name
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.take(1).uppercase(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(contact.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(contact.phone, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(18.dp))

            ActionButton("Call", Icons.Filled.Call) { onCall(); onDismiss() }
            Spacer(Modifier.height(10.dp))
            ActionButton("Send SMS", Icons.Filled.Sms) { onSMS(); onDismiss() }
            Spacer(Modifier.height(10.dp))
            ActionButton("Send Email", Icons.Filled.Email) { onEmail(); onDismiss() }
            Spacer(Modifier.height(10.dp))
            ActionButton("Share Contact", Icons.Filled.Share) {
                // Fixed: Now context is available from the Composable scope
                IntentHelper.shareContact(
                    context,
                    contact.name, contact.phone, contact.email
                )
                onDismiss()
            }
            Spacer(Modifier.height(10.dp))
            ActionButton("Edit Contact", Icons.Filled.Edit) {
                navController.navigate("edit_contact/${contact.id}")
                onDismiss()
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Cancel") }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}