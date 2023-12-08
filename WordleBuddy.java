import java.util.Scanner;
import java.io.File;
import java.util.*;
import java.lang.Math;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordleBuddy {
    public int getGuessesUsed() {
        int guessesUsed = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Did you beat today's Wordle?");
        String answer = scan.nextLine();
        if(answer.equalsIgnoreCase("yes")){
            System.out.println("How many guesses did it take? (answer in words)");
            answer = scan.nextLine();
            if(answer.equalsIgnoreCase("one"))
                guessesUsed = 1;
            else if(answer.equalsIgnoreCase("two"))
                guessesUsed = 2;
            else if(answer.equalsIgnoreCase("three"))
                guessesUsed = 3;
            else if(answer.equalsIgnoreCase("four"))
                guessesUsed = 4;
            else if(answer.equalsIgnoreCase("five"))
                guessesUsed = 5;
            else
                guessesUsed = 6;
        }
        if(answer.equalsIgnoreCase("no")){
            guessesUsed = 6;
        }
        return guessesUsed;
    }

    public String getAnswer() {
        Scanner scan = new Scanner(System.in);
        System.out.println("What was today's wordle answer?");
        return scan.nextLine();
    }

    ArrayList<String> guesses = new ArrayList<>();
    public void getGuesses(int guessesUsed) {
        for (int i = 1; i <= guessesUsed; i++) {
            Scanner scan = new Scanner(System.in);
            System.out.println("What was your guess #" + i);
            guesses.add(scan.nextLine());
        }

    }
    public static void main(String[] args) {
        ArrayList<String> possibleAnswers = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("answers.txt");
         
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
         
            while ((line = bufferedReader.readLine()) != null) {
                possibleAnswers.add(line);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        System.out.println(possibleAnswers.get(0));


        WordleBuddy wb = new WordleBuddy();
        int guessesUsed = wb.getGuessesUsed();
        wb.getGuesses(guessesUsed);
        System.out.println(wb.guesses);
   }
}
