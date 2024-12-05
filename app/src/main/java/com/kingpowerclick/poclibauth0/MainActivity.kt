package com.kingpowerclick.poclibauth0

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kingpowerclick.android.auth0.AuthenticationManager
import com.kingpowerclick.android.auth0.CredentialsModel
import com.kingpowerclick.poclibauth0.ui.theme.PocLibAuth0Theme
import com.kingpowerclick.poclibauth0.ui.theme.Purple80
import com.kingpowerclick.poclibauth0.ui.theme.Yellow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var authenticationManager: AuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationManager = AuthenticationManager(this)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            var refreshToken by remember {
                mutableStateOf("")
            }
            PocLibAuth0Theme {
                Scaffold(modifier = Modifier.fillMaxSize().background(Yellow)) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().background(Yellow)
                    ) {
                        Greeting(
                            modifier = Modifier.padding(innerPadding),
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    authenticationManager.logIn(
                                        context = this@MainActivity,
                                        onSuccess = { result: CredentialsModel ->
                                            Log.d("asdf", "${result.idToken}")
                                            Log.d("asdf", "${result.accessToken}")
                                            Log.d("asdf", "${result.type}")
                                            Log.d("asdf", "${result.refreshToken}")
                                            Log.d("asdf", "${result.expiresAt}")
                                            Log.d("asdf", "${result.scope}")
                                            refreshToken = result.refreshToken!!
                                        },
                                        onFail = {},
                                    )
                                }
                            },
                            content = {
                                Text(
                                    text =
                                        if (refreshToken.isBlank()) {
                                            "Login"
                                        } else {
                                            "Already Login refreshToken = $refreshToken"
                                        },
                                )
                            },
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    authenticationManager.logOut(
                                        context = this@MainActivity,
                                        onSuccess = {
                                            val a = 1
                                        },
                                        onFail = {},
                                    )
                                }
                            },
                            content = {
                                Text(
                                    text =
                                        if (refreshToken.isBlank()) {
                                            "Already Logout"
                                        } else {
                                            "Logout"
                                        },
                                )
                            },
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    authenticationManager.refreshToken(
                                        refreshToken = refreshToken,
                                        onSuccess = { credentials: CredentialsModel ->

                                            refreshToken = credentials.refreshToken!!
                                        },
                                        onFail = {},
                                    )
                                }
                            },
                            content = {
                                Text(
                                    text =
                                        if (refreshToken.isBlank()) {
                                            "Login to enable refreshToken"
                                        } else {
                                            "refreshToken = $refreshToken"
                                        },
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String = stringResource(R.string.app_name),
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PocLibAuth0Theme {
        Greeting(stringResource(R.string.app_name))
    }
}
