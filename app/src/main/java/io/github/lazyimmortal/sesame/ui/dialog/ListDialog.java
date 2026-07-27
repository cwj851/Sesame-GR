package io.github.lazyimmortal.sesame.ui.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.common.SelectModelFieldFunc;
import io.github.lazyimmortal.sesame.entity.idAndName.IdAndName;

public class ListDialog {

    public static <T extends ModelField<?> & SelectModelFieldFunc>
    void show(Context context, T modelField,
              ModelFieldDialog.OnModelFieldClickListener listener) {
        @SuppressWarnings("unchecked")
        ListAdapter listAdapter = new ListAdapter(
                context, (List<? extends IdAndName>) modelField.getExpandValue(), modelField
        );
        AlertDialogBuilder.getAlertDialogBuilder(context, modelField)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    listAdapter.updateModelFieldValue();
                    if (listener != null) {
                        listener.onClick(context, modelField);
                    }
                })
                .setView(getListView(context, listAdapter))
                .create().show();
    }

    private static View getListView(Context context, ListAdapter listAdapter) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_with_list, null);

        // 搜索框和列表
        TextView description = dialogView.findViewById(R.id.description);
        TextInputLayout searchLayout = dialogView.findViewById(R.id.searchLayout);
        TextInputEditText searchInput = dialogView.findViewById(R.id.searchInput);
        ListView listView = dialogView.findViewById(R.id.listView);
        Button selectAll = dialogView.findViewById(R.id.button_selectAll);
        Button selectInvert = dialogView.findViewById(R.id.button_selectInvert);

        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // 搜索功能
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.setFilterList(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        selectAll.setOnClickListener(view -> listAdapter.selectAll());

        selectInvert.setOnClickListener(view -> listAdapter.selectInvert());

        description.setVisibility(View.GONE);
        if (listAdapter.getDescription() != null) {
            description.setText(listAdapter.getDescription());
            description.setVisibility(View.VISIBLE);
        }
        if (listAdapter.listType != ListAdapter.ListType.CHECK) {
            selectAll.setVisibility(View.GONE);
            selectInvert.setVisibility(View.GONE);
        }
        return dialogView;
    }
}
