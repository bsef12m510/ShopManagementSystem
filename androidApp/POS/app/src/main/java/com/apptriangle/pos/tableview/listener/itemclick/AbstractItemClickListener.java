/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.apptriangle.pos.tableview.listener.itemclick;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.apptriangle.pos.tableview.ITableView;
import com.apptriangle.pos.tableview.adapter.recyclerview.CellRecyclerView;
import com.apptriangle.pos.tableview.handler.SelectionHandler;
import com.apptriangle.pos.tableview.listener.ITableViewListener;

/**
 * Created by evrencoskun on 22.11.2017.
 */

public abstract class AbstractItemClickListener implements RecyclerView.OnItemTouchListener {
    private ITableViewListener mListener;
    protected GestureDetector mGestureDetector;
    protected CellRecyclerView mRecyclerView;
    protected SelectionHandler mSelectionHandler;
    protected ITableView mTableView;

    public AbstractItemClickListener(CellRecyclerView recyclerView, ITableView tableView) {
        this.mRecyclerView = recyclerView;
        this.mTableView = tableView;
        this.mSelectionHandler = tableView.getSelectionHandler();

        mGestureDetector = new GestureDetector(mRecyclerView.getContext(), new
                GestureDetector.SimpleOnGestureListener() {

            MotionEvent start;

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                start = e;
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // Check distance to prevent scroll to trigger the event
                if(start != null
                        && Math.abs(start.getRawX() - e.getRawX()) < 20
                        && Math.abs(start.getRawY() - e.getRawY()) < 20) {
                    longPressAction(e);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        return clickAction(view, e);
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}


    protected ITableViewListener getTableViewListener() {
        if (mListener == null) {
            mListener = mTableView.getTableViewListener();
        }
        return mListener;
    }

    abstract protected boolean clickAction(RecyclerView view, MotionEvent e);

    abstract protected void longPressAction(MotionEvent e);
}
