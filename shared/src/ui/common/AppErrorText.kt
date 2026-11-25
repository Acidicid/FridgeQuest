package org.example.project.ui.common

import org.example.project.domain.model.AppError
import org.example.project.resources.Res
import org.example.project.resources.error_invalid_credentials
import org.example.project.resources.error_no_internet
import org.example.project.resources.error_server
import org.example.project.resources.error_session_expired
import org.example.project.resources.error_timeout
import org.example.project.resources.error_unknown
import org.jetbrains.compose.resources.StringResource

/** Доменна помилка -> локалізований текст для снекбара. */
fun AppError.messageRes(): StringResource = when (this) {
    AppError.InvalidCredentials -> Res.string.error_invalid_credentials
    AppError.SessionExpired -> Res.string.error_session_expired
    AppError.NoInternet -> Res.string.error_no_internet
    AppError.Timeout -> Res.string.error_timeout
    AppError.Server -> Res.string.error_server
    AppError.Unknown -> Res.string.error_unknown
}
