package com.example.gymapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymapp.ui.UserInputViewModel

@Composable
fun FunFactsNavigationGraph(userInputViewModel: UserInputViewModel = viewModel()) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.User_Input_Screen) {

        composable(Routes.User_Input_Screen) {
            UserInputScreen(userInputViewModel, showWelcomeScreen = {
                navController.navigate(Routes.Welcome_Screen + "/${it.first}/${it.second}")
            },
                navController = navController // Pasar navController

            )
        }

        composable("${Routes.Welcome_Screen}/{${Routes.USER_NAME}}/{${Routes.PASSWORD_SELECTED}}",
            arguments = listOf(
                navArgument(name = Routes.USER_NAME) { type = NavType.StringType },
                navArgument(name = Routes.PASSWORD_SELECTED) { type = NavType.StringType }
            )
        ) {
            val username = it?.arguments?.getString(Routes.USER_NAME)
            val languajeSelected = it?.arguments?.getString(Routes.PASSWORD_SELECTED)

            WelcomeScreen(username, languajeSelected,navController)
        }

        composable(Routes.Trainings_Screen) {
            TrainingsScreen(navController)
        }
        // Pantalla de registro
        composable(Routes.Register_Screen) {
            UserRegisterScreen(navController = navController) // Pasar el navController aquí
        }
        composable(Routes.PRs_Screen) { PRsScreen(navController) }
        composable(Routes.Weight_Calculator_Screen) { WeightCalculatorScreen(navController) }
    }
}