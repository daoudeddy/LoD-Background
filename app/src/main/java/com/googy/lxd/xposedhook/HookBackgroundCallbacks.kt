package com.googy.lxd.xposedhook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


class HookBackgroundCallbacks : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (HOOK_PKG_NAME != lpparam?.packageName) {
            return
        }

        /**
         * This an example by hooking Processor.class to stop
         * pausing and resuming on Activity Lifecycle Events
         * on line 133
         *
         * public void pauseContainer() {
         *  Log.i(TAG, "pauseContainer");
         *      this.mNetworkService.pauseContainer();
         * }
         * public void resumeContainer() {
         *      Log.i(TAG, "resumeContainer");
         *      this.mNetworkService.resumeContainer();
         * }
         */
        XposedHelpers.findAndHookMethod(
            HOOK_CLASS_NAME_PROCESSOR,
            lpparam.classLoader,
            RESUME,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = false
                }
            })


        XposedHelpers.findAndHookMethod(
            HOOK_CLASS_NAME_PROCESSOR,
            lpparam.classLoader,
            PAUSE,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    param?.result = false
                }
            })
    }

    companion object {
        private const val HOOK_CLASS_NAME_PROCESSOR = "com.samsung.android.lxd.processor.Processor"
        private const val HOOK_PKG_NAME = "com.samsung.android.lxd"

        private const val RESUME = "resumeContainer"
        private const val PAUSE = "pauseContainer"
    }
}