package com.app.contactbook.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.contactbook.ui.viewmodel.ContactViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(navController: NavController, viewModel: ContactViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contact Book app",
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Real contact count
        val contactCount by viewModel.contacts.collectAsState()
        Text(
            text = "${contactCount.size} contacts in your phonebook",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )
        Button(
            onClick = { navController.navigate("add_contact") },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 16.dp)
        ) {
            Text("Add New Contact")
        }
        Button(
            onClick = { navController.navigate("view_contacts") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("View Contacts")
        }
    }
}
