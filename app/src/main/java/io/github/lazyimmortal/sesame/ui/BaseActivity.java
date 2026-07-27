package io.github.lazyimmortal.sesame.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.highcapable.yukihookapi.hook.xposed.parasitic.activity.base.ModuleAppCompatActivity;

import java.util.Objects;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.ModuleInfo;
import io.github.lazyimmortal.sesame.util.ColorUtil;
import io.github.lazyimmortal.sesame.util.LanguageUtil;

public class BaseActivity extends ModuleAppCompatActivity {

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getModuleTheme() {
        return R.style.AppTheme;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        toolbar = findViewById(R.id.x_toolbar);
        setBaseTitle(getBaseTitle());
        setBaseSubtitle(getBaseSubtitle());
        setSupportActionBar(toolbar);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.setLocal(newBase));
    }

    public String getBaseTitle() {
        return ModuleInfo.getAppTitle();
    }

    public String getBaseSubtitle() {
        return null;
    }

    public void setBaseTitle(String title) {
        toolbar.setTitle(title);
        setBaseTitleTextColor(ColorUtil.getColor(this, com.google.android.material.R.attr.colorOnPrimary));
    }

    public void setBaseSubtitle(String subTitle) {
        toolbar.setSubtitle(subTitle);
        setBaseSubtitleTextColor(ColorUtil.getColor(this, com.google.android.material.R.attr.colorOnPrimary));
    }

    public void setBaseTitleTextColor(int color) {
        toolbar.setTitleTextColor(color);
    }

    public void setBaseSubtitleTextColor(int color) {
        toolbar.setSubtitleTextColor(color);
    }

    public void setBaseBackgroundColor(int color) {
        toolbar.setBackgroundColor(color);
        setStatusBarColor();
    }

    public void setStatusBarColor() {
        Window window = getWindow();
        window.getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets windowInsets) {
                WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(windowInsets);
                WindowInsetsCompat.Builder builder = new WindowInsetsCompat.Builder(windowInsetsCompat);
                builder.setInsets(WindowInsetsCompat.Type.statusBars(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.statusBars()));
                ColorDrawable colorDrawable = (ColorDrawable) toolbar.getBackground();
                if (colorDrawable != null) {
                    view.setBackgroundColor(colorDrawable.getColor());
                }
                return Objects.requireNonNull(builder.build().toWindowInsets());
            }
        });
        window.getDecorView().requestApplyInsets();
    }
}
