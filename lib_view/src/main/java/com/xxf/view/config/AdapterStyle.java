package com.xxf.view.config;

/**
 * @author XGod
 * @describe 适配器UI配置
 * @date 2020/5/21 18:08
 */
public class AdapterStyle {
    int itemTitleColor;
    int itemTitleSelectedColor;
    int itemTitleDisableColor;
    int itemTitleTextSize;
    int itemDividerColor;
    int itemBackgroundColor;
    int itemSelectedBgColor;
    int itemDisableBgColor;


    protected AdapterStyle(int itemTitleColor, int itemTitleSelectedColor, int itemTitleDisableColor, int itemTitleTextSize, int itemDividerColor, int itemBackgroundColor, int itemSelectedBgColor, int itemDisableBgColor) {
        this.itemTitleColor = itemTitleColor;
        this.itemTitleSelectedColor = itemTitleSelectedColor;
        this.itemTitleDisableColor = itemTitleDisableColor;
        this.itemTitleTextSize = itemTitleTextSize;
        this.itemDividerColor = itemDividerColor;
        this.itemBackgroundColor = itemBackgroundColor;
        this.itemSelectedBgColor = itemSelectedBgColor;
        this.itemDisableBgColor = itemDisableBgColor;
    }

    protected AdapterStyle(Builder builder) {
        this.itemTitleColor = builder.getItemTitleColor();
        this.itemTitleSelectedColor = builder.getItemTitleSelectedColor();
        this.itemTitleDisableColor = builder.getItemTitleDisableColor();
        this.itemTitleTextSize = builder.getItemTitleTextSize();
        this.itemDividerColor = builder.getItemDividerColor();
        this.itemBackgroundColor = builder.getItemBackgroundColor();
        this.itemSelectedBgColor = builder.getItemSelectedBgColor();
        this.itemDisableBgColor = builder.getItemDisableBgColor();
    }

    public int getItemTitleColor() {
        return itemTitleColor;
    }

    public int getItemTitleSelectedColor() {
        return itemTitleSelectedColor;
    }

    public int getItemTitleDisableColor() {
        return itemTitleDisableColor;
    }

    public int getItemTitleTextSize() {
        return itemTitleTextSize;
    }

    public int getItemDividerColor() {
        return itemDividerColor;
    }

    public int getItemBackgroundColor() {
        return itemBackgroundColor;
    }

    public int getItemSelectedBgColor() {
        return itemSelectedBgColor;
    }

    public int getItemDisableBgColor() {
        return itemDisableBgColor;
    }

    /**
     * 建造者设计
     */
    public static class Builder {
        int itemTitleColor = 0xFF333333;
        int itemTitleSelectedColor = 0xFFEC6D56;
        int itemTitleDisableColor = 0xFFBBBBBB;
        int itemTitleTextSize = 16;//sp
        int itemDividerColor = 0xFFF2F2F2;
        int itemBackgroundColor = 0XFFFFFFFF;
        int itemSelectedBgColor = 0XFFFFFFFF;
        int itemDisableBgColor = 0xFFBBBBBB;

        public Builder setItemTitleColor(int itemTitleColor) {
            this.itemTitleColor = itemTitleColor;
            return this;
        }

        public Builder setItemTitleSelectedColor(int itemTitleSelectedColor) {
            this.itemTitleSelectedColor = itemTitleSelectedColor;
            return this;
        }

        public Builder setItemTitleDisableColor(int itemTitleDisableColor) {
            this.itemTitleDisableColor = itemTitleDisableColor;
            return this;
        }

        public Builder setItemTitleTextSize(int itemTitleTextSize) {
            this.itemTitleTextSize = itemTitleTextSize;
            return this;
        }


        public Builder setItemDividerColor(int itemDividerColor) {
            this.itemDividerColor = itemDividerColor;
            return this;
        }

        public Builder setItemBackgroundColor(int itemBackgroundColor) {
            this.itemBackgroundColor = itemBackgroundColor;
            return this;
        }

        public Builder setItemSelectedBgColor(int itemSelectedBgColor) {
            this.itemSelectedBgColor = itemSelectedBgColor;
            return this;
        }

        public Builder setItemDisableBgColor(int itemDisableBgColor) {
            this.itemDisableBgColor = itemDisableBgColor;
            return this;
        }

        public int getItemTitleColor() {
            return itemTitleColor;
        }

        public int getItemTitleSelectedColor() {
            return itemTitleSelectedColor;
        }

        public int getItemTitleDisableColor() {
            return itemTitleDisableColor;
        }

        public int getItemTitleTextSize() {
            return itemTitleTextSize;
        }

        public int getItemDividerColor() {
            return itemDividerColor;
        }

        public int getItemBackgroundColor() {
            return itemBackgroundColor;
        }

        public int getItemSelectedBgColor() {
            return itemSelectedBgColor;
        }

        public int getItemDisableBgColor() {
            return itemDisableBgColor;
        }

        public AdapterStyle build() {
            return new AdapterStyle(this);
        }
    }
}
