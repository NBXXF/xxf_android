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

    protected AdapterStyle(int itemTitleColor, int itemTitleSelectedColor, int itemTitleDisableColor, int itemTitleTextSize, int itemDividerColor, int itemBackgroundColor) {
        this.itemTitleColor = itemTitleColor;
        this.itemTitleSelectedColor = itemTitleSelectedColor;
        this.itemTitleDisableColor = itemTitleDisableColor;
        this.itemTitleTextSize = itemTitleTextSize;
        this.itemDividerColor = itemDividerColor;
        this.itemBackgroundColor = itemBackgroundColor;
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

        public AdapterStyle build() {
            return new AdapterStyle(itemTitleColor,
                    itemTitleSelectedColor,
                    itemTitleDisableColor,
                    itemTitleTextSize,
                    itemDividerColor,
                    itemBackgroundColor);
        }
    }
}
