package io.github.lazyimmortal.sesame.model.extensions.antSports;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.entity.idAndName.WalkPath;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.HandlerUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.WalkPathIdMap;

public class AntSportsExtension extends Model {
    @Override
    public String getName() {
        return "运动";
    }

    @Override
    public String getEnableFieldName() {
        return "无需开启";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.SPORTS;
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(new EmptyModelField("addWalkCustomWalkPathId", "走路线 | 自定义路线添加", (c, m) -> {
            StringModelField modelField = new StringModelField("", "自定义路线添加", null, "请输入要添加到自定义路线列表的路线代码");
            ModelFieldDialog.show(c, modelField, (c1, m1) -> {
                HandlerUtil.post(() -> {
                    RequestHandler.sendRequestBroadcast(c1, new Request(RequestType.ADD_CUSTOM_WALK_PATH_ID, m1.getConfigValue()));
                    ToastUtil.show(c1, "已发送查询请求，请等待响应结果！");
                });
                HandlerUtil.postDelayed(() -> {
                    if (RuntimeInfo.getInstance(true).getByAll(RuntimeInfo.RuntimeInfoKey.RequestSuccess) == null) {
                        ToastUtil.show(c1, "响应失败:请确保模块在支付宝中正常加载！");
                    }
                    RuntimeInfo.getInstance().putAll(RuntimeInfo.RuntimeInfoKey.RequestSuccess, null);
                }, 3000);
            });
        }));
        modelFields.addField(new EmptyModelField("queryWalkCustomPathList", "走路线 | 自定义路线列表", (c, m) -> {
            WalkPathIdMap.getInstance().load();
            SelectModelField selectModelField = new SelectModelField("", "自定义路线列表", null, WalkPath::getList, "长按可删除路线");
            ModelFieldDialog.show(c, selectModelField);
        }));
//        modelFields.addField(new EmptyModelField("addCustomWalkPathIdQueue", "走路线 | 添加待行走队列", (c, m) -> {
//            StringModelField modelField = new StringModelField("", "添加待行走路线", null, "请输入要添加的路线代码");
//            ModelFieldDialog.show(c, modelField, (c1, m1) -> RequestHandler.sendRequestBroadcast(c1, new Request(RequestType.ADD_CUSTOM_WALK_PATH_ID_QUEUE, m1.getConfigValue())));
//        }));
//        modelFields.addField(new EmptyModelField("clearCustomWalkPathIdQueue", "走路线 | 清除待行走队列", (c, m) -> {
//            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.CLEAR_CUSTOM_WALK_PATH_ID_QUEUE));
//        }, "确认清除待行走队列？"));
        modelFields.addField(new EmptyModelField("syncStepCountNow", "运动步数 | 即刻修改", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.SYNC_STEP_COUNT_NOW));
            ToastUtil.show(c, "已发送修改请求，请在全部记录查看结果！");
        }, "确认向支付宝发送修改请求？"));
        return modelFields;
    }
}
