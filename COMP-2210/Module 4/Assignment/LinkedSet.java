import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides an implementation of the Set interface.
 * A doubly-linked list is used as the underlying data structure.
 * Although not required by the interface, this linked list is
 * maintained in ascending natural order. In those methods that
 * take a LinkedSet as a parameter, this order is used to increase
 * efficiency.
 *
 * @author Dean Hendrix (XXXXX@auburn.edu)
 * @author Matthew Rogers (XXXXX@auburn.edu)
 *
 */
public class LinkedSet<T extends Comparable<T>> implements Set<T> {

    //////////////////////////////////////////////////////////
    // Do not change the following three fields in any way. //
    //////////////////////////////////////////////////////////

    /** References to the first and last node of the list. */
    Node front;
    Node rear;

    /** The number of nodes in the list. */
    int size;

    /////////////////////////////////////////////////////////
    // Do not change the following constructor in any way. //
    /////////////////////////////////////////////////////////

    /**
     * Instantiates an empty LinkedSet.
     */
    public LinkedSet() {
        front = null;
        rear = null;
        size = 0;
    }


    //////////////////////////////////////////////////
    // Public interface and class-specific methods. //
    //////////////////////////////////////////////////

    ///////////////////////////////////////
    // DO NOT CHANGE THE TOSTRING METHOD //
    ///////////////////////////////////////
    /**
     * Return a string representation of this LinkedSet.
     *
     * @return a string representation of this LinkedSet
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (T element : this) {
            result.append(element + ", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("]");
        return result.toString();
    }


    ///////////////////////////////////
    // DO NOT CHANGE THE SIZE METHOD //
    ///////////////////////////////////
    /**
     * Returns the current size of this collection.
     *
     * @return  the number of elements in this collection.
     */
    public int size() {
        return size;
    }

    //////////////////////////////////////
    // DO NOT CHANGE THE ISEMPTY METHOD //
    //////////////////////////////////////
    /**
     * Tests to see if this collection is empty.
     *
     * @return  true if this collection contains no elements, false otherwise.
     */
    public boolean isEmpty() {
        return (size == 0);
    }


    /**
     * Ensures the collection contains the specified element. Neither duplicate
     * nor null values are allowed. This method ensures that the elements in the
     * linked list are maintained in ascending natural order.
     *
     * @param  element  The element whose presence is to be ensured.
     * @return true if collection is changed, false otherwise.
     */
    public boolean add(T element) {
        if(element == null || this.contains(element)){
            return false;
        }
        
        Node n = new Node(element);
        
        Node current = front;

        
        if (isEmpty()) {
            front = n;
            rear = n;
        }
        else if (front.element.compareTo(element) > 0) {
            n.next = front;
            front.prev = n;
            front = n;
        }
        else {
            rear.next = n;
            n.prev = rear;
            rear = n;
            if (n.prev.element.compareTo(element) > 0) {
               while (current != null) {
                  if (current.element.compareTo(element) > 0) {
                     rear = n.prev;
                     current.prev.next = n;
                     n.next = current;
                     n.prev = current.prev;
                     current.prev = n;
                     rear.next = null;
                     break;
                  }
                  current = current.next;
               }
            }
         }
      size++;
      return true;
    }
    
    /**
     * Ensures the collection does not contain the specified element.
     * If the specified element is present, this method removes it
     * from the collection. This method, consistent with add, ensures
     * that the elements in the linked lists are maintained in ascending
     * natural order.
     *
     * @param   element  The element to be removed.
     * @return  true if collection is changed, false otherwise.
     */
    public boolean remove(T element) {
        Node target = searchSet(element);
        
        if(target == null){
            return false;
        }
        
        if(size == 1){
            front = null;
            size = 0;
            return true;
        }
        
        if(target == front){
            front = front.next;
            front.prev = null;
        } else {
            
            target.prev.next = target.next;
            if(target.next != null) {
               target.next.prev = target.prev;
            }
         }
         size = size - 1;
         return true;
        
    }


    /**
     * Searches for specified element in this collection.
     *
     * @param   element  The element whose presence in this collection is to be tested.
     * @return  true if this collection contains the specified element, false otherwise.
     */
    public boolean contains(T element) {
        Node target = searchSet(element);
        if(target == null){
         return false;
        }
        return true;
    }


    /**
     * Tests for equality between this set and the parameter set.
     * Returns true if this set contains exactly the same elements
     * as the parameter set, regardless of order.
     *
     * @return  true if this set contains exactly the same elements as
     *               the parameter set, false otherwise
     */
    public boolean equals(Set<T> s) {
        if(s.size() != this.size()) {
         return false;
        }
        return true;
    }


    /**
     * Tests for equality between this set and the parameter set.
     * Returns true if this set contains exactly the same elements
     * as the parameter set, regardless of order.
     *
     * @return  true if this set contains exactly the same elements as
     *               the parameter set, false otherwise
     */
    public boolean equals(LinkedSet<T> s) {
        if(s.size() != this.size()) {
         return false;
        }
        return true;
    }


    /**
     * Returns a set that is the union of this set and the parameter set.
     *
     * @return  a set that contains all the elements of this set and the parameter set
     */
    public Set<T> union(Set<T> s){
        if(s.isEmpty() || this.equals(s)){
            return this;
        }
        if(this.isEmpty()){
            return s;
        }
        
        Set<T> returnSet = new LinkedSet<T>();
        for(T element : s) {
            returnSet.add(element);
        }
        for(T element : this){
            returnSet.add(element);
        }
        return returnSet;
    }


    /**
     * Returns a set that is the union of this set and the parameter set.
     *
     * @return  a set that contains all the elements of this set and the parameter set
     */
    public Set<T> union(LinkedSet<T> s){
        if(s.isEmpty() || this.equals(s)){
            return this;
        }
        if(this.isEmpty()){
            return s;
        }
        
        Set<T> returnSet = new LinkedSet<T>();
        for(T element : s) {
            returnSet.add(element);
        }
        for(T element : this){
            returnSet.add(element);
        }
        return returnSet;
    }


    /**
     * Returns a set that is the intersection of this set and the parameter set.
     *
     * @return  a set that contains elements that are in both this set and the parameter set
     */
    public Set<T> intersection(Set<T> s) {
        if(s.isEmpty() || this.equals(s)){
            return this;
        }
        if(this.isEmpty()){
            return s;
        }
        Set<T> returnSet = new LinkedSet<T>();
        for(T element : s){
            if(this.contains(element)){
               returnSet.add(element);
            }
        }
        for(T element : this){
            if(s.contains(element)){
               returnSet.add(element);
            }
        }
        return returnSet;
    }

    /**
     * Returns a set that is the intersection of this set and
     * the parameter set.
     *
     * @return  a set that contains elements that are in both
     *            this set and the parameter set
     */
    public Set<T> intersection(LinkedSet<T> s) {
        if(s.isEmpty() || this.equals(s)){
            return this;
        }
        if(this.isEmpty()){
            return s;
        }
        Set<T> returnSet = new LinkedSet<T>();
        for(T element : s){
            if(!this.contains(element)){
               returnSet.add(element);
            }
        }
        for(T element : this){
            if(!s.contains(element)){
               returnSet.add(element);
            }
        }
        return returnSet;
    }


    /**
     * Returns a set that is the complement of this set and the parameter set.
     *
     * @return  a set that contains elements that are in this set but not the parameter set
     */
    public Set<T> complement(Set<T> s) {
        return null;
    }


    /**
     * Returns a set that is the complement of this set and
     * the parameter set.
     *
     * @return  a set that contains elements that are in this
     *            set but not the parameter set
     */
    public Set<T> complement(LinkedSet<T> s) {
        return null;
    }


    /**
     * Returns an iterator over the elements in this LinkedSet.
     * Elements are returned in ascending natural order.
     *
     * @return  an iterator over the elements in this LinkedSet
     */
    public Iterator<T> iterator() {
        return null;
    }


    /**
     * Returns an iterator over the elements in this LinkedSet.
     * Elements are returned in descending natural order.
     *
     * @return  an iterator over the elements in this LinkedSet
     */
    public Iterator<T> descendingIterator() {
        return null;
    }


    /**
     * Returns an iterator over the members of the power set
     * of this LinkedSet. No specific order can be assumed.
     *
     * @return  an iterator over members of the power set
     */
    public Iterator<Set<T>> powerSetIterator() {
        return null;
    }



    //////////////////////////////
    // Private utility methods. //
    //////////////////////////////

    // Feel free to add as many private methods as you need.
    
    private Node searchSet(T target){
      Node node = front;
      while(node != null){
         if(node.element.equals(target)){
            return node;
         }
         node = node.next;
      }
      return null;
    }
    
    ////////////////////
    // Nested classes //
    ////////////////////

    //////////////////////////////////////////////
    // DO NOT CHANGE THE NODE CLASS IN ANY WAY. //
    //////////////////////////////////////////////

    /**
     * Defines a node class for a doubly-linked list.
     */
    class Node {
        /** the value stored in this node. */
        T element;
        /** a reference to the node after this node. */
        Node next;
        /** a reference to the node before this node. */
        Node prev;

        /**
         * Instantiate an empty node.
         */
        public Node() {
            element = null;
            next = null;
            prev = null;
        }

        /**
         * Instantiate a node that containts element
         * and with no node before or after it.
         */
        public Node(T e) {
            element = e;
            next = null;
            prev = null;
        }
    }

}
