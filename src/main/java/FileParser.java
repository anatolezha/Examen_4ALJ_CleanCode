import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileParser {
    private ArrayList<String> parsedCodes;
    private File inputFile;
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
        inputFile = normalizeEOL(new File(fileName));
        parsedCodes = parseFile(new BufferedReader(new FileReader(inputFile)));
    }
    
    public FileParser(){ }
    
    public File normalizeEOL(File inputFile) throws IOException {
        
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String newContent= "";
        
        String line ;
        while ((line = reader.readLine()) != null) {
            newContent += line + "\n";
        }
        reader.close();
        
        BufferedWriter writer =new BufferedWriter(new FileWriter(inputFile));
        writer.write(newContent);
        writer.close();
        return inputFile;
    }
    
    public ArrayList<String> parseFile(BufferedReader inputFile) throws IOException {
        ArrayList<String> parserArray = new ArrayList<>();
        char[][] currentEntry = new char[3][27];
        int charCode;
        int lineBuffer = 0, columnBuffer=0;

        while ((charCode = inputFile.read()) >= 0) {
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
        inputFile.close();
        return parserArray;
    }

    public String parseOneEntry(char[][] currentEntry) {
        String parsedCode = "";
        String code = "";

        for (int i = 0; i < currentEntry[0].length; i+=3) {
            for (int j = 0; j < 3; j++) {
                    code += "" + currentEntry[j][i] + currentEntry[j][i+1] + currentEntry[j][i+2];
            }
            if(Dictionary.get(code) != null) {
                parsedCode += Dictionary.get(code);
            } else {
                return null;
            }
            code = "";
        }
        return parsedCode;
    }

    public ArrayList<String> getParsedCodes() {
        return parsedCodes;
    }
}
