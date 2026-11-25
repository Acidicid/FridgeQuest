package org.example.project.domain.model

enum class UsernameError { Empty, TooShort, TooLong, InvalidChars }

enum class PasswordError { Empty, TooShort, NoDigit, NoLetter }
