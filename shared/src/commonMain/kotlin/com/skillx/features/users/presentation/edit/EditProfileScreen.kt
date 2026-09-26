package com.skillx.features.users.presentation.edit
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.*

@Composable
fun EditProfileScreen(onBack: () -> Unit, onSaved: () -> Unit) {
    var name by remember { mutableStateOf("") }
    Scaffold(topBar = { SkillXTopBar(title = "Edit Profile", onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) {
            SkillXTextField(value = name, onValueChange = { name = it }, label = "Full Name")
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(text = "Save Changes", onClick = { /* ViewModel.onSave() */ })
        }
    }
}
