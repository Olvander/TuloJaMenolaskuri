package fi.tuni.tiko.tulojamenolaskuri;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BarDrawableView extends View {

    private ShapeDrawable mBarDrawable;

    public BarDrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public ShapeDrawable getBarDrawable() {
        return this.mBarDrawable;
    }
}
