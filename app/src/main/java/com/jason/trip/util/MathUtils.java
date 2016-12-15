package com.jason.trip.util;

/**
 * Version V1.0 <豆芽——豆瓣客户端>
 * ClassName:MathUtils
 * Description:
 * Created by Jason on 16/12/15.
 */

public class MathUtils {

    private MathUtils() {}

    public static int clamp(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }

    public static int lerp(int start, int end, float fraction) {
        return (int) (start + (end - start) * fraction);
    }

    public static float lerp(float start, float end, float fraction) {
        return start + (end - start) * fraction;
    }

    public static float unlerp(int start, int end, int value) {
        int domainSize = end - start;
        if (domainSize == 0) {
            throw new IllegalArgumentException("Can't reverse interpolate with domain size of 0");
        }
        return (float) (value - start) / domainSize;
    }

    public static float unlerp(float start, float end, float value) {
        float domainSize = end - start;
        if (domainSize == 0) {
            throw new IllegalArgumentException("Can't reverse interpolate with domain size of 0");
        }
        return (value - start) / domainSize;
    }
}
