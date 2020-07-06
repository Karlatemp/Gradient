/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/07/06 15:15:54
 *
 * gradient/gradient.main/Gradient.java
 */

package io.github.karlatemp.gradient;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Consumer;

public class Gradient {
    /**
     * Get the step of A and B
     *
     * @param from   point A
     * @param to     point B
     * @param amount Amount of points (included A and B)
     * @return The step
     */
    @Contract(pure = true)
    public static @NotNull Vector step(
            @NotNull Vector from,
            @NotNull Vector to,
            @Range(from = 0, to = Integer.MAX_VALUE) int amount) {
        if (amount < 2) return new Vector(0, 0, 0);
        return to.clone().subtract(from).divide(amount - 1);
    }

    /**
     * From point1 to point2 execution step
     *
     * @param from        The point 1
     * @param to          The point 2
     * @param amount      Amount of points (included 1 and 2)
     * @param cloneVector Need clone vector?
     * @param consumer    The callback
     */
    @Contract()
    public static void step(
            @NotNull Vector from,
            @NotNull Vector to,
            @Range(from = 0, to = Integer.MAX_VALUE) int amount,
            boolean cloneVector,
            @NotNull Consumer<@NotNull Vector> consumer
    ) {
        if (amount == 0) return;
        if (amount == 1) {
            consumer.accept(from);
            return;
        }
        Vector vector = from.clone();
        Vector step0 = step(from, to, amount);
        for (int i = 0; i < amount; i++) {
            if (cloneVector)
                consumer.accept(vector.clone());
            else
                consumer.accept(vector);
            vector.add(step0);
        }
    }

    /**
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1] and
     * returns r, g, and b in the set [0, 255].
     *
     * @param hsl The HSL representation
     * @return The RGB representation
     */
    public static Vector hslToRgb(Vector hsl) {
        double h = hsl.getX(), s = hsl.getY(), l = hsl.getZ();
        double r, g, b;

        if (s == 0) {
            r = g = b = l; // achromatic
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = $$$hue2rgb(p, q, h + 1d / 3d);
            g = $$$hue2rgb(p, q, h);
            b = $$$hue2rgb(p, q, h - 1d / 3d);
        }

        return new Vector(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }

    private static double $$$hue2rgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1d / 6d) return p + (q - p) * 6 * t;
        if (t < 1d / 2d) return q;
        if (t < 2d / 3d) return p + (q - p) * (2d / 3d - t) * 6;
        return p;
    }

    public static Vector rgb2hsl(Vector rgb) {
        // see https://en.wikipedia.org/wiki/HSL_and_HSV#Formal_derivation
        // convert r,g,b [0,255] range to [0,1]
        double r = rgb.getX();
        double g = rgb.getY();
        double b = rgb.getZ();
        r = r / 255;
        g = g / 255;
        b = b / 255;
        // get the min and max of r,g,b
        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        // lightness is the average of the largest and smallest color components
        double lum = (max + min) / 2;
        double hue;
        double sat;
        if (max == min) { // no saturation
            hue = 0;
            sat = 0;
        } else {
            double c = max - min; // chroma
            // saturation is simply the chroma scaled to fill
            // the interval [0, 1] for every combination of hue and lightness
            sat = c / (1 - Math.abs(2 * lum - 1));

            if (max == r) {
                // hue = (g - b) / c;
                // hue = ((g - b) / c) % 6;
                hue = (g - b) / c + (g < b ? 6 : 0);
            } else if (max == g) {
                hue = (b - r) / c + 2;
            } else /*if(max == b) */ {
                hue = (r - g) / c + 4;
            }
        }
        hue = Math.round(hue * 60); // °
        sat = Math.round(sat * 100); // %
        lum = Math.round(lum * 100); // %
        return new Vector(hue, sat, lum);
    }
}
