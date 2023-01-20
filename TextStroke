import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class TextStroke extends androidx.appcompat.widget.AppCompatTextView{
    private int outline_color;
    private float outline_width;
    private boolean isDrawing = false;

    public TextStroke(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }
    public TextStroke(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {
        @SuppressLint({"Recycle", "CustomViewStyleable"}) TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.outline_settings);
        outline_color = a.getInteger(R.styleable.outline_settings_stroke_color, Color.WHITE);
        outline_width = a.getFloat(R.styleable.outline_settings_stroke_width, 8f);
    }


    @Override
    public void invalidate() {
        if (isDrawing){
            return;
        }
        super.invalidate();
    }

    @Override
    protected void onDraw( Canvas canvas) {
        if(outline_width > 0) {
            isDrawing = true;
            int currentTextColor = getCurrentTextColor();
            Paint p = getPaint();

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(outline_width*1.2f);
            p.setStrokeCap(Paint.Cap.ROUND);
            setTextColor(Color.GRAY);

            super.onDraw(canvas);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(outline_width*0.8f);
            p.setStrokeCap(Paint.Cap.ROUND);
            setTextColor(outline_color);

            super.onDraw(canvas);

            setTextColor(currentTextColor);
            p.setStyle(Paint.Style.FILL);
            p.setStrokeWidth(0f);
            isDrawing = false;

            super.onDraw(canvas);

        } else {
            super.onDraw(canvas);
        }
    }

}
