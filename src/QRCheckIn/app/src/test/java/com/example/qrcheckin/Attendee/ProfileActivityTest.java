package com.example.qrcheckin.Attendee;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import org.junit.jupiter.api.Test;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;


class ProfileActivityTest {

    @Mock
    private AttendeeDatabaseManager mockDatabaseManager;
    @Mock
    private DocumentSnapshot mockDocumentSnapshot;
    @Mock
    private DocumentReference mockDocumentReference;


    private ProfileActivity profileActivity;
    private Attendee attendee;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        mockDatabaseManager = mock(AttendeeDatabaseManager.class);
        mockDocumentSnapshot = mock(DocumentSnapshot.class);
        mockDocumentReference = mock(DocumentReference.class);

        profileActivity = new ProfileActivity();
        mockDatabaseManager = new AttendeeDatabaseManager();
        attendee = new Attendee();
        profileActivity.dbManager = mockDatabaseManager; // Inject the mock database manager

    }

    @Test
    public void testEditDetails() throws IOException {
        // Mock updated profile details
        String updatedName = "John Doe";
        String updatedContact = "5877783704";
        String updatedHomepage = "https://DoeJohn.com";


        // Define a lambda to capture the arguments and perform some actions
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String value = invocation.getArgument(1);

                // Update the field of profile fields
                attendee.getProfile().setName(value);
                return null;
            }
        }).when(mockDatabaseManager).updateProfileString(eq("name"), eq(updatedName), null);

        // Define a lambda to capture the arguments and perform some actions
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String value = invocation.getArgument(1);

                // Update the field of profile fields
                attendee.getProfile().setName(value);
                return null;
            }
        }).when(mockDatabaseManager).updateProfileString(eq("contact"), eq(updatedContact),null);

        // Define a lambda to capture the arguments and perform some actions
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String value = invocation.getArgument(1);

                // Update the field of profile fields
                attendee.getProfile().setName(value);
                return null;
            }
        }).when(mockDatabaseManager).updateProfileString(eq("homepage"), eq(updatedHomepage), null);






        // Call the method under test
        profileActivity.editDetails(updatedName, updatedContact, updatedHomepage);

        // check the value of name, contact and homepage
        assert attendee.getProfile().getName().equals(updatedName);

        // check the value of name, contact and homepage
        assert attendee.getProfile().getContact().equals(updatedContact);

        // check the value of name, contact and homepage
        assert attendee.getProfile().getHomepage().equals(updatedHomepage);





//        // Mock behavior of getDocRef() and get() method in DatabaseManager
//        when(mockDatabaseManager.getDocRef()).thenReturn(mockDocumentReference);
//        when(mockDatabaseManager.getDocRef().get()).thenReturn(mock(Task.class));
//        when(mockDatabaseManager.getDocRef().get().getResult()).thenReturn(mockDocumentSnapshot);
//
//

//
//
//
//        // Verify that updateProfileString() method is called with the correct arguments
//        verify(mockDatabaseManager).updateProfileString(eq("name"), eq(updatedName), isNull());
//        verify(mockDatabaseManager).updateProfileString(eq("contact"), eq(updatedContact), isNull());
//        verify(mockDatabaseManager).updateProfileString(eq("homepage"), eq(updatedHomepage), isNull());
    }

}