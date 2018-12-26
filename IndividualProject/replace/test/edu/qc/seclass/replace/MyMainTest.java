package edu.qc.seclass.replace;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MyMainTest {

    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {

        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);

    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    private File createTmpFile() throws IOException {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();//delete file on exit
        return tmpfile;
    }

    private File createInputFile1() throws Exception {//tempFile2
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Bill,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!");

        fileWriter.close();
        return file1;
    }

    private File createInputFile2() throws Exception {//tempFile2 with info
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Bill,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice");

        fileWriter.close();
        return file1;
    }

    private File createInputFile3() throws Exception {//tempFile3 with info
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Bill, have you learned your abc and 123?\n" +
                "It is important to know your abc and 123," +
                "so you should study it\n" +
                "and then repeat with me: abc and 123");

        fileWriter.close();
        return file1;
    }


    private File createInputFile5() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Some file testing");

        fileWriter.close();
        return file1;
    }

    private File createInputFile6() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("This is Gabby\n"+ "from 370\n"+ "tetsing here");
        fileWriter.close();
        return file1;
    }

    //for character test
    private File createInputFile7() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("'[];'[]");

        fileWriter.close();
        return file1;
    }

    //for character test
    private File createInputFile8() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("'[];\n"+ "'[];\n"+ "';/'");
        fileWriter.close();
        return file1;
    }

    private File createInputFile9() throws Exception{

        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);
        fileWriter.close();
        return file1;
    }

    //get info in file
    private String getFileContent(String filename) {
        String info = null;
        try {
            info = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    /****************************************************************************************************
     *
     * Test Cases Below: From TSL and New Cases
     *
     * ***************************************************************************************************/


    /*********************************************************************************************************/


    /******************************************************************************************************
     *
     * Test cases from TSL
     *
     * ****************************************************************************************************/

    //Test case 10
    @Test
    public void mainTest0() throws Exception {

        File file = createInputFile1();
        String args[] = { "Bill", "gabby", "bill", "mike", "--", file.getPath()};
        Main.main(args);

        String expected ="Howdy gabby,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy mike\" again!";
        String actual1 = getFileContent(file.getPath());
        assertEquals(expected, actual1);
    }

    //Test case 9
    @Test
    public void mainTest1() throws Exception {

        File file = createInputFile1();
        //file.delete();

        String args[] = {"Bill", "John", "--", file.getPath()};
        Main.main(args);

        String expected ="Howdy John,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(file.getPath());
        assertEquals("Files differ", expected, actual1);
    }

    //Test case 5
    @Test
    public void mainTest2() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-i", "", "hello", "--", inputFile1.getPath()};

        Main.main(args);

        assertEquals("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- <filename> [<filename>]*", errStream.toString().trim());
    }

    //Test case 4
    @Test
    public void mainTest3() throws Exception {

        File inputFile1 = createInputFile1();
        String args[] = { "tom", "love", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //Test case 25
    @Test
    public void mainTest4() throws Exception {

        File inputFile1 = createInputFile6();

        String args[] = {"-i", "gabby", ";", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "This is ;\n"+ "from 370\n"+ "tetsing here";
        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck"))); //no backup

    }

    //Test case 38
    @Test
    public void mainTest5() throws Exception {

        File inputFile1 = createInputFile7();

        String args[] = {";", "gabby", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "'[]gabby'[]";
        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck"))); //no backup

    }

    //Test case 33
    @Test
    public void mainTest6() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile3();


        String args[] = {"a", "  ", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is    test file for the repl  ceWithoutComm  nds utility\n" +
                "Let's m  ke sure it h  s   t le  st    few lines\n" +
                "so th  t we c  n cre  te some interesting test c  ses...\n" +
                "And let's s  y \"howdy bill\"   g  in!";

        String expected2= "Howdy Bill, h  ve you le  rned your   bc   nd 123?\n" +
                "It is import  nt to know your   bc   nd 123," +
                "so you should study it\n" +
                "  nd then repe  t with me:   bc   nd 123";


        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertFalse(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));

    }

    //Test case 1
    @Test
    public void mainTest7() throws Exception {

        File inputFile1 = createInputFile9();


        String args[] = {inputFile1.getPath()};

        Main.main(args);

        String expected1 = "";


        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Files should be the same", expected1, actual1);
    }

    //Test case 7
    @Test
    public void mainTest8() throws Exception {

        File inputFile1 = createInputFile5();


        String args[] = {inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Some file testing";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Files are not the same!", expected1, actual1);
    }

    //Test case 8
    @Test
    public void mainTest9() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-b", "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
    }

    //Test case 45
    @Test
    public void mainTest10() throws Exception {

        File inputFile1 = createInputFile6();

        String args[] = {"-i","-f",  "gabby", "gabz", "--", inputFile1.getPath() };

        Main.main(args);

        String expected1 = "This is gabz\n"+ "from 370\n"+ "tetsing here";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }



    /****************************************************************************************************
     *
     * Newly Created tests
     *
     * ***************************************************************************************************/



    //New test case to Test for backup -b
    @Test
    public void mainTest11() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-b", "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));


    }

    //New test case to test for backup -b, and -f
    @Test
    public void mainTest12() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();


        String args[] = {"-b", "-f", "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";


        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());


        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));


    }

    //New test case to test for backup -b and -l
    @Test
    public void mainTest13() throws Exception {

        File inputFile2 = createInputFile2();
        File inputFile3 = createInputFile3();

        String args[] = {"-b", "-l", "Bill", "William", "--", inputFile2.getPath(), inputFile3.getPath()};
        Main.main(args);  //pass it to the main function

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";  //should change

        String expected3 = "Howdy Bill, have you learned your abc and 123?\n" +
                "It is important to know your abc and 123," +
                "so you should study it\n" +
                "and then repeat with me: abc and 123";


        String actual2 = getFileContent(inputFile2.getPath());
        String actual3 = getFileContent(inputFile3.getPath());


        assertEquals("Files are not the same", expected2, actual2);
        assertEquals("Files are not the same", expected3, actual3);
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile3.getPath() + ".bck")));
    }

    //New test case to test for backup -b and -i
    @Test
    public void mainTest14() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();
        File inputFile3 = createInputFile3();

        String args[] = {"-b","-i",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath(), inputFile3.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";

        String expected3 = "Howdy William, have you learned your abc and 123?\n" +
                "It is important to know your abc and 123," +
                "so you should study it\n" +
                "and then repeat with me: abc and 123";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());
        String actual3 = getFileContent(inputFile3.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertEquals("Files are not the same", expected3, actual3);

        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile3.getPath() + ".bck")));


    }

    //New test case to test for -f and -l occurrence
    @Test
    public void mainTest15() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-f","-l",  "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    //New test case to test for -f and -i occurrence in the specified order
    @Test
    public void mainTest16() throws Exception {
        File inputFile1 = createInputFile6();

        String args[] = {"-f","-i",  "gabby", "gabz", "--", inputFile1.getPath() };

        Main.main(args);

        String expected1 = "This is gabz\n"+ "from 370\n"+ "tetsing here";


        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    //New test case to test for -l and -i
    @Test
    public void mainTest17() throws Exception {

        File inputFile1 = createInputFile6();

        String args[] = {"-f","-i",  "Gabby", "gabz", "--", inputFile1.getPath() };
        Main.main(args);

        String expected1 = "This is gabz\n"+ "from 370\n"+ "tetsing here";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //New test case to test for -b -f -l
    @Test
    public void mainTest18() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-b", "-f","-l",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";


        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());
        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));

    }

    //New test case to test for -b -f -i
    @Test
    public void mainTest19() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-b", "-f","-i",  "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //New test case to test for -b -l -i
    @Test
    public void mainTest20() throws Exception {

        File inputFile1 = createInputFile6();

        String args[] = {"-b", "-f","-i",  "gabby", "gabz", "--", inputFile1.getPath() };
        Main.main(args);

        String expected1 = "This is gabz\n"+ "from 370\n"+ "tetsing here";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    //New test case to test for -f -l -i
    @Test
    public void mainTest21() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-f","-l", "-i", "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    //New test case to test for -b,  -f,  -l, -i
    @Test
    public void mainTest22() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-b", "-f","-l", "-i", "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }


    //New test case to test. Purpose: from string = special characters, to string = special characters
    @Test
    public void mainTest23() throws Exception {
        File inputFile1 = createInputFile7();

        String args[] = {"[", "gabby", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "'[];'[]";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }


    //New test case. Purpose:  from string = space, to string = no-space
    @Test
    public void mainTest24() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {" ", "", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "HowdyBill,\n" +
                "ThisisatestfileforthereplaceWithoutCommandsutility\n" +
                "Let'smakesureithasatleastafewlines\n" +
                "sothatwecancreatesomeinterestingtestcases...\n" +
                "Andlet'ssay\"howdybill\"again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck"))); //no backup

    }


    ///New test case. Purpose: from string = special characters, to string = alphabetic, argument = l
    @Test
    public void mainTest25() throws Exception {

        File inputFile1 = createInputFile8();

        String args[] = {"-l", "/", "gabby", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "'[];\n"+  "'[];\n"+  "';gabby'";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -f -b
    @Test
    public void mainTest26() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();


        String args[] = {"-f", "-b", "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";


        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());
        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -l -b
    @Test
    public void mainTest27() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-l", "-b", "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);  //pass it to the main function

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));

    }


    ///New test case. Purpose: test different operation of the order  -l -f
    @Test
    public void mainTest28() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-l","-f",  "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";


        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);

        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertFalse(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));


    }

    ///New test case. Purpose: test different operation of the order  -i -f
    @Test
    public void mainTest29() throws Exception {
        File inputFile1 = createInputFile6();

        String args[] = {"-i","-f",  "gabby", "gabz", "--", inputFile1.getPath() };

        Main.main(args);

        String expected1 = "This is gabz\n" + "from 370\n" + "tetsing here";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  --i -l
    @Test
    public void mainTest30() throws Exception {
        File inputFile1 = createInputFile6();

        String args[] = {"-i","-f",  "gabby", "gabz", "--", inputFile1.getPath() };
        Main.main(args);

        String expected1 = "This is gabz\n" + "from 370\n" + "tetsing here";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -l -f -b
    @Test
    public void mainTest31() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-l", "-f","-b",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());


        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
    }

    ///New test case. Purpose: test different operation of the order  -b -l -f
    @Test
    public void mainTest33() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();



        String args[] = {"-b", "-l","-f",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());


        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));


    }

    ///New test case. Purpose: test different operation of the order  -l -b -f
    @Test
    public void mainTest34() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();


        String args[] = {"-l", "-b","-f",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";


        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -f -b -l
    @Test
    public void mainTest35() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();



        String args[] = {"-f", "-b","-l",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";




        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());


        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));


    }

    ///New test case. Purpose: test different operation of the order  -f -l -b
    @Test
    public void mainTest36() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();


        String args[] = {"-f", "-l","-b",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";


        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());


        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));


    }

    ///New test case. Purpose: test different operation of the order  -i -f -b
    @Test
    public void mainTest37() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-i", "-f","-b",  "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    ///New test case. Purpose: test different operation of the order  -i -b -f
    @Test
    public void mainTest38() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-i", "-b","-f",  "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    ///New test case. Purpose: test different operation of the order  --b -i -f
    @Test
    public void mainTest39() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();


        String args[] = {"-b", "-i","-f",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2="Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -f -b -i
    @Test
    public void mainTest40() throws Exception {

        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();


        String args[] = {"-f", "-b","-i",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2="Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -f -i -b
    @Test
    public void mainTest41() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();


        String args[] = { "-i","-b",  "Bill", "William", "--", inputFile1.getPath(), inputFile2.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String expected2="Howdy William,\n" +
                "This is another test file for the replaceWithoutCommands utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy William\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertEquals("Files are not the same", expected2, actual2);
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));


    }

    ///New test case. Purpose: test different operation of the order  -i -l -f
    @Test
    public void mainTest42() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-i","-l", "-f", "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -i -l -f -b
    @Test
    public void mainTest43() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-i", "-l","-f", "-b", "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    ///New test case. Purpose: test different operation of the order  -b -f -i -l
    @Test
    public void mainTest44() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-b", "-f","-i", "-l", "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }


    ///New test case. Purpose: test different operation of the order  -f -b -l -i
    @Test
    public void mainTest45() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-f", "-b","-l", "-i", "Bill", "William", "--", inputFile1.getPath()};

        Main.main(args);

        String expected1 = "Howdy William,\n" +
                "This is a test file for the replaceWithoutCommands utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy William\" again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("Files are not the same", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

    }

    //New test case. Purpose: length of the to string longer than the file
    @Test
    public void mainTest46() throws Exception {

        File file = createInputFile5();

        String args[] = {"file", "no file", "--",file.getPath()};

        Main.main(args);

        String actual = getFileContent(file.getPath());

        String expected="Some no file testing";

        assertEquals("Files are not the same", expected, actual);
        assertFalse(Files.exists(Paths.get(file.getPath() + ".bck")));

    }
}
