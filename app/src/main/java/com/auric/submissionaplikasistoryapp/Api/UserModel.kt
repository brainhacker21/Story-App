package com.auric.submissionaplikasistoryapp.Api

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val login: Boolean
)