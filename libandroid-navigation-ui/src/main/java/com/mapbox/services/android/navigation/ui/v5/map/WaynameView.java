package com.mapbox.services.android.navigation.ui.v5.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.mapbox.services.android.navigation.ui.v5.R;
import com.mapbox.services.android.navigation.ui.v5.ThemeSwitcher;

public class WaynameView extends CardView {

    private static final int BACKGROUND_ALPHA = 220;

    private TextView waynameText;
    private int waynameHeight;

    public WaynameView(Context context) {
        super(context);
        init(context);
    }

    public WaynameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WaynameView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, waynameHeight);
    }

    public void setWaynameText(String waynameText) {
        this.waynameText.setText(waynameText);
    }

    public String retrieveWayNameText() {
        return waynameText.getText().toString();
    }

    private void init(Context context) {
        inflate(getContext(), R.layout.wayname_view_layout, this);
        waynameText = findViewById(R.id.waynameText);
        Drawable waynameTextBackground = waynameText.getBackground();
        initializeBackground(waynameTextBackground);
        waynameHeight = (int) context.getResources().getDimension(R.dimen.wayname_view_height);
    }

    private void initializeBackground(Drawable waynameTextBackground) {
        waynameTextBackground.setAlpha(BACKGROUND_ALPHA);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            int navigationViewPrimaryColor = ThemeSwitcher.retrieveThemeColor(getContext(),
                    R.attr.navigationViewPrimary);
            Drawable soundChipBackground = DrawableCompat.wrap(waynameTextBackground).mutate();
            DrawableCompat.setTint(soundChipBackground, navigationViewPrimaryColor);
        }
    }
}
