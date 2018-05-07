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

package com.abc.customcalendar.tableview.handler;


import com.abc.customcalendar.tableview.ITableView;
import com.abc.customcalendar.tableview.adapter.recyclerview.CellRecyclerView;
import com.abc.customcalendar.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by evrencoskun on 24/10/2017.
 */

public class SelectionHandler {

    public static final int UNSELECTED_POSITION = -1;
    private int mSelectedRowPosition = UNSELECTED_POSITION;
    private int mSelectedColumnPosition = UNSELECTED_POSITION;

    private boolean shadowEnabled = true;


    private ITableView mTableView;
    private AbstractViewHolder mPreviousSelectedViewHolder;
    private CellRecyclerView mColumnHeaderRecyclerView;
    private CellRecyclerView mRowHeaderRecyclerView;

    public SelectionHandler(ITableView tableView) {
        this.mTableView = tableView;
        this.mColumnHeaderRecyclerView = mTableView.getColumnHeaderRecyclerView();
        this.mRowHeaderRecyclerView = mTableView.getRowHeaderRecyclerView();
    }

    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    public void setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
    }

    public void setSelectedCellPositions(AbstractViewHolder selectedViewHolder, int column, int
            row) {
        this.setPreviousSelectedView(selectedViewHolder);

        this.mSelectedColumnPosition = column;
        this.mSelectedRowPosition = row;

        if (shadowEnabled) {
            selectedCellView();
        }
    }


    public void setSelectedColumnPosition(AbstractViewHolder selectedViewHolder, int column) {
        this.setPreviousSelectedView(selectedViewHolder);

        this.mSelectedColumnPosition = column;

        selectedColumnHeader();

        // Set unselected others
        mSelectedRowPosition = UNSELECTED_POSITION;
    }

    public int getSelectedColumnPosition() {
        return mSelectedColumnPosition;
    }

    public void setSelectedRowPosition(AbstractViewHolder selectedViewHolder, int row) {
        this.setPreviousSelectedView(selectedViewHolder);

        this.mSelectedRowPosition = row;

        selectedRowHeader();

        // Set unselected others
        mSelectedColumnPosition = UNSELECTED_POSITION;
    }

    public int getSelectedRowPosition() {
        return mSelectedRowPosition;
    }


    public void setPreviousSelectedView(AbstractViewHolder viewHolder) {
        restorePreviousSelectedView();

        if (mPreviousSelectedViewHolder != null) {
            // Change color
            mPreviousSelectedViewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
            // Change state
            mPreviousSelectedViewHolder.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }

        AbstractViewHolder oldViewHolder = mTableView.getCellLayoutManager().getCellViewHolder
                (getSelectedColumnPosition(), getSelectedRowPosition());

        if (oldViewHolder != null) {
            // Change color
            oldViewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
            // Change state
            oldViewHolder.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }

        this.mPreviousSelectedViewHolder = viewHolder;

        // Change color
        mPreviousSelectedViewHolder.setBackgroundColor(mTableView.getSelectedColor());
        // Change state
        mPreviousSelectedViewHolder.setSelected(AbstractViewHolder.SelectionState.SELECTED);
    }


    private void restorePreviousSelectedView() {
        if (mSelectedColumnPosition != UNSELECTED_POSITION && mSelectedRowPosition !=
                UNSELECTED_POSITION) {
            unselectedCellView();
        } else if (mSelectedColumnPosition != UNSELECTED_POSITION) {
            unselectedColumnHeader();
        } else if (mSelectedRowPosition != UNSELECTED_POSITION) {
            unselectedRowHeader();
        }
    }

    private void selectedRowHeader() {
        // Change background color of the selected cell views
        changeVisibleCellViewsBackgroundForRow(mSelectedRowPosition, true);

        // Change background color of the column headers to be shown as a shadow.
        if (shadowEnabled) {
            mTableView.getColumnHeaderRecyclerView().setSelected(AbstractViewHolder.SelectionState.SHADOWED,
                    mTableView.getShadowColor(), false);
        }
    }

    private void unselectedRowHeader() {
        changeVisibleCellViewsBackgroundForRow(mSelectedRowPosition, false);

        // Change background color of the column headers to be shown as a normal.
        mTableView.getColumnHeaderRecyclerView().setSelected(AbstractViewHolder.SelectionState.UNSELECTED,
                mTableView.getUnSelectedColor(), false);
    }

    private void selectedCellView() {
        int shadowColor = mTableView.getShadowColor();


        // Change background color of the row header which is located on Y Position of the cell
        // view.
        AbstractViewHolder rowHeader = (AbstractViewHolder) mRowHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedRowPosition);

        // If view is null, that means the row view holder was already recycled.
        if (rowHeader != null) {
            // Change color
            rowHeader.setBackgroundColor(shadowColor);
            // Change state
            rowHeader.setSelected(AbstractViewHolder.SelectionState.SHADOWED);
        }

        // Change background color of the column header which is located on X Position of the cell
        // view.
        AbstractViewHolder columnHeader = (AbstractViewHolder) mColumnHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedColumnPosition);

        if (columnHeader != null) {
            // Change color
            columnHeader.setBackgroundColor(shadowColor);
            // Change state
            columnHeader.setSelected(AbstractViewHolder.SelectionState.SHADOWED);
        }

    }

    private void unselectedCellView() {
        int unSelectedColor = mTableView.getUnSelectedColor();

        // Change background color of the row header which is located on Y Position of the cell
        // view.
        AbstractViewHolder rowHeader = (AbstractViewHolder) mRowHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedRowPosition);

        // If view is null, that means the row view holder was already recycled.
        if (rowHeader != null) {
            // Change color
            rowHeader.setBackgroundColor(unSelectedColor);
            // Change state
            rowHeader.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }

        // Change background color of the column header which is located on X Position of the cell
        // view.
        AbstractViewHolder columnHeader = (AbstractViewHolder) mColumnHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedColumnPosition);

        if (columnHeader != null) {
            // Change color
            columnHeader.setBackgroundColor(unSelectedColor);
            // Change state
            columnHeader.setSelected(AbstractViewHolder.SelectionState.UNSELECTED);
        }
    }

    private void selectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(mSelectedColumnPosition, true);

        mTableView.getRowHeaderRecyclerView().setSelected(AbstractViewHolder.SelectionState.SHADOWED, mTableView
                .getShadowColor(), false);

    }

    private void unselectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(mSelectedColumnPosition, false);

        mTableView.getRowHeaderRecyclerView().setSelected(AbstractViewHolder.SelectionState.UNSELECTED, mTableView
                .getUnSelectedColor(), false);
    }

    public boolean isCellSelected(int column, int row) {
        return (getSelectedColumnPosition() == column && getSelectedRowPosition() == row) ||
                isColumnSelected(column) || isRowSelected(row);
    }

    public AbstractViewHolder.SelectionState getCellSelectionState(int column, int row) {
        if (isCellSelected(column, row)) {
            return AbstractViewHolder.SelectionState.SELECTED;
        }
        return AbstractViewHolder.SelectionState.UNSELECTED;
    }

    public boolean isColumnSelected(int column) {
        return (getSelectedColumnPosition() == column && getSelectedRowPosition() ==
                UNSELECTED_POSITION);
    }

    public boolean isColumnShadowed(int column) {
        return (getSelectedColumnPosition() == column && getSelectedRowPosition() !=
                UNSELECTED_POSITION) || (getSelectedColumnPosition() == UNSELECTED_POSITION &&
                getSelectedRowPosition() != UNSELECTED_POSITION);
    }

    public boolean isAnyColumnSelected() {
        return (getSelectedColumnPosition() != SelectionHandler.UNSELECTED_POSITION &&
                getSelectedRowPosition() == SelectionHandler.UNSELECTED_POSITION);
    }

    public AbstractViewHolder.SelectionState getColumnSelectionState(int column) {

        if (isColumnShadowed(column)) {
            return AbstractViewHolder.SelectionState.SHADOWED;

        } else if (isColumnSelected(column)) {
            return AbstractViewHolder.SelectionState.SELECTED;

        } else {
            return AbstractViewHolder.SelectionState.UNSELECTED;
        }
    }

    public boolean isRowSelected(int row) {
        return (getSelectedRowPosition() == row && getSelectedColumnPosition() ==
                UNSELECTED_POSITION);
    }

    public boolean isRowShadowed(int row) {
        return (getSelectedRowPosition() == row && getSelectedColumnPosition() !=
                UNSELECTED_POSITION) || (getSelectedRowPosition() == UNSELECTED_POSITION &&
                getSelectedColumnPosition() != UNSELECTED_POSITION);
    }

    public AbstractViewHolder.SelectionState getRowSelectionState(int row) {

        if (isRowShadowed(row)) {
            return AbstractViewHolder.SelectionState.SHADOWED;

        } else if (isRowSelected(row)) {
            return AbstractViewHolder.SelectionState.SELECTED;

        } else {
            return AbstractViewHolder.SelectionState.UNSELECTED;
        }
    }

    private void changeVisibleCellViewsBackgroundForRow(int row, boolean isSelected) {
        int selectedColor = mTableView.getSelectedColor();
        int unSelectedColor = mTableView.getUnSelectedColor();

        CellRecyclerView recyclerView = (CellRecyclerView) mTableView.getCellLayoutManager()
                .findViewByPosition(row);

        if (recyclerView == null) {
            return;
        }

        recyclerView.setSelected(isSelected ? AbstractViewHolder.SelectionState.SELECTED : AbstractViewHolder.SelectionState
                .UNSELECTED, isSelected ? selectedColor : unSelectedColor, false);
    }

    private void changeVisibleCellViewsBackgroundForColumn(int column, boolean isSelected) {
        int selectedColor = mTableView.getSelectedColor();
        int unSelectedColor = mTableView.getUnSelectedColor();

        AbstractViewHolder[] visibleCellViews = mTableView.getCellLayoutManager()
                .getVisibleCellViewsByColumnPosition(column);

        if (visibleCellViews != null) {
            for (AbstractViewHolder viewHolder : visibleCellViews) {
                if (viewHolder != null) {
                    // Get each view container of the cell view and set unselected color.
                    viewHolder.setBackgroundColor(isSelected ? selectedColor : unSelectedColor);

                    // Change selection status of the view holder
                    viewHolder.setSelected(isSelected ? AbstractViewHolder.SelectionState.SELECTED : AbstractViewHolder.SelectionState
                            .UNSELECTED);
                }
            }
        }
    }

    public void changeRowBackgroundColorBySelectionStatus(AbstractViewHolder viewHolder,
                                                          AbstractViewHolder.SelectionState selectionState) {
        if (shadowEnabled && selectionState == AbstractViewHolder.SelectionState.SHADOWED) {
            viewHolder.setBackgroundColor(mTableView.getShadowColor());

        } else if (selectionState == AbstractViewHolder.SelectionState.SELECTED) {
            viewHolder.setBackgroundColor(mTableView.getSelectedColor());

        } else {
            viewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
        }
    }

    public void changeColumnBackgroundColorBySelectionStatus(AbstractViewHolder viewHolder,
                                                             AbstractViewHolder.SelectionState selectionState) {
        if (shadowEnabled && selectionState == AbstractViewHolder.SelectionState.SHADOWED) {
            viewHolder.setBackgroundColor(mTableView.getShadowColor());

        } else if (selectionState == AbstractViewHolder.SelectionState.SELECTED) {
            viewHolder.setBackgroundColor(mTableView.getSelectedColor());

        } else {
            viewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
        }
    }

    public void clearSelection() {
        unselectedRowHeader();
        unselectedCellView();
        unselectedColumnHeader();
    }

    public void setSelectedRowPosition(int row) {
        this.mSelectedRowPosition = row;
    }

    public void setSelectedColumnPosition(int column) {
        this.mSelectedColumnPosition = column;
    }

}
