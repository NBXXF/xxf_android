package com.xxf.view.actiondialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xxf.arch.XXF;
import com.xxf.arch.dialog.XXFDialog;
import com.xxf.utils.DensityUtil;
import com.xxf.view.R;
import com.xxf.view.config.AdapterStyle;
import com.xxf.view.databinding.XxfAdapterItemBottomActionBinding;
import com.xxf.view.databinding.XxfDialogBottomActionBinding;
import com.xxf.view.model.ItemMenu;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.XXFViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * 模仿ios 下部菜单
 */

public class ActionSheetDialog<T> extends XXFDialog<ItemMenu<T>> {
    /**
     * 取消按钮文本
     */
    public static CharSequence CANCEL_BTN_TEXT = "Cancel";

    public static class Builder {
        private Context context;
        private CharSequence mTitle;
        private CharSequence cancelText = ActionSheetDialog.CANCEL_BTN_TEXT;
        private List<ItemMenu<String>> mItems = new ArrayList<>();
        private AdapterStyle adapterStyle = new AdapterStyle.Builder().build();

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setCancelText(@NonNull CharSequence cancelText) {
            this.cancelText = cancelText;
            return this;
        }


        public Builder setAdapterStyle(AdapterStyle adapterStyle) {
            this.adapterStyle = adapterStyle;
            return this;
        }

        public Builder setItems(String[] items) {
            return this.setItems(items, null);
        }

        public Builder setItems(String[] items, String selected) {
            return setItems(new ArrayList<String>(Arrays.asList(items)), selected);
        }

        public Builder setItems(List<String> items, String selected) {
            this.mItems.clear();
            for (String item : items) {
                this.mItems.add(new ItemMenuImpl<>(item, item, TextUtils.equals(item, selected)));
            }
            return this;
        }


        public ActionSheetDialog<String> build() {
            return new ActionSheetDialog<String>(context, mTitle, cancelText, adapterStyle, mItems, DensityUtil.getScreenHeightPx(XXF.getApplication()) / 2);
        }
    }

    public ActionSheetDialog(@NonNull Context context,
                             @Nullable CharSequence title,
                             @NonNull AdapterStyle adapterStyle,
                             @NonNull List<ItemMenu<T>> actionItems) {
        this(context, title, ActionSheetDialog.CANCEL_BTN_TEXT, adapterStyle, actionItems, DensityUtil.getScreenHeightPx(XXF.getApplication()) / 2);
    }

    /**
     * @param context
     * @param title
     * @param adapterStyle
     * @param actionItems
     * @param maxHeightForAdapter PX 适配器高度,默认不限制高度 或者0不限制高度
     */
    public ActionSheetDialog(@NonNull Context context,
                             @Nullable CharSequence title,
                             @Nullable CharSequence cancelText,
                             @NonNull AdapterStyle adapterStyle,
                             @NonNull List<? extends ItemMenu> actionItems,
                             int maxHeightForAdapter) {
        super(context, R.style.xxf_AnimBottomDialog);
        this.adapterStyle = Objects.requireNonNull(adapterStyle);
        this.actionItems = Objects.requireNonNull(actionItems);
        this.maxHeightForAdapter = maxHeightForAdapter;
        this.title = title;
        this.cancelText = cancelText;
    }

    protected XxfDialogBottomActionBinding binding;
    protected ActionItemAdapter actionItemAdapter;
    protected AdapterStyle adapterStyle;
    protected List<? extends ItemMenu> actionItems;
    protected CharSequence title;
    protected CharSequence cancelText;
    protected int maxHeightForAdapter;

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
        binding.dialogCancel.setText(cancelText);
        binding.dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.dialogRecyclerView.setMaxHeight(maxHeightForAdapter);
        binding.dialogRecyclerView.setAdapter(actionItemAdapter = new ActionItemAdapter());
        actionItemAdapter.bindData(true, actionItems);

        actionItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(XXFRecyclerAdapter adapter, XXFViewHolder holder, View itemView, int index, Object o) {
                ItemMenu<T> itemMenu = (ItemMenu) o;
                setComponentResult(itemMenu);
            }
        });

        binding.dialogCancel.setOnClickListener(new View.OnClickListener() {
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

    public void onBindHolder(XXFViewHolder holder,
                             XxfAdapterItemBottomActionBinding binding,
                             @Nullable ItemMenu t, int index) {

        TextView menuTV = binding.tvItemTitle;
        menuTV.setTextSize(adapterStyle.getItemTitleTextSize());
        if (t.isItemDisable()) {
            binding.getRoot().setBackgroundColor(adapterStyle.getItemDisableBgColor());
            menuTV.setTextColor(adapterStyle.getItemTitleDisableColor());
        } else if (t.isItemSelected()) {
            binding.getRoot().setBackgroundColor(adapterStyle.getItemSelectedBgColor());
            menuTV.setTextColor(adapterStyle.getItemTitleSelectedColor());
        } else {
            binding.getRoot().setBackgroundColor(adapterStyle.getItemBackgroundColor());
            menuTV.setTextColor(adapterStyle.getItemTitleColor());
        }
        menuTV.setEnabled(!t.isItemDisable());
        menuTV.setText(t.getItemTitle());
        binding.itemDivider.setBackgroundColor(adapterStyle.getItemDividerColor());
    }

    private class ActionItemAdapter<T extends ItemMenu> extends XXFRecyclerAdapter<XxfAdapterItemBottomActionBinding, T> {

        @Override
        protected XxfAdapterItemBottomActionBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
            return XxfAdapterItemBottomActionBinding.inflate(inflater, viewGroup, false);
        }

        @Override
        public void onBindHolder(XXFViewHolder<XxfAdapterItemBottomActionBinding, T> holder, @Nullable T t, int index) {
            ActionSheetDialog.this.onBindHolder(holder, holder.getBinding(), t, index);
        }
    }
}
