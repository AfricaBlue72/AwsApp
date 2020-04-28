package com.example.awsapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.awsapp.providers.AuthStatus

@Dao
interface CognitoUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCognitoUser(vararg user: CognitoUser)

    @Query("SELECT * FROM CognitoUser WHERE UserName = :userName")
    fun getCognitoUser(userName: String): LiveData<CognitoUser>

    @Query("SELECT * FROM CognitoUser Limit 1")
    fun getCognitoUser(): LiveData<CognitoUser>

    @Query("SELECT status FROM CognitoUser WHERE UserName = :userName")
    fun getCognitoUserStatus(userName: String): LiveData<AuthStatus>

    @Query("UPDATE CognitoUser SET status = :status WHERE UserName = :userName")
    fun updateCognitoUserStatus(userName: String, status: AuthStatus)

    @Query("DELETE FROM CognitoUser WHERE 1=1")
    fun deleteUsers()
}