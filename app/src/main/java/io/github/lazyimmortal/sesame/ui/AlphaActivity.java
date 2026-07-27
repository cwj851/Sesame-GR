//package io.github.lazyimmortal.sesame.ui;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//
//import io.github.lazyimmortal.sesame.R;
//import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
//import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandleAlpha;
//import io.github.lazyimmortal.sesame.rpc.request.Request;
//import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
//import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
//import io.github.lazyimmortal.sesame.util.FileUtil;
//import io.github.lazyimmortal.sesame.util.ToastUtil;
//
//public class AlphaActivity extends BaseActivity {
//    EditText etMethod, etData;
//    Button btnSendRpcRequest, btnViewDebugLog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_alpha);
//        setBaseTitle(getString(R.string.developer_mode));
//        etMethod = findViewById(R.id.et_method);
//        etData = findViewById(R.id.et_data);
//        btnSendRpcRequest = findViewById(R.id.btn_send_rpc_request);
//        btnViewDebugLog = findViewById(R.id.btn_view_debug_log);
//
//        btnSendRpcRequest.setOnClickListener(view -> {
//            String rpc_method = etMethod.getText().toString();
//            String rpc_data = etData.getText().toString();
//            if (rpc_method.isEmpty() || rpc_data.isEmpty()) {
//                ToastUtil.show(AlphaActivity.this, "请求不能为空");
//                return;
//            }
//            RequestHandler.sendRequestBroadcast(AlphaActivity.this,
//                    new Request(
//                            ExtensionsHandleAlpha.AlphaRequestType.PRC.name(), rpc_method, rpc_data
//                    )
//            );
//            ToastUtil.show(AlphaActivity.this, "已发送Rpc请求，请在debug日志查看结果！");
//        });
//
//        btnViewDebugLog.setOnClickListener(view -> viewDebugLog());
//    }
//
//    private void viewDebugLog() {
//        String debugData = "file://" + FileUtil.getDebugLogFile().getAbsolutePath();
//        Intent debugIt = new Intent(this, HtmlViewerActivity.class);
//        debugIt.setData(Uri.parse(debugData));
//        debugIt.putExtra("canClear", true);
//        startActivity(debugIt);
//    }
//}
