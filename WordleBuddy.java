import java.util.Scanner;
import java.io.File;
import java.util.*;
import java.lang.Math;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordleBuddy {
    ArrayList<String> possibleAnswers = new ArrayList<>();
    public void setUp() {
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
    }

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

    ArrayList<String> guesses = new ArrayList<>();
    public void getGuesses(int guessesUsed) {
        for (int i = 1; i <= guessesUsed; i++) {
            Scanner scan = new Scanner(System.in);
            System.out.println("What was your guess #" + i);
            guesses.add(scan.nextLine());
        }
        System.out.println(guesses);
    }

    ArrayList<String> colors = new ArrayList<>();
    public void getColorOfGuess() {
        for (int i = 0; i <= guesses.size(); i++) {
            for (int j = 0; j <= 4; j++) {
                Scanner scan = new Scanner(System.in);
                System.out.println("'" + guesses.get(i) + "'" + " was your guess #" + (i + 1) +"; What color was the letter " + "'" + (guesses.get(i)).substring(j, j+1) +"'? ('green', 'yellow', 'grey')");
                colors.add(scan.nextLine());
            }
        }
        System.out.println(colors);
    }

    public void returnGuessScore(int guessNumber) {
        int x = possibleAnswers.size();
        ArrayList<String> newPossibleAnswers = new ArrayList<>();
        for (int i = 1; i <= guessNumber + 1; i++) { //each guess
            for (int j = 0; j <= 4; i++) { //each 5 set of colors for that guess
                for (int k = 0; k <= x; j++){ //goes through each word from possible guesses
                    if (colors.get(j * i).equals("grey")) {
                        if (newPossibleAnswers.get(k).contains(colors.get(j * i))) {
                            newPossibleAnswers.remove(k);
                        }
                    }
                    if (colors.get(j * i).equals("yellow")) {
                        if (!(newPossibleAnswers.get(k).contains(colors.get(j * i)))) {
                            newPossibleAnswers.remove(k);
                        }
                    }
                    if (colors.get(j * i).equals("green")) {
                        if (!(newPossibleAnswers.get(k).substring(j, j+1).equals(guesses.get(i - 1).substring(j, j+1))))
                            newPossibleAnswers.remove(k);
                    }
                }
            }
            int y = possibleAnswers.size() - newPossibleAnswers.size();
            double z = y / possibleAnswers.size();
            ArrayList<String> possibleAnswers = newPossibleAnswers;
            System.out.println("This guess: " + guesses.get(i - 1) + " got a score of " + z + "! You eleminated " + y + " out of " + x + " possible answers");
        }
    }


    public void runBuddy() {
        for (int i = 1; i <= getGuessesUsed(); i++) {
            returnGuessScore(i);
        }
    }
}
