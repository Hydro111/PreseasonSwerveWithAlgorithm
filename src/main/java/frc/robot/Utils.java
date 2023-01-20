package frc.robot;

import java.util.Arrays;

public final class Utils {
    public static final double clamp(double value, double min, double max) {
        if (value > max) { return max; }
        if (value < min) { return min; }
        return value;
    }

    // H! Yes I implemented a full, generalized algorithim that runs in O(n log n) to find the maximum value in a 4 element array once. What of it?
    /** Finds the minimum value in an array */
    public static final <T extends Comparable> T minArray(T[] array) {
        int pivot = array.length / 2;
        T firstMin = minArray(Arrays.copyOfRange(array, 0, pivot));
        T lastMin = minArray(Arrays.copyOfRange(array, pivot, array.length));
    
        if (firstMin.compareTo(lastMin) < 0) {
          return firstMin;
        }
        return lastMin;
    }
    
    /** Finds the maximum value in an array */
    public static final <T extends Comparable> T maxArray(T[] array) {
        int pivot = array.length / 2;
        T firstMin = minArray(Arrays.copyOfRange(array, 0, pivot));
        T lastMin = minArray(Arrays.copyOfRange(array, pivot, array.length));
    
        if (firstMin.compareTo(lastMin) > 0) {
          return firstMin;
        }
        return lastMin;
    }
}
