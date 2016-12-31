package com.jason.trip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;

import com.jason.trip.R;
import com.jason.trip.base.framwork.SimpleAdapter;
import com.jason.trip.custom.view.ImageLayout;
import com.jason.trip.network.api.Image;
import com.jason.trip.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:HorizontalImageAdapter
 * Description:
 * Created by Jason on 16/12/18.
 */

public class HorizontalImageAdapter
        extends SimpleAdapter<Image, HorizontalImageAdapter.ViewHolder> {

    private OnImageClickListener mOnImageClickListener;

    public HorizontalImageAdapter() {
        setHasStableIds(true);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mOnImageClickListener = listener;
    }

    @Override
    public long getItemId(int position) {
        // Deliberately using plain hash code to identify only this instance.
        return getItem(position).hashCode();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.horizontal_image_item, parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imageLayout.loadImage(getItem(position));
        holder.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onImageClick(holder.getAdapterPosition());
                }
            }
        });
        // FIXME: This won't work properly if items are changed.
        ViewUtils.setVisibleOrGone(holder.dividerSpace, position != getItemCount() - 1);
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        public ImageLayout imageLayout;
        @BindView(R.id.divider)
        public Space dividerSpace;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
