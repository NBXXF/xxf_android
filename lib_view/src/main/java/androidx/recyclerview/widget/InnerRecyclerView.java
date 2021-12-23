package androidx.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Change the package method <code>absorbGlows</code> to be protected so we could
 * override it in the subclass.
 *
 * Created by gjz on 17/11/2016.
 */

public class InnerRecyclerView extends RecyclerView {

    protected InnerRecyclerView(Context context) {
        this(context, null);
    }

    protected InnerRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected InnerRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void absorbGlows(int velocityX, int velocityY) {}
}
