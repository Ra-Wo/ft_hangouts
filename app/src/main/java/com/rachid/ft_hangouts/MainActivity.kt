package com.rachid.ft_hangouts

import android.Manifest
import android.os.Bundle
import android.widget.Toast
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
import com.rachid.ft_hangouts.screens.ContactsListScreen
import com.rachid.ft_hangouts.screens.MessagesScreen
import com.rachid.ft_hangouts.ui.theme.Ft_hangoutsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit


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

    override fun onPause() {
        super.onPause()
        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        prefs.edit { putLong("last_date", System.currentTimeMillis()) }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val lastDate = prefs.getLong("last_date", -1L)
        if (lastDate != -1L) {
            val dateFormatted = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
                .format(Date(lastDate))
            Toast.makeText(this, "Last open: $dateFormatted", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = "home"
    ) {
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
                navController,
                contactId.toString()
            )
        }
    }
}
