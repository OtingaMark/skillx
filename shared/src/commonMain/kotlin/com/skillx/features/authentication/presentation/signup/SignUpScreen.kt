package com.skillx.features.authentication.presentation.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.OrDivider
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SkillIllustration
import com.skillx.designsystem.components.scrollIntoViewOnFocus
import com.skillx.designsystem.theme.SkillXColors
import com.skillx.features.authentication.presentation.component.SocialSignInButtons
import org.koin.compose.koinInject

private val FieldShape = RoundedCornerShape(16.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: (onboardingCompleted: Boolean) -> Unit,
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = koinInject()
) {
    val state by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess, state.onboardingCompleted) {
        val onboardingCompleted = state.onboardingCompleted
        if (state.isSuccess && onboardingCompleted != null) onSignUpSuccess(onboardingCompleted)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { TextButton(onClick = onBack) { Text("‹ Back", color = SkillXColors.TextSecondary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SkillXColors.Background)
            )
        },
        containerColor = SkillXColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                SkillIllustration()
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Create Your Account", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = SkillXColors.TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Join a community of learners and teachers from around the world.", style = MaterialTheme.typography.bodyMedium, color = SkillXColors.TextSecondary)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChanged,
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().scrollIntoViewOnFocus(),
                singleLine = true,
                shape = FieldShape
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().scrollIntoViewOnFocus(),
                singleLine = true,
                shape = FieldShape
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().scrollIntoViewOnFocus(),
                singleLine = true,
                shape = FieldShape
            )
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(text = if (state.isLoading) "Creating Account..." else "Sign Up", onClick = viewModel::onSignUp, enabled = !state.isLoading)

            Spacer(modifier = Modifier.height(24.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(16.dp))

            SocialSignInButtons(
                onGoogleClick = viewModel::onGoogleSignUpClicked,
                onLinkedInClick = viewModel::onLinkedInSignUpClicked,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (state.message.isNotEmpty()) {
                Text(text = state.message, color = SkillXColors.Error, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account? ", color = SkillXColors.TextSecondary)
                TextButton(onClick = onNavigateToLogin, contentPadding = PaddingValues(horizontal = 4.dp)) {
                    Text("Log In", color = SkillXColors.Primary, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
