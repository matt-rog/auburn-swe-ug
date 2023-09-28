import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Defines a library of selection methods on Collections.
 *
 * @author  MATTHEW ROGERS (XXXXX@auburn.edu)
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
     * Returns the minimum value in the Collection coll as defined by the
     * Comparator comp. If either coll or comp is null, this method throws an
     * IllegalArgumentException. If coll is empty, this method throws a
     * NoSuchElementException. This method will not change coll in any way.
     *
     * @param coll    the Collection from which the minimum is selected
     * @param comp    the Comparator that defines the total order on T
     * @return        the minimum value in coll
     * @throws        IllegalArgumentException as per above
     * @throws        NoSuchElementException as per above
     */
    public static <T> T min(Collection<T> coll, Comparator<T> comp) {
        if(coll == null || comp == null) {
         throw new IllegalArgumentException();
        }
        if(coll.size() <= 0) {
         throw new NoSuchElementException();
        }
        T currentMin = null;
        for(T item : coll) {
         if(currentMin == null) {
            currentMin = item;
         }
         if(comp.compare(currentMin, item) > 0) {
            currentMin = item;
         }
        }
        return currentMin;
    }


    /**
     * Selects the maximum value in the Collection coll as defined by the
     * Comparator comp. If either coll or comp is null, this method throws an
     * IllegalArgumentException. If coll is empty, this method throws a
     * NoSuchElementException. This method will not change coll in any way.
     *
     * @param coll    the Collection from which the maximum is selected
     * @param comp    the Comparator that defines the total order on T
     * @return        the maximum value in coll
     * @throws        IllegalArgumentException as per above
     * @throws        NoSuchElementException as per above
     */
    public static <T> T max(Collection<T> coll, Comparator<T> comp) {
        if(coll == null || comp == null) {
         throw new IllegalArgumentException();
        }
        if(coll.size() <= 0) {
         throw new NoSuchElementException();
        }
        T currentMax = null;
        for(T item : coll) {
         if(currentMax == null) {
            currentMax = item;
         }
         if(comp.compare(currentMax, item) < 0) {
            currentMax = item;
         }
        }
        return currentMax;
    }


    /**
     * Selects the kth minimum value from the Collection coll as defined by the
     * Comparator comp. If either coll or comp is null, this method throws an
     * IllegalArgumentException. If coll is empty or if there is no kth minimum
     * value, this method throws a NoSuchElementException. This method will not
     * change coll in any way.
     *
     * @param coll    the Collection from which the kth minimum is selected
     * @param k       the k-selection value
     * @param comp    the Comparator that defines the total order on T
     * @return        the kth minimum value in coll
     * @throws        IllegalArgumentException as per above
     * @throws        NoSuchElementException as per above
     */
    public static <T> T kmin(Collection<T> coll, int k, Comparator<T> comp) {
        if(coll == null || comp == null) {
         throw new IllegalArgumentException();
        }
        if(coll.size() <= 0) {
         throw new NoSuchElementException();
        }
        
        if(1 > k || k > coll.size()) {
         throw new NoSuchElementException();
        }
        
        // Sort array in ascending order
        List<T> sortedList = new ArrayList<>(coll);
        java.util.Collections.<T>sort(sortedList, comp);
         
        // Remove duplicates from array
        List<T> noDupes = new ArrayList<>();
        for(int i = 0; i < sortedList.size()-1; i++) {
         if(comp.compare(sortedList.get(i), sortedList.get(i+1)) != 0){
            noDupes.add(sortedList.get(i));
         }
        }
        
        noDupes.add(sortedList.get(sortedList.size() - 1));
        
        // K must not exceed length of the duplicate-removed array
        if(k > noDupes.size()) {
         throw new NoSuchElementException();
        }           
        
        // Target kth smallest number in noDupes
        return noDupes.get(k-1);
    }


    /**
     * Selects the kth maximum value from the Collection coll as defined by the
     * Comparator comp. If either coll or comp is null, this method throws an
     * IllegalArgumentException. If coll is empty or if there is no kth maximum
     * value, this method throws a NoSuchElementException. This method will not
     * change coll in any way.
     *
     * @param coll    the Collection from which the kth maximum is selected
     * @param k       the k-selection value
     * @param comp    the Comparator that defines the total order on T
     * @return        the kth maximum value in coll
     * @throws        IllegalArgumentException as per above
     * @throws        NoSuchElementException as per above
     */
    public static <T> T kmax(Collection<T> coll, int k, Comparator<T> comp) {
        if(coll == null || comp == null) {
         throw new IllegalArgumentException();
        }
        if(coll.size() <= 0) {
         throw new NoSuchElementException();
        }
        
        if(1 > k || k > coll.size()) {
         throw new NoSuchElementException();
        }
        
        // Sort array in ascending order
        List<T> sortedList = new ArrayList<>(coll);
        java.util.Collections.<T>sort(sortedList, comp);
         
        // Remove duplicates from array
        List<T> noDupes = new ArrayList<>();
        for(int i = 0; i < sortedList.size()-1; i++) {
         if(comp.compare(sortedList.get(i), sortedList.get(i+1)) != 0){
            noDupes.add(sortedList.get(i));
         }
        }
        
        noDupes.add(sortedList.get(sortedList.size() - 1));
        
        // K must not exceed length of the duplicate-removed array
        if(k > noDupes.size()) {
         throw new NoSuchElementException();
        }           
        
        // Target kth smallest number in noDupes
        return noDupes.get(noDupes.size() - k);
    }


    /**
     * Returns a new Collection containing all the values in the Collection coll
     * that are greater than or equal to low and less than or equal to high, as
     * defined by the Comparator comp. The returned collection must contain only
     * these values and no others. The values low and high themselves do not have
     * to be in coll. Any duplicate values that are in coll must also be in the
     * returned Collection. If no values in coll fall into the specified range or
     * if coll is empty, this method throws a NoSuchElementException. If either
     * coll or comp is null, this method throws an IllegalArgumentException. This
     * method will not change coll in any way.
     *
     * @param coll    the Collection from which the range values are selected
     * @param low     the lower bound of the range
     * @param high    the upper bound of the range
     * @param comp    the Comparator that defines the total order on T
     * @return        a Collection of values between low and high
     * @throws        IllegalArgumentException as per above
     * @throws        NoSuchElementException as per above
     */
    public static <T> Collection<T> range(Collection<T> coll, T low, T high,
                                                      Comparator<T> comp) {
        if(coll == null || comp == null) {
         throw new IllegalArgumentException();
        }
        if(coll.size() <= 0) {
         throw new NoSuchElementException();
        }
        
        Collection<T> rangeColl = new ArrayList<T>();
      
        for(T item : coll) {
         if(comp.compare(item, low) >= 0 && comp.compare(item, high) <= 0) {
            rangeColl.add(item);
         }
        }
        if(rangeColl.size() == 0) {
         throw new NoSuchElementException();
        }
        return rangeColl;
    }


    /**
     * Returns the smallest value in the Collection coll that is greater than
     * or equal to key, as defined by the Comparator comp. The value of key
     * does not have to be in coll. If coll or comp is null, this method throws
     * an IllegalArgumentException. If coll is empty or if there is no
     * qualifying value, this method throws a NoSuchElementException. This
     * method will not change coll in any way.
     *
     * @param coll    the Collection from which the ceiling value is selected
     * @param key     the reference value
     * @param comp    the Comparator that defines the total order on T
     * @return        the ceiling value of key in coll
     * @throws        IllegalArgumentException as per above
     * @throws        NoSuchElementException as per above
     */
    public static <T> T ceiling(Collection<T> coll, T key, Comparator<T> comp) {
        if(coll == null || comp == null) {
         throw new IllegalArgumentException();
        }
        if(coll.size() <= 0) {
         throw new NoSuchElementException();
        }
        
        Collection<T> ceilingColl = new ArrayList<T>();
        for(T item : coll) {
         if(comp.compare(item, key) >= 0) {
            ceilingColl.add(item);
         }
        }
      
        if(ceilingColl.size() == 0) {
         throw new NoSuchElementException();
        }
      
        T ceiling = ceilingColl.iterator().next();
        for(T item : ceilingColl) {
         if(comp.compare(item, ceiling) <= 0) {
            ceiling = item;
         }
        }
        return ceiling;
    }


    /**
     * Returns the largest value in the Collection coll that is less than
     * or equal to key, as defined by the Comparator comp. The value of key
     * does not have to be in coll. If coll or comp is null, this method throws
     * an IllegalArgumentException. If coll is empty or if there is no
     * qualifying value, this method throws a NoSuchElementException. This
     * method will not change coll in any way.
     *
     * @param coll    the Collection from which the floor value is selected
     * @param key     the reference value
     * @param comp    the Comparator that defines the total order on T
     * @return        the floor value of key in coll
     * @throws        IllegalArgumentException as per above
     * @throws        NoSuchElementException as per above
     */
    public static <T> T floor(Collection<T> coll, T key, Comparator<T> comp) {
        if(coll == null || comp == null) {
         throw new IllegalArgumentException();
        }
        if(coll.size() <= 0) {
         throw new NoSuchElementException();
        }
        
        Collection<T> floorColl = new ArrayList<T>();
        for(T item : coll) {
         if(comp.compare(item, key) <= 0) {
            floorColl.add(item);
         }
        }
      
        if(floorColl.size() == 0) {
         throw new NoSuchElementException();
        }
      
        T floor = floorColl.iterator().next();
        for(T item : floorColl) {
         if(comp.compare(item, floor) >= 0) {
            floor = item;
         }
        }
        return floor;
    }

}
