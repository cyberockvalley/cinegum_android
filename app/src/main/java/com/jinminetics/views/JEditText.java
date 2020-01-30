package com.jinminetics.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.jinminetics.cinegum.R;


public class JEditText extends AppCompatEditText {
    private static final String BOLD_FONT_PATH = "proxima/Proxima Nova Bold.otf";
    private static final String NORMAL_FONT_PATH = "proxima/ProximaNova-Regular.otf";
    private Context context;
    private OnEnterListener mOnEnterListener;
    public JEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public JEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public JEditText(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        String font = null;
        if(attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.JFontView, 0, 0);
            try {
                font = a.getString(R.styleable.JFontView_fontFace);

            } finally {
                a.recycle();
            }
        }

        if(font !=  null) {
            setTypeface(Utils.getTypeFace(context, font));
        }
        Utils.getTypeFace(context, NORMAL_FONT_PATH);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {
        if(style == Typeface.BOLD) {
            super.setTypeface(Utils.getTypeFace(getContext(), BOLD_FONT_PATH));

        } else {
            super.setTypeface(Utils.getTypeFace(getContext(), NORMAL_FONT_PATH));
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if(mOnEnterListener != null && keyCode == KeyEvent.KEYCODE_ENTER) {
            mOnEnterListener.onEntered();
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        };
    }

    public interface OnEnterListener{
        void onEntered();
    }

    public void setOnEnterListener(OnEnterListener mOnEnterListener) {
        this.mOnEnterListener = mOnEnterListener;
    }
}