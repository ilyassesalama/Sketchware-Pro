package dev.aldi.sayuti.editor.view.item;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatButton;

import com.besome.sketch.beans.ViewBean;

import a.a.a.sy;
import a.a.a.wB;

public class ItemMaterialButton extends AppCompatButton implements sy {

    private Paint paint;
    private float paddingFactor;
    private ViewBean viewBean;
    private boolean hasSelection;
    private boolean hasFixed;
    private Drawable background;

    public ItemMaterialButton(Context context) {
        super(context);
        a(context);
    }

    @Deprecated
    private void a(Context context) {
        paddingFactor = wB.a(context, 1.0f);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x9599d5d0);
        background = getBackground();

        setDrawingCacheEnabled(true);
        setFocusable(false);
    }

    @Override
    public ViewBean getBean() {
        return viewBean;
    }

    @Override
    public void setBean(ViewBean viewBean) {
        this.viewBean = viewBean;
    }

    @Override
    public boolean getFixed() {
        return hasFixed;
    }

    public void setFixed(boolean z) {
        hasFixed = z;
    }

    public boolean getSelection() {
        return hasSelection;
    }

    @Override
    public void setSelection(boolean z) {
        hasSelection = z;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (hasSelection) {
            canvas.drawRect(new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight()), paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public void setBackgroundColor(int color) {
        if (color == 0xffffff) {
            setBackground(background);
        } else {
            super.setBackgroundColor(color);
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding((int) (left * paddingFactor), (int) (top * paddingFactor), (int) (right * paddingFactor), (int) (bottom * paddingFactor));
    }
}
