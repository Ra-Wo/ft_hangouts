package com.rachid.ft_hangouts

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rachid.ft_hangouts.screens.ContactFormScreen
import com.rachid.ft_hangouts.screens.HomeScreen
import com.rachid.ft_hangouts.ui.theme.Ft_hangoutsTheme


const val SMS_PERMISSION_CODE = 101

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ft_hangoutsTheme { App() }
        }
//        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
//
//        if (defaultSmsApp == null) {
//            // set default SMS app
//            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
//            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.packageName)
//            startActivity(intent)
//        } else {
//            Log.d("SMS11", "Default SMS app: $defaultSmsApp")
//        }

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS
            ), SMS_PERMISSION_CODE
        )

    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("Add Contact") {
            ContactFormScreen(navController)
        }
        composable("Edit Contact/{contactId}") { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            ContactFormScreen(navController, contactId)
        }
    }
}
