package io.github.lazyimmortal.sesame.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import io.github.lazyimmortal.sesame.hook.AlipayHooker.loadHooker

@InjectYukiHookWithXposed
object HookEntry : IYukiHookXposedInit {
    override fun onHook() = encase {
        loadApp("io.github.lazyimmortal.sesame", SesameHooker)
    }

    override fun onXposedEvent() {
        YukiXposedEvent.events {
            onHandleLoadPackage {
                ApplicationHook.handleLoadPackage(it)
                loadHooker(AlipayAccountHooker)
                loadHooker(AlipayCaptchaHooker)
            }
        }
    }
}