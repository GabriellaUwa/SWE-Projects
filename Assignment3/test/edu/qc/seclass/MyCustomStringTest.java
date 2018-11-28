package edu.qc.seclass;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;


import static org.junit.Assert.*;

public class MyCustomStringTest {

    private MyCustomStringInterface mycustomstring;

    @Before
    public void setUp() {
        mycustomstring = new MyCustomString();
    }

    @After
    public void tearDown() {
        mycustomstring = null;
    }

    //Everything Works Fine
    @Test
    public void testCountNumbers1() {
        mycustomstring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertEquals(9, mycustomstring.countNumbers());
    }

    //String is Null
    @Test(expected =  NullPointerException.class)
    public void testCountNumbers2() {
        mycustomstring.countNumbers();
    }

    //String is Empty
    @Test
    public void testCountNumbers3() {
        mycustomstring.setString("");
        assertEquals(0, mycustomstring.countNumbers());
    }

    //No numbers in string
    @Test
    public void testCountNumbers4() {
        mycustomstring.setString("No numbers here");
        assertEquals(0, mycustomstring.countNumbers());
    }

    //Value being returned is an int, if no exception
    @Test
    public void testCountNumbers5() {
        mycustomstring.setString("numbers are 11 and 19");
        assertThat(mycustomstring.countNumbers(), instanceOf(int.class));
    }

    //Intentional Fail, Should get back some number is there is a number in string
    @Test
    public void testCountNumbers6() {
        mycustomstring.setString("Numbers 1");
        assertNotEquals(0, mycustomstring.countNumbers());
    }

    //If String is Empty
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd1() {
        mycustomstring.setString("");
        assertEquals("", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(3, false));
    }

    //String is Null case from end =  True
    @Test(expected = NullPointerException.class)
    public void testGetEveryNthCharacterFromBeginningOrEnd2() {
        mycustomstring.getEveryNthCharacterFromBeginningOrEnd(3, true);
    }

    //String is Null case from end =  False
    @Test(expected = NullPointerException.class)
    public void testGetEveryNthCharacterFromBeginningOrEnd3() {
        mycustomstring.getEveryNthCharacterFromBeginningOrEnd(3, false);
    }

    // N is 0 case from end =  False
    @Test(expected = IllegalArgumentException.class)
    public void testGetEveryNthCharacterFromBeginningOrEnd4() {
        mycustomstring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertEquals("", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(0, false));
    }

    // N is 0 case from end =  True
    @Test(expected = IllegalArgumentException.class)
    public void testGetEveryNthCharacterFromBeginningOrEnd5() {
        mycustomstring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertEquals("", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(0, true));
    }

    // String is empty, start from end = True
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd6() {
        mycustomstring.setString("");
        assertEquals("", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(3, true));
    }

    //All works well, startFromEnd =  True
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd7() {
        mycustomstring.setString("Testing stuff");
        assertEquals("ft nte", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(2, true));
    }
    //All works well, startFromEnd =  False
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd8() {
        mycustomstring.setString("Testing stuff");
        assertEquals("etn tf", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(2, false));
    }

    //String is being returned, case False
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd9() {
        mycustomstring.setString("Testing stuff");
        assertThat(mycustomstring.getEveryNthCharacterFromBeginningOrEnd(2, false), instanceOf(String.class));
    }

    //String is being returned, case True
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd10() {
        mycustomstring.setString("Testing stuff");
        assertThat(mycustomstring.getEveryNthCharacterFromBeginningOrEnd(2, true), instanceOf(String.class));
    }
    //negative index, case false
    @Test(expected = IllegalArgumentException.class)
    public void testGetEveryNthCharacterFromBeginningOrEnd11() {
        mycustomstring.setString("Testing stuff");
        mycustomstring.getEveryNthCharacterFromBeginningOrEnd(-1, false);
    }
    //negative index, case true
    @Test(expected = IllegalArgumentException.class)
    public void testGetEveryNthCharacterFromBeginningOrEnd12() {
        mycustomstring.setString("Testing stuff");
        mycustomstring.getEveryNthCharacterFromBeginningOrEnd(-1, true);
    }

    //Intentional Failure, case true
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd13() {
        mycustomstring.setString("Testing stuff");
        assertNotEquals("", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(2, true));
    }

    //Intentional Failure, case false
    @Test
    public void testGetEveryNthCharacterFromBeginningOrEnd14() {
        mycustomstring.setString("Testing stuff");
        assertNotEquals("", mycustomstring.getEveryNthCharacterFromBeginningOrEnd(2, false));
    }

    //Everything Working perfectly
    @Test
    public void testConvertDigitsToNamesInSubstring1() {
        String testString = "I'd b3tThreer put s0me d161ts in this 5tr1n6, right?";
        mycustomstring.setString(testString);
        mycustomstring.convertDigitsToNamesInSubstring(0, testString.length()-1);
        assertEquals("I'd bThreetThreer put sZerome dOneSixOnets in this FivetrOnenSix, right?", mycustomstring.getString());
    }

    //start > end
    @Test(expected = IllegalArgumentException.class)
    public void testConvertDigitsToNamesInSubstring2() {
        String testString = "I'd b3tThreer put s0me d161ts in this 5tr1n6, right?";
        mycustomstring.setString(testString);
        mycustomstring.convertDigitsToNamesInSubstring(testString.length(), 0);
    }

    //When the length does not subtract 1
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertDigitsToNamesInSubstring3() {
        String testString = "I'd b3tThreer put s0me d161ts in this 5tr1n6, right?";
        mycustomstring.setString(testString);
        mycustomstring.convertDigitsToNamesInSubstring(0, testString.length());
    }

    //String is not set
    @Test(expected = NullPointerException.class)
    public void testConvertDigitsToNamesInSubstring4() {
        mycustomstring.convertDigitsToNamesInSubstring(0, mycustomstring.getString().length()-1);
    }

    //Start and end cannot be the same
    @Test(expected = NullPointerException.class)
    public void testConvertDigitsToNamesInSubstring5() {
        mycustomstring.convertDigitsToNamesInSubstring(1, 1);
    }

    //Intentional fail, string should not be the same
    @Test
    public void testConvertDigitsToNamesInSubstring6() {
        String testString = "I'd b3tThreer put s0me d161ts in this 5tr1n6, right?";
        mycustomstring.setString(testString);
        mycustomstring.convertDigitsToNamesInSubstring(0, testString.length()-1);
        assertNotSame("I'd b3tThreer put s0me d161ts in this 5tr1n6, right?", mycustomstring.getString());
    }

    //myCustomString is not Null
    @Test
    public void testConvertDigitsToNamesInSubstring7() {
        String testString = "I'd b3tThreer put s0me d161ts in this 5tr1n6, right?";
        mycustomstring.setString(testString);
        mycustomstring.convertDigitsToNamesInSubstring(0, testString.length()-1);
        assertNotNull(mycustomstring);
    }

    //Negative indexes
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertDigitsToNamesInSubstring8() {
        String testString = "I'd b3tThreer put s0me d161ts in this 5tr1n6, right?";
        mycustomstring.setString(testString);
        mycustomstring.convertDigitsToNamesInSubstring(-1, -1);
    }

}