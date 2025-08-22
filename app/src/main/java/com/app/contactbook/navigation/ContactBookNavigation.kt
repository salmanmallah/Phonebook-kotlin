package com.app.contactbook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.contactbook.screens.addcontact.AddContactScreen
import com.app.contactbook.screens.home.HomeScreen
import com.app.contactbook.screens.viewcontacts.ViewContactsScreen

@Composable
fun ContactBookNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("add_contact") { AddContactScreen(navController) }
        composable("view_contacts") { ViewContactsScreen() }
    }
}
