package com.jason.trip.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.TimeoutError;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.jason.trip.R;
import com.jason.trip.util.ImageUtils;
import com.jason.trip.util.ViewUtils;

import java.util.List;

import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:GalleryAdapter
 * Description:
 * Created by Jason on 16/12/19.
 */

public class GalleryAdapter extends PagerAdapter {

    private List<String> mImageList;
    private OnTapListener mOnTapListener;

    public GalleryAdapter(List<String> imageList, OnTapListener onTapListener) {
        mImageList = imageList;
        mOnTapListener = onTapListener;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        View layout = LayoutInflater.from(context).inflate(R.layout.gallery_item, container, false);
        PhotoView imageView = ButterKnife.findById(layout, R.id.image);
        final TextView errorText = ButterKnife.findById(layout, R.id.error);
        final ProgressBar progressBar = ButterKnife.findById(layout, R.id.progress);
        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (mOnTapListener != null) {
                    mOnTapListener.onTap();
                }
            }
        });
        ImageUtils.loadImage(imageView, mImageList.get(position),
                new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        if (e == null) {
                            new NullPointerException().printStackTrace();
                        } else {
                            e.printStackTrace();
                        }
                        int errorRes = e != null && e.getCause() instanceof TimeoutError
                                ? R.string.gallery_load_timeout : R.string.gallery_load_error;
                        errorText.setText(errorRes);
                        ViewUtils.crossfade(progressBar, errorText);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        ViewUtils.fadeOut(progressBar);
                        return false;
                    }
                }, context);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnTapListener {
        void onTap();
    }
}

