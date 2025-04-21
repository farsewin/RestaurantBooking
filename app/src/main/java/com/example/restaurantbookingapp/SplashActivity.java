package com.example.restaurantbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.restaurantbookingapp.auth.LoginActivity; // تأكد من استيراد LoginActivity من المسار الصحيح

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 8000; // 3 ثواني (مدة أكثر ملائمة)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // تحميل صورة GIF باستخدام Glide
        ImageView gifImageView = findViewById(R.id.gifImage); // تأكد من أن ID متطابق مع XML
        Glide.with(this)
                .asGif()
                .load(R.drawable.splash_anim) // تأكد من وجود الملف في res/drawable/
                .into(gifImageView);

        // الانتقال إلى LoginActivity بعد انتهاء المدة
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // إغلاق SplashActivity لمنع العودة إليها
        }, SPLASH_DURATION);
    }
}