public class WordleBuddyMain {
    public static void main(String[] args) {
        WordleBuddy wb = new WordleBuddy();
        wb.setUp();
        int guessesUsed = wb.getGuessesUsed();
        wb.getGuesses(guessesUsed);
        wb.getColorOfGuess();
        wb.returnGuessScore(guessesUsed);
   }
}
