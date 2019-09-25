package com.minhthuanht.quanlytaichinh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.minhthuanht.quanlytaichinh.utilities.CurrencyUtils;


public class CurrencyTextView extends AppCompatTextView {

    String rawText;

    public CurrencyTextView(Context context) {
        super(context);
    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        rawText = text.toString();
        String currency = text.toString();
        try {
            currency = CurrencyUtils.formatVnCurrence(currency);
        }catch (Exception ignored){}

        super.setText(currency, type);
    }

    @Override
    public CharSequence getText() {
        return rawText;
    }
}
