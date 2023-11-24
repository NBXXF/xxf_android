@file:Suppress("RedundantVisibilityModifier", "unused")
@file:JvmName("DialogFragmentViewBindings")

package com.xxf.viewbinding

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

/**
 * Create new [ViewBinding] associated with the [DialogFragment][this]
 */
@JvmName("viewBindingDialogFragment")
@Deprecated(
    "Use viewBinding delegate",
    ReplaceWith("viewBinding(viewBinder)", "com.xxf.viewbinding.viewBindingActivity")
)
public fun <F : DialogFragment, T : ViewBinding> DialogFragment.dialogViewBinding(
    viewBinder: (F) -> T
): ViewBindingProperty<F, T> {
    return viewBinding(viewBinder)
}

/**
 * Create new [ViewBinding] associated with the [DialogFragment][this]
 *
 * @param vbFactory Function that creates a new instance of [ViewBinding]. `MyViewBinding::bind` can be used
 */
@JvmName("viewBindingDialogFragment")
@Deprecated(
    "Use viewBinding delegate",
    ReplaceWith("viewBinding(vbFactory, viewProvider)", "com.xxf.viewbinding.viewBindingActivity")
)
public inline fun <F : DialogFragment, T : ViewBinding> DialogFragment.dialogViewBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (F) -> View
): ViewBindingProperty<F, T> {
    return viewBinding(vbFactory, viewProvider)
}

/**
 * Create new [ViewBinding] associated with the [DialogFragment][this]
 *
 * @param vbFactory Function that creates a new instance of [ViewBinding]. `MyViewBinding::bind` can be used
 * @param viewBindingRootId Id of the root view of your custom view
 */
@Suppress("unused")
@JvmName("viewBindingDialogFragment")
@Deprecated(
    "Use viewBinding delegate",
    ReplaceWith("viewBinding(vbFactory, viewBindingRootId)", "com.xxf.viewbinding.viewBindingActivity"),
)
public inline fun <T : ViewBinding> DialogFragment.dialogViewBinding(
    crossinline vbFactory: (View) -> T,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<DialogFragment, T> {
    return viewBinding(vbFactory, viewBindingRootId)
}
