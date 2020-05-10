package com.example.awsapp.data

import android.content.Context
import androidx.room.TypeConverter
import com.example.awsapp.authproviders.AuthStatus


class AuthStatusConverter{

    @TypeConverter
    fun fromAuthStatus(value: AuthStatus): Int{
        return value.ordinal
    }

    @TypeConverter
    fun toAuthStatus(value: Int): AuthStatus{
        return when(value){
            -1 -> AuthStatus.UNKNOWN
//            0 -> AuthStatus.GUEST
//            1 -> AuthStatus.SIGNED_IN
//            2 -> AuthStatus.SIGNED_IN_WAIT_FOR_CODE
//            3 -> AuthStatus.SIGNED_IN_NOK
//            4 -> AuthStatus.SIGNED_UP
//            5 -> AuthStatus.SIGNED_UP_WAIT_FOR_CODE
//            6 -> AuthStatus.SIGNED_UP_NOK
//            7 -> AuthStatus.SIGNED_OUT
//            8 -> AuthStatus.SIGNED_OUT_FEDERATED_TOKENS_INVALID
//            9 -> AuthStatus.SIGNED_OUT_USER_POOLS_TOKENS_INVALID
//            10 -> AuthStatus.CONFIRMING_CODE
            else -> AuthStatus.UNKNOWN
        }
    }
}

object InjectorUtils {

    fun getAppRepository(context: Context): AppRepository {
        return AppRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).cognitoUserDao())
    }
}