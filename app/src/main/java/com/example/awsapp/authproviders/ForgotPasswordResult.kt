package com.example.awsapp.authproviders

class ForgotPasswordResult constructor(
    var status: ForgotPasswordStatus = ForgotPasswordStatus.UNKNOWN,
    var message: String? = null,
    var providerResult: Any? = null) {

}