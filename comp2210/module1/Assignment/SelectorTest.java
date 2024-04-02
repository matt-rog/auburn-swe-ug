import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class SelectorTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   /** TESTING MIN **/
   @Test public void testMinNormal() {
      int[] numbers = {9,6,7,2,1,5};
      Assert.assertEquals("Min normal failed", 1, Selector.min(numbers));
   }
   
   @Test (expected = IllegalArgumentException.class) public void testMinEmpty() {
      int[] numbers = {};
      Selector.min(numbers);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testMinNull() {
      int[] numbers = null;
      Selector.min(numbers);
   }
   
   /** TESTING MAX **/
   @Test public void testMaxNormal() {
      int[] numbers = {9,6,7,2,1,5};
      Assert.assertEquals("Max normal failed", 9, Selector.max(numbers));
   }
   
   @Test (expected = IllegalArgumentException.class) public void testMaxEmpty() {
      int[] numbers = {};
      Selector.max(numbers);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testMaxNull() {
      int[] numbers = null;
      Selector.max(numbers);
   }
   
   /** TESTING KMIN **/
   @Test public void testKMinNormal() {
      int[] numbers = {2,8,7,3,4};
      int k = 1;
      Assert.assertEquals("kmax normal failed", 2, Selector.kmin(numbers, k));
   }
   
   @Test (expected = IllegalArgumentException.class) public void testKMinMissing() {
      int k = 9;
      int[] numbers = {2,8,7,3,4};
      Selector.kmin(numbers, k);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testKMinEmpty() {
      int k = 2;
      int[] numbers = {};
      Selector.kmin(numbers, k);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testKMinNull() {
      int k = 2;
      int[] numbers = null;
      Selector.kmin(numbers, k);
   }
   
   /** TESTING KMAX **/
   @Test public void testKMaxNormal() {
      int[] numbers = {2,8,7,3,4};
      int k = 1;
      Assert.assertEquals("kmax normal failed", 8, Selector.kmax(numbers, k));
   }
   
   @Test (expected = IllegalArgumentException.class) public void testKMaxMissing() {
      int k = 9;
      int[] numbers = {2,8,7,3,4};
      Selector.kmax(numbers, k);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testKMaxEmpty() {
      int k = 2;
      int[] numbers = {};
      Selector.kmax(numbers, k);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testKMaxNull() {
      int k = 2;
      int[] numbers = null;
      Selector.kmax(numbers, k);
   }
   
   /** TESTING RANGE **/
   @Test public void testRangeNormal() {
      int low = 1;
      int high = 5;
      int[] numbers = {2,8,7,3,4};
      int[] expected = {2,3,4};
      Assert.assertArrayEquals("Range normal failed", expected, Selector.range(numbers, low, high));
   }
   
   @Test public void testRangeMissing() {
      int low = 9;
      int high = 10;
      int[] numbers = {2,8,7,3,4};
      int[] expected = {};
      Assert.assertArrayEquals("Range missing failed", expected, Selector.range(numbers, low, high));
   }
   
   @Test (expected = IllegalArgumentException.class) public void testRangeEmpty() {
      int low = 1;
      int high = 5;
      int[] numbers = {};
      Selector.range(numbers, low, high);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testRangeNull() {
      int low = 1;
      int high = 5;
      int[] numbers = null;
      Selector.range(numbers, low, high);
   }
   
   /** TESTING CEILING **/
   @Test public void testCeilingNormal() {
      int c = 7;
      int[] numbers = {9,7};
      Assert.assertEquals("Ceiling normal failed", 7, Selector.ceiling(numbers, c));
   }
   
   @Test (expected = IllegalArgumentException.class) public void testCeilingMissing() {
      int c = 10;
      int[] numbers = {9,6,7,2,1,5};
      Selector.ceiling(numbers, c);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testCeilingEmpty() {
      int c = 2;
      int[] numbers = {};
      Selector.ceiling(numbers, c);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testCeilingNull() {
      int c = 2;
      int[] numbers = null;
      Selector.ceiling(numbers, c);
   }
   
   /** TESTING FLOOR **/
   @Test public void testFloorNormal() {
      int f = 6;
      int[] numbers = {9,8,7,3,4};
      Assert.assertEquals("Floor normal failed", 4, Selector.floor(numbers, f));
   }
   
   @Test (expected = IllegalArgumentException.class) public void testFloorMissing() {
      int f = 1;
      int[] numbers = {9,6,7,2,3,5};
      Selector.floor(numbers, f);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testFloorEmpty() {
      int f = 2;
      int[] numbers = {};
      Selector.floor(numbers, f);
   }
   
   @Test (expected = IllegalArgumentException.class) public void testFloorNull() {
      int f = 2;
      int[] numbers = null;
      Selector.floor(numbers, f);
   }
}
