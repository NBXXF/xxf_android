package com.xxf.arch.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * An EditText, which notifies when something was cut/copied/pasted inside it.
 * 
 * @author Lukas Knuth
 * @version 1.0
 */
@SuppressLint("NewApi") public class MyEditText extends androidx.appcompat.widget.AppCompatEditText implements
        MenuItem.OnMenuItemClickListener,ActionMode.Callback {
    private static final int ID_SELECTION_MODE = android.R.id.selectTextMode;
      // Selection context mode
    private static final int ID_SELECT_ALL = android.R.id.selectAll;
    private static final int ID_CUT = android.R.id.cut;
    private static final int ID_COPY = android.R.id.copy;
    private static final int ID_PASTE = android.R.id.paste;
    
    private final Context mContext;

    /*
     * Just the constructors to create a new EditText...
     */
    public MyEditText(Context context) {
        super(context);
        this.mContext = context;
        this.setCustomSelectionActionModeCallback(this);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setCustomSelectionActionModeCallback(this);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
       this.setCustomSelectionActionModeCallback(this);
       this.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
           @Override
           public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

           }
       });
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        //代码效果，有弹出框选择 粘贴，复制，剪切，类似qq效果.....
        menu.add(0, ID_PASTE, 0, "粘贴").setOnMenuItemClickListener(this);
        menu.add(0, ID_CUT, 1, "剪切").setOnMenuItemClickListener(this);
        menu.add(0, ID_COPY, 1, "复制").setOnMenuItemClickListener(this);
        menu.add(0, ID_SELECT_ALL, 1, "全选").setOnMenuItemClickListener(this);
        super.onCreateContextMenu(menu);
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        return onTextContextMenuItem(item.getItemId());
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        // Do your thing:
        boolean consumed = super.onTextContextMenuItem(id);
        // React:
        switch (id) {
        case android.R.id.cut:
            onTextCut();
            break;
        case android.R.id.paste:
            onTextPaste();
            break;
        case android.R.id.copy:
            onTextCopy();
        }
        return consumed;
    }

    /**
     * Text was cut from this EditText.
     */
    public void onTextCut() {
        Toast.makeText(mContext, "Cut!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Text was copied from this EditText.
     */
    public void onTextCopy() {
        Toast.makeText(mContext, "Copy!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Text was pasted into the EditText.
     */
    public void onTextPaste() {
        Toast.makeText(mContext, "Paste!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        menu.removeItem(android.R.id.cut);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.removeItem(android.R.id.cut);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}