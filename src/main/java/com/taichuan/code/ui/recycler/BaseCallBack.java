package com.taichuan.code.ui.recycler;


import java.lang.ref.WeakReference;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * class name:
 * description:
 * author: SGJ
 * create date: 2020/2/19 14:13
 */
public abstract class BaseCallBack extends ItemTouchHelper.Callback {
    protected int mLastActionState;  //记录上次动作（拖动、侧滑）状态

    protected boolean isNotNull(WeakReference<?> pWeakReference) {
        boolean result = false;
        if (pWeakReference != null && pWeakReference.get() != null) {
            result = true;
        }
        return result;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        switch (actionState) {
            case ItemTouchHelper.ACTION_STATE_SWIPE://侧滑，将要删除条目。
            case ItemTouchHelper.ACTION_STATE_DRAG://拖拽，将要移动条目。
                mLastActionState = actionState;
                break;
            default:
                break;
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        switch (mLastActionState) {
            case ItemTouchHelper.ACTION_STATE_DRAG:
                //拖拽结束
                itemMoveOver();
                break;
            case ItemTouchHelper.ACTION_STATE_SWIPE:
                itemSwipeOver();
                //删除结束
                //注意：在这里获取的viewHolder.getLayoutPosition()跟原来的position不一样，有偏差，偏差为1。
                break;
            default:
                break;
        }
    }

    //拖拽结束
    public abstract void itemMoveOver();

    //侧滑结束
    public abstract void itemSwipeOver();
}
