package com.smartstore.service;

import com.smartstore.dao.UserDAO;
import com.smartstore.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthService authService;

    private static User user(int id, String name, String password) {
        User u = new User();
        u.setId(id);
        u.setUsername(name);
        u.setPassword(password);
        u.setRole("CUSTOMER");
        return u;
    }

    @Test
    void signupStoresAHashNotThePassword() {
        when(userDAO.findByUsername("asha")).thenReturn(null);

        assertEquals("OK", authService.signupCustomer("asha", "s3cret!"));

        ArgumentCaptor<String> stored = ArgumentCaptor.forClass(String.class);
        verify(userDAO).saveCustomer(eq("asha"), stored.capture());
        assertNotEquals("s3cret!", stored.getValue());
        assertTrue(stored.getValue().startsWith("$2"), "expected a BCrypt hash");
        assertTrue(new BCryptPasswordEncoder().matches("s3cret!", stored.getValue()));
    }

    @Test
    void loginAcceptsTheRightPasswordAgainstItsHash() {
        String hash = new BCryptPasswordEncoder().encode("s3cret!");
        when(userDAO.findActiveByUsername("asha")).thenReturn(user(1, "asha", hash));

        assertNotNull(authService.login("asha", "s3cret!"));
        assertNull(authService.login("asha", "wrong"));
        verify(userDAO, never()).updatePassword(anyInt(), anyString());
    }

    @Test
    void legacyPlainTextPasswordIsUpgradedToAHashOnFirstLogin() {
        when(userDAO.findActiveByUsername("old")).thenReturn(user(7, "old", "plain123"));

        assertNotNull(authService.login("old", "plain123"));

        ArgumentCaptor<String> upgraded = ArgumentCaptor.forClass(String.class);
        verify(userDAO).updatePassword(eq(7), upgraded.capture());
        assertTrue(new BCryptPasswordEncoder().matches("plain123", upgraded.getValue()));
    }

    @Test
    void wrongPasswordOnALegacyAccountFailsAndChangesNothing() {
        when(userDAO.findActiveByUsername("old")).thenReturn(user(7, "old", "plain123"));

        assertNull(authService.login("old", "nope"));
        verify(userDAO, never()).updatePassword(anyInt(), any());
    }

    @Test
    void unknownOrInactiveUserCannotLogIn() {
        when(userDAO.findActiveByUsername("ghost")).thenReturn(null);
        assertNull(authService.login("ghost", "anything"));
        assertNull(authService.login(null, "anything"));
    }
}
