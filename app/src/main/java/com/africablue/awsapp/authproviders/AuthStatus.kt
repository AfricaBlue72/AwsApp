package com.africablue.awsapp.authproviders

enum class AuthStatus(value: Int){
    ERROR(-2),
    UNKNOWN(-1),
    GUEST(0),
    SIGNED_IN(1),
    SIGNED_IN_WAIT_FOR_CODE(2),
    SIGNED_IN_NOK(3),
    SIGNED_UP(4),
    SIGNED_UP_WAIT_FOR_CODE(5),
    SIGNED_UP_NOK(6),
    SIGNED_OUT(7),
    SIGNED_OUT_FEDERATED_TOKENS_INVALID(8),
    SIGNED_OUT_USER_POOLS_TOKENS_INVALID(9),
    CONFIRMING_CODE(10),
    NEW_PASSWORD_REQUIRED(11),
    FORGOT_PASSWORD_CODE(14)
}