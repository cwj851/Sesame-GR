package io.github.lazyimmortal.sesame.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.IntType
import io.github.lazyimmortal.sesame.util.Log
import io.github.lazyimmortal.sesame.util.ToastUtil

object AlipayAccountHooker : YukiBaseHooker() {
    private val TAG = AlipayAccountHooker::class.java.simpleName
    private val AccountManagerListAdapterClass by lazyClassOrNull("com.alipay.mobile.security.accountmanager.data.AccountManagerListAdapter")
    private val getCountMethodTips by lazy {
       // ToastUtil.show(ApplicationHook.getContext(), "已突破账号限制")
    }

    override fun onHook() {
        AccountManagerListAdapterClass?.runCatching {
            method {
                name("getCount")
                emptyParam()
                returnType(IntType)
            }.hook {
                replaceAny {
                    val count = instance.current().field {
                        name("queryAccountList")
                        superClass(true)
                    }.cast<List<Any>>()?.size ?: 0
                    if (count > 5) {
                        getCountMethodTips
                    }
                    count
                }
            }
        }?.onFailure {
            Log.printStackTrace(TAG, it)
        }
    }
}