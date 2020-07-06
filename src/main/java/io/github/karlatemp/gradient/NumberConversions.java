/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/07/06 15:12:56
 *
 * gradient/gradient.main/NumberConversions.java
 */

package io.github.karlatemp.gradient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utils for casting number types to other number types
 */
public final class NumberConversions {
    private NumberConversions() {
    }

    public static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int ceil(final double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int round(double num) {
        return floor(num + 0.5d);
    }

    public static double square(double num) {
        return num * num;
    }

    public static int toInt(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        if (object == null) return 0;

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException ignore) {
        }
        return 0;
    }

    public static float toFloat(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        if (object == null) return 0;

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException ignore) {
        }
        return 0;
    }

    public static double toDouble(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        if (object == null) return 0;

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException ignore) {
        }
        return 0;
    }

    public static long toLong(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        if (object == null) return 0;

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException ignore) {
        }
        return 0;
    }

    public static short toShort(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        if (object == null) return 0;

        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException ignore) {
        }

        return 0;
    }

    public static byte toByte(@Nullable Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        if (object == null) return 0;

        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException ignore) {
        }
        return 0;
    }

    public static boolean isFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }

    public static boolean isFinite(float f) {
        return Math.abs(f) <= Float.MAX_VALUE;
    }

    public static void checkFinite(double d, @NotNull String message) {
        if (!isFinite(d)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkFinite(float d, @NotNull String message) {
        if (!isFinite(d)) {
            throw new IllegalArgumentException(message);
        }
    }
}
