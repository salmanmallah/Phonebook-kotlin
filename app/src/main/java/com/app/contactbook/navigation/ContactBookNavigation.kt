package com.app.contactbook.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.contactbook.screens.addcontact.AddContactScreen
import com.app.contactbook.screens.home.HomeScreen
import com.app.contactbook.screens.viewcontacts.ViewContactsScreen
import com.app.contactbook.screens.success.ContactSavedScreen
import com.app.contactbook.screens.editcontact.EditContactScreen
import com.app.contactbook.data.database.DatabaseFactory
import androidx.compose.runtime.remember

@Composable
fun ContactBookNavHost(navController: NavHostController) {
    // Get ViewModel from DatabaseFactory
    val viewModel = remember { DatabaseFactory.getViewModel(navController.context) }
    
    NavHost(
        navController = navController, 
        startDestination = "home"
    ) {
        composable(
            route = "home",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800)
                )
            }
        ) { 
            HomeScreen(navController, viewModel) 
        }
        
        composable(
            route = "add_contact",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            }
        ) { 
            AddContactScreen(navController, viewModel) 
        }
        
        composable(
            route = "view_contacts",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            }
        ) { 
            ViewContactsScreen(navController, viewModel) 
        }
        
        composable(
            route = "contact_saved",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            }
        ) { 
            ContactSavedScreen(navController) 
        }
        
        // Edit Contact Route
        composable(
            route = "edit_contact/{contactId}",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800, easing = androidx.compose.animation.core.EaseInOut)
                )
            }
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")?.toIntOrNull() ?: 0
            val contact = viewModel.contacts.value.find { it.id == contactId }
            
            if (contact != null) {
                EditContactScreen(navController, viewModel, contact)
            } else {
                // Handle contact not found
                navController.popBackStack()
            }
        }
    }
}
