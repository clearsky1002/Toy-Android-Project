package kristoffer.com.pointwisetoy.CustomView;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Kristoffer on 2/17/16.
 */
public class PathWithPaint {
    private Path path;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    private Paint mPaint;

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }
}
