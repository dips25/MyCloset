package my.closet.fashion.style.customs;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by kingty on 16/1/29.
 */
@SuppressLint("AppCompatCustomView")
public class ZoomImageView extends ImageView implements IAttacher{
	Attacher mAttacher;
	public ZoomImageView(Context context) {
		super(context);
		mAttacher = new Attacher(this);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mAttacher = new Attacher(this);
	}

	public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mAttacher = new Attacher(this);
	}

	@Override
	public void draw(Canvas canvas) {
		mAttacher.callOnDrawBeforeSuper(canvas);
		super.draw(canvas);
	}

	@Override
	public void setOriginalHeight(float originHeight) {
		mAttacher.setOriginalHeight(originHeight);
	}

	@Override
	public void setOriginalWidth(float originWidth) {
		mAttacher.setOriginalWidth(originWidth);
	}

	@Override
	public ValueAnimator zoomInAnimation(ViewGroup viewGroup, CallBack<Float> callBack) {
		return mAttacher.zoomInAnimation(viewGroup,callBack);
	}

	@Override
	public ValueAnimator zoomOutAnimation(ViewGroup viewGroup, CallBack<Float> callBack) {
		return mAttacher.zoomOutAnimation(viewGroup,callBack);
	}

	@Override
	public String zoomAnimationKey() {
		return mAttacher.zoomAnimationKey();
	}

	@Override
	public void setZoomAnimationKey(String key) {

		mAttacher.setZoomAnimationKey(key);
	}

}
