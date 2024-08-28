import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author XXF
 * @version 1.0
 * @since 2024/8/28 18:23
 */
fun FragmentManager.removeAllFragment() {
    beginTransaction().apply {
        fragments.forEach {
            remove(it)
        }
    }.commitNowAllowingStateLoss()
}

fun FragmentManager.removeFragment(vararg fragments: Fragment) {
    beginTransaction().apply {
        fragments.forEach {
            remove(it)
        }
    }.commitNowAllowingStateLoss()
}
