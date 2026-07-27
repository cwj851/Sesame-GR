package io.github.lazyimmortal.sesame.hook

import android.os.Build
import androidx.annotation.RequiresApi
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.injectModuleAppResources
import com.highcapable.yukihookapi.hook.factory.registerModuleAppActivities

@RequiresApi(Build.VERSION_CODES.N)
object AlipayHooker : YukiBaseHooker() {
    override fun onHook() {
        onAppLifecycle {
            onCreate {
                resources.injectModuleAppResources()
                registerModuleAppActivities()

            }
        }
    }
}