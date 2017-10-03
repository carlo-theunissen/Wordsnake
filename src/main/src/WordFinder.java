package main.src;

import java.util.ArrayList;
import java.util.List;

/**
 * This class calculates the valid words that can be made of one given.
 * After he is done, he stores all the founded words in an array (if it isn't present yet).
 * And remembers when a word is stored. 
 * 
 * @author carlotheunissen
 *
 */
@MultiThreaded
public class WordFinder extends  Thread {

	boolean test = false;
	/**
	 * The word the algoritme has to work with 
	 */
	private final String word;
	
	/**
	 * The word this algoritme is about to use is made by an other word
	 * Hence, one letter of the word is not needed to change. 
	 * For example: This class is constructed with the word "Dog" 
	 * and "Dog" is made by the word "Dot". Then the ignoreIndex will be 3
	 * because the 3th letter has already been changed to construct the 
	 * current word. 
	 * If the ignoreIndex is "-1" will the value be unused. 
	 */
	private final int ignoreIndex;
	
	/**
	 * The dictonary what contains all the aviable words  
	 */
	private final List<String> dictionary; 
	
	/**
	 * The words the algoritme has found
	 */
	private List<String> processed;
	
	/**
	 * The words the algoritme has added to the "found" list 
	 */
	private List<String> added;
	
	/**
	 * The list where the founded words need to be added. It checks first if a word is already
	 * present in this list. When it is not it will simply be added, it will also be added to the "added"
	 * list. 
	 */
	private List<String> found;
	
	/**
	 * Whenever or not the calculation is done
	 */
	private volatile boolean done; 
	
	private String killWord;
	
	private int currentLetterIndex = 0;
	private int currentLetterCount = 0;
	
	/**
	 * 
	 * @param word The starter word
	 * @param ignoreIndex What letter index has to be ignored? 
	 * @param dictonary A list containing all the valid words
	 * @param found The list where all the founded words has to be added
	 */
	WordFinder(final String word, final int ignoreIndex, final List<String> dictionary, List<String> found){
		this.word = word;
		this.ignoreIndex = ignoreIndex;
		this.dictionary = dictionary;
		this.found = found;
		this.processed = new ArrayList<String>();
		this.added = new ArrayList<String>();
		done = false;
	}
	
	
	public boolean isDone(){
		return done;
	}

	
	/**
	 * Gives the calculated result back when isDone() equals true. 
	 * If there isn't a result yet "null" will be returned 
	 * @return null, List<String>
	 */
	public List<String> getResult(){
		if(!isDone()){
			return null;
		}
		return added;
	}


	@Override
	public void run() {
		done = false;
		setWords();
		claimWords();
		done = true;
	}
	
	private void setWords(){
		while(true){
			String word = nextWord();
			if(word == null){
				return;
			}
			
			if(word.equals(killWord)){
				
				processed.clear();
				processed.add(word);
				return;
			}
			if(dictionary.contains(word)){
				processed.add(word);
			}
		}	
	}
	private void claimWords(){
		if(processed == null){
			return;
		}

		for(String word : processed){
			synchronized(found){
				if(found.contains(word)){
					continue;
				}
				if(word.equals(killWord)){
					test = true;
				}
				found.add(word);
			}

			added.add(word);
		}
	}
	private String nextWord(){
		char chars[] = this.word.toCharArray();
		if(currentLetterIndex == ignoreIndex){
			currentLetterIndex++;
		}
		if(currentLetterCount  > 25){
			currentLetterIndex++;
			currentLetterCount = 0;
		}
		if(currentLetterIndex >= chars.length){
			return null;
		}
		chars[currentLetterIndex] =  (char) ('a' + currentLetterCount);
		
		currentLetterCount++;
		
		return new String(chars);
	}


	public String getKillWord() {
		return killWord;
	}


	/**
	 * The calculation will stop if this word is found
	 * @param killWord
	 */
	public void setKillWord(String killWord) {
		this.killWord = killWord;
	}

}
