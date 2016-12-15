package com.jason.trip.custom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.trip.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:CardIconButton
 * Description:
 * Created by Jason on 16/12/15.
 */

public class CardIconButton extends LinearLayout {

    @BindView(R.id.cardiconbutton_image)
    ImageView mImage;
    @BindView(R.id.cardiconbutton_text)
    TextView mText;

    public CardIconButton(Context context) {
        super(context);

        init(getContext(), null, 0, 0);
    }

    public CardIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(getContext(), attrs, 0, 0);
    }

    public CardIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(getContext(), attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardIconButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(getContext(), attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        setClickable(true);
        setFocusable(true);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);

        inflate(context, R.layout.card_icon_button, this);
        ButterKnife.bind(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardIconButton,
                defStyleAttr, defStyleRes);
        Drawable src = a.getDrawable(R.styleable.CardIconButton_android_src);
        if (src != null) {
            mImage.setImageDrawable(src);
        }
        CharSequence text = a.getText(R.styleable.CardIconButton_android_text);
        setText(text);
        a.recycle();
    }

    public ImageView getImageView() {
        return mImage;
    }

    public TextView getTextView() {
        return mText;
    }

    public void setIcon(Drawable icon) {
        mImage.setImageDrawable(icon);
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }
}

