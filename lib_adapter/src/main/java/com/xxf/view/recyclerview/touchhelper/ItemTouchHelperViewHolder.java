package com.xxf.view.recyclerview.touchhelper;


import androidx.recyclerview.widget.ItemTouchHelper;


public interface ItemTouchHelperViewHolder {

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     * <p>
     * 震动效果
     * <uses-permission android:name="android.permission.VIBRATE" />
     * try {
     * //获取系统震动服务
     * Vibrator vib = (Vibrator) viewHolder.itemView.getContext().getSystemService(Service.VIBRATOR_SERVICE);
     * //震动70毫秒
     * vib.vibrate(70);
     * } catch (Throwable e) {
     * e.printStackTrace();
     * }
     */
    void onItemTouchSelected();


    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemTouchClear();
}
