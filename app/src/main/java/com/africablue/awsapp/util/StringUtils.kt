package com.africablue.awsapp.util

object StringUtils {
    fun obscureEmail(email: String): String?{
        val at = email.indexOf('@')
        val dot = email.lastIndexOf('.')

        if(at == -1 || dot == -1)
            return null

        return email.
            replaceRange(1,email.indexOf('@'), "****").
            replaceRange((at+1), dot, "****")
    }
}