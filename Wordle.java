import java.util.Scanner;
import java.io.File;
import java.util.*;
import java.lang.Math;

public class Wordle {
		ArrayList<String> answers = new ArrayList<String>();
		ArrayList<String> guesses = new ArrayList<String>();
		ArrayList<String> used = new ArrayList<String>();
		TreeMap<Character , Integer> letters = new TreeMap<Character, Integer>();
		TreeMap<Character , Integer> noDupeLetters = new TreeMap<Character, Integer>();
		HashMap<String, Integer[]> guessList = new HashMap<String, Integer[]>();
		LinkedHashSet<Character> lettersInFinal = new LinkedHashSet<Character>();
		HashSet<Character> lettersNotInFinal = new HashSet<Character>();
		HashMap<Character, HashSet<Integer>> positionMapping = new HashMap<Character, HashSet<Integer>>();
		HashMap<Character, HashSet<Integer>> notPositionMapping = new HashMap<Character, HashSet<Integer>>();
		char[] fixedValues = new char[5];

		public Wordle() {
			try {
				Scanner aScan = new Scanner(new File("answers.txt"));
				Scanner bScan = new Scanner(new File("guesses.txt"));
				Scanner cScan = new Scanner(new File("used.txt"));
				while (aScan.hasNext()) {
					this.answers.add(aScan.next());
				}
				while (bScan.hasNext()) {
					this.guesses.add(bScan.next());
				}
				while (cScan.hasNext()) {
					String toAdd = cScan.next();
					this.used.add(toAdd);
					this.answers.remove(toAdd);
				}
			} catch (Exception e) {
				System.out.println("File does not exist");
			}
		}

	public static void main(String[] args) {
		Wordle wordle = new Wordle();

		/*
		
		wordle.countLetters();

		for (Map.Entry<Character, Integer> letter : wordle.letters.entrySet()) {
			System.out.println(letter.getKey() + " " + letter.getValue() + " " +  wordle.noDupeLetters.get(letter.getKey()));
		}
		*/
		//wordle.bestGuess();
/*		long startTime = System.nanoTime();
		System.out.println("Best First Word: " + wordle.firstGuess());
		long endTime = System.nanoTime();
		System.out.println("Elapsed time: " + Math.round((endTime-startTime)/1000000000.0));
		*/
		int[] hits;
		Scanner guessScan = new Scanner(System.in);
		while(wordle.answers.size() > 1) {
			System.out.println("What was your guess");
			String guess = guessScan.next();

			hits = sampleAnswer();
			
			



			wordle.process(guess, hits);


			wordle.makeGuess(guess, hits);

			
			System.out.println("Possible words: " + wordle.answers.size());
			for (int i = 0; i < wordle.answers.size(); i++) {
				System.out.println(wordle.answers.get(i));
			}
			if (wordle.answers.size() > 1) {
				System.out.println("Best guess: " + wordle.bestGuess());
			}
		}
	}

	public void process(String guess, int[] hits) {
		//this.guessList.add(guess, hits);
		for (int i = 0; i < 5; i++) {
			char currentChar = guess.charAt(i);
			if (hits[i] == 2) {
				if (!this.positionMapping.containsKey(currentChar)){
					this.positionMapping.put(currentChar, new HashSet<Integer>());
				}
				this.positionMapping.get(currentChar).add(i);
				fixedValues[i] = currentChar;
			} else if (hits[i] == 1) {
				this.lettersInFinal.add(currentChar);
				if (!this.notPositionMapping.containsKey(currentChar)) {
					this.notPositionMapping.put(currentChar, new HashSet<Integer>());
				}
				this.notPositionMapping.get(currentChar).add(i);
			} else {
				this.lettersNotInFinal.add(currentChar);
			}
		}
	}

	public String bestGuess() {
		double entropy = 0;
		int n = this.answers.size();
		int numToEliminate = 0;

		for (int i = 0; i < n; i++) {
			entropy -= (1.0/n)*Math.log(1.0/n);
		}
		double bestEntropy = entropy;
		String bestGuess = this.guesses.get(0);
		double entropyTotal;
		for (String guess : this.guesses) {
			//System.out.print(guess);
			entropyTotal = 0;
			for (String answer : this.answers) {
				numToEliminate = 0;
				int[] hits = testGuess(guess, answer);
				double thisEntropy = 0;
				for (String answerCheck : this.answers) {
					boolean eliminateThis = false;
					for (int i = 0; i < 5; i++) {
						char guessChar = guess.charAt(i);
						if (hits[i] == 2) {
							if (answerCheck.charAt(i) != guessChar) {
								eliminateThis = true;
							}
						} else if (hits[i] == 1) {
							if (answerCheck.indexOf(guessChar) == -1) {
								eliminateThis = true;
							}
						} else if (hits[i] == 0) {
							if (answerCheck.charAt(i) == guess.charAt(i)) {
								eliminateThis = true;
							}
						}
						char answerCheckChar = answerCheck.charAt(i);
						if (!eliminateThis && this.lettersNotInFinal.contains(answerCheckChar)) {
							eliminateThis = true;
						}
						if (!eliminateThis && fixedValues[i] != 0 && fixedValues[i] != answerCheckChar) {
							eliminateThis = true;
						}
						if (!eliminateThis && this.notPositionMapping.containsKey(answerCheckChar) && this.notPositionMapping.get(answerCheckChar).contains(i)) {
							eliminateThis = true;
						}
						if (!eliminateThis) {
							Object[] lettersInFinal = this.lettersInFinal.toArray();
							for (int lettersInFinalIndex = 0; lettersInFinalIndex < lettersInFinal.length; lettersInFinalIndex++) {
								char currentLetter = (char)lettersInFinal[lettersInFinalIndex];
								if (answerCheck.indexOf(currentLetter) == -1) {
									eliminateThis = true;
								}
							}
						}
					}
					if (eliminateThis) {
						numToEliminate++;
					}
				}
				//System.out.println(numToEliminate);
				for (int i = 0; i < n; i++) {
					if (i < (n-numToEliminate)) {
						thisEntropy -= (1.0/(n-numToEliminate)) * Math.log(1.0/(n-numToEliminate));
					}
				}
				entropyTotal += thisEntropy;
			}
			entropy = entropyTotal/n;
			//System.out.println(": " + entropy);
			if (entropy < bestEntropy && entropy > 0) {
				bestEntropy = entropy;
				bestGuess = guess;
			}
		}
		return bestGuess;
	}

	public static int[] sampleAnswer() {
		Scanner scan = new Scanner(System.in);
		int[] hits = new int[5];
		for (int i = 0; i < 5; i++) {
			System.out.println("What was the result for square " + i);
			hits[i] = scan.nextInt();
		}
		return hits;

	}

	public void makeGuess(String guess, int[] hits) {
		Object[] answersArray = this.answers.toArray();
		for (int index = 0; index < answersArray.length; index++) {
			String answer = (String)answersArray[index]; 
			for (int i = 0; i < 5; i++) {
				char guessChar = guess.charAt(i);
				if (hits[i] == 2) {
					if (answer.charAt(i) != guessChar) {
						this.answers.remove(answer);
					}

				} else if (hits[i] == 1) {
					if (answer.indexOf(guessChar) == -1) {
						this.answers.remove(answer);
					}
				} else if (hits[i] == 0) {
					if (answer.charAt(i) == guess.charAt(i)) {
						this.answers.remove(answer);
					}
				}
				char answerChar = answer.charAt(i);
				if (this.lettersNotInFinal.contains(answerChar)) {
					this.answers.remove(answer);
				}
				if (fixedValues[i] != 0 && fixedValues[i] != answerChar) {
					this.answers.remove(answer);
				}
				if (this.notPositionMapping.containsKey(answerChar) && this.notPositionMapping.get(answerChar).contains(i)) {
					this.answers.remove(answer);
				}
				Object[] lettersInFinal = this.lettersInFinal.toArray();
				for (int lettersInFinalIndex = 0; lettersInFinalIndex < lettersInFinal.length; lettersInFinalIndex++) {
					char currentLetter = (char)lettersInFinal[lettersInFinalIndex];
					if (answer.indexOf(currentLetter) == -1) {
						this.answers.remove(answer);
					}
				}
			}
		}
	}

	public String firstGuess() {
		double entropy = 0;
		int n = this.answers.size();
		System.out.println(n);
		int numToEliminate = 0;

		for (int i = 0; i < n; i++) {
			entropy -= (1.0/n)*Math.log(1.0/n);
		}
		int count = 0;
		double bestEntropy = entropy;
		String bestGuess = this.guesses.get(0);
		//System.out.println(entropy);
		double entropyTotal;
		double bestEntropyTotal = entropy * n;
		for (String guess : this.guesses) {
			
			System.out.println(guess);
			System.out.println("Best Guess: " + bestGuess);
			entropyTotal = 0;
			for (String answer : this.answers) {
				numToEliminate = 0;
				int[] hits = testGuess(guess, answer);
				double thisEntropy = 0;
				for (String answerCheck : this.answers) {
					boolean eliminateThis = false;
					for (int i = 0; i < 5; i++) {
						char guessChar = guess.charAt(i);
						if (hits[i] == 2) {
							if (answerCheck.charAt(i) != guessChar) {
								eliminateThis = true;
							}
						} else if (hits[i] == 1) {
							if (answerCheck.indexOf(guessChar) == -1) {
								eliminateThis = true;
							}
						} else if (hits[i] == 0) {
							if (answerCheck.charAt(i) == guess.charAt(i)) {
								eliminateThis = true;
							}
						}
					}
					if (eliminateThis) {
						numToEliminate++;
					}
				}
				for (int i = 0; i < n; i++) {
					double p = n-numToEliminate;
					if (i < p) {
						thisEntropy -= (1.0/(p)) * Math.log(1.0/(p));
					}
				}
				entropyTotal += thisEntropy;
				if (entropyTotal > bestEntropyTotal) {
					break;
				}
			}
			entropy = entropyTotal/n;
			//System.out.println("Entropy: " + entropy);
			if (entropy < bestEntropy && entropy > 0) {
				bestEntropy = entropy;
				bestGuess = guess;
			}
		}
		return bestGuess;
	}

	public static int[] testGuess(String guess, String answer) {
		int[] hits = new int[5];
		for (int i = 0; i < 5; i++) {
			if (guess.charAt(i) == answer.charAt(i)) {
				hits[i] = 2;
			} else if (answer.indexOf(guess.charAt(i)) != -1) {
				hits[i] = 1;
			} else {
				hits[i] = 0;
			}
		}
		return hits;
	}

	public void countLetters() {
		for (String word : this.answers) {
			HashSet<Character> used = new HashSet<Character>();
			for (int i = 0; i < 4; i++) {
				char letter = word.charAt(i);
				if (this.letters.containsKey(letter)) {
					this.letters.put(letter, this.letters.get(letter) + 1);
					if (!used.contains(letter)) {
						if (this.noDupeLetters.containsKey(letter)) {
							this.noDupeLetters.put(letter, this.noDupeLetters.get(letter) + 1);
						} else {
							this.noDupeLetters.put(letter, 1);
						}
					}
				} else {
					this.letters.put(letter, 1);
				}
				used.add(letter);
			}
		}
	}

}