package com.skillx.features.authentication.platform

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.skillx.features.authentication.domain.model.GoogleSignInCredential

actual class GoogleCredentialLauncher(
    private val context: Context,
    private val serverClientId: String
) {
    actual suspend fun requestCredential(): GoogleSignInCredential {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
        return GoogleSignInCredential.IdToken(credential.idToken)
    }
}
