package com.xxf.ktx

import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.getSupportFragmentManager

/*********************FragmentManager******************/
fun FragmentManager.findDialogFragments(): List<DialogFragment> {
    return this.fragments.filter { it ->
        (it is DialogFragment) && it.showsDialog
    }.map { it as DialogFragment }
}

@JvmName("findDialogFragmentsByClass")
inline fun <reified T : DialogFragment> FragmentManager.findDialogFragments(): List<T> {
    return this.fragments.filter { it ->
        (it is DialogFragment) && it.showsDialog && it.javaClass == T::class.java
    }.map { it as T }
}

fun FragmentManager.findDialogFragment(tag: String): DialogFragment? {
    return this.findFragmentByTag(tag) as? DialogFragment
}

fun FragmentManager.findDialogFragment(@IdRes id: Int): DialogFragment? {
    return this.findFragmentById(id) as? DialogFragment
}


/*********************Fragment******************/

fun Fragment.findDialogFragments(): List<DialogFragment> {
    return this.childFragmentManager.findDialogFragments()
}

@JvmName("findDialogFragmentsByClass")
inline fun <reified T : DialogFragment> Fragment.findDialogFragments(): List<T> {
    return this.childFragmentManager.findDialogFragments<T>()
}

fun Fragment.findDialogFragment(tag: String): DialogFragment? {
    return this.childFragmentManager.findDialogFragment(tag)
}

fun Fragment.findDialogFragment(@IdRes id: Int): DialogFragment? {
    return this.childFragmentManager.findDialogFragment(id)
}


/*********************FragmentActivity******************/
fun FragmentActivity.findDialogFragments(): List<DialogFragment> {
    return this.supportFragmentManager.findDialogFragments()
}

@JvmName("findDialogFragmentsByClass")
inline fun <reified T : DialogFragment> FragmentActivity.findDialogFragments(): List<T> {
    return this.supportFragmentManager.findDialogFragments<T>()
}

fun FragmentActivity.findDialogFragment(tag: String): DialogFragment? {
    return this.supportFragmentManager.findDialogFragment(tag)
}

fun FragmentActivity.findDialogFragment(@IdRes id: Int): DialogFragment? {
    return this.supportFragmentManager.findDialogFragment(id)
}


/*********************批量取消******************/

fun <T : DialogFragment> List<T>.dismiss() {
    this.onEach {
        it.dismiss()
    }
}

fun <T : DialogFragment> List<T>.dismissNow() {
    this.onEach {
        it.dismissNow()
    }
}

fun <T : DialogFragment> List<T>.dismissAllowingStateLoss() {
    this.onEach {
        it.dismissAllowingStateLoss()
    }
}

/*********************安全show防范闪退******************/

/**
 * 避免java.lang.IllegalStateException
 */
fun <T : DialogFragment> T.showAllowingStateLoss(
    transaction: FragmentTransaction,
    tag: String?
): Int {
    val manager: FragmentManager? = transaction.getSupportFragmentManager()
    if (manager != null) {
        /**
         * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
         */
        if (manager.isStateSaved) {
            return -1
        }
        /**
         * java.lang.IllegalStateException: Fragment already added
         */
        manager.findFragmentByTag(tag)?.let {
            manager.beginTransaction()
                .remove(it)
                .commitNowAllowingStateLoss()
        }
        try {
            manager.executePendingTransactions()
        } catch (_: Throwable) {
        }
    }
    if (this.isAdded) {
        return -1
    }
    return this.show(transaction, tag)
}


/**
 * 避免java.lang.IllegalStateException
 */
fun <T : DialogFragment> T.showAllowingStateLoss(manager: FragmentManager, tag: String?) {
    /**
     * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
     */
    if (manager.isStateSaved) {
        return
    }
    /**
     * java.lang.IllegalStateException: Fragment already added
     */
    manager.findFragmentByTag(tag)?.let {
        manager.beginTransaction()
            .remove(it)
            .commitNowAllowingStateLoss()
    }
    try {
        manager.executePendingTransactions()
    } catch (_: Throwable) {
    }
    if (this.isAdded) {
        return
    }
    this.show(manager, tag)
}

/**
 * 避免java.lang.IllegalStateException
 */
fun <T : DialogFragment> T.showNowAllowingStateLoss(manager: FragmentManager, tag: String?) {
    /**
     * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
     */
    if (manager.isStateSaved) {
        return
    }
    /**
     * java.lang.IllegalStateException: Fragment already added
     */
    manager.findFragmentByTag(tag)?.let {
        manager.beginTransaction()
            .remove(it)
            .commitNowAllowingStateLoss()
    }
    try {
        manager.executePendingTransactions()
    } catch (_: Throwable) {
    }
    if (this.isAdded) {
        return
    }
    this.showNow(manager, tag)
}
