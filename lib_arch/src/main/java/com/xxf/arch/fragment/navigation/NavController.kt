package com.xxf.arch.fragment.navigation

import android.app.Activity
import android.content.ComponentName
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHost
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.xxf.arch.R
import com.xxf.cache.ReflectionCache


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
