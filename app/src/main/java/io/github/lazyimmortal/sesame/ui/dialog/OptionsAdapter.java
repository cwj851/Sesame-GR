package io.github.lazyimmortal.sesame.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.rpc.request.Request;

public class OptionsAdapter extends BaseAdapter {
    private final String itemId;
    private final List<String> list = new ArrayList<>();

    // 视图
    private final LayoutInflater inflater;

    public OptionsAdapter(Context context, String itemId) {
        inflater = LayoutInflater.from(context);
        list.add("查看森林");
        list.add("查看庄园");
        list.add("查看资料");
        list.add("查询能量来源");
        this.itemId = itemId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }
        ((TextView) view).setText(getItem(i).toString());
        return view;
    }

    public DialogInterface.OnClickListener getOnClickListener(Context context) {
        return (dialogInterface, i) -> {
            String url = null;
            switch (i) {
                case 0:
                    url = "alipays://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2F60000002.h5app.alipay.com%2Fwww%2Fhome.html%3FuserId%3D";
                    break;

                case 1:
                    url = "alipays://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2F66666674.h5app.alipay.com%2Fwww%2Findex.htm%3Fuid%3D";
                    break;

                case 2:
                    url = "alipays://platformapi/startapp?appId=20000166&actionType=profile&userId=";
                    break;
                case 3:
                    try {
                        String broadcast_data = itemId;
                        if (broadcast_data.isEmpty()) {
                            return;
                        }
                        RequestHandler.sendRequestBroadcast(context, new Request("QUERY_FRIEND_ENERGY","",broadcast_data));
/*                                                             Intent intent = new Intent(
                                                                    "com.eg.android.AlipayGphone.cactus.rpctest");
                                                            intent.putExtra("method", "");
                                                            intent.putExtra("data", broadcast_data);
                                                            intent.putExtra("type", "QUERY_FRIEND_ENERGY");
                                                            c.sendBroadcast(intent); */
                        ToastUtil.show(context,"已发送查询请求，请在森林日志查看结果！");
                                                            /* Toast.makeText(c, "已发送查询请求，请在森林日志查看结果！", Toast.LENGTH_SHORT)
                                                                    .show(); */
                    } catch (Throwable ignored) {
                    }
            }
            if (url != null) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url + itemId));
                context.startActivity(it);
            }
        };
    }
}
