import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileParser {
    private ArrayList<String> parsedCodes;
    private final Map<String, String> Dictionary = new HashMap<>(){
        {
            put("     |  |", "1");
            put(" _  _||_ ", "2");
            put(" _  _| _|", "3");
            put("   |_|  |", "4");
            put(" _ |_  _|", "5");
            put(" _ |_ |_|", "6");
            put(" _   |  |", "7");
            put(" _ |_||_|", "8");
            put(" _ |_| _|", "9");

        }
    };

    public FileParser(String fileName) throws IOException {
        normalizeEOL(fileName);
        parsedCodes = parseFile();
    }

    private void normalizeEOL(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        BufferedWriter writer = new BufferedWriter( new FileWriter("normalizeFile.txt"));
        String line ;
        while ((line = reader.readLine()) != null) {
            writer.append(line + "\n");
        }
        writer.close();
        reader.close();
    }
    
    private ArrayList<String> parseFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("normalizeFile.txt");
        ArrayList<String> parserArray = new ArrayList<>();
        char[][] currentEntry = new char[3][27];
        int charCode;
        int lineBuffer = 0, columnBuffer=0;

        while ((charCode = fileInputStream.read()) >= 0) {
            if(lineBuffer >= 3) {
                lineBuffer = 0;
                columnBuffer = 0;
                parserArray.add(parseOneEntry(currentEntry));
            } else if (charCode == 10) {
                lineBuffer += 1;
                columnBuffer = 0;
            } else {
                currentEntry[lineBuffer][columnBuffer] = (char)charCode;
                columnBuffer += 1;
            }
        }
        parserArray.add(parseOneEntry(currentEntry));

        return parserArray;
    }

    private String parseOneEntry(char[][] currentEntry) {
        String parsedCode = "";
        String code = "";

        for (int i = 0; i < currentEntry[0].length; i+=3) {
            for (int j = 0; j < 3; j++) {
                    code += "" + currentEntry[j][i] + currentEntry[j][i+1] + currentEntry[j][i+2];
            }
            parsedCode += Dictionary.get(code);
            code = "";
        }
        return parsedCode;
    }

    public ArrayList<String> getParsedCodes() {
        return parsedCodes;
    }
}
