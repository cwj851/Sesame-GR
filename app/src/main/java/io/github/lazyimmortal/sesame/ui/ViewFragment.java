package io.github.lazyimmortal.sesame.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class ViewFragment extends Fragment {
    public static Map<String, View> viewMap = new HashMap<>();
    private final String modelCode;

    public ViewFragment(String modelCode) {
        super();
        this.modelCode = modelCode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return viewMap.get(modelCode);
    }
}
