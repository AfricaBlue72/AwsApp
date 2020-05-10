package com.example.awsapp.authproviders

class AuthResult constructor(
    var status: AuthStatus = AuthStatus.UNKNOWN,
    var message: String? = null,
    var providerResult: Any? = null,
    var forgotPasswordStatus: ForgotPasswordStatus = ForgotPasswordStatus.UNKNOWN) {

}