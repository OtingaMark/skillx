package com.skillx.app

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.interfaces.LogInCallback
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback

// RevenueCat Test Store public Android key for development/testing.
// Replace this with the production Android public SDK key before release.
private const val REVENUECAT_API_KEY =
    "test_qtBufwxOIFJgidYfpnUZRjlDMrr"

// =========================================================
// SKILLX DESIGN SYSTEM - DAY 11
// =========================================================

private val SkillXLightColors = lightColorScheme(
    primary = Color(0xFF3157D5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE2FF),
    onPrimaryContainer = Color(0xFF0A1A5E),
    secondary = Color(0xFF52627D),
    secondaryContainer = Color(0xFFD9E2F9),
    background = Color(0xFFF8F9FF),
    surface = Color.White,
    surfaceVariant = Color(0xFFE9EAF2)
)

@Composable
private fun SkillXTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SkillXLightColors,
        shapes = MaterialTheme.shapes.copy(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(18.dp),
            large = RoundedCornerShape(24.dp)
        ),
        typography = MaterialTheme.typography.copy(
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            titleLarge = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            titleMedium = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        ),
        content = content
    )
}

@Composable
private fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        content = {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = { content() }
            )
        }
    )
}

@Composable
private fun PrimaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text)
    }
}

@Composable
private fun SecondaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text)
    }
}


// =========================================================
// MAIN ACTIVITY
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        initializeRevenueCat()

        setContent {

            SkillXTheme {
                SkillXApp()
            }
        }
    }


    private fun initializeRevenueCat() {

        Purchases.logLevel = LogLevel.DEBUG

        if (Purchases.isConfigured) {
            return
        }

        val firebaseUser =
            FirebaseAuth.getInstance().currentUser

        val appUserId =
            firebaseUser?.uid

        val builder =
            PurchasesConfiguration.Builder(
                this,
                REVENUECAT_API_KEY
            )

        if (appUserId != null) {
            builder.appUserID(appUserId)
        }

        Purchases.configure(builder.build())
    }
}


// =========================================================
// DAY 12-13 RELEASE CANDIDATE
// Testing, validation, demo readiness and final UX flow
// =========================================================

// =========================================================
// MAIN APP
// =========================================================

@Composable
fun SkillXApp() {

    val auth = FirebaseAuth.getInstance()

    var screen by remember {

        mutableStateOf(
            if (auth.currentUser != null)
                "home"
            else
                "welcome"
        )
    }

    var selectedMatch by remember {
        mutableStateOf<SkillMatch?>(null)
    }

    var selectedLessonRequest by remember {
        mutableStateOf<LessonRequest?>(null)
    }

    when (screen) {

        "welcome" -> {

            WelcomeScreen(
                onLogin = {
                    screen = "login"
                },
                onSignUp = {
                    screen = "signup"
                }
            )
        }


        "signup" -> {

            SignUpScreen(
                onAccountCreated = {

                    loginToRevenueCat()

                    screen = "home"
                },
                onBack = {

                    screen = "welcome"
                }
            )
        }


        "login" -> {

            LoginScreen(
                onLoginSuccess = {

                    loginToRevenueCat()

                    screen = "home"
                },
                onBack = {

                    screen = "welcome"
                }
            )
        }


        "home" -> {

            HomeScreen(
                onProfile = {

                    screen = "profile"
                },
                onSkills = {

                    screen = "skills"
                },
                onFindSkill = {

                    screen = "matches"
                },
                onLessonRequests = {

                    screen = "lessonRequests"
                },
                onRevenueCat = {

                    screen = "revenueCat"
                },
                onSafety = {

                    screen = "safety"
                },
                onHowItWorks = {

                    screen = "howItWorks"
                },
                onLogout = {

                    logoutFromRevenueCat()

                    auth.signOut()

                    screen = "welcome"
                }
            )
        }


        "profile" -> {

            ProfileScreen(
                onBack = {

                    screen = "home"
                },
                onEditProfile = {

                    screen = "editProfile"
                }
            )
        }


        "editProfile" -> {

            EditProfileScreen(
                onBack = {

                    screen = "profile"
                }
            )
        }


        "skills" -> {

            SkillsScreen(
                onBack = {

                    screen = "home"
                }
            )
        }


        "matches" -> {

            MatchesScreen(
                onBack = {

                    screen = "home"
                },
                onViewProfile = { match ->

                    selectedMatch = match

                    screen = "matchProfile"
                }
            )
        }


        "matchProfile" -> {

            selectedMatch?.let { match ->

                MatchProfileScreen(
                    match = match,
                    onBack = {

                        screen = "matches"
                    },
                    onRequestLesson = {

                        screen = "requestLesson"
                    },
                    onReportUser = {

                        screen = "reportUser"
                    }
                )
            }
        }


        "requestLesson" -> {

            selectedMatch?.let { match ->

                RequestLessonScreen(
                    match = match,
                    onBack = {

                        screen = "matchProfile"
                    },
                    onRequestSent = {

                        screen = "matchProfile"
                    }
                )
            }
        }


        "lessonRequests" -> {

            LessonRequestsScreen(
                onBack = {

                    screen = "home"
                },
                onRateLesson = { request ->

                    selectedLessonRequest = request
                    screen = "rateLesson"
                }
            )
        }


        "safety" -> {

            SafetyScreen(
                onBack = {

                    screen = "home"
                }
            )
        }

        "howItWorks" -> {

            HowSkillXWorksScreen(
                onBack = {

                    screen = "home"
                }
            )
        }

        "reportUser" -> {

            selectedMatch?.let { match ->

                ReportUserScreen(
                    user = match,
                    onBack = {

                        screen = "matchProfile"
                    },
                    onReported = {

                        screen = "matchProfile"
                    }
                )
            }
        }

        "rateLesson" -> {

            selectedLessonRequest?.let { request ->

                RateLessonScreen(
                    request = request,
                    onBack = {

                        screen = "lessonRequests"
                    },
                    onRated = {

                        screen = "lessonRequests"
                    }
                )
            }
        }

        "revenueCat" -> {

            RevenueCatScreen(
                onBack = {

                    screen = "home"
                }
            )
        }
    }
}


// =========================================================
// REVENUECAT LOGIN
// =========================================================

fun loginToRevenueCat() {

    val firebaseUser =
        FirebaseAuth.getInstance().currentUser

    val uid =
        firebaseUser?.uid

    if (uid == null || !Purchases.isConfigured) {
        return
    }

    Purchases.sharedInstance.logIn(
        uid,
        object : LogInCallback {

            override fun onReceived(
                customerInfo: com.revenuecat.purchases.CustomerInfo,
                created: Boolean
            ) {
                println("RevenueCat user connected: $uid")
                println("New RevenueCat customer: $created")
            }

            override fun onError(
                error: com.revenuecat.purchases.PurchasesError
            ) {
                println("RevenueCat login failed.")
            }
        }
    )
}


// =========================================================
// REVENUECAT LOGOUT
// =========================================================

fun logoutFromRevenueCat() {

    if (!Purchases.isConfigured) {
        return
    }

    Purchases.sharedInstance.logOut(
        object : ReceiveCustomerInfoCallback {

            override fun onReceived(
                customerInfo: com.revenuecat.purchases.CustomerInfo
            ) {
                println("RevenueCat user logged out.")
            }

            override fun onError(
                error: com.revenuecat.purchases.PurchasesError
            ) {
                println("RevenueCat logout failed.")
            }
        }
    )
}


// =========================================================
// WELCOME
// =========================================================

@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SectionCard {
                Text(
                    text = "SkillX",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Learn. Teach. Exchange.",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Find students who can teach what you want to learn,\nand share the skills you already know.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "New users start with 5 SkillX points.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryAction(
                text = "Create Account",
                onClick = onSignUp
            )

            Spacer(modifier = Modifier.height(10.dp))

            SecondaryAction(
                text = "Login",
                onClick = onLogin
            )
        }
    }
}


// =========================================================
// SIGN UP
// =========================================================

@Composable
fun SignUpScreen(
    onAccountCreated: () -> Unit,
    onBack: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Create Account",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {

                name = it
            },
            label = {

                Text("Name")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {

                email = it
            },
            label = {

                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {

                password = it
            },
            label = {

                Text("Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {

                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    errorMessage = "Please fill in all fields."
                    return@Button
                }

                if (!isValidEmail(email)) {
                    errorMessage = "Please enter a valid email address."
                    return@Button
                }

                if (password.length < 6) {
                    errorMessage = "Password must be at least 6 characters."
                    return@Button
                }

                loading = true
                errorMessage = ""

                auth.createUserWithEmailAndPassword(
                    email.trim(),
                    password
                )
                    .addOnSuccessListener { result ->

                        val uid =
                            result.user?.uid

                        if (uid != null) {

                            val userData =
                                hashMapOf<String, Any>(

                                    "name" to
                                            name.trim(),

                                    "email" to
                                            email.trim(),

                                    "teachSkills" to
                                            emptyList<String>(),

                                    "learnSkills" to
                                            emptyList<String>(),

                                    "points" to
                                            5
                                )

                            db.collection("users")
                                .document(uid)
                                .set(userData)
                                .addOnSuccessListener {

                                    loading = false

                                    onAccountCreated()
                                }
                                .addOnFailureListener { exception ->

                                    loading = false

                                    errorMessage =
                                        exception.message
                                            ?: "Could not save profile."
                                }

                        } else {

                            loading = false

                            errorMessage =
                                "Account was created but user ID was missing."
                        }
                    }
                    .addOnFailureListener { exception ->

                        loading = false

                        errorMessage =
                            exception.message
                                ?: "Account creation failed."
                    }

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (loading)
                    "Creating..."
                else
                    "Create Account"
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        TextButton(
            onClick = onBack
        ) {

            Text("Back")
        }

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = errorMessage,
                color =
                    MaterialTheme.colorScheme.error
            )
        }
    }
}


// =========================================================
// LOGIN
// =========================================================

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Login",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {

                email = it
            },
            label = {

                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {

                password = it
            },
            label = {

                Text("Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {

                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter email and password."
                    return@Button
                }

                if (!isValidEmail(email)) {
                    errorMessage = "Please enter a valid email address."
                    return@Button
                }

                loading = true
                errorMessage = ""

                auth.signInWithEmailAndPassword(
                    email.trim(),
                    password
                )
                    .addOnSuccessListener {

                        loading = false

                        onLoginSuccess()
                    }
                    .addOnFailureListener { exception ->

                        loading = false

                        errorMessage =
                            exception.message
                                ?: "Login failed."
                    }

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (loading)
                    "Logging in..."
                else
                    "Login"
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        TextButton(
            onClick = onBack
        ) {

            Text("Back")
        }

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = errorMessage,
                color =
                    MaterialTheme.colorScheme.error
            )
        }
    }
}


// =========================================================
// HOME
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfile: () -> Unit,
    onSkills: () -> Unit,
    onFindSkill: () -> Unit,
    onLessonRequests: () -> Unit,
    onRevenueCat: () -> Unit,
    onSafety: () -> Unit,
    onHowItWorks: () -> Unit,
    onLogout: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("Student") }
    var points by remember { mutableStateOf(5) }

    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    name = document.getString("name") ?: "Student"
                    points = document.getLong("points")?.toInt() ?: 5
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SkillX",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Skill exchange for students",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Welcome, $name",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "What would you like to do today?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text(
                            text = "Your SkillX Balance",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$points points",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Teach a completed lesson to earn 1 point.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            item {
                SectionCard {
                    Text(
                        text = "Learn",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Find students who can teach a skill you want to learn.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    PrimaryAction(
                        text = "Find a Skill to Learn",
                        onClick = onFindSkill
                    )
                }
            }

            item {
                SectionCard {
                    Text(
                        text = "Your Lessons",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Manage requests, accepted lessons and completed sessions.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SecondaryAction(
                        text = "My Lesson Requests",
                        onClick = onLessonRequests
                    )
                }
            }

            item {
                SectionCard {
                    Text(
                        text = "SkillX Tools",
                        style = MaterialTheme.typography.titleLarge
                    )
                    SecondaryAction(
                        text = "Buy SkillX Points",
                        onClick = onRevenueCat
                    )
                    SecondaryAction(
                        text = "My Skills",
                        onClick = onSkills
                    )
                    SecondaryAction(
                        text = "My Profile",
                        onClick = onProfile
                    )
                }
            }

            item {
                SectionCard {
                    Text(
                        text = "New to SkillX?",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "See how matching, lessons and SkillX points work from start to finish.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    SecondaryAction(
                        text = "How SkillX Works",
                        onClick = onHowItWorks
                    )
                }
            }

            item {
                SecondaryAction(
                    text = "Safety Guidelines",
                    onClick = onSafety
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Meet safely and keep SkillX focused on learning.",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


// =========================================================
// BUY SKILLX POINTS - DAY 9
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueCatScreen(
    onBack: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    val context = LocalContext.current

    var offerings by remember {
        mutableStateOf<Offerings?>(null)
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var purchasing by remember {
        mutableStateOf(false)
    }

    var points by remember {
        mutableStateOf(0)
    }

    var message by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    fun loadPoints() {

        val uid =
            auth.currentUser?.uid

        if (uid == null) {
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                points =
                    document.getLong("points")
                        ?.toInt()
                        ?: 0
            }
    }

    LaunchedEffect(Unit) {

        loadPoints()

        if (!Purchases.isConfigured) {

            loading = false
            errorMessage =
                "RevenueCat is not configured."

            return@LaunchedEffect
        }

        val firebaseUser =
            auth.currentUser

        if (firebaseUser == null) {

            loading = false
            errorMessage =
                "No Firebase user is logged in."

            return@LaunchedEffect
        }

        Purchases.sharedInstance.logIn(
            firebaseUser.uid,
            object : LogInCallback {

                override fun onReceived(
                    customerInfo: com.revenuecat.purchases.CustomerInfo,
                    created: Boolean
                ) {

                    Purchases.sharedInstance.getOfferings(
                        object : ReceiveOfferingsCallback {

                            override fun onReceived(
                                receivedOfferings: Offerings
                            ) {

                                offerings =
                                    receivedOfferings

                                loading = false
                                errorMessage = ""
                            }

                            override fun onError(
                                error: com.revenuecat.purchases.PurchasesError
                            ) {

                                loading = false

                                errorMessage =
                                    "Could not load point packages. Check your RevenueCat offerings."
                            }
                        }
                    )
                }

                override fun onError(
                    error: com.revenuecat.purchases.PurchasesError
                ) {

                    loading = false

                    errorMessage =
                        "Could not connect your account to RevenueCat."
                }
            }
        )
    }

    fun buyPackage(
        packageToBuy: Package
    ) {

        val activity =
            context.findActivity()

        if (activity == null) {

            errorMessage =
                "Could not start the purchase screen."

            return
        }

        if (purchasing) {
            return
        }

        val productId =
            packageToBuy.product.id

        val pointsToAdd =
            pointsForProduct(productId)

        if (pointsToAdd <= 0) {

            errorMessage =
                "Unknown SkillX point package: $productId"

            return
        }

        purchasing = true
        message = ""
        errorMessage = ""

        val purchaseParams =
            PurchaseParams.Builder(
                activity,
                packageToBuy
            ).build()

        Purchases.sharedInstance.purchase(
            purchaseParams,
            object : PurchaseCallback {

                override fun onCompleted(
                    storeTransaction: com.revenuecat.purchases.models.StoreTransaction,
                    customerInfo: com.revenuecat.purchases.CustomerInfo
                ) {

                    val uid =
                        auth.currentUser?.uid

                    if (uid == null) {

                        purchasing = false
                        errorMessage =
                            "Purchase succeeded, but no Firebase user was found."
                        return
                    }

                    db.collection("users")
                        .document(uid)
                        .update(
                            "points",
                            com.google.firebase.firestore.FieldValue.increment(
                                pointsToAdd.toLong()
                            )
                        )
                        .addOnSuccessListener {

                            purchasing = false
                            points += pointsToAdd

                            message =
                                "Purchase successful. You received $pointsToAdd SkillX points."
                        }
                        .addOnFailureListener { exception ->

                            purchasing = false

                            errorMessage =
                                "Purchase succeeded, but points could not be added to your account. ${exception.message ?: "Please contact support."}"
                        }
                }

                override fun onError(
                    error: com.revenuecat.purchases.PurchasesError,
                    userCancelled: Boolean
                ) {

                    purchasing = false

                    if (userCancelled) {

                        message =
                            "Purchase cancelled."

                    } else {

                        errorMessage =
                            "Purchase failed. Please try again."
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Buy SkillX Points")
                },

                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            item {

                Text(
                    text = "Your SkillX Points",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = points.toString(),
                    style =
                        MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text =
                        "Need more points? Buy a point package below.",
                    style =
                        MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                if (loading) {

                    Text("Loading point packages...")

                } else if (offerings?.current == null) {

                    Text(
                        text =
                            "No point packages are available yet. Create products and a default offering in RevenueCat.",
                        color =
                            MaterialTheme.colorScheme.error
                    )

                } else {

                    val packages =
                        offerings?.current?.availablePackages
                            ?: emptyList()

                    packages.forEach { packageToBuy ->

                        val productId =
                            packageToBuy.product.id

                        val pointsInPackage =
                            pointsForProduct(productId)

                        if (pointsInPackage > 0) {

                            Card(
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier =
                                        Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text =
                                            "$pointsInPackage SkillX Points",
                                        style =
                                            MaterialTheme.typography.titleLarge
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(8.dp)
                                    )

                                    Text(
                                        text =
                                            packageToBuy.product.price.formatted
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(12.dp)
                                    )

                                    Button(
                                        onClick = {
                                            buyPackage(packageToBuy)
                                        },
                                        modifier =
                                            Modifier.fillMaxWidth(),
                                        enabled = !purchasing
                                    ) {

                                        Text(
                                            if (purchasing)
                                                "Processing..."
                                            else
                                                "Buy $pointsInPackage Points"
                                        )
                                    }
                                }
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )
                        }
                    }
                }

                if (message.isNotEmpty()) {

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(
                        text = message,
                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }

                if (errorMessage.isNotEmpty()) {

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(
                        text = errorMessage,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(32.dp)
                )

                Text(
                    text =
                        "Teaching a completed lesson earns 1 point. Attending a completed lesson costs 1 point.",
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


// =========================================================
// DAY 9 HELPERS
// =========================================================

fun pointsForProduct(
    productId: String
): Int {

    return when (productId) {

        "skillx_points_5" -> 5

        "skillx_points_15" -> 15

        "skillx_points_30" -> 30

        else -> 0
    }
}

fun Context.findActivity(): Activity? {

    return when (this) {

        is Activity -> this

        is ContextWrapper -> baseContext.findActivity()

        else -> null
    }
}


// =========================================================
// PROFILE
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var name by remember {
        mutableStateOf("Loading...")
    }

    var email by remember {
        mutableStateOf("")
    }

    var teachSkills by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var learnSkills by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var points by remember {
        mutableStateOf(5)
    }

    LaunchedEffect(Unit) {

        val uid =
            auth.currentUser?.uid

        if (uid != null) {

            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    name =
                        document.getString("name")
                            ?: ""

                    email =
                        document.getString("email")
                            ?: ""

                    teachSkills =
                        getSkillsFromFirestore(
                            document,
                            "teachSkills"
                        )

                    learnSkills =
                        getSkillsFromFirestore(
                            document,
                            "learnSkills"
                        )

                    points =
                        document.getLong("points")
                            ?.toInt()
                            ?: 5
                }
        }
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {

                    Text("My Profile")
                },
                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {

                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            item {

                Text(
                    text = name,
                    style =
                        MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(email)

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text =
                        "SkillX Points: $points",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Skills I Teach",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (teachSkills.isEmpty()) {

                    Text(
                        "No teaching skills added."
                    )

                } else {

                    teachSkills.forEach { skill ->

                        Text("• $skill")
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text =
                        "Skills I Want to Learn",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (learnSkills.isEmpty()) {

                    Text(
                        "No learning skills added."
                    )

                } else {

                    learnSkills.forEach { skill ->

                        Text("• $skill")
                    }
                }

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                Button(
                    onClick = onEditProfile,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Edit Profile")
                }
            }
        }
    }
}


// =========================================================
// EDIT PROFILE
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var name by remember {
        mutableStateOf("")
    }

    var teachSkillsText by remember {
        mutableStateOf("")
    }

    var learnSkillsText by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        val uid =
            auth.currentUser?.uid

        if (uid != null) {

            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    name =
                        document.getString("name")
                            ?: ""

                    teachSkillsText =
                        getSkillsFromFirestore(
                            document,
                            "teachSkills"
                        ).joinToString(", ")

                    learnSkillsText =
                        getSkillsFromFirestore(
                            document,
                            "learnSkills"
                        ).joinToString(", ")
                }
        }
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {

                    Text("Edit Profile")
                },
                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {

                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            item {

                OutlinedTextField(
                    value = name,
                    onValueChange = {

                        name = it
                    },
                    label = {

                        Text("Name")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = teachSkillsText,
                    onValueChange = {

                        teachSkillsText = it
                    },
                    label = {

                        Text("Skills I Teach")
                    },
                    placeholder = {

                        Text("e.g. Python, Guitar")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = learnSkillsText,
                    onValueChange = {

                        learnSkillsText = it
                    },
                    label = {

                        Text("Skills I Want to Learn")
                    },
                    placeholder = {

                        Text("e.g. Java, Piano")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = {

                        val uid =
                            auth.currentUser?.uid

                        if (uid == null) {

                            message =
                                "You are not logged in."

                            return@Button
                        }

                        loading = true
                        message = ""

                        val updates =
                            hashMapOf<String, Any>(

                                "name" to
                                        name.trim(),

                                "teachSkills" to
                                        parseSkills(
                                            teachSkillsText
                                        ),

                                "learnSkills" to
                                        parseSkills(
                                            learnSkillsText
                                        )
                            )

                        db.collection("users")
                            .document(uid)
                            .update(updates)
                            .addOnSuccessListener {

                                loading = false

                                message =
                                    "Profile saved."
                            }
                            .addOnFailureListener { exception ->

                                loading = false

                                message =
                                    exception.message
                                        ?: "Could not save profile."
                            }

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        if (loading)
                            "Saving..."
                        else
                            "Save Changes"
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                if (message.isNotEmpty()) {

                    Text(message)
                }
            }
        }
    }
}


// =========================================================
// SKILLS
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    onBack: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var teachSkills by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var learnSkills by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var newTeachSkill by remember {
        mutableStateOf("")
    }

    var newLearnSkill by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var saving by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        val uid =
            auth.currentUser?.uid

        if (uid != null) {

            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    teachSkills =
                        getSkillsFromFirestore(
                            document,
                            "teachSkills"
                        )

                    learnSkills =
                        getSkillsFromFirestore(
                            document,
                            "learnSkills"
                        )
                }
        }
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {

                    Text("My Skills")
                },
                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {

                        Text("Back")
                    }
                }
            )
        },

        bottomBar = {

            Button(
                onClick = {

                    val uid =
                        auth.currentUser?.uid

                    if (uid != null && !saving) {

                        saving = true

                        val updates =
                            hashMapOf<String, Any>(

                                "teachSkills" to
                                        teachSkills,

                                "learnSkills" to
                                        learnSkills
                            )

                        db.collection("users")
                            .document(uid)
                            .update(updates)
                            .addOnSuccessListener {

                                saving = false
                                message = "Skills saved successfully."
                            }
                            .addOnFailureListener { exception ->

                                saving = false
                                message = exception.message
                                    ?: "Could not save skills."
                            }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(if (saving) "Saving..." else "Save Skills")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {

            item {

                Text(
                    text =
                        "Skills I Can Teach",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = newTeachSkill,
                    onValueChange = {

                        newTeachSkill = it
                    },
                    label = {

                        Text(
                            "Add a teaching skill"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {

                        val skill =
                            newTeachSkill.trim()

                        if (
                            skill.isNotEmpty() &&
                            !teachSkills.any {
                                it.equals(
                                    skill,
                                    ignoreCase = true
                                )
                            }
                        ) {

                            teachSkills =
                                teachSkills + skill

                            newTeachSkill = ""
                        }

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        "Add Teaching Skill"
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                teachSkills.forEach { skill ->

                    SkillRow(
                        skill = skill,
                        onRemove = {

                            teachSkills =
                                teachSkills.filter {
                                    it != skill
                                }
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                Text(
                    text =
                        "Skills I Want to Learn",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = newLearnSkill,
                    onValueChange = {

                        newLearnSkill = it
                    },
                    label = {

                        Text(
                            "Add a learning skill"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {

                        val skill =
                            newLearnSkill.trim()

                        if (
                            skill.isNotEmpty() &&
                            !learnSkills.any {
                                it.equals(
                                    skill,
                                    ignoreCase = true
                                )
                            }
                        ) {

                            learnSkills =
                                learnSkills + skill

                            newLearnSkill = ""
                        }

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        "Add Learning Skill"
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                learnSkills.forEach { skill ->

                    SkillRow(
                        skill = skill,
                        onRemove = {

                            learnSkills =
                                learnSkills.filter {
                                    it != skill
                                }
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                if (message.isNotEmpty()) {

                    Text(message)
                }
            }
        }
    }
}


// =========================================================
// SKILL ROW
// =========================================================

@Composable
fun SkillRow(
    skill: String,
    onRemove: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = skill,
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = onRemove
        ) {

            Text("Remove")
        }
    }
}


// =========================================================
// MATCH DATA
// =========================================================

data class SkillMatch(
    val uid: String,
    val name: String,
    val email: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>,
    val matchedSkill: String
)


// =========================================================
// MATCHES
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    onBack: () -> Unit,
    onViewProfile: (SkillMatch) -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var matches by remember {
        mutableStateOf<List<SkillMatch>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        val currentUser =
            auth.currentUser

        if (currentUser == null) {

            loading = false

            errorMessage =
                "You are not logged in."

            return@LaunchedEffect
        }

        db.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { myDocument ->

                val myLearningSkills =
                    getSkillsFromFirestore(
                        myDocument,
                        "learnSkills"
                    )

                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->

                        val foundMatches =
                            mutableListOf<SkillMatch>()

                        for (
                        document in result.documents
                        ) {

                            if (
                                document.id ==
                                currentUser.uid
                            ) {

                                continue
                            }

                            val otherTeachSkills =
                                getSkillsFromFirestore(
                                    document,
                                    "teachSkills"
                                )

                            val matchedSkill =
                                findMatchingSkill(
                                    myLearningSkills,
                                    otherTeachSkills
                                )

                            if (
                                matchedSkill != null
                            ) {

                                foundMatches.add(
                                    SkillMatch(

                                        uid =
                                            document.id,

                                        name =
                                            document.getString(
                                                "name"
                                            )
                                                ?: "Student",

                                        email =
                                            document.getString(
                                                "email"
                                            )
                                                ?: "",

                                        teachSkills =
                                            otherTeachSkills,

                                        learnSkills =
                                            getSkillsFromFirestore(
                                                document,
                                                "learnSkills"
                                            ),

                                        matchedSkill =
                                            matchedSkill
                                    )
                                )
                            }
                        }

                        matches =
                            foundMatches

                        loading = false
                    }
                    .addOnFailureListener { exception ->

                        loading = false

                        errorMessage =
                            exception.message
                                ?: "Could not find students."
                    }
            }
            .addOnFailureListener { exception ->

                loading = false

                errorMessage =
                    exception.message
                        ?: "Could not load your skills."
            }
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {

                    Text("Find a Skill")
                },
                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {

                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        when {

            loading -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        "Finding students..."
                    )
                }
            }

            errorMessage.isNotEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp)
                ) {

                    Text(
                        text = errorMessage,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }

            matches.isEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp)
                ) {

                    Text(
                        text = "No matches yet.",
                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "Add skills you want to learn and wait for another student who can teach them."
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {

                    items(matches) { match ->

                        MatchCard(
                            match = match,
                            onClick = {

                                onViewProfile(match)
                            }
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )
                    }
                }
            }
        }
    }
}


// =========================================================
// MATCH CARD
// =========================================================

@Composable
fun MatchCard(
    match: SkillMatch,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = match.name,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                "Can teach: ${match.matchedSkill}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                "Email: ${match.email}"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("View Profile")
            }
        }
    }
}


// =========================================================
// MATCH PROFILE
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchProfileScreen(
    match: SkillMatch,
    onBack: () -> Unit,
    onRequestLesson: () -> Unit,
    onReportUser: () -> Unit
) {

    Scaffold(
        topBar = {

            TopAppBar(
                title = {

                    Text("Student Profile")
                },
                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {

                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            item {

                Text(
                    text = match.name,
                    style =
                        MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(match.email)

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                RatingSummary(
                    userId = match.uid
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text =
                        "Skill You Want to Learn",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    "• ${match.matchedSkill}"
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Skills They Teach",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                match.teachSkills.forEach { skill ->

                    Text("• $skill")
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text =
                        "Skills They Want to Learn",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (
                    match.learnSkills.isEmpty()
                ) {

                    Text(
                        "No learning skills listed."
                    )

                } else {

                    match.learnSkills.forEach { skill ->

                        Text("• $skill")
                    }
                }

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                Button(
                    onClick = onRequestLesson,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        "Request a Lesson"
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = onReportUser,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Report User")
                }
            }
        }
    }
}


// =========================================================
// REQUEST LESSON
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestLessonScreen(
    match: SkillMatch,
    onBack: () -> Unit,
    onRequestSent: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var sending by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    var success by remember {
        mutableStateOf(false)
    }

    var requesterName by remember {
        mutableStateOf("Student")
    }

    LaunchedEffect(Unit) {

        val uid =
            auth.currentUser?.uid

        if (uid != null) {

            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    requesterName =
                        document.getString("name")
                            ?: "Student"
                }
        }
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Request Lesson")
                },

                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            Text(
                text =
                    "Request a lesson from ${match.name}",
                style =
                    MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text("Skill:")

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = match.matchedSkill,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text("Teacher: ${match.name}")

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                "Your point is not deducted until the lesson is completed."
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                onClick = {

                    val currentUser =
                        auth.currentUser

                    if (currentUser == null) {

                        message =
                            "You are not logged in."

                        return@Button
                    }

                    if (sending || success) {
                        return@Button
                    }

                    sending = true
                    message = ""

                    db.collection("lessonRequests")
                        .whereEqualTo(
                            "requesterId",
                            currentUser.uid
                        )
                        .whereEqualTo(
                            "teacherId",
                            match.uid
                        )
                        .whereEqualTo(
                            "skill",
                            match.matchedSkill.trim()
                        )
                        .get()
                        .addOnSuccessListener { snapshot ->

                            val activeRequest =
                                snapshot.documents.firstOrNull { document ->

                                    val status =
                                        document.getString("status")
                                            ?.lowercase()
                                            ?.trim()

                                    status == "pending" ||
                                            status == "accepted"
                                }

                            if (activeRequest != null) {

                                sending = false

                                val status =
                                    activeRequest
                                        .getString("status")
                                        ?.lowercase()
                                        ?.trim()

                                message =
                                    if (status == "accepted") {
                                        "You already have an accepted lesson for this skill with this student."
                                    } else {
                                        "You already have a pending lesson request for this skill with this student."
                                    }

                                return@addOnSuccessListener
                            }

                            val requestData =
                                hashMapOf<String, Any>(

                                    "requesterId" to
                                            currentUser.uid,

                                    "teacherId" to
                                            match.uid,

                                    "requesterName" to
                                            requesterName,

                                    "teacherName" to
                                            match.name,

                                    "skill" to
                                            match.matchedSkill.trim(),

                                    "status" to
                                            "pending",

                                    "createdAt" to
                                            FieldValue.serverTimestamp()
                                )

                            db.collection("lessonRequests")
                                .add(requestData)
                                .addOnSuccessListener {

                                    sending = false
                                    success = true

                                    message =
                                        "Lesson request sent successfully."
                                }
                                .addOnFailureListener { exception ->

                                    sending = false

                                    message =
                                        exception.message
                                            ?: "Could not send lesson request."
                                }
                        }
                        .addOnFailureListener { exception ->

                            sending = false

                            message =
                                exception.message
                                    ?: "Could not check existing lesson requests."
                        }
                },

                modifier = Modifier.fillMaxWidth(),

                enabled =
                    !sending &&
                            !success

            ) {

                Text(
                    when {

                        sending ->
                            "Checking..."

                        success ->
                            "Request Sent"

                        else ->
                            "Send Lesson Request"
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            if (message.isNotEmpty()) {

                Text(
                    text = message,
                    color =
                        if (success)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                )
            }

            if (success) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedButton(
                    onClick = onRequestSent,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Back to Student Profile")
                }
            }
        }
    }
}


// =========================================================
// LESSON REQUEST DATA
// =========================================================

data class LessonRequest(
    val id: String,
    val requesterId: String,
    val teacherId: String,
    val requesterName: String,
    val teacherName: String,
    val skill: String,
    val status: String
)


// =========================================================
// LESSON REQUESTS
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonRequestsScreen(
    onBack: () -> Unit,
    onRateLesson: (LessonRequest) -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var requests by remember {
        mutableStateOf<List<LessonRequest>>(
            emptyList()
        )
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var message by remember {
        mutableStateOf("")
    }

    fun loadRequests() {

        val uid =
            auth.currentUser?.uid

        if (uid == null) {

            loading = false

            message =
                "You are not logged in."

            return
        }

        db.collection("lessonRequests")
            .get()
            .addOnSuccessListener { result ->

                val myRequests =
                    mutableListOf<LessonRequest>()

                for (
                document in result.documents
                ) {

                    val requesterId =
                        document.getString(
                            "requesterId"
                        )
                            ?: ""

                    val teacherId =
                        document.getString(
                            "teacherId"
                        )
                            ?: ""

                    if (
                        requesterId == uid ||
                        teacherId == uid
                    ) {

                        myRequests.add(
                            LessonRequest(

                                id =
                                    document.id,

                                requesterId =
                                    requesterId,

                                teacherId =
                                    teacherId,

                                requesterName =
                                    document.getString(
                                        "requesterName"
                                    )
                                        ?: "Student",

                                teacherName =
                                    document.getString(
                                        "teacherName"
                                    )
                                        ?: "Student",

                                skill =
                                    document.getString(
                                        "skill"
                                    )
                                        ?: "",

                                status =
                                    document.getString(
                                        "status"
                                    )
                                        ?: "pending"
                            )
                        )
                    }
                }

                requests =
                    myRequests

                loading = false
            }
            .addOnFailureListener { exception ->

                loading = false

                message =
                    exception.message
                        ?: "Could not load lesson requests."
            }
    }

    LaunchedEffect(Unit) {

        loadRequests()
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {

                    Text("My Lesson Requests")
                },
                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {

                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        when {

            loading -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        "Loading lesson requests..."
                    )
                }
            }

            message.isNotEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp)
                ) {

                    Text(
                        message,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }

            requests.isEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp)
                ) {

                    Text(
                        "No lesson requests yet.",
                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "Lesson requests you send or receive will appear here."
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {

                    items(
                        items = requests,
                        key = {
                            it.id
                        }
                    ) { request ->

                        LessonRequestCard(
                            request = request,
                            onChanged = {

                                loading = true

                                loadRequests()
                            },
                            onRateLesson = onRateLesson
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )
                    }
                }
            }
        }
    }
}


// =========================================================
// LESSON REQUEST CARD
// =========================================================

@Composable
fun LessonRequestCard(
    request: LessonRequest,
    onChanged: () -> Unit,
    onRateLesson: (LessonRequest) -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    val currentUid =
        auth.currentUser?.uid

    val isTeacher =
        currentUid == request.teacherId

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = request.skill,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (isTeacher) {

                Text(
                    "Lesson requested by: ${request.requesterName}"
                )

            } else {

                Text(
                    "Teacher: ${request.teacherName}"
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                "Status: ${request.status.uppercase()}"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (
                isTeacher &&
                request.status == "pending"
            ) {

                Button(
                    onClick = {

                        db.collection(
                            "lessonRequests"
                        )
                            .document(request.id)
                            .update(
                                "status",
                                "accepted"
                            )
                            .addOnSuccessListener {

                                onChanged()
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        "Accept Lesson"
                    )
                }
            }

            if (
                request.status == "accepted"
            ) {

                Button(
                    onClick = {

                        completeLesson(
                            request = request,

                            onSuccess = {

                                onChanged()
                            },

                            onFailure = { error ->

                                println(
                                    "Completion error: $error"
                                )
                            }
                        )

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        "Mark Lesson Completed"
                    )
                }
            }

            if (
                request.status == "completed"
            ) {

                Text(
                    text =
                        "Lesson completed. 1 point was transferred.",
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {

                        onRateLesson(request)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Rate This Lesson")
                }
            }
        }
    }
}


// =========================================================
// COMPLETE LESSON - DAY 7
// =========================================================

fun completeLesson(
    request: LessonRequest,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {

    val db =
        FirebaseFirestore.getInstance()

    val learnerRef =
        db.collection("users")
            .document(request.requesterId)

    val teacherRef =
        db.collection("users")
            .document(request.teacherId)

    val lessonRef =
        db.collection("lessonRequests")
            .document(request.id)

    db.runTransaction { transaction ->

        val learnerSnapshot =
            transaction.get(learnerRef)

        val teacherSnapshot =
            transaction.get(teacherRef)

        val lessonSnapshot =
            transaction.get(lessonRef)

        val learnerPoints =
            learnerSnapshot
                .getLong("points")
                ?.toInt()
                ?: 0

        val teacherPoints =
            teacherSnapshot
                .getLong("points")
                ?.toInt()
                ?: 0

        val currentStatus =
            lessonSnapshot.getString(
                "status"
            )
                ?: "pending"

        if (
            currentStatus == "completed"
        ) {

            throw Exception(
                "This lesson has already been completed."
            )
        }

        if (
            currentStatus != "accepted"
        ) {

            throw Exception(
                "Only accepted lessons can be completed."
            )
        }

        if (
            learnerPoints <= 0
        ) {

            throw Exception(
                "The learner does not have enough SkillX points."
            )
        }

        transaction.update(
            learnerRef,
            "points",
            learnerPoints - 1
        )

        transaction.update(
            teacherRef,
            "points",
            teacherPoints + 1
        )

        transaction.update(
            lessonRef,
            "status",
            "completed"
        )

        null

    }
        .addOnSuccessListener {

            onSuccess()
        }
        .addOnFailureListener { exception ->

            onFailure(
                exception.message
                    ?: "Could not complete lesson."
            )
        }
}



// =========================================================
// RATING SUMMARY
// =========================================================

@Composable
fun RatingSummary(
    userId: String
) {

    val db =
        FirebaseFirestore.getInstance()

    var average by remember {
        mutableStateOf<Double?>(null)
    }

    var count by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(userId) {

        db.collection("ratings")
            .whereEqualTo("ratedUserId", userId)
            .get()
            .addOnSuccessListener { result ->

                val scores =
                    result.documents.mapNotNull { document ->

                        document.getLong("rating")
                            ?.toDouble()
                    }

                count = scores.size

                average =
                    if (scores.isEmpty()) {
                        null
                    } else {
                        scores.average()
                    }
            }
    }

    if (average == null) {

        Text("No ratings yet.")

    } else {

        Text(
            text =
                "Rating: ${String.format("%.1f", average)} / 5 ($count rating${if (count == 1) "" else "s"})",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


// =========================================================
// RATE LESSON - DAY 10
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateLessonScreen(
    request: LessonRequest,
    onBack: () -> Unit,
    onRated: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    val currentUid =
        auth.currentUser?.uid

    val ratedUserId =
        if (currentUid == request.teacherId) {
            request.requesterId
        } else {
            request.teacherId
        }

    val ratedUserName =
        if (currentUid == request.teacherId) {
            request.requesterName
        } else {
            request.teacherName
        }

    var selectedRating by remember {
        mutableStateOf(0)
    }

    var comment by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    var alreadyRated by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(request.id, currentUid) {

        if (currentUid == null) {
            message = "You are not logged in."
            return@LaunchedEffect
        }

        if (request.status != "completed") {
            message = "Only completed lessons can be rated."
            return@LaunchedEffect
        }

        val ratingId =
            "${request.id}_$currentUid"

        db.collection("ratings")
            .document(ratingId)
            .get()
            .addOnSuccessListener { document ->

                alreadyRated = document.exists()

                if (alreadyRated) {
                    message = "You have already rated this lesson."
                }
            }
            .addOnFailureListener { exception ->

                message =
                    exception.message
                        ?: "Could not check your previous rating."
            }
    }

    fun submitRating() {

        if (currentUid == null) {
            message = "You are not logged in."
            return
        }

        if (selectedRating !in 1..5) {
            message = "Please select a rating from 1 to 5."
            return
        }

        if (alreadyRated) {
            message = "You have already rated this lesson."
            return
        }

        loading = true
        message = ""

        val ratingId =
            "${request.id}_$currentUid"

        val ratingData =
            hashMapOf<String, Any>(
                "lessonId" to request.id,
                "raterId" to currentUid,
                "ratedUserId" to ratedUserId,
                "ratedUserName" to ratedUserName,
                "rating" to selectedRating,
                "comment" to comment.trim(),
                "createdAt" to FieldValue.serverTimestamp()
            )

        db.collection("ratings")
            .document(ratingId)
            .set(ratingData)
            .addOnSuccessListener {

                loading = false
                alreadyRated = true
                message = "Rating submitted successfully."
            }
            .addOnFailureListener { exception ->

                loading = false
                message =
                    exception.message
                        ?: "Could not submit rating."
            }
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Rate Lesson")
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            item {

                Text(
                    text = "Rate your lesson with $ratedUserName",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Skill: ${request.skill}"
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "How was the lesson?",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    for (rating in 1..5) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            RadioButton(
                                selected = selectedRating == rating,
                                onClick = {
                                    selectedRating = rating
                                },
                                enabled = !alreadyRated
                            )

                            Text(rating.toString())
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = {
                        comment = it
                    },
                    label = {
                        Text("Optional comment")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !alreadyRated
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {
                        submitRating()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading && !alreadyRated
                ) {

                    Text(
                        if (loading) {
                            "Submitting..."
                        } else {
                            "Submit Rating"
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                if (message.isNotEmpty()) {

                    Text(
                        text = message,
                        color =
                            if (message.contains("successfully")) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                    )
                }

                if (message.contains("successfully")) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    OutlinedButton(
                        onClick = onRated,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Back to Lesson Requests")
                    }
                }
            }
        }
    }
}


// =========================================================
// HOW SKILLX WORKS - DAY 13 DEMO READY
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowSkillXWorksScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How SkillX Works") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Learn from students. Teach what you know.",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "SkillX connects students based on the skills they want to learn and the skills other students can teach.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item { HowItWorksStep("1", "Create your profile", "Add the skills you can teach and the skills you want to learn.") }
            item { HowItWorksStep("2", "Find a match", "SkillX compares your learning skills with other students' teaching skills.") }
            item { HowItWorksStep("3", "Request a lesson", "Choose a matched student and send a request for the skill you want to learn.") }
            item { HowItWorksStep("4", "Complete the lesson", "The teacher accepts the request and marks it completed after the lesson happens.") }
            item { HowItWorksStep("5", "Exchange a point", "The learner spends 1 point and the teacher earns 1 point after completion.") }
            item { HowItWorksStep("6", "Rate and stay safe", "Participants can rate completed lessons. Use public or institution-approved meeting places and report unsafe behaviour.") }

            item {
                SectionCard {
                    Text("Point Rules", style = MaterialTheme.typography.titleLarge)
                    Text("• New users start with 5 points.")
                    Text("• 1 completed lesson costs the learner 1 point.")
                    Text("• The teacher receives 1 point after completion.")
                    Text("• Points cannot go below zero.")
                    Text("• Additional points can be purchased through RevenueCat.")
                }
            }

            item {
                SectionCard {
                    Text("Complete SkillX Flow", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Profile → Match → Request → Accept → Complete → Point Exchange → Rate",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun HowItWorksStep(
    number: String,
    title: String,
    description: String
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    number,
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// =========================================================
// SAFETY GUIDELINES - DAY 10
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyScreen(
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Safety Guidelines")
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            item {

                Text(
                    text = "Stay Safe on SkillX",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                SafetyRule(
                    "Meet in public or institution-approved places."
                )

                SafetyRule(
                    "Do not share passwords, financial details, or other sensitive information."
                )

                SafetyRule(
                    "Keep lesson communication respectful and focused on the agreed skill."
                )

                SafetyRule(
                    "If a user behaves inappropriately or you feel unsafe, stop the interaction and report the user."
                )

                SafetyRule(
                    "Do not send money directly to another student. SkillX points are handled by the platform."
                )

                SafetyRule(
                    "Only mark a lesson completed after the lesson has actually taken place."
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "SkillX is designed for peer learning. Use good judgment and follow your institution's safety policies.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Composable
fun SafetyRule(
    text: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Text(
            text = "• $text",
            modifier = Modifier.padding(16.dp)
        )
    }
}


// =========================================================
// REPORT USER - DAY 10
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportUserScreen(
    user: SkillMatch,
    onBack: () -> Unit,
    onReported: () -> Unit
) {

    val auth =
        FirebaseAuth.getInstance()

    val db =
        FirebaseFirestore.getInstance()

    var reason by remember {
        mutableStateOf("")
    }

    var customDetails by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    var submitted by remember {
        mutableStateOf(false)
    }

    val reasons = listOf(
        "Inappropriate behavior",
        "Harassment or bullying",
        "Spam or scam",
        "Unsafe behavior",
        "Other"
    )

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Report User")
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            item {

                Text(
                    text = "Report ${user.name}",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    "Reports help SkillX identify behavior that may make other students unsafe."
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Reason",
                    style = MaterialTheme.typography.titleMedium
                )

                reasons.forEach { option ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = reason == option,
                            onClick = {
                                reason = option
                            },
                            enabled = !submitted
                        )

                        Text(option)
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = customDetails,
                    onValueChange = {
                        customDetails = it
                    },
                    label = {
                        Text("Additional details")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !submitted
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {

                        val reporterId =
                            auth.currentUser?.uid

                        if (reporterId == null) {
                            message = "You are not logged in."
                            return@Button
                        }

                        if (reason.isBlank()) {
                            message = "Please select a reason."
                            return@Button
                        }

                        if (loading || submitted) {
                            return@Button
                        }

                        loading = true
                        message = ""

                        val reportData =
                            hashMapOf<String, Any>(
                                "reporterId" to reporterId,
                                "reportedUserId" to user.uid,
                                "reportedUserName" to user.name,
                                "reason" to reason,
                                "details" to customDetails.trim(),
                                "createdAt" to FieldValue.serverTimestamp(),
                                "status" to "open"
                            )

                        db.collection("reports")
                            .add(reportData)
                            .addOnSuccessListener {

                                loading = false
                                submitted = true
                                message = "Report submitted successfully."
                            }
                            .addOnFailureListener { exception ->

                                loading = false
                                message =
                                    exception.message
                                        ?: "Could not submit report."
                            }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading && !submitted
                ) {

                    Text(
                        if (loading) {
                            "Submitting..."
                        } else {
                            "Submit Report"
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                if (message.isNotEmpty()) {

                    Text(
                        text = message,
                        color =
                            if (submitted) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                    )
                }

                if (submitted) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    OutlinedButton(
                        onClick = onReported,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Back to Student Profile")
                    }
                }
            }
        }
    }
}


// =========================================================
// FIND MATCHING SKILL
// =========================================================

fun findMatchingSkill(
    myLearningSkills: List<String>,
    theirTeachingSkills: List<String>
): String? {

    for (
    mySkill in myLearningSkills
    ) {

        for (
        theirSkill in theirTeachingSkills
        ) {

            if (
                mySkill.trim().equals(
                    theirSkill.trim(),
                    ignoreCase = true
                )
            ) {

                return theirSkill
            }
        }
    }

    return null
}


// =========================================================
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
}


// PARSE SKILLS
// =========================================================

fun parseSkills(
    text: String
): List<String> {

    return text
        .split(",", "\n")
        .map {

            it.trim()
        }
        .filter {

            it.isNotEmpty()
        }
        .distinct()
}


// =========================================================
// GET SKILLS FROM FIRESTORE
// =========================================================

fun getSkillsFromFirestore(
    document: DocumentSnapshot,
    fieldName: String
): List<String> {

    val value =
        document.get(fieldName)

    return when (value) {

        is List<*> -> {

            value
                .filterIsInstance<String>()
                .map {

                    it.trim()
                }
                .filter {

                    it.isNotEmpty()
                }
                .distinct()
        }

        is String -> {

            parseSkills(value)
        }

        else -> {

            emptyList()
        }
    }
}
