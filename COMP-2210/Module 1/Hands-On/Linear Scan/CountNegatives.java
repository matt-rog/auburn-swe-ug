/**
 * Applies the linear scan strategy to counting the number of negative
 * values in an array.
 */
public class CountNegatives {

    /**
     * Returns the number of negative values in the given array.
     */
    public static int countNegatives(int[]a) {
        int negativeCount = 0;
        for( int number : a ){
            if( number < 0 ){
                negativeCount++;
            }
        }
        return negativeCount;
    }
}