package com.example.variableheightbanner;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View container; // The banner container whose height interpolates
    private ViewPager2 viewPager;
    private BannerAdapter adapter;

    // Heights for each page in dp (can be per-item inside adapter too)
    private final List<Integer> pageHeightsDp = Arrays.asList(190, 250, 210, 280, 160);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.banner_container);
        viewPager = findViewById(R.id.view_pager);

        List<BannerItem> items = Arrays.asList(
                new BannerItem("秋季大促", 0xFFFF6F61),
                new BannerItem("数码会场", 0xFF42A5F5),
                new BannerItem("服饰上新", 0xFF66BB6A),
                new BannerItem("家居精选", 0xFFAB47BC),
                new BannerItem("超值秒杀", 0xFFFFA726)
        );

        adapter = new BannerAdapter(items);
        viewPager.setAdapter(adapter);

        // Set initial height
        setContainerHeightDp(pageHeightsDp.get(0));

        // Listen for page scroll to interpolate height
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                int startIndex = position;
                int targetIndex = Math.min(position + 1, pageHeightsDp.size() - 1);
                float t = positionOffset; // 0..1

                float start = pageHeightsDp.get(startIndex);
                float end = pageHeightsDp.get(targetIndex);
                float value = (1f - t) * start + t * end;
                setContainerHeightDp((int) value);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Optionally snap to exact height with a short animate if needed
                animateContainerToHeightDp(pageHeightsDp.get(position), 120);
            }
        });
    }

    private void setContainerHeightDp(int heightDp) {
        int hPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp,
                getResources().getDisplayMetrics());
        ViewGroup.LayoutParams lp = container.getLayoutParams();
        lp.height = hPx;
        container.setLayoutParams(lp);
    }

    private void animateContainerToHeightDp(int targetDp, long durationMs) {
        int currentPx = container.getHeight();
        int targetPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, targetDp,
                getResources().getDisplayMetrics());
        ValueAnimator animator = ValueAnimator.ofInt(currentPx, targetPx);
        animator.setDuration(durationMs);
        animator.addUpdateListener(a -> {
            int value = (int) a.getAnimatedValue();
            ViewGroup.LayoutParams lp = container.getLayoutParams();
            lp.height = value;
            container.setLayoutParams(lp);
        });
        animator.start();
    }

    public static class BannerItem {
        public final String title;
        @ColorInt public final int color;

        public BannerItem(String title, @ColorInt int color) {
            this.title = title;
            this.color = color;
        }
    }
}
