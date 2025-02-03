import java.util.Scanner;

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

        System.out.println("New");
        System.out.println("-------------------------------------------------");
        System.out.println("1. Note");
        System.out.println("2. Journal");
        System.out.println("-------------------------------------------------");

        choice = scanner.nextInt();

        switch(choice){
            case 1:
                System.out.println("New Note");
                break;
            case 2:
                System.out.println("New Journal");
                break;
        }
        System.out.println("-------------------------------------------------");
        input = scanner.next();
        System.out.println(input);
        System.out.println("-------------------------------------------------");
    }
}
