package kristoffer.com.pointwisetoy.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Kristoffer on 2/17/16.
 */
public class DrawingView extends View {
    private Path path;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private boolean mShouldClean;

    public DrawingView(Context context, Paint paint) {
        super(context);
        path = new Path();
        mBitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = paint;
        this.setBackgroundColor(Color.WHITE);
    }

    private ArrayList<PathWithPaint> _graphics1 = new ArrayList<PathWithPaint>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PathWithPaint pp = new PathWithPaint();
        mCanvas.drawPath(path, mPaint);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            path.moveTo(event.getX(), event.getY());
            path.lineTo(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path.lineTo(event.getX(), event.getY());
            pp.setPath(path);
            pp.setmPaint(mPaint);
            _graphics1.add(pp);
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mShouldClean) {
            canvas.drawColor(Color.WHITE);
            _graphics1.clear();
            mCanvas = new Canvas(mBitmap);
            path = new Path();
            mShouldClean = false;
        } else {
            super.onDraw(canvas);
            if (_graphics1.size() > 0) {
                canvas.drawPath(
                        _graphics1.get(_graphics1.size() - 1).getPath(),
                        _graphics1.get(_graphics1.size() - 1).getmPaint());
            }
        }
    }

    public Path getPath() {
        if (_graphics1.size() > 0) {
            return _graphics1.get(_graphics1.size() - 1).getPath();
        } else {
            return null;
        }
    }

    public void cleanDrawings() {
        mShouldClean = true;
        this.invalidate();
    }
}
