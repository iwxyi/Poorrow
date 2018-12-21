package com.iwxyi.BillsFragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecycleItemTouchHelper extends ItemTouchHelper.Callback {

    private static final String TAG = "RecycleItemTouchHelper";
    private final ItemTouchHelperCallback helperCallback;

    public RecycleItemTouchHelper(ItemTouchHelperCallback helperCallback) {
        this.helperCallback = helperCallback;
    }

    /**
     * 设置滑动类型标记
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END);
    }

    /**
     * 设置是否支持长按拖动
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    /**
     * 设置是否支持滑动
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * 拖拽切换item的回调
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return 是否切换了位置
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        helperCallback.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    /**
     * 滑动item
     * @param viewHolder
     * @param direction 滑动的方向
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        helperCallback.onItemDelete(viewHolder.getAdapterPosition());
    }

    /**
     * item被选中的时候回调
     * @param viewHolder
     * @param actionState 当前item的状态
     *                    ItemTouchHelper.ACTION_STATE_IDLE 闲置状态
     *                    ItemTouchHelper.ACTION_STATE_SWIPE 滑动中状态
     *                    ItemTouchHelper.ACTION_STATE_DRAG 拖拽中状态
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    public interface ItemTouchHelperCallback {
        void onItemDelete(int position);
        void onMove(int fromPosition, int toPosition);
    }
}
