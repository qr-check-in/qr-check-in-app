//package com.example.qrcheckin;
//
//import com.example.qrcheckin.Event.EventDatabaseManager;
//import com.example.qrcheckin.Event.EventListView;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.google.firebase.firestore.Query;
//import static org.mockito.Mockito.*;
//
//
//
//public class EventListTest {
//    @Mock
//    private Query mockQuery;
//
//    private MockEventDatabaseManager mockEventDbManager;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockEventDbManager = new MockEventDatabaseManager(mockQuery);
//    }
//
//    @Test
//    public void testQueryUpdateWhenTabChanges() {
//        // Assuming you have a way to set the mock database manager in EventListView.
//        // If not, consider adding a package-private setter method for testing purposes
//        // or using a dependency injection framework.
//
//        EventListView activity = new EventListView();
//        activity.setEventDatabaseManager(mockEventDbManager);
//
//        // Simulate tab change
//        activity.onUpcomingTabClicked(); // You would need to make this method package-private or public for testing
//
//        // Verify the query was updated
//        // This is more of a conceptual example, as you can't directly verify internal method calls like this without exposing them or their effects somehow
//        verify(mockEventDbManager, times(1)).getCollectionRef();
//    }
//}
//public class MockEventDatabaseManager extends EventDatabaseManager {
//    private Query mockQuery;
//
//    public MockEventDatabaseManager(Query query) {
//        this.mockQuery = query;
//    }
//
//    @Override
//    public Query getCollectionRef() {
//        return mockQuery;
//    }
//git }