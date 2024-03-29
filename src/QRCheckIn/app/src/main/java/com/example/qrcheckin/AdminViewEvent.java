package com.example.qrcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminViewEvent extends AppCompatActivity {
    private RecyclerView eventsRecyclerView;
    private FirebaseFirestore db;
    private AdminEventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_events_admin);
        Toolbar toolbar = findViewById(R.id.events);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView header = findViewById(R.id.mainHeader);
        header.setText("Current Events");

        db = FirebaseFirestore.getInstance();
        eventsRecyclerView = findViewById(R.id.event_recycler_view);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerView();

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(event);
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = db.collection("events").orderBy("eventName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();

        eventAdapter = new AdminEventAdapter(options, new AdminEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                // Navigate to AdminEventPage
                Intent intent = new Intent(AdminViewEvent.this, AdminEventPage.class);
                intent.putExtra("DOCUMENT_ID", documentSnapshot.getId());
                startActivity(intent);
            }
        });

        eventsRecyclerView.setAdapter(eventAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventAdapter.stopListening();
    }
}
