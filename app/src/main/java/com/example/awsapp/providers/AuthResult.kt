package com.example.awsapp.providers

class AuthResult constructor(
    val status: AuthStatus,
    val message: String,
    val providerResult: Any?) {

}