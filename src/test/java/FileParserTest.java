 
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    private FileParser fileParser = new FileParser();

    @TempDir
    static Path tempDir;
    
    @BeforeAll
    static void setup() {
        assertTrue(Files.isDirectory(tempDir));
        
    }
    
    @Test
    public void TestParseOneEntryFunctionWithValidArrayShouldReturnString(){
        char[][] inputArray = new char[][]{
                {' ',' ',' ',' ','_',' ',' ','_',' ',' ',' ',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' '},
                {' ',' ','|',' ','_','|',' ','_','|','|','_','|','|','_',' ','|','_',' ',' ',' ','|','|','_','|','|','_','|'},
                {' ',' ','|','|','_',' ',' ','_','|',' ',' ','|',' ','_','|','|','_','|',' ',' ','|','|','_','|',' ','_','|'},
        };
        String expectedResult = "123456789";
        
        assertEquals(expectedResult, fileParser.parseOneEntry(inputArray));
    }

    @Test
    public void TestParseOneEntryFunctionWithInvalidArrayShouldReturnNull(){
        FileParser fileParser = new FileParser();
        char[][] inputArray = new char[][]{
                {' ',' ',' ',' ','_',' ',' ','|',' ',' ',' ',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' '},
                {' ',' ','|',' ','_','_',' ','_','|','|','_','|','|','_',' ','|','_',' ',' ',' ','|','|','_','|','|','_','|'},
                {' ',' ','|','|','|',' ',' ','_','|',' ',' ','|',' ','_','|','|','_','|',' ',' ','|','|','_','|',' ','_','|'},
        };

        assertEquals(null, fileParser.parseOneEntry(inputArray));
    }
    
    @Test
    public void parseFileShouldReturnOneValidStringIfValidEntryInFile(){
        Path output  = tempDir.resolve("testFile.txt");

        try {
            FileWriter fileWriter = new FileWriter(output.toString());
            fileWriter.write(
                    "    _  _     _  _  _  _  _ \n" +
                    "  | _| _||_||_ |_   ||_||_|\n" +
                    "  ||_  _|  | _||_|  ||_| _|\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader inputReader;
        ArrayList<String> expectedResult = new ArrayList<>(){{
            add("123456789");
        }};
        
        try {
            inputReader = new BufferedReader( new FileReader(output.toString()));
            assertEquals(expectedResult, fileParser.parseFile(inputReader));
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseFileShouldReturnNullStringIfInvalidEntryInFile(){
        Path output  = tempDir.resolve("testFile.txt");

        try {
            FileWriter fileWriter = new FileWriter(output.toString());
            fileWriter.write(
                        "    _  _     _  _  _  _  _ \n" +
                            "  | _| _|| ||_ |_   ||_|| |\n" +
                            "  ||_  _|  | |||_|   | | _|\n" +
                            "");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader inputReader;
        ArrayList<String> expectedResult = new ArrayList<>(){{
            add(null);
        }};

        try {
            inputReader = new BufferedReader( new FileReader(output.toString()));
            assertEquals(expectedResult, fileParser.parseFile(inputReader));
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseFileShouldReturnValidStringsIfOnlyValidEntriesInFile(){
        Path output  = tempDir.resolve("testFile.txt");

        try {
            FileWriter fileWriter = new FileWriter(output.toString());
            fileWriter.write(
                    "    _  _     _  _  _  _  _ \n" +
                            "  | _| _||_||_ |_   ||_||_|\n" +
                            "  ||_  _|  | _||_|  ||_| _|\n");
            for (int i = 0; i < 99; i++) {
                fileWriter.append(
                                "\n    _  _     _  _  _  _  _ \n" +
                                "  | _| _||_||_ |_   ||_||_|\n" +
                                "  ||_  _|  | _||_|  ||_| _|\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader inputReader;
        ArrayList<String> expectedResult = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            expectedResult.add("123456789");
        }

        try {
            inputReader = new BufferedReader( new FileReader(output.toString()));
            assertEquals(expectedResult, fileParser.parseFile(inputReader));
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void parseFileShouldReturnNullStringsIfInvalidEntriesInFile(){
        Path output  = tempDir.resolve("testFile.txt");

        try {
            FileWriter fileWriter = new FileWriter(output.toString());
            fileWriter.write(
                    "    _  _     _  _  _  _  _ \n" +
                            "  | _| _||_||_ |_   ||_||_|\n" +
                            "  ||_  _|  | _||_|  ||_| _|\n");
            for (int i = 0; i < 99; i++) {
                if(i%2 == 0){
                    fileWriter.write(
                            "\n    _  _     _  _  _  _  _ \n" +
                                    "  | _| _|| ||_ |_   ||_|| |\n" +
                                    "  ||_  _|  | |||_|   | | _|\n" +
                                    "");
                } else {
                    fileWriter.append(
                            "\n    _  _     _  _  _  _  _ \n" +
                                    "  | _| _||_||_ |_   ||_||_|\n" +
                                    "  ||_  _|  | _||_|  ||_| _|\n");
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader inputReader;
        ArrayList<String> expectedResult = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            if(i%2 == 0){
                expectedResult.add("123456789");
            } else {
                expectedResult.add(null);
            }
            
        }

        try {
            inputReader = new BufferedReader( new FileReader(output.toString()));
            assertEquals(expectedResult, fileParser.parseFile(inputReader));
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void normalizeEOLShouldReturnFileWithNoCRInEOL() throws IOException {
        Path output  = tempDir.resolve("testFile.txt");
        Path expected = tempDir.resolve("exepected.txt");
        FileWriter fileWriter = new FileWriter(output.toString());
        fileWriter.write(
                "    _  _     _  _  _  _  _ \r\n" +
                        "  | _| _||_||_ |_   ||_||_|\r\n" +
                        "  ||_  _|  | _||_|  ||_| _|\r\n");
        fileWriter.close();
        fileWriter = new FileWriter(expected.toString());
        fileWriter.write(
                "    _  _     _  _  _  _  _ \n" +
                        "  | _| _||_||_ |_   ||_||_|\n" +
                        "  ||_  _|  | _||_|  ||_| _|\n");
        fileWriter.close();

        
        assertTrue(FileUtils.contentEquals(fileParser.normalizeEOL(new File(output.toString())), new File(expected.toString())), "The file are differents");

        
    }
}