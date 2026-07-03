package androidx.fragment.app

/**
 * 暴漏fragmentFragmentManager
 */
fun <T : FragmentTransaction> T.getSupportFragmentManager(): FragmentManager? {
    if (this is BackStackRecord) {
        return this.mManager
    }
    return null
}