package com.sito.customer.view.photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.GlideUtils;
import com.sito.library.widget.ExtendedViewPager;
import com.sito.library.widget.TouchImageView;


public class ViewPagePhotoActivity extends BaseActivity {
    private String[] mUrls;
    private int mIndex = 0;
    private ImageView[] mImageViews;
    private Bitmap bmp;
    int count;

    public static void startActivity(Context context, String[] urls, int index) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(ConstantStr.KEY_URL, urls);
        bundle.putInt(ConstantStr.KEY_ID, index);
        Intent intent = new Intent(context, ViewPagePhotoActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bData = getIntent().getExtras();
        assert bData != null;
        mUrls = bData.getStringArray(ConstantStr.KEY_URL);
        mIndex = bData.getInt(ConstantStr.KEY_ID, 0);
        if (mUrls == null) {
            finish();
            return;
        }
        count = mUrls.length;
        for (String mUrl : mUrls) {
            if (TextUtils.isEmpty(mUrl)) {
                count = count - 1;
            }
        }
        setContentView(R.layout.photos);
        transparentStatusBar();
        setDarkStatusIcon(false);
        initView();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (bmp != null) {
            bmp.recycle();
            bmp = null;
        }
        System.gc();
    }


    private void initView() {
        mImageViews = new ImageView[mUrls.length];
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        if (count > 1) {
            for (int i = 0; i < mUrls.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                if (i == mUrls.length - 1) {
                    imageView.setPadding(0, 0, 0, 0);
                } else {
                    imageView.setPadding(0, 0, 10, 0);
                }
                mImageViews[i] = imageView;
                if (i == 0) {
                    mImageViews[i].setImageResource(R.drawable.page003);
                } else {
                    mImageViews[i].setImageResource(R.drawable.page004);
                }
                group.addView(mImageViews[i]);
            }
        }
        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TouchImageAdapter());
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(mIndex);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            finish();
        }
    };

    private class TouchImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final TouchImageView img = new TouchImageView(container.getContext());
//            img.setScaleType(ScaleType.CENTER);
            img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            img.setAdjustViewBounds(true);
            img.setOnClickListener(mOnClickListener);
            img.setScaleType(ScaleType.CENTER_INSIDE);
            GlideUtils.ShowImage(ViewPagePhotoActivity.this, mUrls[position], img, R.drawable.picture_default);
//            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            container.addView(img);
            return img;
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

    class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            index = index % mUrls.length;
            ImageView[] images = mImageViews;
            for (int i = 0; i < mUrls.length; i++) {
                images[index].setImageResource(R.drawable.page003);
                if (index != i) {
                    images[i].setImageResource(R.drawable.page004);
                }
            }
        }
    }
}