
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

package com.ncorti.slidetoact.utils;

import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.element.Element;
import ohos.agp.render.ColorMatrix;
import ohos.agp.utils.Color;

/**
 * SlideToActIconUtil.
 */
public class SlideToActIconUtil {

    private SlideToActIconUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Change icon color using color matrix.
     * reference from https://stackoverflow.com/a/11171509
     *
     * @param icon instance of Element.
     * @param color target color.
     */
    public static void setIconColor(Element icon, Color color) {
        int iColor = color.getValue();

        int red   = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue  = iColor & 0xFF;

        float[] matrix = {
                0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0 };

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setMatrix(matrix);

        icon.setColorMatrix(colorMatrix);
    }

    /**
     * Creates a [AnimatorValue] for the complete icon.
     *
     * @param listener animator value update listner.
     */
    public static AnimatorValue createIconAnimator(
            AnimatorValue.ValueUpdateListener listener
    ) {
        AnimatorValue tickAnimator = new AnimatorValue();
        tickAnimator.setValueUpdateListener(listener);
        return tickAnimator;
    }
}
