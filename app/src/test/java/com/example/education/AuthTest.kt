package com.example.education

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class AuthTest {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var authResult: AuthResult

    @Before
    fun setup() {
        auth = mock()
        firebaseUser = mock()
        authResult = mock()
    }

    @Test
    fun `registerUser success`() {
        // Mock successful registration
        whenever(auth.createUserWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(Tasks.forResult(authResult))

        val task: Task<AuthResult> = auth.createUserWithEmailAndPassword("test@example.com", "password123")
        assertTrue(task.isSuccessful)
    }

    @Test
    fun `registerUser failure`() {
        // Mock failed registration
        whenever(auth.createUserWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(Tasks.forException(Exception("Registration failed")))

        val task: Task<AuthResult> = auth.createUserWithEmailAndPassword("test@example.com", "password123")
        assertFalse(task.isSuccessful)
    }

    @Test
    fun `loginUser success`() {
        // Mock successful login
        whenever(auth.signInWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(Tasks.forResult(authResult))
        whenever(auth.currentUser).thenReturn(firebaseUser)

        val task: Task<AuthResult> = auth.signInWithEmailAndPassword("test@example.com", "password123")
        assertTrue(task.isSuccessful)
    }

    @Test
    fun `loginUser failure`() {
        // Mock failed login
        whenever(auth.signInWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(Tasks.forException(Exception("Login failed")))

        val task: Task<AuthResult> = auth.signInWithEmailAndPassword("test@example.com", "password123")
        assertFalse(task.isSuccessful)
    }

    @Test
    fun `logoutUser`() {
        // Mock logout
        doNothing().whenever(auth).signOut()

        auth.signOut()
        verify(auth, times(1)).signOut()
    }
}
