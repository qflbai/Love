package com.qflbai.love;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jaeger.library.StatusBarUtil;
import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.ScaleModifier;

import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class LaunchActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private ImageView imageView;
    int[] images = {R.mipmap.love1, R.mipmap.love2,
            R.mipmap.love3, R.mipmap.love4,
            R.mipmap.love5, R.mipmap.love6,
            R.mipmap.love7, R.mipmap.love8,
            R.mipmap.love9, R.mipmap.love10,
            R.mipmap.love11, R.mipmap.love12,
            R.mipmap.love13, R.mipmap.love14,
            R.mipmap.love15, R.mipmap.love16,
            R.mipmap.love17, R.mipmap.love18,
            R.mipmap.love19, R.mipmap.love20,
            R.mipmap.love21, R.mipmap.love22,
            R.mipmap.love23, R.mipmap.love24,
            R.mipmap.love25, R.mipmap.love26,
            R.mipmap.love27
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        StatusBarUtil.setTranslucent(this, 0);

        imageView = findViewById(R.id.iv);
        constraintLayout = findViewById(R.id.cl);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;

        Random random = new Random();
        int randomIndex = Math.abs(random.nextInt(27));
        int randomIndex1 = Math.abs(random.nextInt(27));
        int randomIndex2 = Math.abs(random.nextInt(27));
        int randomIndex3 = Math.abs(random.nextInt(27));
        int bg = images[randomIndex];
        int bg1 = images[randomIndex1];
        int bg2 = images[randomIndex2];
        int bg3 = images[randomIndex3];

        RequestBuilder<Drawable> apply = Glide.with(this)
                .load(bg)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 10)));

        apply.into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                constraintLayout.setBackground(resource);
            }
        });


        final ParticleSystem particleSystem1 = new ParticleSystem(this, 1, bg, 3000);
        particleSystem1.setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
                .setAcceleration(0.000003f, 90)
                .setInitialRotationRange(0, 360)
                .setRotationSpeed(120)
                .setFadeOut(3000)
                .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                .emit(widthPixel / 2, 0, 10);

        final ParticleSystem particleSystem2 = new ParticleSystem(this, 1, bg2, 3000);
        particleSystem2.setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
                .setAcceleration(0.000003f, 90)
                .setInitialRotationRange(0, 360)
                .setRotationSpeed(120)
                .setFadeOut(3000)
                .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                .emit(0, heightPixel / 2, 10);

        final ParticleSystem particleSystem3 = new ParticleSystem(this, 1, bg3, 3000);
        particleSystem3.setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
                .setAcceleration(0.000003f, 90)
                .setInitialRotationRange(0, 360)
                .setRotationSpeed(120)
                .setFadeOut(3000)
                .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                .emit(widthPixel, heightPixel / 2, 10);


        final ParticleSystem particleSystem4 = new ParticleSystem(this, 1, bg1, 3000);
        particleSystem4.setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
                .setAcceleration(0.000003f, 90)
                .setInitialRotationRange(0, 360)
                .setRotationSpeed(120)
                .setFadeOut(3000)
                .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                .emit(widthPixel / 2, heightPixel, 10);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                findViewById(R.id.iv).post(new Runnable() {
                    @Override
                    public void run() {
                        particleSystem1.stopEmitting();
                        particleSystem2.stopEmitting();
                        particleSystem3.stopEmitting();
                        particleSystem4.stopEmitting();
                        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
