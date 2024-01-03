
package com.xxf.media.album;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xxf.media.album.ui.AlbumActivity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

public final class AlbumLauncher {

    private final WeakReference<FragmentActivity> mContext;


    private AlbumLauncher(Fragment fragment) {
        this(fragment.getActivity());
    }

    private AlbumLauncher(FragmentActivity activity) {
        mContext = new WeakReference<>(activity);
    }

    /**
     * Start Matisse from an Activity.
     * <p>
     * This Activity's {@link Activity#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param activity Activity instance.
     * @return Matisse instance.
     */
    public static AlbumLauncher from(FragmentActivity activity) {
        return new AlbumLauncher(activity);
    }

    /**
     * Start Matisse from a Fragment.
     * <p>
     * This Fragment's {@link Fragment#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param fragment Fragment instance.
     * @return Matisse instance.
     */
    public static AlbumLauncher from(Fragment fragment) {
        return new AlbumLauncher(fragment);
    }

    /**
     * Obtain user selected media' {@link Uri} list in the starting Activity or Fragment.
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return User selected media' {@link Uri} list.
     */
    public static List<Uri> obtainResult(Intent data) {
        return data.getParcelableArrayListExtra(AlbumActivity.EXTRA_RESULT_SELECTION);
    }

    /**
     * Obtain user selected media path list in the starting Activity or Fragment.
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return User selected media path list.
     */
    public static List<String> obtainPathResult(Intent data) {
        return data.getStringArrayListExtra(AlbumActivity.EXTRA_RESULT_SELECTION_PATH);
    }

    /**
     * Obtain state whether user decide to use selected media in original
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return Whether use original photo
     */
    public static boolean obtainOriginalState(Intent data) {
        return data.getBooleanExtra(AlbumActivity.EXTRA_RESULT_ORIGINAL_ENABLE, false);
    }

    /**
     * MIME types the selection constrains on.
     * <p>
     * Types not included in the set will still be shown in the grid but can't be chosen.
     *
     * @param mimeTypes MIME types set user can choose from.
     * @return {@link SelectionCreator} to build select specifications.
     * @see MimeType
     * @see SelectionCreator
     */
    public SelectionCreator choose(Set<MimeType> mimeTypes) {
        return this.choose(mimeTypes, true);
    }

    /**
     * MIME types the selection constrains on.
     * <p>
     * Types not included in the set will still be shown in the grid but can't be chosen.
     *
     * @param mimeTypes          MIME types set user can choose from.
     * @param mediaTypeExclusive Whether can choose images and videos at the same time during one single choosing
     *                           process. true corresponds to not being able to choose images and videos at the same
     *                           time, and false corresponds to being able to do this.
     * @return {@link SelectionCreator} to build select specifications.
     * @see MimeType
     * @see SelectionCreator
     */
    public SelectionCreator choose(Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        return new SelectionCreator(this, mimeTypes, mediaTypeExclusive);
    }

    @Nullable
    FragmentActivity getActivity() {
        return mContext.get();
    }

}
