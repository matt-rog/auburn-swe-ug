import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;

/**
 * Binary search.
 */
public class BinarySearch {

    /**
     * Returns the index of the first key in a[] that equals the search key, 
     * or -1 if no such key exists. This method throws a NullPointerException
     * if any parameter is null.
     */
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
      if(a == null || key == null || comparator == null){
         throw new NullPointerException();
      }
      
      int result = -1;
      int left = 0;
      int right = a.length - 1;
      while(left <= right) {
         int mid = (left + right) / 2;
         if (comparator.compare(key, a[mid]) < 0) {
            right = mid - 1;
         }
         else if (comparator.compare(key, a[mid]) > 0) {
            left = mid + 1;
         }
         else {
          right = mid - 1;
          result = mid;        
         }      
      }
      return result;
    }

    /**
     * Returns the index of the last key in a[] that equals the search key, 
     * or -1 if no such key exists. This method throws a NullPointerException
     * if any parameter is null.
     */
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
      if(a == null || key == null || comparator == null){
         throw new NullPointerException();
      }
      
      int result = -1;
      int left = 0;
      int right = a.length - 1;
      while(left <= right) {
         int mid = (left + right) / 2;
         if (comparator.compare(key, a[mid]) < 0) {
            right = mid - 1;
         }
         else if (comparator.compare(key, a[mid]) > 0) {
            left = mid + 1;
         }
         else {
          left = mid + 1;
          result = mid;        
         }      
      }
      return result;
    }

}
