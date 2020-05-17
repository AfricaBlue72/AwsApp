package com.africablue.awsauth.authproviders

enum class ForgotPasswordStatus(value: Int){
    ERROR(-1),
    UNKNOWN(0),
    CONFIRM(1),
    DONE(2)
}