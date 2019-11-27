package com.qflbai.love;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @author: qflbai
 * @CreateDate: 2019/11/1 15:32
 * @Version: 1.0
 * @description:
 */
public class ShowPageView extends ViewPager {
    public ShowPageView(@NonNull Context context) {
        super(context);
    }

    public ShowPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
