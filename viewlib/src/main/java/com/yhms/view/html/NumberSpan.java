/*
 * Copyright (C) 2017 Dominik Mosberger <https://github.com/mosberger>
 * Copyright (C) 2013 Leszek Mzyk <https://github.com/imbryk>
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

package com.yhms.view.html;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

public class NumberSpan implements LeadingMarginSpan, ParcelableSpan {
    private final int mGapWidth;
    private final String mNumber;

    public static final int STANDARD_GAP_WIDTH = 10;

    public NumberSpan(int gapWidth, int number) {
        mGapWidth = gapWidth;
        mNumber = Integer.toString(number).concat(".");
    }

    public NumberSpan(int number) {
        mGapWidth = STANDARD_GAP_WIDTH;
        mNumber = Integer.toString(number).concat(".");
    }

    public NumberSpan(Parcel src) {
        mGapWidth = src.readInt();
        mNumber = src.readString();
    }

    @Override
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /** @hide */
    public int getSpanTypeIdInternal() {
        return 8;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /** @hide */
    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mGapWidth);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return 2 * STANDARD_GAP_WIDTH + mGapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();

            p.setStyle(Paint.Style.FILL);

            if (c.isHardwareAccelerated()) {
                c.save();
                c.drawText(mNumber, x + dir, baseline, p);
                c.restore();
            } else {
                c.drawText(mNumber, x + dir, (top + bottom) / 2.0f, p);
            }

            p.setStyle(style);
        }
    }
}
