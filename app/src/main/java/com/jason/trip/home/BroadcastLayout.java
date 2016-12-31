package com.jason.trip.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.jason.trip.R;
import com.jason.trip.adapter.HorizontalImageAdapter;
import com.jason.trip.base.framwork.GalleryActivity;
import com.jason.trip.custom.view.CardIconButton;
import com.jason.trip.custom.view.ImageLayout;
import com.jason.trip.custom.view.TimeActionTextView;
import com.jason.trip.link.UriHandler;
import com.jason.trip.listener.OnHorizontalScrollListener;
import com.jason.trip.network.api.Attachment;
import com.jason.trip.network.api.Broadcast;
import com.jason.trip.network.api.Image;
import com.jason.trip.network.api.Photo;
import com.jason.trip.profile.ProfileActivity;
import com.jason.trip.util.CheatSheetUtils;
import com.jason.trip.util.DrawableUtils;
import com.jason.trip.util.ImageUtils;
import com.jason.trip.util.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialedittext.internal.ViewCompat;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:BroadcastLayout
 * Description:
 * Created by Jason on 16/12/16.
 */

public class BroadcastLayout extends LinearLayout {

    @BindView(R.id.avatar)
    ImageView mAvatarImage;
    @BindView(R.id.name)
    TextView mNameText;
    @BindView(R.id.time_action)
    TimeActionTextView mTimeActionText;
    @BindView(R.id.attachment)
    RelativeLayout mAttachmentLayout;
    @BindView(R.id.attachment_image)
    ImageView mAttachmentImage;
    @BindView(R.id.attachment_title)
    TextView mAttachmentTitleText;
    @BindView(R.id.attachment_description)
    TextView mAttachmentDescriptionText;
    @BindView(R.id.single_image)
    ImageLayout mSingleImageLayout;
    @BindView(R.id.image_list_layout)
    FrameLayout mImageListLayout;
    @BindView(R.id.image_list_description_layout)
    FrameLayout mImageListDescriptionLayout;
    @BindView(R.id.image_list_description)
    TextView mImageListDescriptionText;
    @BindView(R.id.image_list)
    RecyclerView mImageList;
    @BindView(R.id.text_space)
    Space mTextSpace;
    @BindView(R.id.text)
    TextView mTextText;
    @BindView(R.id.like)
    CardIconButton mLikeButton;
    @BindView(R.id.comment)
    CardIconButton mCommentButton;
    @BindView(R.id.rebroadcast)
    CardIconButton mRebroadcastButton;

    private Listener mListener;

    private Long mBoundBroadcastId;

    private HorizontalImageAdapter mImageListAdapter;

    public BroadcastLayout(Context context) {
        super(context);

        init(getContext(), null, 0, 0);
    }

    public BroadcastLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(getContext(), attrs, 0, 0);
    }

    public BroadcastLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(getContext(), attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BroadcastLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(getContext(), attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        setOrientation(VERTICAL);

        inflate(context, R.layout.broadcast_layout, this);
        ButterKnife.bind(this);

        ViewCompat.setBackground(mImageListDescriptionLayout, DrawableUtils.makeScrimDrawable());
        mImageList.setHasFixedSize(true);
        mImageList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false));
        mImageListAdapter = new HorizontalImageAdapter();
        mImageList.setAdapter(mImageListAdapter);
        mImageList.addOnScrollListener(new OnHorizontalScrollListener() {

            private boolean mShowingDescription = true;

            @Override
            public void onScrolledLeft() {
                if (!mShowingDescription) {
                    mShowingDescription = true;
                    ViewUtils.fadeIn(mImageListDescriptionLayout);
                }
            }

            @Override
            public void onScrolledRight() {
                if (mShowingDescription) {
                    mShowingDescription = false;
                    ViewUtils.fadeOut(mImageListDescriptionLayout);
                }
            }
        });

        ViewUtils.setTextViewLinkClickable(mTextText);

        CheatSheetUtils.setup(mLikeButton);
        CheatSheetUtils.setup(mCommentButton);
        CheatSheetUtils.setup(mRebroadcastButton);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void bindBroadcast(final Broadcast broadcast) {

        final Context context = getContext();

        if (broadcast.isInterest) {
            mAvatarImage.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.recommendation_avatar_icon_grey600_40dp));
            mAvatarImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // FIXME
                    UriHandler.open("https://www.douban.com/interest/1/1/", context);
                }
            });
        } else {
            ImageUtils.loadAvatar(mAvatarImage, broadcast.author.avatar, context);
            mAvatarImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(ProfileActivity.makeIntent(broadcast.author, context));
                }
            });
        }
        mNameText.setText(broadcast.getAuthorName());
        mTimeActionText.setDoubanTimeAndAction(broadcast.createdAt, broadcast.action);

        boolean isRebind = mBoundBroadcastId != null && mBoundBroadcastId == broadcast.id;
        // HACK: Attachment and text should not change on rebind.
        if (!isRebind) {

            Attachment attachment = broadcast.attachment;
            if (attachment != null) {
                mAttachmentLayout.setVisibility(VISIBLE);
                mAttachmentTitleText.setText(attachment.title);
                mAttachmentDescriptionText.setText(attachment.description);
                if (!TextUtils.isEmpty(attachment.image)) {
                    mAttachmentImage.setVisibility(VISIBLE);
                    ImageUtils.loadImage(mAttachmentImage, attachment.image, context);
                } else {
                    mAttachmentImage.setVisibility(GONE);
                }
                final String attachmentUrl = attachment.href;
                if (!TextUtils.isEmpty(attachmentUrl)) {
                    mAttachmentLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UriHandler.open(attachmentUrl, context);
                        }
                    });
                } else {
                    mAttachmentLayout.setOnClickListener(null);
                }
            } else {
                mAttachmentLayout.setVisibility(GONE);
            }

            final ArrayList<Image> images = broadcast.images.size() > 0 ? broadcast.images
                    : Photo.toImageList(broadcast.photos);
            int numImages = images.size();
            if (numImages == 1) {
                final Image image = images.get(0);
                mSingleImageLayout.setVisibility(VISIBLE);
                mSingleImageLayout.loadImage(image);
                mSingleImageLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(GalleryActivity.makeIntent(image, context));
                    }
                });
            } else {
                mSingleImageLayout.setVisibility(GONE);
            }
            if (numImages > 1) {
                mImageListLayout.setVisibility(VISIBLE);
                mImageListDescriptionText.setText(context.getString(
                        R.string.broadcast_image_list_count_format, numImages));
                mImageListAdapter.replace(images);
                mImageListAdapter.setOnImageClickListener(
                        new HorizontalImageAdapter.OnImageClickListener() {
                            @Override
                            public void onImageClick(int position) {
                                context.startActivity(GalleryActivity.makeImageListIntent(images,
                                        position, context));
                            }
                        });
            } else {
                mImageListLayout.setVisibility(GONE);
            }

            boolean textSpaceVisible = (attachment != null || numImages > 0)
                    && !TextUtils.isEmpty(broadcast.text);
            ViewUtils.setVisibleOrGone(mTextSpace, textSpaceVisible);
            mTextText.setText(broadcast.getTextWithEntities(context));
        }

        mLikeButton.setText(broadcast.getLikeCountString());
        LikeBroadcastManager likeBroadcastManager = LikeBroadcastManager.getInstance();
        if (likeBroadcastManager.isWriting(broadcast.id)) {
            mLikeButton.setActivated(likeBroadcastManager.isWritingLike(broadcast.id));
            mLikeButton.setEnabled(false);
        } else {
            mLikeButton.setActivated(broadcast.isLiked);
            mLikeButton.setEnabled(true);
        }
        mLikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLikeClicked();
                }
            }
        });
        RebroadcastBroadcastManager rebroadcastBroadcastManager =
                RebroadcastBroadcastManager.getInstance();
        if (rebroadcastBroadcastManager.isWriting(broadcast.id)) {
            mRebroadcastButton.setActivated(rebroadcastBroadcastManager.isWritingRebroadcast(
                    broadcast.id));
            mRebroadcastButton.setEnabled(false);
        } else {
            mRebroadcastButton.setActivated(broadcast.isRebroadcasted());
            mRebroadcastButton.setEnabled(true);
        }
        mRebroadcastButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRebroadcastClicked();
                }
            }
        });
        mCommentButton.setText(broadcast.getCommentCountString());
        mCommentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCommentClicked();
                }
            }
        });

        mBoundBroadcastId = broadcast.id;
    }

    public void releaseBroadcast() {
        mAvatarImage.setImageDrawable(null);
        mAttachmentImage.setImageDrawable(null);
        mSingleImageLayout.releaseImage();
        mImageListAdapter.clear();
        mBoundBroadcastId = null;
    }

    public interface Listener {
        void onLikeClicked();
        void onRebroadcastClicked();
        void onCommentClicked();
    }
}
