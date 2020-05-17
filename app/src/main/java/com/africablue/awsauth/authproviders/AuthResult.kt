package com.africablue.awsauth.authproviders

class AuthResult constructor(
    var status: AuthStatus = AuthStatus.UNKNOWN,
    var message: String? = null,
    var providerResult: Any? = null,
    var forgotPasswordStatus: ForgotPasswordStatus = ForgotPasswordStatus.UNKNOWN,
    var changePasswordStatus: ChangePasswordStatus = ChangePasswordStatus.UNKNOWN){
}