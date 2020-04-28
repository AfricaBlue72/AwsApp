package com.example.awsapp.data

import com.example.awsapp.util.AuthStatus

class AppRepository private constructor(private val cognitoUserDao: CognitoUserDao) {

    fun addCognitoUser(user: CognitoUser){
        cognitoUserDao.deleteUsers()
        cognitoUserDao.addCognitoUser(user)
    }

    fun getCognitoUser(userName: String) = cognitoUserDao.getCognitoUser(userName)

    fun getCognitoUser() = cognitoUserDao.getCognitoUser()

    fun getCognitoUserStatus(userName: String) = cognitoUserDao.getCognitoUserStatus(userName)

    fun updateCognitoUserStatus(userName: String, status: AuthStatus) = cognitoUserDao.updateCognitoUserStatus(userName, status)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppRepository? = null

        fun getInstance(cognitoUserDao: CognitoUserDao) =
            instance ?: synchronized(this) {
                instance ?: AppRepository(cognitoUserDao).also { instance = it }
            }
    }
}

