package memory;

import java.util.LinkedList;

/**
 * This memory model allocates memory cells based on the first-fit method.
 * deluppgift 1
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {
	private int size; //storlek på memory segment
	private LinkedList<Pointer> freeList;
	private LinkedList<Pointer> occupiedList;

	/**
	 * Initializes an instance of a first fit-based memory.
	 * 
	 * @param size The number of cells.
	 */
	public FirstFit(int size) {
		super(size);
		freeList = new LinkedList<Pointer>();
		occupiedList = new LinkedList<Pointer>();
		// TODO Implement this!
	}

	/**
	 * Allocates a number of memory cells. 
	 * 
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		Pointer pointer = null;

		for(int i = 0; i < freeList.size(); i++) { //search freeList
			pointer = freeList.get(i); //keep track of the object in the list of the current iteration
			segmentsLength = pointer.read(); //?? vill ta reda på segmentets längd
			if(segmentsLength > size) {
				//block = needed part of the found segment
				//update freeList to reflect the allocation
				//block[-1] = size + 1;, remember block size, needed for deallocation (= release-method)
				//return block
			}
		}
		return pointer;
	}
	
	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		//vad är segment?
		//måste göra något med listan occupiedList? finns inte i pseudokoden i kap 12.1.3

		//segment = object -1;
		//segment.length = object[-1];
		//insert segment into the freeList;

		//segment = p -1;
		//segment.length = p[-1];
		//insert segment into the freeList;
	}
	
	/**
	 * Prints a simple model of the memory. Example:
	 * 
	 * |    0 -  110 | Allocated
	 * |  111 -  150 | Free
	 * |  151 -  999 | Allocated
	 * | 1000 - 1024 | Free
	 */
	@Override
	public void printLayout() {
		// TODO Implement this!
	}
}
