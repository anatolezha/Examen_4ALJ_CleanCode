import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            FileParser fileParser = new FileParser("code.txt");
            System.out.println(fileParser.getParsedCodes().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
