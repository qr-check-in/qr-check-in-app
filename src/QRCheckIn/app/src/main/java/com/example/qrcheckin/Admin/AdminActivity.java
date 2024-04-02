package com.example.qrcheckin.Admin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcheckin.Common.ImageStorageManager;
import com.example.qrcheckin.R;

public class AdminActivity extends AppCompatActivity {
    ImageView addAdminImageView;
    TextView addAdminTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        // Display the QR code to add other admins
        addAdminImageView = findViewById(R.id.addAdminImageView);
        addAdminTextView = findViewById(R.id.addAdminTextView);
        ImageStorageManager storage = new ImageStorageManager("AddAdminQRCode.jpg");
        storage.displayImage(addAdminImageView);
        addAdminImageView.getLayoutParams().width = 400;
        addAdminImageView.getLayoutParams().height = 400;


    }
}

