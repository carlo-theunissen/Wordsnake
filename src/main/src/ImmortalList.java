package main.src;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * An immortal list, for performance. 
 * It only allows the read function. 
 * @author carlotheunissen
 *
 * @param <T>
 */
@MultiThreaded
public class ImmortalList implements List<String> {

	protected final String[] data;
	/**
	 * used for caching
	 */
	protected final int length;
	
	ImmortalList(String[] array){
		this.data = array;
		Arrays.sort(this.data);
		
		length = this.data.length;
	}
	
	@Override
	public int size() {
		return length;
	}

	@Override
	public boolean isEmpty() {
		return length == 0;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public Iterator<String> iterator() {
		return new Itr();
	}

    private class Itr implements Iterator<String> {
        private int i = -1;       // index of next element to return
        
        public boolean hasNext() {
            return i < length;
        }

        public String next() {

            if (i >= length){
            	throw new NoSuchElementException();
            }
                
            i++;
            return data[i];
        }


    }

	@Override
	public Object[] toArray() {
		return data.clone();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> T[] toArray(T[] a) {
		return a = (T[]) data.clone();
	}

	@Override
	public boolean add(String e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		Iterator<?> itr = c.iterator();
		while(itr.hasNext()){
			if(!contains(itr.next())){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends String> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {}

	@Override
	public String get(int index) {
		return data[index];
	}

	@Override
	public String set(int index, String element) {
		
		return data[index];
	}

	@Override
	public void add(int index, String element) {}

	@Override
	public String remove(int index) {
		
		return null;
	}

	@Override
	public int indexOf(Object o) {
		return Arrays.binarySearch(this.data, (String) o);
	}

	@Override
	public int lastIndexOf(Object o) {
	    return indexOf(o);
	}

	@Override
	public ListIterator<String> listIterator() {
		return null;
	}

	@Override
	public ListIterator<String> listIterator(int index) {
		return null;
	}

	@Override
	public List<String> subList(int fromIndex, int toIndex) {
		return null;
	}

}
