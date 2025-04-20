package com.example.restaurantbookingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class RestaurantCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_restaurant_card); // تأكد أن layout يحتوي على VideoView

        // ربط VideoView
        VideoView videoView = findViewById(R.id.restaurantVideo);

        // إعداد المسار للفيديو
        String path = "android.resource://" + getPackageName() + "/" + R.raw.restaurant_video;
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);

        // تشغيل الفيديو تلقائياً بالصوت والتكرار
        videoView.setOnPreparedListener(mp -> {
            mp.setVolume(1.0f, 1.0f); // تفعيل الصوت
            mp.setLooping(true);     // تكرار الفيديو
            videoView.start();       // تشغيل الفيديو
        });

        // عند الضغط على زر الحجز
        Button bookButton = findViewById(R.id.bookButton);
        bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantCardActivity.this, ReservationActivity.class);
            startActivity(intent);
        });
    }
}
