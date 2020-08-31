/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wdullaer.materialdatetimepicker.date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.wdullaer.materialdatetimepicker.R;

import java.util.Calendar;

public class MyMonthView extends MonthView {

    private final float TEXT_SIZE_16;

    public MyMonthView(Context context, AttributeSet attr, DatePickerController controller) {
        super(context, attr, controller);
        Resources res = context.getResources();
        TEXT_SIZE_16 = res.getDimensionPixelSize(R.dimen.mdtp_month_label_size_16);
    }

    @Override
    public void drawMonthDay(Canvas canvas, int year, int month, int day,
                             int x, int y, int startX, int stopX, int startY, int stopY) {
        if (mSelectedDay == day) {
            canvas.drawCircle(x, y - (MINI_DAY_NUMBER_TEXT_SIZE / 3), DAY_SELECTED_CIRCLE_SIZE,
                    mSelectedCirclePaint);
        }
        mMonthNumPaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
        if (isHighlighted(year, month, day)) {
            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        } else {
            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        }
        if (mController.isOutOfRange(year, month, day)) {
            mMonthNumPaint.setColor(mDisabledDayTextColor);
        } else if (mSelectedDay == day) {
            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mMonthNumPaint.setColor(mSelectedDayTextColor);
        } else if (mHasToday && mToday == day) {
            mMonthNumPaint.setColor(mTodayNumberColor);
        } else {
            mMonthNumPaint.setColor(isHighlighted(year, month, day) ? mHighlightedDayTextColor : mDayTextColor);
        }
        canvas.drawText(String.valueOf(day), x, y, mMonthNumPaint);
    }

    @Override
    protected void drawMonthTitle(Canvas canvas) {
        int x = (mWidth + 2 * mEdgePadding) / 2;
        int y = (getMonthHeaderSize() - MONTH_DAY_LABEL_TEXT_SIZE) / 2;
        mMonthTitlePaint.setColor(getResources().getColor(R.color.sito_choose_day_color));
        mMonthTitlePaint.setTextSize(TEXT_SIZE_16);
        canvas.drawText(getMonthAndYearString(), x, y, mMonthTitlePaint);
    }

    @Override
    protected void drawMonthDayLabels(Canvas canvas) {
        int y = getMonthHeaderSize() - (MONTH_DAY_LABEL_TEXT_SIZE / 2);
        int dayWidthHalf = (mWidth - mEdgePadding * 2) / (mNumDays * 2);
        mMonthDayLabelPaint.setColor(getResources().getColor(R.color.sito_choose_day_color));
        mMonthDayLabelPaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
        for (int i = 0; i < mNumDays; i++) {
            int x = (2 * i + 1) * dayWidthHalf + mEdgePadding;
            int calendarDay = (i + mWeekStart) % mNumDays;
            mDayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay);
            String weekString = "周" + getWeekDayLabel(mDayLabelCalendar);
            canvas.drawText(weekString, x, y, mMonthDayLabelPaint);
        }
    }
}
