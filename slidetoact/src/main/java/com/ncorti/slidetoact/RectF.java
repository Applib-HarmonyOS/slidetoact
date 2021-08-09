
/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package com.ncorti.slidetoact;

import ohos.agp.utils.RectFloat;
import ohos.media.image.common.Rect;

/**
 * RectF class.
 */
public class RectF extends RectFloat {
    /**
     * default constant.
     **/
    public static final float DEFAULT_VAL_POINT_FIVE = 0.5f;

    /**
     * Default constructor for RectF.
     */
    public RectF() {
        super();
    }

    /**
     * * Parameterised constructor for RectF.
     *
     * @param left   left
     * @param top    top
     * @param right  right
     * @param bottom bottom
     */
    public RectF(float left, float top, float right, float bottom) {
        super(left, top, right, bottom);
    }

    /**
     * * calculate center X.
     *
     * @return centerX
     */
    public final float centerX() {
        return (left + right) * DEFAULT_VAL_POINT_FIVE;
    }

    /**
     * * calculate center Y.
     *
     * @return centerY
     */
    public final float centerY() {
        return (top + bottom) * DEFAULT_VAL_POINT_FIVE;
    }

    /**
     * * calculates width.
     *
     * @return width
     */
    public final float width() {
        return right - left;
    }

    /**
     * * calculates height.
     *
     * @return height
     */
    public final float height() {
        return bottom - top;
    }

    /**
     * * set RectF coordinates.
     *
     * @param src src
     */
    public void set(RectF src) {
        if (src == null) {
            return;
        }
        left = src.left;
        top = src.top;
        right = src.right;
        bottom = src.bottom;
    }

    /**
     * inset.
     *
     * @param dxValue dx
     * @param dyValue dy
     */
    public void inset(float dxValue, float dyValue) {
        left += dxValue;
        top += dyValue;
        right -= dxValue;
        bottom -= dyValue;
    }

    /**
     * roundOut.
     *
     * @param dst dst
     */
    public void roundOut(Rect dst) {
        if (dst == null) {
            return;
        }
        dst.cropRect((int) Math.floor(left), (int) Math.floor(top), (int) Math.ceil(right), (int) Math.ceil(bottom));
    }
}

