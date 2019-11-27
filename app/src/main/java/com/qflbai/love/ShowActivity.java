package com.qflbai.love;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class ShowActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private ShowPageView showPageView;
    private List<PhotoInfo> mPhotoInfos;
    private JumpInfo mJumpInfo;
    private ImageView imageView;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        StatusBarUtil.setTranslucent(this, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPhotoInfos = new ArrayList<>();
        EventBus.getDefault().register(this);

        List<PhotoInfo> photoInfos = mJumpInfo.getPhotoInfos();
        mPhotoInfos.addAll(photoInfos);
        final int position = mJumpInfo.getPosition();

        PhotoInfo photoInfo = mPhotoInfos.get(position);
        String mipmapSrc = photoInfo.getPath();

        constraintLayout = findViewById(R.id.cl);
        setBgImage(mipmapSrc);


        showPageView = findViewById(R.id.vp);
        showPageView.setAdapter(new SamplePagerAdapter(mPhotoInfos));
        showPageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PhotoInfo photoInfo1 = mPhotoInfos.get(position);
                setBgImage(photoInfo1.getPath());


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        imageView = findViewById(R.id.photo_view);
        Glide.with(ShowActivity.this)
                .load(mipmapSrc)
                .into(imageView);
        ViewCompat.setTransitionName(imageView, "image");
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        /**
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         */
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTarget(imageView);


        getWindow().setSharedElementEnterTransition(transitionSet);
        getWindow().setSharedElementExitTransition(transitionSet);

        android.transition.Transition sharedElementExitTransition = getWindow().getSharedElementExitTransition();
        sharedElementExitTransition.addListener(new android.transition.Transition.TransitionListener() {
            @Override
            public void onTransitionStart(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionEnd(android.transition.Transition transition) {

                showPageView.setVisibility(View.VISIBLE);
                showPageView.setCurrentItem(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(800);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.clearAnimation();
                                imageView.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onTransitionCancel(android.transition.Transition transition) {


            }

            @Override
            public void onTransitionPause(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionResume(android.transition.Transition transition) {

            }
        });

        transitionSet.addListener(new android.transition.Transition.TransitionListener() {
            @Override
            public void onTransitionStart(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionEnd(android.transition.Transition transition) {


            }

            @Override
            public void onTransitionCancel(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionPause(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionResume(android.transition.Transition transition) {

            }
        });

    }


    private void setBgImage( String bgDrawable) {
        Glide.with(this)
                .load(bgDrawable)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 10)))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        constraintLayout.setBackground(resource);
                    }
                });
    }

    @Subscribe(sticky = true)
    public void subscribe(JumpInfo jumpInfo) {
        mJumpInfo = jumpInfo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class SamplePagerAdapter extends PagerAdapter {

        private final List<PhotoInfo> infos;


        public SamplePagerAdapter(List<PhotoInfo> photoInfos) {
            infos = photoInfos;
        }

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            PhotoInfo photoInfo = infos.get(position);

            Glide.with(ShowActivity.this)
                    .load(photoInfo.getPath())
                    .into(photoView);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
