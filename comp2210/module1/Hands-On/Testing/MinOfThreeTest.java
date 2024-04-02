import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class MinOfThreeTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always fails. **/
   @Test public void testMin1() {
      int a = 1;
      int b = 1;
      int c = 5;
      MinOfThree test = new MinOfThree();
      Assert.assertEquals("One test failed", a, test.min1(a,b,c));
   }
   
   /** A test that always fails. **/
   @Test public void testMin2() {
      int a = 1;
      int b = 7;
      int c = 5;
      MinOfThree test = new MinOfThree();
      Assert.assertEquals("Two test failed", a, test.min1(a,b,c));
   }
}
