package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.MainContainerScreen
import com.example.ui.navigation.NavRoutes
import com.example.ui.screens.ai.AiAssistantScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.auth.BusinessSetupScreen
import com.example.ui.screens.auth.SplashScreen
import com.example.ui.screens.customers.CustomerDetailScreen
import com.example.ui.screens.notifications.NotificationsScreen
import com.example.ui.screens.sales.CreateInvoiceScreen
import com.example.ui.screens.sales.InvoiceDetailScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.theme.BizNovaTheme
import com.example.viewmodel.BizNovaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BizNovaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDark by viewModel.isDarkMode.collectAsState()

            BizNovaTheme(darkTheme = isDark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentUser by viewModel.currentUser.collectAsState()

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Splash.route
                    ) {
                        composable(NavRoutes.Splash.route) {
                            SplashScreen(
                                onSplashFinished = {
                                    if (currentUser != null && currentUser!!.isLoggedIn) {
                                        navController.navigate(NavRoutes.MainContainer.route) {
                                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(NavRoutes.Auth.route) {
                                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        composable(NavRoutes.Auth.route) {
                            AuthScreen(
                                onLoginSuccess = { name, phone ->
                                    navController.navigate("${NavRoutes.BusinessSetup.route}/$name/$phone") {
                                        popUpTo(NavRoutes.Auth.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("${NavRoutes.BusinessSetup.route}/{name}/{phone}") { backStackEntry ->
                            val nameArg = backStackEntry.arguments?.getString("name") ?: "Rajesh Sharma"
                            val phoneArg = backStackEntry.arguments?.getString("phone") ?: "+91 98765 43210"

                            BusinessSetupScreen(
                                initialName = nameArg,
                                initialPhone = phoneArg,
                                onSetupComplete = { name, phone, email, bName, bType, gst, address ->
                                    viewModel.updateUserProfile(name, phone, email, bName, bType, gst, address)
                                    navController.navigate(NavRoutes.MainContainer.route) {
                                        popUpTo(NavRoutes.BusinessSetup.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(NavRoutes.MainContainer.route) {
                            MainContainerScreen(
                                viewModel = viewModel,
                                onCreateInvoiceClick = { navController.navigate(NavRoutes.CreateInvoice.route) },
                                onAddProductClick = { navController.navigate(NavRoutes.MainContainer.route) },
                                onAddCustomerClick = { navController.navigate(NavRoutes.MainContainer.route) },
                                onAddExpenseClick = { navController.navigate(NavRoutes.MainContainer.route) },
                                onAiAssistantClick = { navController.navigate(NavRoutes.AiAssistant.route) },
                                onNotificationClick = { navController.navigate(NavRoutes.Notifications.route) },
                                onProfileClick = { navController.navigate(NavRoutes.Settings.route) },
                                onSaleDetailClick = { sale ->
                                    navController.navigate(NavRoutes.InvoiceDetail.route)
                                },
                                onCustomerDetailClick = { customer ->
                                    navController.navigate(NavRoutes.CustomerDetail.route)
                                }
                            )
                        }

                        composable(NavRoutes.CreateInvoice.route) {
                            CreateInvoiceScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onInvoiceCreated = { saleId ->
                                    navController.navigate(NavRoutes.InvoiceDetail.route) {
                                        popUpTo(NavRoutes.MainContainer.route)
                                    }
                                }
                            )
                        }

                        composable(NavRoutes.InvoiceDetail.route) {
                            InvoiceDetailScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(NavRoutes.CustomerDetail.route) {
                            CustomerDetailScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(NavRoutes.AiAssistant.route) {
                            AiAssistantScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(NavRoutes.Settings.route) {
                            SettingsScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onLogoutClick = {
                                    navController.navigate(NavRoutes.Auth.route) {
                                        popUpTo(NavRoutes.MainContainer.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(NavRoutes.Notifications.route) {
                            NotificationsScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
