package com.qflbai.love;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private Context mContext;
    private List<PhotoInfo> photoInfos;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private ImageView ivBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucent(MainActivity.this, 0);
        mContext = this;
        setupWindowAnimations();
        initUi();
    }

    private void initUi() {
        constraintLayout = findViewById(R.id.cl);
        toolbar = findViewById(R.id.toolbar);
        ivBg = findViewById(R.id.iv_bg);
        setSupportActionBar(toolbar);

        mRv = findViewById(R.id.rv);
        photoInfos = new ArrayList<>();

        for (int i = 1; i < 289; i++) {
            //int id = getResources().getIdentifier("love" + (i + 1), "drawable", mContext.getPackageName());
            String path = "file:///android_asset/love" + i + ".jpg";

            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.setPath(path);
            photoInfos.add(photoInfo);

        }

        photoInfos = randomList(photoInfos);


        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        /*StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(staggeredGridLayoutManager);*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRv.setLayoutManager(gridLayoutManager);
        PhotoAdapter photoAdapter = new PhotoAdapter(mContext, photoInfos);
        photoAdapter.setOnItemClick(new PhotoAdapter.OnItmeClick() {
            @Override
            public void onItmeClick(View view, int position) {
                ViewCompat.setTransitionName(view, "image");
                Pair<View, String> pair1 = new Pair<>((View) view, ViewCompat.getTransitionName(view));
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1);

                JumpInfo jumpInfo = new JumpInfo();
                jumpInfo.setPhotoInfos(photoInfos);
                jumpInfo.setPosition(position);
                EventBus.getDefault().postSticky(jumpInfo);
                Intent intent = new Intent(mContext, ShowActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });
        mRv.setAdapter(photoAdapter);

        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager linearManager = (GridLayoutManager) layoutManager;

                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();

                    final PhotoInfo photoInfo = photoInfos.get(firstItemPosition);

                    RequestBuilder<Drawable> apply = Glide.with(mContext)
                            .load(photoInfo.getPath())
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 10)));

                    apply.into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            constraintLayout.setBackground(resource);
                        }
                    });


                }
            }
        });

    }

    public static boolean isEmpty(List sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    /**
     * 打乱ArrayList
     */
    public static List<PhotoInfo> randomList(List<PhotoInfo> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        ArrayList randomList = new ArrayList<PhotoInfo>(sourceList.size());
        do {
            int randomIndex = Math.abs(new Random().nextInt(sourceList.size()));
            randomList.add(sourceList.remove(randomIndex));
        } while (sourceList.size() > 0);

        return randomList;
    }


    public List<Integer> getImageResourId(String imgName) {
        List<Integer> imgList = new ArrayList<>();
        Resources resources = mContext.getResources();
        Field[] fields = R.drawable.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String name = fields[i].getName();
            String substring = name.substring(name.length() - 1);
            if (name.startsWith("a") && isInteger(substring)) {
                int resId = resources.getIdentifier(name, "drawable", mContext.getPackageName());
                imgList.add(resId);
            }
        }
        return imgList;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private void setupWindowAnimations() {
        /*Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);*/

        Slide slide = new Slide(Gravity.LEFT);
        // slide.setDuration(1000);
        getWindow().setReturnTransition(slide);
    }


    private void palette(Bitmap bitmap) {
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                /**
                 * Color.BLUE代表默认色值（分析不出来的时候的色值）
                 */
                //暗、柔和的颜色
                int darkMutedColor = palette.getDarkMutedColor(Color.BLUE);
                //亮、柔和
                int lightMutedColor = palette.getLightMutedColor(Color.BLUE);
                //暗、鲜艳
                int darkVibrantColor = palette.getDarkVibrantColor(Color.BLUE);
                //亮、鲜艳
                int lightVibrantColor = palette.getLightVibrantColor(Color.BLUE);
                //柔和
                int mutedColor = palette.getMutedColor(Color.BLUE);
                //鲜艳
                int vibrantColor = palette.getVibrantColor(Color.BLUE);
                /**
                 * 获取某种特性的颜色样品
                 */
                Palette.Swatch lightVibrantSwatch = palette.getVibrantSwatch();
                Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();

                if (darkVibrantSwatch == null) {
                    return;
                }

                toolbar.setTitleTextColor(darkVibrantSwatch.getTitleTextColor());
                toolbar.setBackgroundColor(darkVibrantSwatch.getRgb());
                StatusBarUtil.setColor(MainActivity.this, darkVibrantSwatch.getRgb());
            }
        });
    }
}
