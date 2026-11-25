package org.example.project.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.example.project.resources.Res
import org.example.project.resources.home_account
import org.example.project.resources.home_greeting
import org.example.project.resources.home_greeting_anonymous
import org.example.project.resources.home_logout
import org.example.project.resources.home_placeholder
import org.example.project.resources.home_refresh
import org.example.project.ui.common.messageRes
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onLoggedOut: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToAuth -> onLoggedOut()
                is HomeEffect.ShowError -> scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(getString(effect.error.messageRes()))
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbarHostState, Modifier.navigationBarsPadding()) },
    ) { padding ->
        HomeContent(state, viewModel::onIntent, Modifier.padding(padding))
    }
}

@Composable
private fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().safeContentPadding().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        val user = state.user
        if (user == null && state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = if (user != null) stringResource(Res.string.home_greeting, user.firstName)
                else stringResource(Res.string.home_greeting_anonymous),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
            if (user != null) {
                Text(
                    text = stringResource(Res.string.home_account, user.username, user.email),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
            Text(
                text = stringResource(Res.string.home_placeholder),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onIntent(HomeIntent.RefreshClicked) },
            enabled = !state.isLoading && !state.isLoggingOut,
            modifier = Modifier.widthIn(max = 360.dp).fillMaxWidth().height(52.dp),
        ) {
            if (state.isLoading && user != null) {
                CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(Res.string.home_refresh))
            }
        }
        OutlinedButton(
            onClick = { onIntent(HomeIntent.LogoutClicked) },
            enabled = !state.isLoggingOut,
            modifier = Modifier.widthIn(max = 360.dp).fillMaxWidth().height(52.dp),
        ) {
            Text(stringResource(Res.string.home_logout))
        }
    }
}
