package io.github.lazyimmortal.sesame.model.extensions.antForest;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.entity.DishImageEntity;
import io.github.lazyimmortal.sesame.entity.idAndName.EcoLifeDishImage;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.DishImageIdMap;

public class AntForestExtension extends Model {
    @Override
    public String getName() {
        return "森林&庄园";
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
        modelFields.addField(new EmptyModelField("getTreeItems", "查询树苗余量🌱", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.GET_TREE_ITEMS));
            ToastUtil.show(c, "已发送查询请求，请在森林日志查看结果！");
        }, "确认向支付宝发送查询请求？"));
        modelFields.addField(new EmptyModelField("getNewTreeItems", "查询树苗上新🌱", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.GET_NEW_TREE_ITEMS));
            ToastUtil.show(c, "已发送查询请求，请在森林日志查看结果！");
        }, "确认向支付宝发送查询请求？"));
        modelFields.addField(new EmptyModelField("getUnlockTreeItems", "查询未解锁项目🐘", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.GET_UNLOCK_TREE_ITEMS));
            ToastUtil.show(c, "已发送查询请求，请在森林日志查看结果！");
        }, "确认向支付宝发送查询请求？"));
        modelFields.addField(new EmptyModelField("queryAreaTrees", "查询未解锁地区🗺️", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.QUERY_AREA_TREES));
            ToastUtil.show(c, "已发送查询请求，请在森林日志查看结果！");
        }, "确认向支付宝发送查询请求？"));
        modelFields.addField(new EmptyModelField("queryPropList", "查询森林道具数量🎭", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.QUERY_PROP_LIST));
            ToastUtil.show(c, "已发送查询请求，请在森林日志查看结果！");
        }, "确认向支付宝发送查询请求？"));
        modelFields.addField(new EmptyModelField("queryFarmFood", "查询庄园美食数量🍱", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.QUERY_FARM_FOOD));
            ToastUtil.show(c, "已发送查询请求，请在庄园日志查看结果！");
        }, "确认向支付宝发送查询请求？"));
        modelFields.addField(new EmptyModelField("dishImageAdd", "光盘行动 | 添加照片", (c, m) -> {
            StringModelField beforeMealsDishImage = new StringModelField("", "添加光盘行动饭前照片",
                    "", "请输入饭前照片代码\n格式:A*...........");
            ModelFieldDialog.show(c, beforeMealsDishImage, (c1, m1) -> {
                if (!DishImageEntity.checkDishImage(beforeMealsDishImage.getValue())) {
                    ToastUtil.show(c1, "照片格式有误");
                    return;
                }
                StringModelField afterMealsDishImage = new StringModelField("", "添加光盘行动饭后照片",
                        "", "请输入饭后照片代码\n格式:A*...........");
                ModelFieldDialog.show(c, afterMealsDishImage, (c2, m2) -> {
                    if (!DishImageEntity.checkDishImage(afterMealsDishImage.getValue())) {
                        ToastUtil.show(c2, "照片格式有误");
                        return;
                    }
                    DishImageEntity dishImage = new DishImageEntity(beforeMealsDishImage.getValue(), afterMealsDishImage.getValue());
                    if (!dishImage.checkDishImage()) {
                        ToastUtil.show(c, "饭前照片与饭后照片不能相同");
                    } else {
                        DishImageIdMap.getInstance().load();
                        if (DishImageIdMap.getInstance().add(dishImage)) {
                            ToastUtil.show(c, "光盘行动照片已添加到列表中");
                        } else {
                            ToastUtil.show(c, "光盘行动照片添加失败");
                        }
                    }
                });
            });
        }));
        modelFields.addField(new EmptyModelField("dishImageList", "光盘行动 | 照片列表", (c, m) -> {
            DishImageIdMap.getInstance().load();
            SelectModelField selectModelField = new SelectModelField("", "光盘行动 | 照片列表",
                    null, EcoLifeDishImage::getList, "长按可删除照片");
            ModelFieldDialog.show(c, selectModelField);
        }));
        modelFields.addField(new EmptyModelField("dishImageNow", "光盘行动 | 即刻打卡", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.DISH_IMAGE_NOW));
            ToastUtil.show(c, "已发送打卡请求，请在森林日志查看结果！");
        }, "确认向支付宝发送打卡请求？"));
        modelFields.addField(new EmptyModelField("queryUserCooperatePlantList", "生态保护 | 更新用户合种列表", (c, m) -> {
            RequestHandler.sendRequestBroadcast(c, new Request(RequestType.QUERY_USER_COOPERATE_PLANT_LIST));
            ToastUtil.show(c, "已发送更新请求，请在用户设置查看结果！");
        }, "确认想支付宝发送更新请求"));
        return modelFields;
    }
}
