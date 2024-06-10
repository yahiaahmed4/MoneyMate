package com.example.moneymate4;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.airbnb.lottie.LottieAnimationView;
public class MainActivity extends AppCompatActivity {

    LottieAnimationView lottie;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottie = findViewById(R.id.lottie);
        lottie.playAnimation();

        new Handler().postDelayed(new Runnable() {
@Override
public void run() {
        Intent intent = new Intent(MainActivity.this,startup.class);
        startActivity(intent);
        finish();
        }
        }, 4000);
        }
        }