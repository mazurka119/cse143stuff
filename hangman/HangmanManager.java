//Mel Steppe
//Stuart Reges
//TA: Kelly Ho
//2/4/2022
//Hw #4: Hangman Manager
//Class to manage a game of evil hangman by manipulating the game so that a word isn't actually picked until the end.
import java.util.*;

public class HangmanManager {
   private int guessesLeft;                  //number of guesses the player has left
   private String toDisplay;                 //pattern of guessed letters that is displayed
   private Set<String> wordOptions;          //available words for the answer to be
   private Set<Character> lettersGuessed;    //letters that player has guessed
   
  /*
   * Constructs a HangmanManager with a displayed pattern of the designated length
   * @throws IllegalArgumentException if length is less than 1 or max is negative
   * @param dictionary - possible words that could be used in hangman game
   * @param length - length of the word to be guessed
   * @param max - maximum number of guesses player can have
   */
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      guessesLeft = max;
      wordOptions = new TreeSet<>();
      lettersGuessed = new TreeSet<>(); 
      for (String word : dictionary ) {
         if (word.length() == length) {
            wordOptions.add(word);
         }
      }
      toDisplay = "-"; 
      for ( int i = 1; i < length; i++ ) {
         toDisplay += " -";
      }  
   }

  /*
   * Gives access to the current words in consideration by hangman manager
   * @pre - wordOptions must not be empty
   */
   public Set<String> words() {
      return wordOptions;
   }

  /*
   * Returns the number of guesses the player has left
   * @pre - number of guesses must not be <= 0
   */
   public int guessesLeft() {
      return guessesLeft; 
   
   }
   
  /*
   * Returns the letters that have already been guessed by the player
   * @pre lettersGuessed should not be empty
   */
   public Set<Character> guesses() {
      return lettersGuessed;
   }
   
  /*
   * Returns the pattern that should be displayed
   * @throws IllegalArgumentException if wordOptions is empty
   */
   public String pattern() { 
      if (wordOptions.isEmpty()) {
         throw new IllegalArgumentException();
      } 
      return toDisplay; 
   }

  /*
   * Checks if the letter guessed is found in the set of words with the largest size
   * @returns the number of occurrences of the guess if it is a correct guess and 0
   *  if it is an incorrect guess
   * @pre ignores case of character guessed
   * @throws IllegalStateException if there are no guesses left or wordOptions is empty
   * @throws IllegalArgumentException if the letter has already been guessed
   * @param guess - letter that the player has guessed
   */
   public int record(char guess) { 
      if (guessesLeft < 1 || wordOptions.isEmpty()) {
         throw new IllegalStateException();
      } else if (lettersGuessed.contains(guess)) {
         throw new IllegalArgumentException();
      } 
      lettersGuessed.add(guess);
      Map<String, Set<String>> finder = new TreeMap<>();
      for ( String word : wordOptions) {
         String pattern = createPattern(guess, word);           
         if (!finder.containsKey(pattern)) {
            Set<String> wordSet = new TreeSet<>();
            finder.put(pattern, wordSet);
         }
         finder.get(pattern).add(word);
      }
      int maxKey = 0;
      for (String keyName : finder.keySet()) {
         if (finder.get(keyName).size() > maxKey) {
            wordOptions.clear();
            wordOptions.addAll(finder.get(keyName));
            toDisplay = keyName;
            maxKey = finder.get(keyName).size();
         }
      }
      return decisionMaker(guess);
   }
   
  /*
   * Creates the pattern of dashes and letters for each guess
   * @pre String word should not be empty, ignores case of guess and word
   * @param guess - letter guessed by player
   * @param word - word for pattern to be created from
   */
   private String createPattern(char guess, String word) { 
      String pattern = "";
      for (int i = 0; i < word.length(); i++) {
         if (lettersGuessed.contains(word.charAt(i))) { 
            pattern += (word.charAt(i) + " ");
         } else if (word.charAt(i) != guess) {
            pattern += "- ";
         } else {
            pattern += (guess + " ");
         }
      }
      return pattern;
             
   }
   
  /*
   * Decides if the player's guess is correct or incorrect, and how many guesses they have left
   * @returns number of the guesses occurrences if it comes from the largest set (correct guess)
   * @returns 0 if it is not in the largest set (incorrect guess)
   * @pre guesses left should not be 0, toDisplay should not be empty
   * @pre ignores case of character guessed
   */
   private int decisionMaker(char guess) { 
      int guessOccurrence = 0; 
      for ( int i = 0; i < toDisplay.length(); i ++) {
         if (toDisplay.charAt(i) == guess) {
            guessOccurrence++;
         }
      }
      if (guessOccurrence == 0) {
         guessesLeft--;
      }
      return guessOccurrence;
   }
}
