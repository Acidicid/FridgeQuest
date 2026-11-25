package org.example.project.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.example.project.domain.model.PasswordError
import org.example.project.domain.model.UsernameError
import org.example.project.resources.Res
import org.example.project.resources.app_name
import org.example.project.resources.auth_hide
import org.example.project.resources.auth_hint
import org.example.project.resources.auth_password
import org.example.project.resources.auth_show
import org.example.project.resources.auth_sign_in
import org.example.project.resources.auth_subtitle
import org.example.project.resources.auth_title
import org.example.project.resources.auth_username
import org.example.project.resources.err_password_empty
import org.example.project.resources.err_password_short
import org.example.project.resources.err_username_chars
import org.example.project.resources.err_username_empty
import org.example.project.resources.err_username_long
import org.example.project.resources.err_username_short
import org.example.project.ui.common.messageRes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onLoggedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val usernameFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Події (Effect) від ViewModel: навігація, фокус, снекбар з помилкою
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthEffect.NavigateToHome -> onLoggedIn()
                AuthEffect.FocusUsername -> usernameFocus.requestFocus()
                AuthEffect.FocusPassword -> passwordFocus.requestFocus()
                is AuthEffect.ShowError -> scope.launch {
                    // окремий launch, щоб снекбар не блокував обробку наступних подій
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(getString(effect.error.messageRes()))
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(snackbarHostState, Modifier.navigationBarsPadding().imePadding())
        },
    ) { padding ->
        AuthContent(
            state = state,
            onIntent = viewModel::onIntent,
            usernameFocus = usernameFocus,
            passwordFocus = passwordFocus,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun AuthContent(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
    usernameFocus: FocusRequester,
    passwordFocus: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AuthHeader()
            Spacer(Modifier.height(32.dp))

            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                shadowElevation = 2.dp,
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // --- Імʼя користувача: дія клавіатури "Next" -> фокус на пароль
                    OutlinedTextField(
                        value = state.username,
                        onValueChange = { onIntent(AuthIntent.UsernameChanged(it)) },
                        label = { Text(stringResource(Res.string.auth_username)) },
                        singleLine = true,
                        enabled = !state.isLoading,
                        isError = state.usernameError != null,
                        supportingText = state.usernameError?.let { errorText(it.message()) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocus.requestFocus() },
                        ),
                        modifier = Modifier.fillMaxWidth().focusRequester(usernameFocus),
                    )

                    // --- Пароль: дія клавіатури "Done" -> те саме, що кнопка "Увійти"
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) },
                        label = { Text(stringResource(Res.string.auth_password)) },
                        singleLine = true,
                        enabled = !state.isLoading,
                        isError = state.passwordError != null,
                        supportingText = state.passwordError?.let { errorText(it.message()) },
                        visualTransformation = if (state.isPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            TextButton(onClick = { onIntent(AuthIntent.TogglePasswordVisibility) }) {
                                Text(
                                    stringResource(
                                        if (state.isPasswordVisible) Res.string.auth_hide else Res.string.auth_show
                                    )
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboard?.hide()
                                onIntent(AuthIntent.SignInClicked)
                            },
                        ),
                        modifier = Modifier.fillMaxWidth().focusRequester(passwordFocus),
                    )

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = {
                            keyboard?.hide()
                            onIntent(AuthIntent.SignInClicked)
                        },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        } else {
                            Text(stringResource(Res.string.auth_sign_in))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.auth_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun AuthHeader() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "FQ",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
    Spacer(Modifier.height(16.dp))
    Text(
        text = stringResource(Res.string.app_name),
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = stringResource(Res.string.auth_title),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = stringResource(Res.string.auth_subtitle),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}

/** Текст помилки під полем (supportingText чекає саме @Composable-лямбду). */
private fun errorText(res: StringResource): @Composable () -> Unit = { Text(stringResource(res)) }

private fun UsernameError.message(): StringResource = when (this) {
    UsernameError.Empty -> Res.string.err_username_empty
    UsernameError.TooShort -> Res.string.err_username_short
    UsernameError.TooLong -> Res.string.err_username_long
    UsernameError.InvalidChars -> Res.string.err_username_chars
}

private fun PasswordError.message(): StringResource = when (this) {
    PasswordError.Empty -> Res.string.err_password_empty
    PasswordError.TooShort -> Res.string.err_password_short
}
