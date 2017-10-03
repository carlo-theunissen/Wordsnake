/**
 * 
 */
package main.src;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents one "Node" in the chain of words. 
 * It has the ability to branch out with new words. 
 * @author carlotheunissen
 *
 */
public class Node {
	
	static private int count = 0;
	protected String startWord;
	protected ArrayList<Node> childs;
	protected WordFinder wordFinder;
	protected List<String> dictornary;
	protected List<String> foundWords;
	private String killWord;
	private boolean threadFinished = false;

	
	public Node(String startWord, int ignoredIndex, List<String> dictornary, List<String> foundWords){
		count++;
		this.startWord = startWord;
		this.foundWords = foundWords;
		this.dictornary = dictornary;
		this.wordFinder = new WordFinder(startWord, ignoredIndex, dictornary, foundWords);
	}
	
	public static int getCount(){
		return count;
	}
	public static void resetCount(){
		count = 0;
	}
	
	
	/**
	 * starts the threads and starts searching for new words.
	 * @throws OutOfMemoryError
	 */
	public void startSearching() throws OutOfMemoryError{
		if(this.wordFinder.getState().equals(  State.NEW )){
			this.wordFinder.setKillWord(killWord);
			this.wordFinder.start();
			return;
		}
		
		if(childs != null){
			for(Node child : childs){
				child.startSearching();
			}
		}
	}

	
	/**
	 * Set the state of the node so it can start searching
	 * @return if he is doing something
	 */
	public boolean setupSearch(){
		
		if(this.wordFinder.getState().equals(  State.NEW )){
			return true;
		}
		
		
		if(childs == null && threadFinished() ){
			List<String> found =  this.wordFinder.getResult();
			if(found == null){
				return false;
			}
			makeChilderen(found);
		}
		
		if(childs != null){
			boolean result = false;
			for (Iterator<Node> iterator = childs.iterator(); iterator.hasNext(); ) {
				Node child = iterator.next();
				
				boolean childResult = child.setupSearch();
				if(!childResult){
					iterator.remove();
				}
				result |= childResult;
			}
			return result;
		}
		
		return false; //unreachable part of the code :(
	}
	
	private void makeChilderen(List<String> found){
		childs = new ArrayList<Node>();
		for(String item : found){
			Node temp = new Node(item,findIgnoreIndex(item),dictornary, foundWords);
			temp.setKillWord(killWord);
			childs.add(temp);
		}
	}
	
	/**
	 * @param word
	 * @return
	 */
	private int findIgnoreIndex(String word){
		for(int i = 0; i < word.length(); i++){
			if(word.charAt(i) != startWord.charAt(i)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns whatever or not the search is completed
	 * @return if the search is done
	 */
	public boolean isFinished(){
		if(!threadFinished()){
			return false;
		}
		
		if(childs != null){
			for(Node child : childs){
				if(!child.isFinished()){
					return false;
				}
			}
		}
		
		return true;
		
		
	}
	
	/**
	 * Returns true when the thread has completed. It does a simple test. 
	 * @return
	 */
	private boolean threadFinished(){
		return threadFinished || (threadFinished =  this.wordFinder.getState().equals(  State.TERMINATED ));
	}
	/**
	 * Blocks the current thread until the calculations are done
	 * @throws InterruptedException 
	 */
	public void blockTillSearchFinished() throws InterruptedException{
		if(this.wordFinder.isAlive()){
			this.wordFinder.join();
			return;
		}
		if(childs != null){
			for(Node child : childs){
				child.blockTillSearchFinished();
			}
		}
	}
	
	

	private boolean hasKillWord(List<String> list){
		
		if(list.size() != 1){
			return false;
		}

		return list.get(0).equals( killWord );
	}
	
	/**
	 * Returns the snake to the killword. The killword is faster. 
	 * @return
	 */
	public List<String> wordSnakeToKillWord(){
		if(!threadFinished()) return null;
		
		List<String> result = this.wordFinder.getResult();
	
		if(result == null){
			return null;
		}
		
		if(hasKillWord(result)){
			return startSnake(killWord);
		}
		
		if(childs != null){
			for(Node child : childs){
				List<String> out = child.wordSnakeToKillWord();
				if(out != null){
					out.add(startWord);
					return out;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the word snake to a certain word, if there is no snake, null will be returned
	 * @param word
	 * @return
	 */
	public List<String> wordSnake(String word){
		if(!threadFinished()) return null;
		
		List<String> result = this.wordFinder.getResult();
		
		if(result == null){
			return null;
		}
		
		for(String itr : result){
			if(itr.equals(word)){
				return startSnake(word);
			}
		}
		
		if(childs != null){
			
			for(Node child : childs){
				List<String> out = child.wordSnake(word);
				if(out != null){
					out.add(startWord);
					return out;
				}
			}
		}
		return null;
	}

	private List<String> startSnake(String word){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(word);
		temp.add(startWord);
		return temp;
	}
	public String getKillWord() {
		return killWord;
	}

	public void setKillWord(String killWord) {
		this.killWord = killWord;
	}
}
