package com.example.mymap

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirebaseUIActivity: AppCompatActivity() {
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_method_picker)
        createSignInIntent()
    }

    private fun createSignInIntent() {

        val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_auth_method_picker)
            .setGoogleButtonId(R.id.btnGoogle)
            .setEmailButtonId(R.id.btnEmail)
            .build()

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setAuthMethodPickerLayout(customLayout)
            .setTheme(R.style.Theme_MyMap)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            userExist(this)
        }
        else{
            AlertDialog.Builder(this)
                .setTitle("Login Error")
                .setMessage("Try to turn on your internet")
                .show()
        }
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
            }
    }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
            }
    }

    open fun emailLink() {
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(
                "com.example.mymap",
                true,
                null)
            .setHandleCodeInApp(true)
            .setUrl("https://google.com")
            .build()

        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder()
                .enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings)
                .build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    open fun catchEmailLink() {
        val providers: List<AuthUI.IdpConfig> = emptyList()

        if (AuthUI.canHandleIntent(intent)) {
            val extras = intent.extras ?: return
            val link = extras.getString("email_link_sign_in")
            if (link != null) {
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link)
                    .setAvailableProviders(providers)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun userExist (thisContext: Context) {
        val apiInterface = ApiInterface.create().getUsers()

        apiInterface?.enqueue( object : Callback<ErrorMessage> {

            override fun onResponse(call: Call<ErrorMessage>, response: Response<ErrorMessage>) {
                if(!response.isSuccessful) {
                    AlertDialog.Builder(thisContext)
                        .setTitle("Error")
                        .setMessage("Error communicating with the server")
                        .show()
                }
                val errorMessage = response.body()!!
                lateinit var intent:Intent
                if (errorMessage.message == "user not found") {
                    intent = Intent (thisContext, PersonalizeActivity::class.java)
                } else {
                    intent = Intent (thisContext, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<ErrorMessage>, t: Throwable) {
                AlertDialog.Builder(thisContext)
                    .setTitle("HTTP Error")
                    .setMessage("Try to turn on your internet")
                    .show()
            }
        })
    }
}