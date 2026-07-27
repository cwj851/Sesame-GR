package io.github.lazyimmortal.sesame.model.extensions.logModel;

import android.content.Intent;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.ui.ExtensionsActivity;
import io.github.lazyimmortal.sesame.util.IntentUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class LogModel extends Model {
    @Override
    public String getName() {
        return "日志";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(new EmptyModelField("forestLogViewer", "森林记录 | 查看",
                (c, m) -> IntentUtil.viewLogAndCannotClear(c, LogType.FOREST_LOG))
        );
        modelFields.addField(new EmptyModelField("farmLogViewer", "庄园记录 | 查看",
                (c, m) -> IntentUtil.viewLogAndCannotClear(c, LogType.FARM_LOG))
        );
        modelFields.addField(new EmptyModelField("otherLogViewer", "其他记录 | 查看",
                (c, m) -> IntentUtil.viewLogAndCannotClear(c, LogType.OTHER_LOG))
        );
        modelFields.addField(new EmptyModelField("recordLogViewer", "全部记录 | 查看",
                (c, m) -> IntentUtil.viewLogAndCannotClear(c, LogType.RECORD_LOG))
        );
        modelFields.addField(new EmptyModelField("debugLogViewer", "抓包记录 | 查看",
                (c, m) -> IntentUtil.viewLog(c, LogType.DEBUG_LOG))
        );
        modelFields.addField(new EmptyModelField("chatLogViewer", "答题记录 | 查看",
                (c, m) -> IntentUtil.viewLog(c, LogType.CHAT_LOG))
        );
        modelFields.addField(new EmptyModelField("systemLogViewer", "系统日志 | 查看",
                (c, m) -> IntentUtil.viewLogAndNotNextLine(c, LogType.SYSTEM_LOG))
        );
        modelFields.addField(new EmptyModelField("errorLogViewer", "错误日志 | 查看",
                (c, m) -> IntentUtil.viewLogAndNotNextLine(c, LogType.ERROR_LOG))
        );
        modelFields.addField(new EmptyModelField("runtimeLogViewer", "运行日志 | 查看",
                (c, m) -> IntentUtil.viewLogAndNotNextLine(c, LogType.RUNTIME_LOG),
                "确认查看运行日志？可能要加载很久...")
        );
        modelFields.addField(new EmptyModelField("runtimeLogExport", "运行日志 | 导出", (c, m) -> {
            try {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/octet-stream");
                intent.putExtra(Intent.EXTRA_TITLE, LogType.RUNTIME_LOG.getLogFileName());
                ExtensionsActivity.getInstance(c).exportRuntimeLogLauncher.launch(intent);
            } catch (Exception e) {
                Log.printStackTrace(e);
                ToastUtil.show(c, "文档选择器跳转失败");
            }
        }));
        return modelFields;
    }
}
