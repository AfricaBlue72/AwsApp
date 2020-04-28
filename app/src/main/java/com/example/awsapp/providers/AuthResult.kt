package com.example.awsapp.providers

class AuthResult constructor(
    var status: AuthStatus = AuthStatus.UNKNOWN,
    var message: String? = null,
    var providerResult: Any? = null) {

}