package com.jinminetics.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.jinminetics.cinegum.R;


public class JButton extends AppCompatButton {

    public JButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public JButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public JButton(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
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
    }

}