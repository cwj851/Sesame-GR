package io.github.lazyimmortal.sesame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;

public class ExtensionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension);

        String viewName = "";
        Intent intent = getIntent();
        if (intent != null) {
            viewName = intent.getStringExtra("viewName");
            setBaseSubtitle(getString(R.string.extensions) + ":" + viewName);
        }
        LinearLayout linearLayout = findViewById(R.id.liner_layout_extension);
        ExtensionsHandle.handleRequest(new Request(RequestType.ADD_EXTENSION_VIEW.name(), viewName, linearLayout));
    }
}
