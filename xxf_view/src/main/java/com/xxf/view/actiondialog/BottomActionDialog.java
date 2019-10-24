package com.xxf.view.actiondialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.xxf.view.R;
import com.xxf.view.databinding.XxfDialogBottomActionBinding;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;

import java.util.List;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * 模仿ios 下部菜单
 */

public class BottomActionDialog extends AppCompatDialog {

    private BottomActionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private BottomActionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public BottomActionDialog(@NonNull Context context,
                              String title,
                              @NonNull List<? extends ItemMenu> actionItems) {
        super(context, R.style.xxf_AnimBottomDialog);
        this.actionItems = actionItems;
        this.title = title;
    }

    XxfDialogBottomActionBinding binding;
    ActionItemAdapter actionItemAdapter;
    List<? extends ItemMenu> actionItems;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = XxfDialogBottomActionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window win = getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setWindowAnimations(R.style.xxf_AnimBottomDialog);
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.BOTTOM);
        win.getAttributes().dimAmount = 0.5f;
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setCanceledOnTouchOutside(true);
        win.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        binding.dialogTitle.setText(title);
        binding.dialogTitle.setVisibility(!TextUtils.isEmpty(title) ? View.VISIBLE : View.GONE);
        binding.dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.dialogRecyclerView.setAdapter(actionItemAdapter = new ActionItemAdapter());
        actionItemAdapter.bindData(true, actionItems);

        actionItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View view, int position) {
                dismiss();
                ItemMenu itemMenu = (ItemMenu) actionItemAdapter.getItem(position);
                itemMenu.doAction();
            }
        });

        binding.tvItemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        closeKeyboard();

        super.show();
    }

    private void closeKeyboard() {
        try {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ActionItemAdapter<T extends ItemMenu> extends BaseRecyclerAdapter<T> {

        @Override
        public int bindView(int viewtype) {
            return R.layout.xxf_adapter_item_bottom_action;
        }

        @Override
        public void onBindHolder(BaseViewHolder holder, @Nullable T t, int index) {
            TextView tvActionBottomDialog = holder.obtainView(R.id.tv_item_title);
            tvActionBottomDialog.setText(t.getItemTitle());
            tvActionBottomDialog.setTextColor(t.getItemColor());
            tvActionBottomDialog.setEnabled(!t.isItemDisable());
        }
    }
}
