import java.util.Comparator;

/**
 * Autocomplete term representing a (query, weight) pair.
 * 
 */
public class Term implements Comparable<Term> {

    /**
     * Initialize a term with the given query and weight.
     * This method throws a NullPointerException if query is null,
     * and an IllegalArgumentException if weight is negative.
     */
    
    private String query;
    private long weight; 
    
    public Term(String queryIn, long weightIn) {
      if(queryIn.equals(null)){
         throw new NullPointerException();
      }
      if(weightIn < 0){
         throw new IllegalArgumentException();
      }
      query = queryIn;
      weight = weightIn;
    }

    // Obligatory getter methods
    public long getWeight(){
      return weight;
    }

    public String getQuery(){
      return query;
    }

    /**
     * Compares the two terms in descending order of weight.
     */
    public static Comparator<Term> byDescendingWeightOrder() {
      return new Comparator<Term>() {
            public int compare(Term term1, Term term2) {
                int comp = 0;
                if(term2.getWeight() > term1.getWeight()){
                  comp = 1;
                }
                if(term1.getWeight() > term2.getWeight()){
                  comp = -1;
                }
                return comp;
            }
        };
    }

    /**
     * Compares the two terms in ascending lexicographic order of query,
     * but using only the first length characters of query. This method
     * throws an IllegalArgumentException if length is less than or equal
     * to zero.
     */
    public static Comparator<Term> byPrefixOrder(int length) {
      if(length <= 0){
         throw new IllegalArgumentException();
      }
      return new Comparator<Term>() {
            public int compare(Term term1, Term term2) {
                if(term1.getQuery().length() < length || term2.getQuery().length() < length) {
                  return term1.compareTo(term2);
                }
                return (int) (term1.getQuery().substring(0, length).compareTo(term2.getQuery().substring(0, length)));
            }
        };
    }

    /**
     * Compares this term with the other term in ascending lexicographic order
     * of query.
     */
    @Override
    public int compareTo(Term other) {
        return this.getQuery().compareTo(other.getQuery());
    }

    /**
     * Returns a string representation of this term in the following format:
     * query followed by a tab followed by weight
     */
    @Override
    public String toString(){
      String output = this.query + "\t" + this.weight;
      return output;
    }

}

