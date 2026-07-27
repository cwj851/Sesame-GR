package io.github.lazyimmortal.sesame.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class HtmlViewerActivity extends BaseActivity {
    MyWebView mWebView;
    ProgressBar pgb;
    Uri uri;
    Boolean canClear;

    private final ActivityResultLauncher<Intent> exportLogLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null && uri.getPath() != null) {
                    FileUtil.exportFile(HtmlViewerActivity.this, new File(uri.getPath()), result.getData().getData());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_viewer);

        mWebView = findViewById(R.id.mwv_webview);
        pgb = findViewById(R.id.pgb_webview);

        mWebView.setWebChromeClient(
                new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int progress) {
                        pgb.setProgress(progress);
                        if (progress < 100) {
                            setBaseSubtitle(getString(R.string.loading));
                            pgb.setVisibility(View.VISIBLE);
                        } else {
                            setBaseSubtitle(mWebView.getTitle());
                            pgb.setVisibility(View.GONE);
                        }
                    }
                }
        );
        Intent intent = getIntent();
        if (intent != null) {
            uri = intent.getData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        WebSettings settings = mWebView.getSettings();
        if (intent != null) {
            if (intent.getBooleanExtra("nextLine", true)) {
                settings.setTextZoom(85);
                settings.setUseWideViewPort(false);
            } else {
                settings.setTextZoom(100);
                settings.setUseWideViewPort(true);
            }
            uri = intent.getData();
            if (uri != null) {
                mWebView.loadUrl(uri.toString());
            }
            canClear = intent.getBooleanExtra("canClear", true);
            return;
        }
        settings.setTextZoom(100);
        settings.setUseWideViewPort(true);
    }

    private final int EXPORT_FILE = 1;
    private final int CLEAR_FILE = 2;
    private final int OPEN_WITH_OTHER_BROWSER = 3;
    private final int COPY_THE_URL = 4;
    private final int SCROLL_TO_TOP = 5;
    private final int SCROLL_TO_BOTTOM = 6;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (uri != null) {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                menu.add(0, EXPORT_FILE, EXPORT_FILE, R.string.export_file);
                if (canClear) {
                    menu.add(0, CLEAR_FILE, CLEAR_FILE, R.string.clear_file);
                }
            } else if ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())) {
                menu.add(0, OPEN_WITH_OTHER_BROWSER, OPEN_WITH_OTHER_BROWSER, R.string.open_with_other_browser);
            }
        }
        menu.add(0, COPY_THE_URL, COPY_THE_URL, R.string.copy_the_url);
        menu.add(0, SCROLL_TO_TOP, SCROLL_TO_TOP, R.string.scroll_to_top);
        menu.add(0, SCROLL_TO_BOTTOM, SCROLL_TO_BOTTOM, R.string.scroll_to_bottom);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EXPORT_FILE: {
                if (uri == null || uri.getPath() == null) {
                    break;
                }
                File exportFile = FileUtil.exportFile(new File(uri.getPath()));
                if (exportFile == null) {
                    break;
                }
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/octet-stream");
                intent.putExtra(Intent.EXTRA_TITLE, exportFile.getName());
                exportLogLauncher.launch(intent);
                break;
            }

            case CLEAR_FILE:
                if (uri == null) {
                    break;
                }
                String path = uri.getPath();
                if (path != null) {
                    File file = new File(path);
                    if (FileUtil.clearFile(file)) {
                        ToastUtil.show(this, "文件已清空");
                        mWebView.reload();
                    }
                }
                break;

            case OPEN_WITH_OTHER_BROWSER:
                if (uri == null) {
                    break;
                }
                String scheme = uri.getScheme();
                if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if ("file".equalsIgnoreCase(scheme)) {
                    ToastUtil.show(this, "该文件不支持用浏览器打开");
                } else {
                    ToastUtil.show(this, "不支持用浏览器打开");
                }
                break;

            case COPY_THE_URL:
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null, mWebView.getUrl()));
                ToastUtil.show(this, R.string.copy_success);
                break;

            case SCROLL_TO_TOP:
                mWebView.scrollTo(0, 0);
                break;

            case SCROLL_TO_BOTTOM:
                mWebView.scrollToBottom();
                break;
        }
        return true;
    }
}
