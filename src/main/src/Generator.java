package main.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Generator {
	
	private final ImmortalList wordList;
	private Vector<String> foundWords;
	private Node firstNode;
	
	
	public Generator(String pathname) throws IOException{
		wordList = new ImmortalList(getWordList(pathname));
		foundWords = new Vector<String>(10000,5000);
		
	}
	
	public List<String> getWordList(){
		return wordList;
	}
	
	/**
	 * Generates the wordsnake
	 * @param start
	 * @param end
	 * @return the wordsnake if found, otherwise null
	 */
	public List<String> generate(final String start, final String end){
		foundWords.clear();
		Node.resetCount();
		foundWords.add(start);
		firstNode = new Node(start, -1, wordList, foundWords);
		firstNode.setKillWord(end);
		
		return loop();
	}
	

	private List<String> loop(){
		List<String> result;
		boolean again;
		
		while(true){
			
			if(! firstNode.setupSearch()){
				return null;
			}
			
			do{
				again = false;
				
				//starts the search to new words
				try {
					firstNode.startSearching();
				} catch (OutOfMemoryError e) {
					again = true; 
				}
				
				//search for the killword
				result = firstNode.wordSnakeToKillWord();
				if(result != null){
					return result;
				}
				
				//determains if we need to check the current search setup again.
				again |= !firstNode.isFinished();
				
				
			}while(again);
		
		}

	}
	public void printStats(){
		System.out.println(foundWords.size() + " Words checked");
		System.out.println(Node.getCount() + " Branches made");
	}
	public void cleanUp(){
		try {
			firstNode.blockTillSearchFinished();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		firstNode = null;
		foundWords.clear();
		Node.resetCount();
	}
	
	
	/**
	 * Reads the file specified by the pathname and returns all the lines
	 * as an array.
	 * @param pathname
	 * @return
	 * @throws FileNotFoundException 
	 */
	private String[] getWordList(String pathname) throws FileNotFoundException{
		Vector<String> list = new Vector<String>(360000,500);
		System.out.println(pathname);
		File file = new File(getClass().getResource(pathname).getFile());

		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			list.add(line);
		}

		scanner.close();
		String[] out = new String[list.size()];
		list.toArray(out);
		return out;
		
	}
	
}
