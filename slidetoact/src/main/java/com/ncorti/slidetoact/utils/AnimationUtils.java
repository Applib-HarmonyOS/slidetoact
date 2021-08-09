
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

import ohos.agp.colors.RgbColor;

/**
 * Animation Utils.
 */
public class AnimationUtils {

    private AnimationUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * get the animated value with fraction and values.
     *
     * @param fraction 0~1
     * @param values   float array
     * @return float animated value
     */
    public static float getAnimatedValue(float fraction, int... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        if (values.length == 1) {
            return values[0] * fraction;
        } else {
            if (fraction == 1) {
                return values[values.length - 1];
            }
            float oneFraction = 1f / (values.length - 1);
            float offFraction = 0;
            for (int i = 0; i < values.length - 1; i++) {
                if (offFraction + oneFraction >= fraction) {
                    return values[i] + (fraction - offFraction) * (values.length - 1) * (values[i + 1] - values[i]);
                }
                offFraction += oneFraction;
            }
        }
        return 0;
    }

    /**
     * get the animated value with fraction and values.
     *
     * @param fraction 0~1
     * @param colors   color array
     * @return int color
     */
    public static int getAnimatedColor(float fraction, int... colors) {
        if (colors == null || colors.length == 0) {
            return 0;
        }
        if (colors.length == 1) {
            return getAnimatedColor(0, colors[0], fraction);
        } else {
            if (fraction == 1) {
                return colors[colors.length - 1];
            }
            float oneFraction = 1f / (colors.length - 1);
            float offFraction = 0;
            for (int i = 0; i < colors.length - 1; i++) {
                if (offFraction + oneFraction >= fraction) {
                    return getAnimatedColor(colors[i], colors[i + 1], (fraction - offFraction) * (colors.length - 1));
                }
                offFraction += oneFraction;
            }
        }
        return 0;
    }

    /**
     * get the animated color with start color, end color and fraction.
     *
     * @param fraction 0~1
     * @param from     start color
     * @param to       end color
     * @return int color
     */
    public static int getAnimatedColor(int from, int to, float fraction) {
        RgbColor colorFrom = RgbColor.fromArgbInt(from);
        RgbColor colorTo = RgbColor.fromArgbInt(to);
        int red = (int) (colorFrom.getRed() + (colorTo.getRed() - colorFrom.getRed()) * fraction);
        int blue = (int) (colorFrom.getBlue() + (colorTo.getBlue() - colorFrom.getBlue()) * fraction);
        int green = (int) (colorFrom.getGreen() + (colorTo.getGreen() - colorFrom.getGreen()) * fraction);
        int alpha = (int) (colorFrom.getAlpha() + (colorTo.getAlpha() - colorFrom.getAlpha()) * fraction);
        RgbColor currentRgbColor = new RgbColor(red, green, blue, alpha);
        return currentRgbColor.asArgbInt();
    }
}
