package com.mapbox.services.android.navigation.ui.v5;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.fragment.app.FragmentActivity;

public class ContextHelper {
    public static FragmentActivity getFragmentActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (FragmentActivity) context;
            } else {
                return getFragmentActivity(((ContextWrapper) context).getBaseContext());
            }
        }
        return null;
    }
}
