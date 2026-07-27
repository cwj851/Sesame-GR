package io.github.lazyimmortal.sesame.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import io.github.lazyimmortal.sesame.data.ModuleInfo

object SesameHooker : YukiBaseHooker() {
    override fun onHook() {
        ModuleInfo::class.java.name.toClass()
            .method {
                name = "isModuleActive"
                emptyParam()
            }.hook().replaceToTrue()
    }
}