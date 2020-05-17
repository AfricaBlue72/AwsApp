package com.africablue.awsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.africablue.awsapp.authproviders.AuthStatus

@Entity
data class CognitoUser(
    val userId: String?,
    @PrimaryKey val userName:String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val age: String?,
    val status: AuthStatus
) {
}