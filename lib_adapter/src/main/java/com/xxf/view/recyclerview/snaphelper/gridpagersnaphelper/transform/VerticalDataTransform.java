package com.xxf.view.recyclerview.snaphelper.gridpagersnaphelper.transform;



public class VerticalDataTransform<T> extends AbsRowDataTransform<T> {

    public VerticalDataTransform(int row, int column) {
        super(row, column);
    }

    @Override
    protected int transformIndex(int index, int row, int column) {
        return index;
    }
}
