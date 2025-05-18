package com.rachid.ft_hangouts

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rachid.ft_hangouts.screens.ContactFormScreen
import com.rachid.ft_hangouts.screens.ContactsListScreen
import com.rachid.ft_hangouts.screens.MessagesScreen
import com.rachid.ft_hangouts.screens.SettingsScreen
import com.rachid.ft_hangouts.ui.theme.Ft_hangoutsTheme
import com.rachid.ft_hangouts.ui.theme.ThemeManager
import com.rachid.ft_hangouts.ui.theme.ThemeType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.pm.PackageManager.PERMISSION_GRANTED
import com.rachid.ft_hangouts.components.PermissionNotGrantedScreen


const val SMS_PERMISSION_CODE = 101

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)

    private var timestamp = 0L
    var currentTheme = mutableStateOf(ThemeType.PURPLE)
    var smsPermissionsGranted = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeManager = ThemeManager(LocalContext.current)
            val themeType = themeManager.getTheme()

            currentTheme.value = ThemeType.valueOf(
                themeType.name.uppercase()
            )

            Ft_hangoutsTheme(
                dynamicColor = false,
                theme = currentTheme.value,
            ) {
                if (!smsPermissionsGranted.value) {
                    // Show permission not granted screen
                    PermissionNotGrantedScreen(
                        onRequestPermission = { requestSmsPermissions() })
                } else {
                    App(
                        onThemeChange = { selectedTheme: ThemeType ->
                            currentTheme.value = selectedTheme
                        }, selectedTheme = currentTheme.value
                    )
                }
            }
        }

        // Check if SMS permissions are granted
        if (!areSmsPermissionsGranted()) {
            requestSmsPermissions()
        } else {
            smsPermissionsGranted.value = true
        }
    }

    override fun onPause() {
        super.onPause()
        timestamp = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()

        // Show the last open date
        val lastDate = timestamp
        if (lastDate != -1L && lastDate != 0L) {
            val dateFormatted =
                SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(Date(lastDate))
            Toast.makeText(this, "Last open: $dateFormatted", Toast.LENGTH_SHORT).show()
        }

        // Check if SMS permissions are granted
        if (areSmsPermissionsGranted()) {
            smsPermissionsGranted.value = true
        }
    }

    private fun areSmsPermissionsGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.RECEIVE_SMS
        ) == PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.SEND_SMS
        ) == PERMISSION_GRANTED
    }

    private fun requestSmsPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS
            ), SMS_PERMISSION_CODE
        )
    }
}

@Composable
fun App(
    onThemeChange: (ThemeType) -> Unit, selectedTheme: ThemeType = ThemeType.PURPLE
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }) {
        composable("home") {
            ContactsListScreen(navController)
        }
        composable("Add Contact") {
            ContactFormScreen(navController)
        }
        composable("Edit Contact/{contactId}") { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            ContactFormScreen(navController, contactId)
        }
        composable("Messages/{contactId}") { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            MessagesScreen(
                navController, contactId.toString()
            )
        }
        composable("Settings") {
            SettingsScreen(navController, onThemeChange, selectedTheme)
        }
    }
}

