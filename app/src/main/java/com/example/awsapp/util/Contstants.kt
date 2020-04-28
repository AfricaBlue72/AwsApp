package com.example.awsapp.util

const val APP_TAG = "--AWSAPP--"
const val DATABASE_NAME = "AwsApp-db"

enum class AuthStatus(value: Int){
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
    CONFIRMING_CODE(10)
}

//https://medium.com/step-by-step-building-mobile-with-mobile-backend/building-landmark-app-with-aws-amplify-google-sign-in-with-graphql-61f52fb115d7