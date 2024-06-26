package com.xxf.arch.fragment.navigation

import android.app.Activity
import android.content.ComponentName
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHost
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.xxf.application.lifecycle.findViewLifecycleOwner
import com.xxf.arch.R
import com.xxf.cache.ReflectionCache
import com.xxf.ktx.asFragmentActivityOrNull
import com.xxf.ktx.asFragmentOrNull

fun FragmentContainerView.getNavHostFragment(): NavHostFragment? {
    return when (val findViewLifecycleOwner = this.findViewLifecycleOwner()) {
        is Fragment -> {
            findViewLifecycleOwner?.childFragmentManager?.findFragmentById(this.id) as? NavHostFragment
        }

        is FragmentActivity -> {
            findViewLifecycleOwner?.supportFragmentManager?.findFragmentById(this.id) as? NavHostFragment
        }

        else -> {
            null
        }
    }
}

fun FragmentContainerView.getNavController(): NavController? {
    return getNavHostFragment()?.navController
}


/**
 * 通过navControllerViewId 获取上面加载的NavController
 */
fun LifecycleOwner.getNavHostFragment(@IdRes navControllerViewId: Int): NavHostFragment? {
    return when (this) {
        is Fragment -> {
            asFragmentOrNull()?.childFragmentManager?.findFragmentById(navControllerViewId) as? NavHostFragment
        }

        is FragmentActivity -> {
            asFragmentActivityOrNull()?.supportFragmentManager?.findFragmentById(navControllerViewId) as? NavHostFragment
        }

        else -> {
            null
        }
    }
}

/**
 * 通过navControllerViewId 获取上面加载的NavController
 */
fun LifecycleOwner.getNavController(@IdRes navControllerViewId: Int): NavController? {
    return getNavHostFragment(navControllerViewId)?.navController
}

/**
 * 是否在导航控制器中
 */
fun Fragment.isInNavController(): Boolean {
    return try {
        findNavControllerOrNull() != null
    } catch (e: Throwable) {
        false
    }
}


/**
 * 获取导航控制器容器
 * 不在导航控制器中会报错
 * Get the root view for the fragment's layout (the one returned by onCreateView), if provided.
 */
fun Fragment.findNavControllerOrNull(): NavController? {
    return try {
        NavHostFragment.findNavController(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

fun View.findNavControllerOrNull(): NavController? {
    return try {
        return this.findNavController()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

/**
 * 找到容器对应的NavHost
 */
fun View.findNavHost(): NavHost? {
    return this.findNavControllerView()?.getFragment<Fragment>() as? NavHost
}

/**
 * 找到容器对应的NavHostFragment
 */
fun View.findNavHostFragment(): NavHostFragment? {
    return this.findNavControllerView()?.getFragment<Fragment>() as? NavHostFragment
}


/**
 * 找到 容器view
 */
fun View.findNavControllerView(): FragmentContainerView? {
    return generateSequence(this) {
        it.parent as? View?
    }.mapNotNull {
        //官方打的这个Tag
        val tag = it.getTag(R.id.nav_controller_view_tag)
        if (tag == null) null else it
    }.firstOrNull() as? FragmentContainerView
}

/**
 * 结束
 * 对于DialogFragment 是dismiss
 * 对于Activity 是 finish
 */
fun View.finishNav(): Boolean {
    val findNavHostFragment = findNavHostFragment()
    return when (val lifecycle = findNavHostFragment?.parentFragment) {
        is DialogFragment -> {
            lifecycle.dismissAllowingStateLoss()
            true
        }

        is Fragment -> {
            lifecycle.requireActivity().finish()
            true
        }

        else -> {
            if (findNavHostFragment != null) {
                findNavHostFragment.requireActivity().finish()
                true
            } else {
                false
            }
        }
    }
}


/**
 * 获取回退栈数量
 */
//fun NavController.getNavCount(): Int {
//    return currentBackStack.value.size
//}
//
///**
// * 获取回退栈
// */
//fun NavController.getNavBackStack(): List<NavBackStackEntry> {
//    return currentBackStack.value
//}

/**
 * 支持更高的自定义
 */
@MainThread
fun NavController.navigateTo(
    node: NavDestination,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val method = ReflectionCache.getInstance().getMethod(
        this.javaClass, "navigate",
        NavDestination::class.java,
        Bundle::class.java,
        NavOptions::class.java,
        Navigator.Extras::class.java
    )
    method.invoke(node, args, navOptions, navigatorExtras)
}

@MainThread
@JvmName("navigateFragment")
fun <T : Fragment> NavController.navigate(
    target: Class<T>,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    this.navigateTo(
        FragmentNavigator
            .Destination(this.navigatorProvider)
            .setClassName(target.name),
        args,
        navOptions,
        navigatorExtras
    )
}

@MainThread
@JvmName("navigateActivity")
fun <T : Activity> NavController.navigate(
    target: Class<T>,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    this.navigateTo(
        ActivityNavigator
            .Destination(this.navigatorProvider)
            .setComponentName(ComponentName(this.context, target)),
        args,
        navOptions,
        navigatorExtras
    )
}

@MainThread
@JvmName("navigateDialogFragment")
fun <T : DialogFragment> NavController.navigate(
    target: Class<T>,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    this.navigateTo(
        DialogFragmentNavigator
            .Destination(this.navigatorProvider)
            .setClassName(target.name),
        args,
        navOptions,
        navigatorExtras
    )
}

/**
 * 简化参数 可以不传
 * 系统的必须传递这些参数 那怕是空 不是很友好
 */
@MainThread
@JvmName("navigateSimplifyParameters")
fun NavController.navigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    this.navigate(resId, args, navOptions, navigatorExtras)
}

