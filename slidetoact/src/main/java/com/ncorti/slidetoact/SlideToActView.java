package com.ncorti.slidetoact;

import com.ncorti.slidetoact.utils.AnimationUtils;
import com.ncorti.slidetoact.utils.LogUtil;
import com.ncorti.slidetoact.utils.SlideToActIconUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.VectorElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;
import ohos.vibrator.agent.VibratorAgent;

/**
 *  Class representing the custom view, SlideToActView.
 *
 *  <p>SlideToActView is an elegant material designed slider, that enrich your app
 *  with a "Slide-to-unlock" like widget.</p>
 */
public class SlideToActView extends Component
        implements
        Component.LayoutRefreshedListener,
        Component.DrawTask,
        Component.TouchEventListener,
        Component.EstimateSizeListener {

    private static final String TAG = SlideToActView.class.getSimpleName();

    /* -------------------- LAYOUT BOUNDS -------------------- */
    private static final float DESIRED_SLIDER_HEIGHT_VP = 72F;
    private static final float DESIRED_SLIDER_WIDTH_VP = 280F;

    /* -------------------- MEMBERS -------------------- */

    /**
     * Slider width and height.
     */
    private int sliderHeight = 0;
    private int sliderWidth = 0;

    /**
     * Text message.
     */
    private CharSequence text = "SlideToActView";

    /**
     * Border Radius, default to mAreaHeight/2, -1 when not initialized.
     */
    private int borderRadius = -1;

    /**
     * Outer color used by the slider (primary).
     */
    private Color outerColor;

    /**
     * Inner color used by the slider (secondary, icon and border).
     */
    private Color innerColor;

    /**
     * Text color.
     */
    private Color textColor;

    /**
     * Custom Icon color.
     */
    private Color iconColor = new Color(getContext().getColor(ResourceTable.Color_slidetoact_defaultAccent));

    /**
     * Public flag to reverse the slider by 180 degree.
     */
    private boolean sliderReversed = false;

    /**
     * Public flag to lock the rotation icon.
     */
    private boolean isRotateIcon = true;

    /**
     * Public flag to lock the slider.
     */
    private boolean sliderLocked = false;

    /**
     * Public flag to enable complete animation.
     */
    private boolean isAnimateCompletion = true;

    /**
     * Private size for the text message.
     */
    private int textSize = 48;

    /**
     * Margin for Icon.
     */
    private int iconMargin = 28;

    /**
     * Margin of the cursor from the outer area.
     */
    private int areaMargin = 24;

    /**
     * Duration of the complete and reset animation (in milliseconds).
     */
    private long animationDuration = 300;

    /**
     * Duration of vibration after bumping to the end point.
     */
    private long bumpVibration = 0L;

    /**
     * Custom Slider Icon.
     */
    private Element sliderIcon;

    /**
     * Custom Complete Icon.
     */
    private Element completeIcon;

    /* -------------------- REQUIRED FIELDS -------------------- */
    /**
     * Grace value, when mPositionPerc > mGraceValue slider will perform the 'complete' operations.
     */
    private static final float GRACE_VALUE = 0.8F;

    /**
     * Inner rectangle (used for arrow rotation).
     */
    private RectF mInnerRect;

    /**
     * Outer rectangle (used for area drawing).
     */
    private RectF mOuterRect;

    /**
     * Height of the drawing area.
     */
    private int mAreaHeight = 0;
    /**
     * Width of the drawing area.
     */
    private int mAreaWidth = 0;
    /**
     * Actual Width of the drawing area, used for animations.
     */
    private int mActualAreaWidth = 0;

    /**
     * Arrow vector element.
     */
    private Element mDrawableArrow;

    /**
     * Tick vector element.
     */
    private Element mDrawableTick;

    private boolean mFlagDrawTick = false;

    /**
     * Margin for Arrow Icon.
     */
    private int mArrowMargin;

    /**
     * Margin for Tick Icon.
     */
    private int mTickMargin;

    /**
     * Slider cursor effective position. This is used to handle the `reversed` scenario.
     */
    private int mEffectivePosition = 0;

    /**
     * Slider cursor position (between 0 and (`mAreaWidth - mAreaHeight)).
     */
    private int mPosn = 0;

    /**
     * Slider cursor position in percentage (between 0f and 1f).
     */
    private float mPositionPerc = 0f;

    /**
     * 1/mPositionPerc.
     */
    private float mPositionPercInv = 1f;

    /**
     * Positioning of text.
     */
    private float mTextYposition = -1f;
    private float mTextXposition = -1f;

    /**
     * Flag to understand if user is moving the slider cursor.
     */
    private boolean mFlagMoving = false;

    /**
     * Last X coordinate for the touch event.
     */
    private float mLastX = 0F;

    /**
     * Private flag to check if the slide gesture have been completed.
     */
    private boolean mIsCompleted = false;

    /**
     * Margin of the cursor from the outer area.
     */
    private int mActualAreaMargin;

    /* -------------------- Interfaces -------------------- */
    /**
     * Public Slide event listeners.
     */
    private OnSlideToActAnimationEventListener onSlideToActAnimationEventListener = null;
    private OnSlideCompleteListener onSlideCompleteListener = null;
    private OnSlideResetListener onSlideResetListener = null;
    private OnSlideUserFailedListener onSlideUserFailedListener = null;

    /* -------------------- PAINT & DRAW -------------------- */
    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    public SlideToActView(Context context) {
        super(context);
        init(null);
    }

    public SlideToActView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(attrSet);
    }

    public SlideToActView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(attrSet);
    }

    private void init(AttrSet attrSet) {
        LogUtil.info(TAG, "Init method called");

        Color defaultOuter = new Color(getContext().getColor(ResourceTable.Color_slidetoact_defaultAccent));
        Color defaultWhite = new Color(getContext().getColor(ResourceTable.Color_slidetoact_white));

        sliderHeight = AttrHelper.vp2px(DESIRED_SLIDER_HEIGHT_VP, getContext());
        sliderWidth = AttrHelper.vp2px(DESIRED_SLIDER_WIDTH_VP, getContext());

        LogUtil.info(TAG, "Init method mDesiredSliderHeight: " + sliderHeight
                + " mDesiredSliderWidth: " + sliderWidth);

        Optional<Attr> attr;
        if (attrSet != null) {
            attr = attrSet.getAttr(Attribute.SLIDER_HEIGHT);
            sliderHeight = attr.map(Attr::getDimensionValue).orElse(sliderHeight);

            attr = attrSet.getAttr(Attribute.BORDER_RADIUS);
            borderRadius = attr.map(Attr::getDimensionValue).orElse(-1);

            attr = attrSet.getAttr(Attribute.OUTER_COLOR);
            outerColor = attr.map(Attr::getColorValue).orElse(defaultOuter);

            attr = attrSet.getAttr(Attribute.INNER_COLOR);
            innerColor = attr.map(Attr::getColorValue).orElse(defaultWhite);

            // For text color, check if the `text_color` is set.
            // if not the `inner_color` is set.
            attr = attrSet.getAttr(Attribute.TEXT_COLOR);
            textColor = attr.map(Attr::getColorValue).orElse(innerColor);

            attr = attrSet.getAttr(Attribute.TEXT);
            text = attr.map(Attr::getStringValue).orElse("SlideToActView");

            attr = attrSet.getAttr(Attribute.TEXT_SIZE);
            textSize = attr.map(Attr::getDimensionValue)
                    .orElse(AttrHelper.convertDimensionToPix(getContext(), "16fp", 48));

            attr = attrSet.getAttr(Attribute.SLIDER_LOCKED);
            sliderLocked = attr.map(Attr::getBoolValue).orElse(false);

            attr = attrSet.getAttr(Attribute.SLIDER_REVERSED);
            sliderReversed = attr.map(Attr::getBoolValue).orElse(false);

            attr = attrSet.getAttr(Attribute.ROTATE_ICON);
            isRotateIcon = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ANIMATE_COMPLETION);
            isAnimateCompletion = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ANIMATION_DURATION);
            animationDuration = attr.map(Attr::getLongValue).orElse(300L);

            attr = attrSet.getAttr(Attribute.BUMP_VIBRATION);
            bumpVibration = attr.map(Attr::getLongValue).orElse(0L);

            attr = attrSet.getAttr(Attribute.AREA_MARGIN);
            areaMargin = attr.map(Attr::getDimensionValue)
                    .orElse(AttrHelper.convertDimensionToPix(getContext(), "8vp", 24));

            sliderIcon = attrSet.getAttr(Attribute.SLIDER_ICON).isPresent()
                    ? attrSet.getAttr(Attribute.SLIDER_ICON).get().getElement()
                    : new VectorElement(getContext(), ResourceTable.Graphic_slidetoact_ic_arrow);

            // For icon color. check if the `slide_icon_color` is set.
            // if not the `outer_color` is set.
            attr = attrSet.getAttr(Attribute.SLIDER_ICON_COLOR);
            iconColor = attr.map(Attr::getColorValue).orElse(outerColor);

            completeIcon = attrSet.getAttr(Attribute.COMPLETE_ICON).isPresent()
                    ? attrSet.getAttr(Attribute.COMPLETE_ICON).get().getElement()
                    : new VectorElement(getContext(), ResourceTable.Graphic_slidetoact_ic_check);

            attr = attrSet.getAttr(Attribute.ICON_MARGIN);
            iconMargin = attr.map(Attr::getDimensionValue)
                    .orElse(AttrHelper.convertDimensionToPix(getContext(), "16vp", 28));
        }

        mArrowMargin = iconMargin;
        mTickMargin = iconMargin;

        mActualAreaMargin = areaMargin;

        mDrawableArrow = sliderIcon;
        mDrawableTick = completeIcon;

        SlideToActIconUtil.setIconColor(mDrawableArrow, iconColor);

        mOuterRect = new RectF(
                mActualAreaWidth,
                0f,
                mAreaWidth - mActualAreaWidth * 1f,
                mAreaHeight
        );

        mInnerRect = new RectF(
                (mActualAreaMargin + mEffectivePosition),
                mActualAreaMargin,
                (mAreaHeight + mEffectivePosition) - mActualAreaMargin * 1f,
                mAreaHeight - mActualAreaMargin * 1f
        );

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(outerColor);

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(innerColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);

        setLayoutRefreshedListener(this);
        setEstimateSizeListener(this);
        setTouchEventListener(this);
        addDrawTask(this);
    }

    @Override
    public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
        LogUtil.info(TAG, "onEstimateSize method called");
        int width = measureDimension(sliderWidth, widthEstimateConfig);
        int height = measureDimension(sliderHeight, heightEstimateConfig);

        LogUtil.info(TAG, "onEstimateSize method Width: " + width + " Height: " + height);
        //Do Size Estimation here and don't forgot to call setEstimatedSize(width, height)
        setEstimatedSize(EstimateSpec.getSizeWithMode(width, EstimateSpec.PRECISE),
                EstimateSpec.getSizeWithMode(height, EstimateSpec.PRECISE));

        return true;
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = EstimateSpec.getMode(measureSpec);
        int specSize = EstimateSpec.getSize(measureSpec);
        if (specMode == EstimateSpec.PRECISE) {
            result = specSize;
        } else {
            result = defaultSize; // UNSPECIFIED
            if (specMode == EstimateSpec.NOT_EXCEED) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        LogUtil.info(TAG, "onDraw method called");
        if (canvas == null) {
            return;
        }

        //Draw outer area
        mOuterRect.set(
                new RectF(
                        mActualAreaWidth,
                        0f,
                        mAreaWidth - mActualAreaWidth * 1f,
                        mAreaHeight
                )
        );

        canvas.drawRoundRect(
                mOuterRect,
                borderRadius,
                borderRadius,
                mOuterPaint
        );

        String textToDraw;
        if (mPositionPercInv == 0) {
            textToDraw = "";
        } else {
            textToDraw = text.toString();
        }

        canvas.drawText(
                mTextPaint,
                textToDraw,
                mTextXposition,
                mTextYposition
        );

        //draw inner view
        // ratio is used to compute the proper border radius for the inner rect (see #8).
        float ratio = (mAreaHeight - 2.0f * mActualAreaMargin) / mAreaHeight;

        mInnerRect.set(
                new RectF(
                        (mActualAreaMargin + mEffectivePosition),
                        mActualAreaMargin,
                        (mAreaHeight + mEffectivePosition) - mActualAreaMargin * 1f,
                        mAreaHeight - mActualAreaMargin * 1f

                )
        );

        canvas.drawRoundRect(
                mInnerRect,
                borderRadius * ratio,
                borderRadius * ratio,
                mInnerPaint
        );

        // Arrow angle
        // We compute the rotation of the arrow and we apply .rotate transformation on the canvas.
        canvas.save();

        if (sliderReversed) {
            canvas.scale(-1F, 1F, mInnerRect.centerX(), mInnerRect.centerY());
        }

        if (isRotateIcon) {
            //Current angle for Arrow Icon.
            float arrowAngle = -180 * mPositionPerc;
            canvas.rotate(arrowAngle, mInnerRect.centerX(), mInnerRect.centerY());
            LogUtil.info(TAG, "mArrowAngle: " + arrowAngle + " mPositionPerc:" + mPositionPerc);
        }

        mDrawableArrow.setBounds(
                (int) mInnerRect.left + mArrowMargin,
                (int) mInnerRect.top + mArrowMargin,
                (int) mInnerRect.right - mArrowMargin,
                (int) mInnerRect.bottom - mArrowMargin
        );

        if (mDrawableArrow.getBounds().left <= mDrawableArrow.getBounds().right
                && mDrawableArrow.getBounds().top <= mDrawableArrow.getBounds().bottom
        ) {
            mDrawableArrow.drawToCanvas(canvas);
        }
        canvas.restore();

        // Tick drawing
        mDrawableTick.setBounds(
                mActualAreaWidth + mTickMargin,
                mTickMargin,
                mAreaWidth - mTickMargin - mActualAreaWidth,
                mAreaHeight - mTickMargin
        );

        SlideToActIconUtil.setIconColor(mDrawableTick, innerColor);

        LogUtil.info(TAG, "mFlagDrawTick: " + mFlagDrawTick
                + " mActualAreaWidth: " + mActualAreaWidth
                + " mTickMargin: " + mTickMargin
                + " mAreaWidth: " + mAreaWidth
                + " mAreaHeight: " + mAreaHeight
                + " mPositionPercInv: " + mPositionPercInv);

        if (mFlagDrawTick) {
            mDrawableTick.drawToCanvas(canvas);
        }
    }

    @Override
    public void onRefreshed(Component component) {
        LogUtil.info(TAG, "onRefreshed method called");
        int w = component.getWidth();
        int h = component.getHeight();
        mAreaWidth = w;
        mAreaHeight = h;

        LogUtil.info(TAG, "onRefreshed method mAreaWidth: " + mAreaWidth + " mAreaHeight: " + mAreaHeight);

        if (borderRadius == -1) {
            // Round if not set up
            borderRadius = h / 2;
        }

        // Text horizontal/vertical positioning (both centered)
        mTextXposition = mAreaWidth / 3.0f;
        mTextYposition = (mAreaHeight / 2.0f)
                - (mTextPaint.descent() + mTextPaint.ascent()) / 2;

        invalidate();
        // Make sure the position is recomputed.
        updatePosition(mPosn);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (touchEvent != null && isEnabled()) {
            float x = touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
            float y = touchEvent.getPointerPosition(touchEvent.getIndex()).getY();

            LogUtil.info(TAG, "onTouchEvent method x: " + x + " y: " + y);

            switch (touchEvent.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN: {
                    eventPointDown(x, y);
                }
                break;
                case TouchEvent.PRIMARY_POINT_UP: {
                    eventPointUp();
                }
                break;
                case TouchEvent.POINT_MOVE: {
                    eventPointMove(x);
                }
                break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    private void eventPointDown(float x, float y) {
        if (checkInsideButton(x, y)) {
            mFlagMoving = true;
            mLastX = x;
        } else {
            // Clicking outside the area -> User failed, notify the listener.
            if (onSlideUserFailedListener != null) {
                onSlideUserFailedListener.onSlideFailed(this, true);
            }
        }
    }

    private void eventPointUp() {
        if ((mPosn > 0 && sliderLocked)
                || (mPosn > 0 && mPositionPerc < GRACE_VALUE)
        ) {
            // Check for grace value
            AnimatorValue positionAnimator = new AnimatorValue();
            positionAnimator.setDuration(animationDuration);
            positionAnimator.setValueUpdateListener((animatorValue, v) -> {
                float value = AnimationUtils.getAnimatedValue(v, mPosn, 0);
                updatePosition((int) value);
                invalidate();
            });
            positionAnimator.start();
        } else if (mPosn > 0 && mPositionPerc >= GRACE_VALUE) {
            setEnabled(false); // Fully disable touch events
            startAnimationComplete();
        } else if (mFlagMoving && mPosn == 0  && onSlideUserFailedListener != null) {
            // mFlagMoving == true means user successfully grabbed the slider,
            // but mPosition == 0 means that the slider is released at the beginning
            // so either a Tap or the user slided back.

            onSlideUserFailedListener.onSlideFailed(this, false);
        }
        mFlagMoving = false;
    }

    private void eventPointMove(float x) {
        if (mFlagMoving) {
            // True if the cursor was not at the end position before this event
            boolean wasIncomplete = mPositionPerc < 1f;

            float diffX = x - mLastX;
            mLastX = x;
            increasePosition((int) diffX);
            invalidate();

            // If this event brought the cursor to the end position, we can vibrate
            if (bumpVibration > 0 && wasIncomplete && mPositionPerc == 1f) {
                handleVibration();
            }
        }
    }

    /**
     * Private method to check if user has touched the slider cursor.
     *
     * @param x The x coordinate of the touch event
     * @param y The y coordinate of the touch event
     * @return A boolean that informs if user has pressed or not
     */
    private boolean checkInsideButton(float x, float y) {
        return (
                0 < y
                        && y < mAreaHeight
                        && mEffectivePosition < x
                        && x < (mAreaHeight + mEffectivePosition)
            );
    }

    /**
     * Private method for increasing/decreasing the position
     * Ensure that position never exits from its range [0, (mAreaWidth - mAreaHeight)].
     *
     * <p>Please note that the increment is inverted in case of a reversed slider.
     *
     * @param inc Increment to be performed (negative if it's a decrement)
     */
    private void increasePosition(int inc) {
        LogUtil.info(TAG, "increasePosition Before mPosition: " + mPosn + " And Diff: " + inc);
        if (sliderReversed) {
            updatePosition(mPosn - inc);
        } else {
            updatePosition(mPosn + inc);
        }
        LogUtil.info(TAG, "increasePosition After mPosition: " + mPosn);
        if (mPosn < 0) {
            updatePosition(0);
        }
        if (mPosn > (mAreaWidth - mAreaHeight)) {
            updatePosition(mAreaWidth - mAreaHeight);
        }
    }

    private void updatePosition(int position) {
        this.mPosn = position;
        if (mAreaWidth - mAreaHeight == 0) {
            // Avoid 0 division
            mPositionPerc = 0f;
            mPositionPercInv = 1f;
            return;
        }
        mPositionPerc = position * 1f / (mAreaWidth - mAreaHeight);
        mPositionPercInv = 1 - position * 1f / (mAreaWidth - mAreaHeight);

        LogUtil.info(TAG, "UpdatePosition position: " + position + " mPositionPerc: " + mPositionPerc);
        updateEffectivePosition(mPosn);
    }

    private void updateEffectivePosition(int effectivePosition) {
        if (sliderReversed) {
            this.mEffectivePosition = (mAreaWidth - mAreaHeight) - effectivePosition;
        } else {
            this.mEffectivePosition = effectivePosition;
        }
    }

    /**
     * Private method that is performed when user completes the slide.
     */
    private void startAnimationComplete() {

        // Animator that moves the cursor
        AnimatorValue finalPositionAnimator = new AnimatorValue();
        finalPositionAnimator.setValueUpdateListener((animatorValue, v) -> {
            float value = AnimationUtils.getAnimatedValue(v, mPosn, mAreaWidth - mAreaHeight);
            updatePosition((int) value);
            invalidate();
        });

        // Animator that bounce away the cursors
        AnimatorValue marginAnimator = new AnimatorValue();
        marginAnimator.setValueUpdateListener((animatorValue, v) -> {
            float value = AnimationUtils.getAnimatedValue(
                    v,
                    mActualAreaMargin,
                    (int) ((mInnerRect.width() / 2) + mActualAreaMargin));
            mActualAreaMargin = (int) value;
            invalidate();
        });
        marginAnimator.setCurveType(Animator.CurveType.ANTICIPATE_OVERSHOOT);

        // Animator that reduces the outer area (to right)
        AnimatorValue areaAnimator = new AnimatorValue();
        areaAnimator.setValueUpdateListener((animatorValue, v) -> {
            float value = AnimationUtils.getAnimatedValue(v, 0, (mAreaWidth - mAreaHeight) / 2);
            mActualAreaWidth = (int) value;
            invalidate();
        });

        final boolean[] startedOnce = {false};
        AnimatorValue.ValueUpdateListener tickListener = (animatorValue, v) -> {
            if (!mFlagDrawTick) {
                LogUtil.info(TAG, "Tick listner call from SlideToActView");
                mFlagDrawTick = true;
                mTickMargin = iconMargin;
            }

            if (!startedOnce[0]) {
                invalidate();
                startedOnce[0] = true;
            }
        };

        AnimatorValue tickAnimator = SlideToActIconUtil.createIconAnimator(tickListener);

        List<Animator> animators = new ArrayList<>();
        if (mPosn < mAreaWidth - mAreaHeight) {
            animators.add(finalPositionAnimator);
        }

        if (isAnimateCompletion) {
            animators.add(marginAnimator);
            animators.add(areaAnimator);
            animators.add(tickAnimator);
        }

        AnimatorGroup animSet = new AnimatorGroup();

        animSet.runSerially(animators.toArray(new Animator[animators.size()]));

        animSet.setDuration(animationDuration);

        animSet.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
                if (onSlideToActAnimationEventListener != null) {
                    onSlideToActAnimationEventListener
                            .onSlideCompleteAnimationStarted(SlideToActView.this, mPositionPerc);
                }
            }

            @Override
            public void onStop(Animator animator) {
                // Do nothing
            }

            @Override
            public void onCancel(Animator animator) {
                // Do nothing
            }

            @Override
            public void onEnd(Animator animator) {
                mIsCompleted = true;
                if (onSlideToActAnimationEventListener != null) {
                    onSlideToActAnimationEventListener.onSlideCompleteAnimationEnded(SlideToActView.this);
                }

                if (onSlideCompleteListener != null) {
                    onSlideCompleteListener.onSlideComplete(SlideToActView.this);
                }
            }

            @Override
            public void onPause(Animator animator) {
                // Do nothing
            }

            @Override
            public void onResume(Animator animator) {
                // Do nothing
            }
        });
        animSet.start();
    }

    /**
     * Private method that is performed when you want to reset the cursor.
     */
    private void startAnimationReset() {
        mIsCompleted = false;
        AnimatorGroup animSet = new AnimatorGroup();

        // Animator that reduces the tick size
        AnimatorValue tickAnimator = new AnimatorValue();
        tickAnimator.setValueUpdateListener((animatorValue, v) -> {
            mTickMargin = (int) v;
            invalidate();
        });

        // Animator that enlarges the outer area
        AnimatorValue areaAnimator = new AnimatorValue();
        areaAnimator.setValueUpdateListener((animatorValue, v) -> {
            float value = AnimationUtils.getAnimatedValue(v, mActualAreaWidth, 0);
            // Now we can hide the tick till the next complete
            mFlagDrawTick = false;
            mActualAreaWidth = (int) value;
            invalidate();
        });

        AnimatorValue positionAnimator = new AnimatorValue();
        positionAnimator.setValueUpdateListener((animatorValue, v) -> {
            float value = AnimationUtils.getAnimatedValue(v, mPosn, 0);
            updatePosition((int) value);
            invalidate();
        });

        // Animator that re-draw the cursors
        AnimatorValue marginAnimator = new AnimatorValue();
        marginAnimator.setValueUpdateListener((animatorValue, v) -> {
            float value = AnimationUtils.getAnimatedValue(v, mActualAreaMargin, areaMargin);
            mActualAreaMargin = (int) value;
            invalidate();
        });
        marginAnimator.setCurveType(Animator.CurveType.ANTICIPATE_OVERSHOOT);

        // Animator that makes the arrow appear
        AnimatorValue arrowAnimator = new AnimatorValue();
        arrowAnimator.setValueUpdateListener((animatorValue, v) -> {
            float value = AnimationUtils.getAnimatedValue(v, mArrowMargin, iconMargin);
            mArrowMargin = (int) value;
            invalidate();
        });
        marginAnimator.setCurveType(Animator.CurveType.OVERSHOOT);

        if (isAnimateCompletion) {
            animSet.runSerially(
                    tickAnimator,
                    areaAnimator,
                    positionAnimator,
                    marginAnimator,
                    arrowAnimator
            );
        } else {
            animSet.runSerially(positionAnimator);
        }

        animSet.setDuration(animationDuration);

        animSet.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
                if (onSlideToActAnimationEventListener != null) {
                    onSlideToActAnimationEventListener.onSlideResetAnimationStarted(SlideToActView.this);
                }
            }

            @Override
            public void onStop(Animator animator) {
                // Do nothing
            }

            @Override
            public void onCancel(Animator animator) {
                // Do nothing
            }

            @Override
            public void onEnd(Animator animator) {
                setEnabled(true);

                if (onSlideToActAnimationEventListener != null) {
                    onSlideToActAnimationEventListener.onSlideResetAnimationEnded(SlideToActView.this);
                }

                if (onSlideResetListener != null) {
                    onSlideResetListener.onSlideReset(SlideToActView.this);
                }
            }

            @Override
            public void onPause(Animator animator) {
                // Do nothing
            }

            @Override
            public void onResume(Animator animator) {
                // Do nothing
            }
        });
        animSet.start();
    }

    /**
     * Private method to handle vibration logic, called when the cursor it moved to the end of
     * it's path.
     */
    private void handleVibration() {
        if (bumpVibration <= 0) {
            return;
        }

        VibratorAgent vibrator = new VibratorAgent();
        vibrator.startOnce((int) bumpVibration);
    }

    /**
     * Method that reset the slider.
     */
    public void resetSlider() {
        if (mIsCompleted) {
            startAnimationReset();
        }
    }

    /**
     * Get text message.
     *
     * @return text message in CharSequence form.
     */
    public CharSequence getText() {
        return text;
    }

    /**
     * Set text message.
     *
     * @param text The text messsage.
     */
    public void setText(CharSequence text) {
        this.text = text;
        invalidate();
    }

    /**
     * Get border radius of outer and inner view.
     *
     * @return border radius in int form.
     */
    public int getBorderRadius() {
        return borderRadius;
    }

    /**
     * Set border radius of outer and inner view.
     *
     * @param borderRadius The border radius.
     */
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    /**
     * Get color of outer view.
     *
     * @return Outer color in Color instance form.
     */
    public Color getOuterColor() {
        return outerColor;
    }

    /**
     * Set color of outer view.
     *
     * @param outerColor The outer color.
     */
    public void setOuterColor(Color outerColor) {
        this.outerColor = outerColor;
        mOuterPaint.setColor(outerColor);
        invalidate();
    }

    /**
     * Get color of inner view.
     *
     * @return Inner color in Color instance form.
     */
    public Color getInnerColor() {
        return innerColor;
    }

    /**
     * Set color of inner view.
     *
     * @param innerColor The inner color.
     */
    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
        mInnerPaint.setColor(innerColor);
        invalidate();
    }

    /**
     * Get color of text message.
     *
     * @return text color in Color instance form.
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Set color of text message.
     *
     * @param textColor The text color.
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        mTextPaint.setColor(textColor);
        invalidate();
    }

    /**
     * Get color of slider icon.
     *
     * @return Slider icon color in Color instance form.
     */
    public Color getIconColor() {
        return iconColor;
    }

    /**
     * Set color of slider icon.
     *
     * @param iconColor The slider icon color.
     */
    public void setIconColor(Color iconColor) {
        this.iconColor = iconColor;
        SlideToActIconUtil.setIconColor(mDrawableArrow, iconColor);
        invalidate();
    }

    /**
     * Return the status of slider (Reversed or normal).
     *
     * @return true if the status of slider is reversed.
     */
    public boolean isSliderReversed() {
        return sliderReversed;
    }

    /**
     * Sets whether slider is reverse or normal.
     *
     * @param sliderReversed true if slider is reverse.
     */
    public void setSliderReversed(boolean sliderReversed) {
        this.sliderReversed = sliderReversed;
        // We reassign the position field to trigger the re-computation of the effective position.
        updatePosition(mPosn);
        invalidate();
    }

    /**
     * Return the status of icon (is rotate or not, when slider is moving).
     *
     * @return true if the status of icon is rotate.
     */
    public boolean isRotateIcon() {
        return isRotateIcon;
    }

    /**
     * Sets whether icon is rotate or not.
     *
     * @param rotateIcon true if you want to rotate icon when slider is moving.
     */
    public void setRotateIcon(boolean rotateIcon) {
        isRotateIcon = rotateIcon;
    }

    /**
     * return the status of slider (locked or not).
     *
     * @return true if status of slider is locked.
     */
    public boolean isSliderLocked() {
        return sliderLocked;
    }

    /**
     * Sets whether slider is locked or not.
     *
     * @param sliderLocked true if slider locked.
     */
    public void setSliderLocked(boolean sliderLocked) {
        this.sliderLocked = sliderLocked;
    }

    /**
     * Return the status of animation completion (enabled or disabled).
     *
     * @return true if status of animation completion is enabled.
     */
    public boolean isAnimateCompletion() {
        return isAnimateCompletion;
    }

    /**
     * Sets whether animation completion is enable or disable.
     *
     * @param animateCompletion true if animation completion enable.
     */
    public void setAnimateCompletion(boolean animateCompletion) {
        isAnimateCompletion = animateCompletion;
    }

    /**
     * Get the text size of message.
     *
     * @return Text size in int form.
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * Set the text size of message.
     *
     * @param textSize The text size of message.
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        mTextPaint.setTextSize(textSize);
        invalidate();
    }

    /**
     * Get the icon margin.
     *
     * @return The icon margin in int form.
     */
    public int getIconMargin() {
        return iconMargin;
    }

    /**
     * Set the icon margin.
     *
     * @param iconMargin the icon margin.
     */
    public void setIconMargin(int iconMargin) {
        this.iconMargin = iconMargin;
        mArrowMargin = iconMargin;
        mTickMargin = iconMargin;
        invalidate();
    }

    /**
     * Get the margin between outer and inner view.
     *
     * @return The area margin in int form.
     */
    public int getAreaMargin() {
        return areaMargin;
    }

    /**
     * Set the area margin (distance between outer and inner view).
     *
     * @param areaMargin The area margin.
     */
    public void setAreaMargin(int areaMargin) {
        this.areaMargin = areaMargin;
        mActualAreaMargin = areaMargin;
        invalidate();
    }

    /**
     * Get the duration of animation.
     *
     * @return The duration of animation in mili-second.
     */
    public long getAnimationDuration() {
        return animationDuration;
    }

    /**
     * Set the duration of animation.
     *
     * @param animationDuration The animation duration in mili-second.
     */
    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    /**
     * Get the bump vibration duration.
     *
     * @return The duration of bump vibration.
     */
    public long getBumpVibration() {
        return bumpVibration;
    }

    /**
     * Set the bump vibration duration.
     *
     * @param bumpVibration The bump vibration duration in mili-second.
     */
    public void setBumpVibration(long bumpVibration) {
        this.bumpVibration = bumpVibration;
    }

    /**
     * Get the slider icon.
     *
     * @return The slider icon in VectorElement form.
     */
    public Element getSliderIcon() {
        return sliderIcon;
    }

    /**
     * Set the slider icon.
     *
     * @param sliderIcon The slider icon.
     */
    public void setSliderIcon(Element sliderIcon) {
        this.sliderIcon = sliderIcon;
        mDrawableArrow = sliderIcon;
        SlideToActIconUtil.setIconColor(mDrawableArrow, iconColor);
        invalidate();
    }

    /**
     * Get the complete icon.
     *
     * @return The complete icon in VectorElement form.
     */
    public Element getCompleteIcon() {
        return completeIcon;
    }

    /**
     * Set the complete icon.
     *
     * @param completeIcon The complete icon.
     */
    public void setCompleteIcon(VectorElement completeIcon) {
        this.completeIcon = completeIcon;
        mDrawableTick = completeIcon;
        SlideToActIconUtil.setIconColor(mDrawableTick, iconColor);
        invalidate();
    }

    /**
     * Method that returns the 'mIsCompleted' flag.
     *
     * @return True if slider is in the Complete state
     */
    public boolean isCompleted() {
        return mIsCompleted;
    }

    /**
     * Set the slide complete listner.
     *
     * @param onSlideCompleteListener instance of OnSlideCompleteListener interface.
     */
    public void setOnSlideCompleteListener(OnSlideCompleteListener onSlideCompleteListener) {
        this.onSlideCompleteListener = onSlideCompleteListener;
    }

    /**
     * Set the slider animation event listner.
     *
     * @param onSlideToActAnimationEventListener instance of OnSlideToActAnimationEventListener interface.
     */
    public void setOnSlideToActAnimationEventListener(
            OnSlideToActAnimationEventListener onSlideToActAnimationEventListener) {
        this.onSlideToActAnimationEventListener = onSlideToActAnimationEventListener;
    }

    /**
     * Set the slider reset listner.
     *
     * @param onSlideResetListener instance of OnSlideResetListener interface.
     */
    public void setOnSlideResetListener(OnSlideResetListener onSlideResetListener) {
        this.onSlideResetListener = onSlideResetListener;
    }

    /**
     * Set the slider user failed listner.
     *
     * @param onSlideUserFailedListener instance of OnSlideUserFailedListener interface.
     */
    public void setOnSlideUserFailedListener(OnSlideUserFailedListener onSlideUserFailedListener) {
        this.onSlideUserFailedListener = onSlideUserFailedListener;
    }

    /**
     * Event handler for the SlideToActView animation events.
     * This event handler can be used to react to animation events from the Slide,
     * the event will be fired whenever an animation start/end.
     */
    public interface OnSlideToActAnimationEventListener {

        /**
         * Called when the slide complete animation start. You can perform actions during the
         * complete animations.
         *
         * @param view      The SlideToActView who created the event
         * @param threshold The mPosition (in percentage [0f,1f]) where the user has left the cursor
         */
        void onSlideCompleteAnimationStarted(SlideToActView view, float threshold);

        /**
         * Called when the slide complete animation finish. At this point the slider is stuck in the
         * center of the slider.
         *
         * @param view The SlideToActView who created the event
         */
        void onSlideCompleteAnimationEnded(SlideToActView view);

        /**
         * Called when the slide reset animation start. You can perform actions during the reset
         * animations.
         *
         * @param view The SlideToActView who created the event
         */
        void onSlideResetAnimationStarted(SlideToActView view);

        /**
         * Called when the slide reset animation finish. At this point the slider will be in the
         * ready on the left of the screen and user can interact with it.
         *
         * @param view The SlideToActView who created the event
         */
        void onSlideResetAnimationEnded(SlideToActView view);
    }

    /**
     * Event handler for the slide complete event.
     * Use this handler to react to slide event
     */
    public interface OnSlideCompleteListener {
        /**
         * Called when user performed the slide.
         *
         * @param view The SlideToActView who created the event
         */
        void onSlideComplete(SlideToActView view);
    }

    /**
     * Event handler for the slide react event.
     * Use this handler to inform the user that he can slide again.
     */
    public interface OnSlideResetListener {
        /**
         * Called when slides is again available.
         *
         * @param view The SlideToActView who created the event
         */
        void onSlideReset(SlideToActView view);
    }

    /**
     * Event handler for the user failure with the Widget.
     * You can subscribe to this event to get notified when the user is wrongly
     * interacting with the widget to eventually educate it:
     *
     * <p>- The user clicked outside of the cursor
     * - The user slided but left when the cursor was back to zero
     *
     * <p>You can use this listener to show a Toast or other messages.
     */
    public interface OnSlideUserFailedListener {
        /**
         * Called when user failed to interact with the slider slide.
         *
         * @param view      The SlideToActView who created the event
         * @param isOutside True if user pressed outside the cursor
         */
        void onSlideFailed(SlideToActView view, boolean isOutside);
    }
}
