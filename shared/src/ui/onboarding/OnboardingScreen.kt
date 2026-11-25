package org.example.project.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.resources.Res
import org.example.project.resources.onb_desc_1
import org.example.project.resources.onb_desc_2
import org.example.project.resources.onb_desc_3
import org.example.project.resources.onb_desc_4
import org.example.project.resources.onb_next
import org.example.project.resources.onb_skip
import org.example.project.resources.onb_start
import org.example.project.resources.onb_title_1
import org.example.project.resources.onb_title_2
import org.example.project.resources.onb_title_3
import org.example.project.resources.onb_title_4
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private data class OnboardingPage(
    val title: StringResource,
    val description: StringResource,
)

private val pages = listOf(
    OnboardingPage(Res.string.onb_title_1, Res.string.onb_desc_1),
    OnboardingPage(Res.string.onb_title_2, Res.string.onb_desc_2),
    OnboardingPage(Res.string.onb_title_3, Res.string.onb_desc_3),
    OnboardingPage(Res.string.onb_title_4, Res.string.onb_desc_4),
)

/** "Розумна" обгортка: бере ViewModel з Koin, слухає ефекти. */
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToAuth -> onFinished()
            }
        }
    }

    OnboardingContent(state = state, onIntent = viewModel::onIntent)
}

/** "Тупий" UI: малює state і віддає назовні intent-и. */
@Composable
private fun OnboardingContent(
    state: OnboardingState,
    onIntent: (OnboardingIntent) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = state.currentPage, pageCount = { pages.size })

    // State -> пейджер: ViewModel сказала "сторінка N" - гортаємо туди
    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }
    // Пейджер -> Intent: користувач свайпнув - повідомляємо ViewModel
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { onIntent(OnboardingIntent.PageChanged(it)) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .padding(24.dp),
    ) {
        // Кнопка "Пропустити" вгорі справа
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            if (!state.isLastPage) {
                TextButton(onClick = { onIntent(OnboardingIntent.SkipClicked) }) {
                    Text(stringResource(Res.string.onb_skip))
                }
            } else {
                Spacer(Modifier.height(48.dp))
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) { index ->
            PageContent(number = index + 1, page = pages[index])
        }

        PageDots(count = pages.size, current = state.currentPage)
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onIntent(OnboardingIntent.NextClicked) },
            enabled = !state.isFinishing,
            modifier = Modifier.fillMaxWidth().height(56.dp),
        ) {
            Text(stringResource(if (state.isLastPage) Res.string.onb_start else Res.string.onb_next))
        }
    }
}

@Composable
private fun PageContent(number: Int, page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Проста "ілюстрація": коло з номером кроку квесту
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
        Spacer(Modifier.height(40.dp))
        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PageDots(count: Int, current: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(count) { i ->
            val selected = i == current
            val width by animateDpAsState(if (selected) 24.dp else 8.dp)
            val color by animateColorAsState(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            )
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
