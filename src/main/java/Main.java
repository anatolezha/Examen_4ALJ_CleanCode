import java.io.*;

public class Main {

    public static void main(String[] args) {
        if(args[0] == null){
            System.out.println("Veuillez rentrer un nom de fichier en argument du programme");
        } else {
            try {
                FileParser fileParser = new FileParser(args[0]);
                System.out.println(fileParser.getParsedCodes().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
