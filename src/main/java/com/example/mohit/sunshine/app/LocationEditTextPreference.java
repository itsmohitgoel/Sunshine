package com.example.mohit.sunshine.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * Created by Mohit on 06-10-2016.
 */
public class LocationEditTextPreference extends EditTextPreference {
    public static final int DEFAULT_MINIMUM_LOCATION_LENGTH = 2;
    private int mMinLength;
    public LocationEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.LocationEditTextPreference,
                0, 0
        );

        try {
            mMinLength = a.getInteger(R.styleable.LocationEditTextPreference_minLength, DEFAULT_MINIMUM_LOCATION_LENGTH);
        }finally {
            a.recycle();
        }
    }
}
