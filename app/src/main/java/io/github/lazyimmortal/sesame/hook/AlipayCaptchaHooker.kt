package io.github.lazyimmortal.sesame.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import io.github.lazyimmortal.sesame.model.normal.base.BaseModel
import io.github.lazyimmortal.sesame.util.Log

object AlipayCaptchaHooker : YukiBaseHooker() {
    private val TAG = AlipayCaptchaHooker::class.java.simpleName

    private val CaptchaDialogClass by lazyClass("com.alipay.rdssecuritysdk.v3.captcha.view.CaptchaDialog")
    private val RpcRdsUtilImplClass by lazyClassOrNull("com.alipay.edge.observer.rpc.RpcRdsUtilImpl")

    override fun onHook() {
        CaptchaDialogClass.runCatching {
            method {
                name("show")
                emptyParam()
            }.hook {
                replaceUnit {
                    if (BaseModel.getBlockCaptchaDialogMode() != 1) {
                        callOriginal()
                    }
                }
            }
        }.onFailure {
            Log.printStackTrace(TAG, it)
        }

        RpcRdsUtilImplClass?.runCatching {
            method {
                name = "rdsCaptchaHandle"
                paramCount = 7
            }.hook {
                replaceAny {
                    if (BaseModel.getBlockCaptchaDialogMode() != 2) {
                        callOriginal()
                    } else {
                        Log.chat("桀桀桀")
                        0
                    }
                }
            }
        }?.onFailure {
            Log.printStackTrace(TAG, it)
        }
    }
}
