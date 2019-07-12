 
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
    public void parseOneEntry_function_with_valid_array_should_return_string(){
        char[][] inputArray = new char[][]{
                {' ',' ',' ',' ','_',' ',' ','_',' ',' ',' ',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' '},
                {' ',' ','|',' ','_','|',' ','_','|','|','_','|','|','_',' ','|','_',' ',' ',' ','|','|','_','|','|','_','|'},
                {' ',' ','|','|','_',' ',' ','_','|',' ',' ','|',' ','_','|','|','_','|',' ',' ','|','|','_','|',' ','_','|'},
        };
        String expectedResult = "123456789";
        
        assertEquals(expectedResult, fileParser.parseOneEntry(inputArray));
    }

    @Test
    public void parseOneEntry_function_with_invalid_array_input_should_return_null(){
        FileParser fileParser = new FileParser();
        char[][] inputArray = new char[][]{
                {' ',' ',' ',' ','_',' ',' ','|',' ',' ',' ',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' ',' ','_',' '},
                {' ',' ','|',' ','_','_',' ','_','|','|','_','|','|','_',' ','|','_',' ',' ',' ','|','|','_','|','|','_','|'},
                {' ',' ','|','|','|',' ',' ','_','|',' ',' ','|',' ','_','|','|','_','|',' ',' ','|','|','_','|',' ','_','|'},
        };
        
        assertNull(fileParser.parseOneEntry(inputArray), "Entry is not null");
    }
    
    @Test
    public void parse_file_should_return_one_valid_string_if_valid_entry_in_file(){
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
    public void parse_file_should_return_null_string_if_invalid_entry_in_file(){
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
    public void parse_file_should_return_valid_strings_if_valid_entries_in_file(){
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
    public void parse_file_should_return_null_strings_if_invalid_entries_in_file(){
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
    public void normalize_EOL_should_return_file_with_no_CR_in_EOL() throws IOException {
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
    
    @Test
    public void stringToIntArray_should_return_int_array_with_correct_values_if_string_is_valid(){
        String input = "123456789";
        int[] expected = {1,2,3,4,5,6,7,8,9};
        
        assertArrayEquals(expected, fileParser.stringToIntArray(input));
    }

    @Test
    public void stringToIntArray_should_throw_IllegalArgumentException_if_input_contain_no_digit_char(){
        String input = "123456a89";

        assertThrows(IllegalArgumentException.class, () -> fileParser.stringToIntArray(input));
    }
    
    @Test
    public void check_sum_should_return_true_if_valid_code(){
        int[] input = {4,5,7,5,0,8,0,0,0};
        
        assertTrue(fileParser.checkSum(input), "The checksum is false");
        
    }
    
    
    @Test
    public void check_sum_should_return_false_if_invalid_code(){
        int[] input = {3,5,6,6,0,9,7,0,1};
            
        assertFalse(fileParser.checkSum(input), "The checksum is false");

    }
}