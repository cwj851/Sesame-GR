package io.github.lazyimmortal.sesame.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<String> modelCodeList = new ArrayList<>();
    private final List<String> modelNameList = new ArrayList<>();

    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addView(String modelCode, String modelName, View view) {
        modelCodeList.add(modelCode);
        modelNameList.add(modelName);
        ViewFragment.viewMap.put(modelCode, view);
    }

    public String getModelName(int position) {
        return modelNameList.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ViewFragment(modelCodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelCodeList.size();
    }
}
