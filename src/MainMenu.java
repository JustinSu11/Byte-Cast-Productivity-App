import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class MainMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int choice;

        System.out.println("Pick It Up");
        System.out.println("-------------------------------------------------");
        System.out.println("1. New");
        System.out.println("2. Open");
        System.out.println("-------------------------------------------------");

        choice = scanner.nextInt();

        switch(choice){
            case 1:
                subMenuForNew(args);
                break;
        }
    }
    public static void subMenuForNew(String[] args){
        Scanner scanner = new Scanner(System.in);

        int choice;
        String input;
        String docName;
        String fileName;

        System.out.println("New");
        System.out.println("-------------------------------------------------");
        System.out.println("1. Note");
        System.out.println("2. Journal");
        System.out.println("-------------------------------------------------");

        choice = scanner.nextInt();

        //Print out message appropriate for the choice prompting user to enter a name for the file
        switch(choice){
            case 1:
                System.out.println("Give a name for a new note: ");
                break;
            case 2:
                System.out.println("Give a name for a new journal: ");
                break;
        }

        //Takes input from user to create a .txt file based off the name given
        docName = scanner.next();
        fileName = docName + ".txt";

        //User enters note body here
        System.out.println("Enter your text here: ");
        input = scanner.next();
        scanner.close();

        //Prints out a bare-bone interface to for user to visualize a note
        System.out.println(docName);
        System.out.println("-------------------------------------------------");
        System.out.println(input);
        System.out.println("-------------------------------------------------");
        /*
        Saves the input into the working directory named after the filename
        need to make a folder for notes and a folder for journals to hold the saved files for now
        */

        try (FileWriter newNote = new FileWriter(fileName)) {
            newNote.write(input);
            System.out.println("File was saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
