package io.github.lazyimmortal.sesame.model.extensions.antForest;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class FetchRanking extends Model {

    @Override
    public String getName() {
        return "拉取总榜";
    }

    @Override
    public String getEnableFieldName() {
        return "无需开启";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.FOREST;
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(new EmptyModelField("fetchRanking", "拉取总榜", (context, modelField) -> {
            RequestHandler.sendRequestBroadcast(context, new Request(RequestType.FETCH_RANKING));
            ToastUtil.show(context, "已发送拉取总榜请求，请在日志查看结果！");
        }, "确认拉取总榜数据？将从 userList.json 中读取用户列表并逐个查询最新数据。"));
        return modelFields;
    }
}
