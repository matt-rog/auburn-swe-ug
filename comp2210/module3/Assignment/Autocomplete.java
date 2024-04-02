import java.util.Arrays;

/**
 * Autocomplete.
 */
public class Autocomplete {

	private Term[] terms;

	/**
	 * Initializes a data structure from the given array of terms.
	 * This method throws a NullPointerException if terms is null.
	 */
	public Autocomplete(Term[] termsIn) {
	   if(termsIn == null){
         throw new NullPointerException();
      }
      Arrays.sort(termsIn);
      terms = termsIn;
      System.out.println("terms created");
    }

	/** 
	 * Returns all terms that start with the given prefix, in descending order of weight. 
	 * This method throws a NullPointerException if prefix is null.
	 */
	public Term[] allMatches(String prefix) {
   	if(prefix == null){
         throw new NullPointerException();
      }
      Term target = new Term(prefix, 0);
      
      // find range of matching terms
      int index1 = BinarySearch.<Term>firstIndexOf(terms, target, target.byPrefixOrder(prefix.length()));
      int index2 = BinarySearch.<Term>lastIndexOf(terms, target, target.byPrefixOrder(prefix.length()));
      
      // Term[] arr instantiated with proper indecies
      Term[] results = new Term[(index2 - index1) + 1];
      
      // n populates results w/ matching index range from terms[]
      int n = 0;
      for(int i = index1; i < index2+1; i++){
         results[n] = terms[i];
         n++;
      }
      
      Arrays.sort(results, target.byDescendingWeightOrder());
      return results;
    }
}
