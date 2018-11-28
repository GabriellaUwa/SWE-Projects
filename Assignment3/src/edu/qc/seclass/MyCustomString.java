package edu.qc.seclass;

import java.util.HashMap;
import java.util.Scanner;

public class MyCustomString implements MyCustomStringInterface {

    String myString;

    public String getString() {
        return myString;
    }

    public void setString(String string) {
        this.myString = string;
    }

    public int countNumbers() {

        if (getString() == null) {
            throw new NullPointerException("String is Null");
        } else if (getString().isEmpty()) {
            return 0;
        } else {
            int count = 0;
            for (int i = 0; i < getString().length(); i++) {
                if (Character.isDigit(getString().charAt(i))) {
                    count++;
                }
            }
            return count;
        }
    }

    public String getEveryNthCharacterFromBeginningOrEnd(int n, boolean startFromEnd) {

        if (n <= 0) {
            throw new IllegalArgumentException("n is less than or equal to 0");
        }
        if (getString() == null && n > 0) {
            throw new NullPointerException("String is null");
        }
        if (n > getString().length()) {
            return "";
        }
        if (startFromEnd) {
            String str = "";
            for (int i = getString().length() - n; i >= 0; i -= n) {
                str += getString().charAt(i);
            }
            return str;
        } else{
            String str = "";
            for (int i = n-1; i < getString().length(); i += n) {
                str += Character.toString(getString().charAt(i));
            }
            return str;
        }
    }

    public void convertDigitsToNamesInSubstring(int startPosition, int endPosition) {

        if (startPosition > endPosition) {
            throw new IllegalArgumentException("startPosition > endPosition");
        }
        if (startPosition <= endPosition) {
            if (getString() == null && startPosition > 0 && endPosition > 0) {
                throw new NullPointerException("String is null");
            }
            if (startPosition >= 0 && endPosition < getString().length()) {
                HashMap<Character, String> map = new HashMap<>();
                map.put('0', "Zero");
                map.put('1', "One");
                map.put('2', "Two");
                map.put('3', "Three");
                map.put('4', "Four");
                map.put('5', "Five");
                map.put('6', "Six");
                map.put('7', "Seven");
                map.put('8', "Eight");
                map.put('9', "Nine");

                StringBuilder builder = new StringBuilder();
                for (int i = startPosition; i <= endPosition; i++) {
                    char c = myString.charAt(i);
                    if (map.get(c) != null && !map.get(c).isEmpty()) {
                        builder.append(map.get(c));
                    } else {
                        builder.append(c);
                    }
                }
                myString = builder.toString(); //properly appended value to be returned

            } else {
                throw new MyIndexOutOfBoundsException("Out of Bounds");
            }
        }
    }
}