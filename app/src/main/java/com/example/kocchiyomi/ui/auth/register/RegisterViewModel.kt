package com.example.kocchiyomi.ui.auth.register


import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kocchiyomi.data.model.User
import com.example.kocchiyomi.utils.ErrorMessage
import com.example.kocchiyomi.utils.FirestoreHelper
import com.example.kocchiyomi.utils.LoadState
import com.google.firebase.auth.FirebaseAuth


class RegisterViewModel: ViewModel(){
    val navigateToHomeMutableLiveData = MutableLiveData<Boolean?>()
    val switchToLoginMutableLiveData = MutableLiveData<Boolean?>()
    val loadingState = MutableLiveData<LoadState>()


    private val emailMatch = MutableLiveData<Boolean>()
    private val userNameMatch = MutableLiveData<Boolean>()
    private val passwordMatch = MutableLiveData<Boolean>()
    private val confirmPasswordMatch = MutableLiveData<Boolean>()

    private fun storeUser(user: User) {
        val db = FirestoreHelper.firestoreInstance
        user.uid?.let {
            uid -> db.collection("users").document(uid).set(user).addOnSuccessListener {
                navigateToHomeMutableLiveData.value = true
                switchToLoginMutableLiveData.value = false
            }.addOnFailureListener{
                loadingState.value = LoadState.FAILURE
                ErrorMessage.errorMessage = it.message
            }
        }
    }

    fun register(
        auth: FirebaseAuth,
        email: String,
        password: String,
        userName: String
    ) {
        loadingState.value = LoadState.LOADING

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                storeUser(User(it.user?.uid, userName, email))
                loadingState.value = LoadState.SUCCESS
            }.addOnFailureListener {
                loadingState.value = LoadState.FAILURE
                ErrorMessage.errorMessage = it.message
            }
    }

    fun doneNavigating() {
        navigateToHomeMutableLiveData.value = null
    }

    fun enableSwitch() {
        switchToLoginMutableLiveData.value = true
    }

    fun doneSwitch() {
        switchToLoginMutableLiveData.value = null
    }

    fun isUserNameMatch(it: String): LiveData<Boolean> {
        userNameMatch.value = it.length >= 3
        return userNameMatch
    }

    fun isEmailMatch(it: String): LiveData<Boolean> {
        emailMatch.value =  Patterns.EMAIL_ADDRESS.matcher(it).matches()
        return emailMatch
    }

    fun isPasswordMatch(it: String): LiveData<Boolean>  {
        passwordMatch.value = it.length >= 6
        return passwordMatch
    }

    fun isConfirmPasswordMatch(password: String, confirmPassword: String): LiveData<Boolean> {
        confirmPasswordMatch.value = TextUtils.equals(password, confirmPassword)
        return confirmPasswordMatch
    }
}

class RegisterViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}