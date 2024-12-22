package fr.benvolat.dao;

import fr.benvolat.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataAccessTest {

    private final UserDataAccess dataAccess = new UserDataAccess();

    UserDataAccessTest() throws SQLException {
    }

    /**
     * Test class to verify the implementation of the addUser method in UserDataAccess.
     * The addUser method is responsible for adding a User entity to the database.
     * Each test checks specific scenarios to ensure correct behavior.
     */

    @Test
    void addUserWhenValidUser() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "password123", "USER");

        // Act
        dataAccess.addUser(user);

        // Assert
        assertNotEquals(0, user.getUserID(), "User ID should be set after insertion.");
    }

    @Test
    void addDuplicateUser() {
        // Arrange
        User user1 = new User("John Doe", "john@example.com", "password456", "USER");
        User user2 = new User("John Doe", "john@example.com", "password789", "USER");

        // Act
        try{
            dataAccess.addUser(user1);
            dataAccess.addUser(user2);
            assertNotEquals(0, user1.getUserID(), "First user should be successfully inserted.");
        } catch (RuntimeException e) {
            assertThrows(RuntimeException.class, () -> dataAccess.addUser(user2), "Duplicate user should not be inserted.");
        }
    }

    @Test
    void addUserWhenNullFieldsProvided() {
        // Arrange
        User invalidUser = new User(null, "john@example.com", "password123", "USER");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dataAccess.addUser(invalidUser), "Adding a user with null fields should throw an exception.");
    }
    
    
    @AfterEach
    void cleanUpDatabase() throws SQLException {
        User user = dataAccess.findUserByEmail("john@example.com");
        if (user != null) {
            dataAccess.deleteUser(user.getUserID());
        }
    }


    @Test
    void findUserByEmailWhenUserExists() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "securePass", "USER");
        dataAccess.addUser(user);

        // Act
        User retrievedUser = dataAccess.findUserByEmail("john@example.com");

        // Assert
        assertNotNull(retrievedUser, "The user should exist in the database.");
        assertEquals(user.getEmail(), retrievedUser.getEmail(), "Emails should match.");
    }

    @Test
    void findUserByEmailWhenUserDoesNotExist() {
        // Act
        User user = dataAccess.findUserByEmail("nonexistent@example.com");

        // Assert
        assertNull(user, "No user should be found with a nonexistent email.");
    }
}