package org.example.project.data.auth.model

import kotlinx.serialization.Serializable
import org.example.project.data.network.ACCESS_TOKEN_LIFETIME_MINS
import org.example.project.domain.model.User

// ---------- Запити ----------

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = ACCESS_TOKEN_LIFETIME_MINS,
)

@Serializable
data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int = ACCESS_TOKEN_LIFETIME_MINS,
)

// ---------- Відповіді ----------

@Serializable
data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val accessToken: String,
    val refreshToken: String,
)

@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
)

/** DTO (як прийшло з сервера) -> доменна модель (як зручно застосунку). */
fun UserDto.toDomain() = User(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    email = email,
)
