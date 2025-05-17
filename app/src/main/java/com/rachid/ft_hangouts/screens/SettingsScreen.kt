package com.rachid.ft_hangouts.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.components.TopBar
import com.rachid.ft_hangouts.ui.theme.BlueColorScheme
import com.rachid.ft_hangouts.ui.theme.GreenColorScheme
import com.rachid.ft_hangouts.ui.theme.PurpleColorScheme
import com.rachid.ft_hangouts.ui.theme.RedColorScheme
import com.rachid.ft_hangouts.ui.theme.ThemeManager
import com.rachid.ft_hangouts.ui.theme.ThemeType

@Composable
fun SettingsScreen(
    navController: NavHostController,
    onThemeChange: (ThemeType) -> Unit,
    selectedTheme: ThemeType = ThemeType.PURPLE
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.settings),
                startContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                navController.popBackStack()
                            }) {
                        Icon(
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = stringResource(
                                id = R.string.back
                            ),
                            modifier = Modifier
                                .size(35.dp)
                                .padding(4.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
            )
        }) { innerPadding: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // state for the scrollable content
            val scrollState = rememberScrollState()
            val themeManager = ThemeManager(navController.context)

            fun handleChangeTheme(theme: ThemeType) {
                onThemeChange(theme)
                themeManager.setTheme(theme)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.select_theme),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(12.dp, 12.dp, 12.dp, 0.dp)
                        .fillMaxWidth()
                )
                ThemeSelectorDropdown(
                    theme = selectedTheme,
                    themes = listOf(
                        "Purple" to PurpleColorScheme,
                        "Red" to RedColorScheme,
                        "Green" to GreenColorScheme,
                        "Blue" to BlueColorScheme
                    ),
                    onThemeSelected = { theme ->
                        handleChangeTheme(theme)
                    }
                )
            }
        }
    }
}


@Composable
fun ThemeSelectorDropdown(
    theme: ThemeType,
    themes: List<Pair<String, ColorScheme>>,
    onThemeSelected: (ThemeType) -> Unit
) {
    var expanded = remember { mutableStateOf(false) }
    var selectedTheme = remember { mutableStateOf(theme.name) } // default

    Box {
        // Button to show dropdown
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
                .clickable { expanded.value = true }
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(12.dp)
                    .background(
                        color = ThemeType.valueOf(selectedTheme.value.uppercase()).lightColorScheme.primary,
                        shape = RoundedCornerShape(50)
                    )
            )
            Text(
                text = selectedTheme.value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )

            // Dropdown Menu
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                themes.forEach { theme ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(12.dp)
                                        .background(
                                            color = ThemeType.valueOf(theme.first.uppercase()).lightColorScheme.primary,
                                            shape = RoundedCornerShape(50)
                                        )
                                )
                                Text(theme.first)
                            }
                        },
                        onClick = {
                            selectedTheme.value = theme.first
                            expanded.value = false
                            onThemeSelected(ThemeType.valueOf(theme.first.uppercase()))
                        }
                    )
                }
            }
        }

    }
}
