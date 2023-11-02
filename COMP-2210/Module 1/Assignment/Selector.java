import java.util.Arrays;

/**
* Defines a library of selection methods
* on arrays of ints.
*
* @author   Matthew Rogers (XXXXX@auburn.edu)
* @author   Dean Hendrix (XXXXX@auburn.edu)
* @version  TODAY
*
*/
public final class Selector {

    /**
     * Can't instantiate this class.
     *
     * D O   N O T   C H A N G E   T H I S   C O N S T R U C T O R
     *
     */
    private Selector() { }


    /**
     * Selects the minimum value from the array a. This method
     * throws IllegalArgumentException if a is null or has zero
     * length. The array a is not changed by this method.
     */
    public static int min(int[] a) throws IllegalArgumentException {
      // No null arrays, no empty arrays
      if(a == null || a.length <= 0) {
         throw new IllegalArgumentException();      
      }
      
      // Declaring a base min
      int currentMin = a[0];
      
      // Search for a smaller x in a
      for(int x : a) {
         if(x < currentMin) {
            currentMin = x; // Smaller x is the new current min
         }
      }
      return currentMin;
    }


    /**
     * Selects the maximum value from the array a. This method
     * throws IllegalArgumentException if a is null or has zero
     * length. The array a is not changed by this method.
     */
    public static int max(int[] a) {
      // No null arrays, no empty arrays
      if(a == null || a.length <= 0) {
         throw new IllegalArgumentException();      
      }
      
      // Declaring a base max
      int currentMax = a[0];
      
      // Find a larger x in a
      for(int x : a) {
         if(x > currentMax) {
            currentMax = x; // larger x is the new current max
         }
      }
      return currentMax;
   }


    /**
     * Selects the kth minimum value from the array a. This method
     * throws IllegalArgumentException if a is null, has zero length,
     * or if there is no kth minimum value. Note that there is no kth
     * minimum value if k < 1, k > a.length, or if k is larger than
     * the number of distinct values in the array. The array a is not
     * changed by this method.
     */
    public static int kmin(int[] a, int k) {
        // No null arrays, no empty arrays
        if(a == null || a.length <= 0) {
         throw new IllegalArgumentException();            
        }
        // K's target must be within range of array
        if(1 > k || k > a.length) {
         throw new IllegalArgumentException();
        }
        
        // Sort array in ascending order
        int[] sortedArr = Arrays.copyOf(a, a.length);
        Arrays.sort(sortedArr);
         
        // Remove duplicates from array
        int[] noDupes = {};
        for(int i = 0; i < sortedArr.length-1; i++) {
         if(sortedArr[i] != sortedArr[i+1]){
            noDupes = Arrays.copyOf(noDupes, noDupes.length + 1);
            noDupes[noDupes.length - 1] = sortedArr[i];
         }
        }
        noDupes = Arrays.copyOf(noDupes, noDupes.length + 1); // Add on final integer
        noDupes[noDupes.length - 1] = sortedArr[sortedArr.length - 1];
        
        // K must not exceed length of the duplicate-removed array
        if(k > noDupes.length) {
         throw new IllegalArgumentException();
        }           
        
        // Target kth smallest number in noDupes
        return noDupes[k-1];        
    }


    /**
     * Selects the kth maximum value from the array a. This method
     * throws IllegalArgumentException if a is null, has zero length,
     * or if there is no kth maximum value. Note that there is no kth
     * maximum value if k < 1, k > a.length, or if k is larger than
     * the number of distinct values in the array. The array a is not
     * changed by this method.
     */
    public static int kmax(int[] a, int k) {
        // No null arrays, no empty arrays
        if(a == null || a.length <= 0) {
         throw new IllegalArgumentException();            
        }
        // K's target must be within range of array
        if(1 > k || k > a.length) {
         throw new IllegalArgumentException();
        }
        
        // Sort array in ascending order
        int[] sortedArr = Arrays.copyOf(a, a.length);
        Arrays.sort(sortedArr);
         
        // Remove duplicates from array
        int[] noDupes = {};
        for(int i = 0; i < sortedArr.length-1; i++) {
         if(sortedArr[i] != sortedArr[i+1]){
            noDupes = Arrays.copyOf(noDupes, noDupes.length + 1);
            noDupes[noDupes.length - 1] = sortedArr[i];
         }
        }
        noDupes = Arrays.copyOf(noDupes, noDupes.length + 1); // Add on final integer
        noDupes[noDupes.length - 1] = sortedArr[sortedArr.length - 1];
        
        // K must not exceed length of the duplicate-removed array
        if(k > noDupes.length) {
         throw new IllegalArgumentException();
        }           
        
        // Target kth largest number in noDupes
        return noDupes[noDupes.length - k];        
    }


    /**
     * Returns an array containing all the values in a in the
     * range [low..high]; that is, all the values that are greater
     * than or equal to low and less than or equal to high,
     * including duplicate values. The length of the returned array
     * is the same as the number of values in the range [low..high].
     * If there are no qualifying values, this method returns a
     * zero-length array. Note that low and high do not have
     * to be actual values in a. This method throws an
     * IllegalArgumentException if a is null or has zero length.
     * The array a is not changed by this method.
     */
    public static int[] range(int[] a, int low, int high) {
      // No null arrays, no empty arrays
      if(a == null || a.length <= 0) {
         throw new IllegalArgumentException();
      }
      
      int[] rangeArr = {}; // Empty array to fill in w/ our new range values
      
      for(int item : a) {
         if(item >= low && item <= high) { // if item in a is within the constraints
            rangeArr = Arrays.copyOf(rangeArr, rangeArr.length + 1);
            rangeArr[rangeArr.length - 1] = item; // Expand array and add item
         }
      }
      return rangeArr; // (Returns empty if none found: line 146)
    }


    /**
     * Returns the smallest value in a that is greater than or equal to
     * the given key. This method throws an IllegalArgumentException if
     * a is null or has zero length, or if there is no qualifying
     * value. Note that key does not have to be an actual value in a.
     * The array a is not changed by this method.
     */
    public static int ceiling(int[] a, int key) {
      // No null arrays, no empty arrays
      if(a == null || a.length == 0) {
         throw new IllegalArgumentException();
      }
      
      // Populating array with integers higher than key
      int[] ceilingArr = {};
      for(int x : a) {
         if(x >= key) {
            ceilingArr = Arrays.copyOf(ceilingArr, ceilingArr.length + 1);
            ceilingArr[ceilingArr.length - 1] = x; // Expand array and add item
         }
      }
      
      if(ceilingArr.length == 0) {
         throw new IllegalArgumentException();
      }
      
      int currentCeiling = ceilingArr[0];
      for(int y : ceilingArr) {
         if(y <= currentCeiling) {
            currentCeiling = y;
         }
      }
      return currentCeiling;
   }


    /**
     * Returns the largest value in a that is less than or equal to
     * the given key. This method throws an IllegalArgumentException if
     * a is null or has zero length, or if there is no qualifying
     * value. Note that key does not have to be an actual value in a.
     * The array a is not changed by this method.
     */
    public static int floor(int[] a, int key) {
      // No null arrays, no empty arrays
      if(a == null || a.length == 0) {
         throw new IllegalArgumentException();
      }
      
      // Populating array with integers lower than key
      int[] floorArr = {};
      for(int x : a) {
         if(x <= key) {
            floorArr = Arrays.copyOf(floorArr, floorArr.length + 1);
            floorArr[floorArr.length - 1] = x; // Expand array and add item
         }
      }
      
      if(floorArr.length == 0) {
         throw new IllegalArgumentException();
      }
      
      int currentFloor = floorArr[0];
      for(int y : floorArr) {
         if(y >= currentFloor) {
            currentFloor = y;
         }
      }
      return currentFloor;
   }
}