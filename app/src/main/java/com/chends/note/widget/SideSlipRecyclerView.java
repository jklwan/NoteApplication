package com.chends.note.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Scroller;

import com.chends.note.R;
import com.chends.note.utils.DisplayUtil;


/**
 * 左滑显示右侧布局RecyclerView<br/>
 * 右侧的布局度需要为固定值<br/>
 */
public class SideSlipRecyclerView extends RecyclerView {

    private float mDownX;//手指初次按下的X坐标
    private float mDownY;//手指初次按下的Y坐标
    private boolean isSideShow;//右侧布局是否显示
    private ViewGroup mPointChild;//手指按下位置的item组件
    private int mSideWidth;//右侧组件的宽度
    //private LinearLayout.LayoutParams mItemLayoutParams;//手指按下时所在的item的布局参数
    private int mScreenWidth = DisplayUtil.getScreenWidth();//屏幕的宽度
    private int mPointPosition = NO_POSITION;//手指按下位置所在的item位置
    //private boolean isAnimation = false; // 是否正在滚动
    private boolean canSide;
    /**
     * 滑动类
     */
    private Scroller scroller;
    private int animDuration = 200;
    /**
     * 当右侧显示时，点击一个非当前item，则隐藏右侧的item，并把此次事件结束
     */
    private boolean isCancelTouch = false;
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    //private float startX, startY;
    /**
     * 是锁定状态
     */
    private boolean isLockStatus = false;
    /**
     * 是否左右滑动状态
     */
    private boolean isSide = false;
    private final float mTouchSlop;
    private LayoutManager layoutManager;

    public SideSlipRecyclerView(Context context) {
        this(context, null);
    }

    public SideSlipRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideSlipRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
        scroller = new Scroller(context);
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        if (getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager lm = (LinearLayoutManager) getLayoutManager();
            if (lm.getOrientation() == LinearLayoutManager.VERTICAL) {
                canSide = true;
                this.layoutManager = layoutManager;
                return;
            }
        }
        canSide = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!canSide) {
            return super.dispatchTouchEvent(event);
        }
        int action = event.getActionMasked();
        if (isCancelTouch) {
            switch (action) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isCancelTouch = false;
                    break;
            }
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                mDownX = event.getX();
                mDownY = event.getY();

                //侧滑
                mPointPosition = pointToPosition(mDownX, event.getY());
                //LogUtil.i("*******pointToPosition(mDownX, mDownY): " + mPointPosition);
                if (mPointPosition != NO_POSITION) {
                    if (isSideShow && mPointChild != null) {
                        // 如果已经显示则直接关闭
                        View view = layoutManager.findViewByPosition(mPointPosition);
                        if (!mPointChild.equals(view)) {
                            turnNormal();
                            isCancelTouch = true;
                            return true;
                        }
                    }
                    if (positionIsItem()) {
                        //获取当前的item
                        View view = layoutManager.findViewByPosition(mPointPosition);
                        if (view instanceof ViewGroup) {
                            mPointChild = (ViewGroup) view;
                            getSideWidth();
                        } else {
                            return super.dispatchTouchEvent(event);
                        }
                    }
                    onLocked();
                } else {
                    if (isSideShow) {
                        turnNormal();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (positionIsItem()) {
                    if (!isLockStatus) {
                        final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                        final float x = event.getX(activePointerIndex);
                        final float y = event.getY(activePointerIndex);

                        float xGap = Math.abs(x - mDownX);
                        float yGap = Math.abs(y - mDownY);
                        // 判断是左右还是上下
                        if (xGap > yGap && xGap > mTouchSlop && positionIsItem()) {
                            isSide = true;
                            isLockStatus = true;
                        } else if (yGap > xGap && yGap > mTouchSlop) {
                            isSide = false;
                            isLockStatus = true;
                            if (isSideShow) {
                                // 上下滑动时隐藏右侧
                                turnNormal();
                            }
                        }
                        if (xGap > yGap && xGap > mTouchSlop && !isLockStatus) {
                            // 非锁定时
                            onUnLocked();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isLockStatus = false;
                onUnLocked();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = event.getActionIndex();
                mDownX = event.getX(index);
                mActivePointerId = event.getPointerId(index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                mDownX = event.getX(event.findPointerIndex(mActivePointerId));
                break;
            default:
                isLockStatus = false;
                onUnLocked();
                isSide = false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 获取侧滑菜单的宽度
     */
    private void getSideWidth() {
        mSideWidth = 0;
        View sideView;
        int sideWidth;
        for (int i = 1, count = mPointChild.getChildCount(); i < count; i++) {
            sideView = mPointChild.getChildAt(i);
            if (sideView != null && (sideView.getVisibility() == VISIBLE)) {
                sideWidth = sideView.getLayoutParams().width;
                if (sideWidth <= 0) {
                    sideWidth = sideView.getWidth();
                }
                if (sideWidth <= 0) {
                    sideView.measure(0, 0);
                    sideWidth = sideView.getMeasuredWidth();
                }
                mSideWidth += sideWidth;
            }
        }
    }

    /**
     * 点击位置转为子view的position
     * @param x x
     * @param y y
     * @return position
     */
    private int pointToPosition(float x, float y) {
        View targetView = findChildViewUnder(x, y);
        if (targetView == null) return NO_POSITION;
        return getChildAdapterPosition(targetView);
    }

    /**
     * 判断点击的position是否item
     */
    private boolean positionIsItem() {
        if (getAdapter() == null) return false;
        int realPosition = mPointPosition;
        int itemCount = getAdapter().getItemCount();
        return realPosition >= 0 && realPosition < itemCount;
    }

    /**
     * 锁定时禁止viewpager的滑动
     */
    private void onLocked() {
        ViewParent parent = getParent();
        CustomViewPager pager;
        while (parent != null) {
            if (parent instanceof CustomViewPager) {
                pager = (CustomViewPager) parent;
                pager.setTag(R.id.tag_custom_view_page, pager.getScrollable());
                pager.setScrollable(false);
            }
            parent = parent.getParent();
        }
    }

    /**
     * 解除锁定时
     */
    private void onUnLocked() {
        ViewParent parent = getParent();
        CustomViewPager pager;
        boolean scroll = true;
        while (parent != null) {
            if (parent instanceof CustomViewPager) {
                pager = (CustomViewPager) parent;
                if (pager.getTag(R.id.tag_custom_view_page) instanceof Boolean) {
                    scroll = (boolean) pager.getTag(R.id.tag_custom_view_page);
                }
                ((CustomViewPager) parent).setScrollable(scroll);
            }
            parent = parent.getParent();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!canSide) {
            return super.onInterceptTouchEvent(event);
        }
        if (isCancelTouch) {
            return true;
        }
        //事件拦截
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (isSide) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (positionIsItem() && isSideShow) {
                    final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                    final float x = event.getX(activePointerIndex);

                    boolean isSideTouch = x > mScreenWidth - mSideWidth;
                    if (x == mDownX && !isSideTouch) {
                        // 点击同一个位置且非点击的右侧，则恢复
                        turnNormal();
                        MotionEvent cancelEvent = MotionEvent.obtain(event);
                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                                (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                        onTouchEvent(cancelEvent);
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canSide) {
            return super.onTouchEvent(event);
        }
        if (isCancelTouch) {
            return true;
        }
        if (isSide && positionIsItem()) {
            //事件响应
            requestDisallowInterceptTouchEvent(true);
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    onTouchEvent(cancelEvent);
                    final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                    final float x = event.getX(activePointerIndex);
                    int deltaX = (int) (x - mDownX);
                    mDownX = x;

                    int scrollX = mPointChild.getScrollX();
                    // 手指拖动itemView滑动, deltaX大于0向右滑动，小于0向左滑动
                    if (deltaX > 0) {
                        if (scrollX > 0) {
                            // 右侧显示时
                            if (deltaX < scrollX) {
                                // 滑动的距离小于显示的距离
                                mPointChild.scrollBy(-deltaX, 0);
                            } else {
                                // 滑动的距离大于显示的距离, 滑动到头
                                mPointChild.scrollBy(-scrollX, 0);
                            }
                        }
                    } else if (deltaX < 0) {
                        if (scrollX < mSideWidth) {
                            //右侧未显示完全
                            int endWidth = mSideWidth - scrollX;// 剩余显示的距离
                            if (-deltaX < endWidth) {
                                // 滑动的距离小于剩余显示的距离
                                mPointChild.scrollBy(-deltaX, 0);
                            } else {
                                // 滑动的距离剩余显示的距离, 滑动到头
                                mPointChild.scrollBy(endWidth, 0);
                            }
                        }
                    }
                    //LogUtil.d("onTouchEvent ACTION_MOVE deltaX:" + deltaX + ",scrollX:" + scrollX);
                    return true;
                case MotionEvent.ACTION_UP:
                    int upScrollX = mPointChild.getScrollX();
                    //LogUtil.d("onTouchEvent ACTION_UP:" + upScrollX);
                    if (upScrollX >= mSideWidth / 2) {
                        // 滑动到显示右侧
                        scrollShowSide();
                    } else {
                        // 滑动到关闭右侧
                        turnNormal();
                    }
                    isSide = false;
                    //事件响应
                    requestDisallowInterceptTouchEvent(false);
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (scroller.computeScrollOffset() && mPointChild != null) {
            // 让ListView item根据当前的滚动偏移量进行滚动
            mPointChild.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            //LogUtil.d("computeScroll x:" + scroller.getCurrX() + "y:" + scroller.getCurrY());
            postInvalidate();

            // 滚动动画结束的时候调用回调接口
            /*if (scroller.isFinished()) {
                //mPointChild.scrollTo(0, 0);
            }*/
        }
    }

    /**
     * 转换到显示状态
     */
    public void scrollShowSide() {
        isSideShow = true;
        if (mPointChild == null) return;
        // 时间
        float delta = (float) (Math.abs(mSideWidth - mPointChild.getScrollX())) / (mSideWidth / 2f) * animDuration;
        // x偏移量
        int offset = mSideWidth - mPointChild.getScrollX();
        if (offset == 0) return;
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(mPointChild.getScrollX(), 0, offset, 0, (int) Math.abs(delta));
        postInvalidate(); // 刷新itemView
    }

    /**
     * 转换为正常隐藏情况
     */
    public void turnNormal() {
        isSideShow = false;
        if (mPointChild == null) return;
        // 时间
        float delta = (float) mPointChild.getScrollX() / (mSideWidth / 2f) * animDuration;
        // x偏移量
        int offset = -mPointChild.getScrollX();
        if (offset == 0) return;
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(mPointChild.getScrollX(), 0, offset, 0, (int) Math.abs(delta));
        postInvalidate(); // 刷新itemView
    }

    /**
     * 变化为正常状态
     */
    public void changeToNormal() {
        if (isSideShow) {
            turnNormal();
        }
    }
}
