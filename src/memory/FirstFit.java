package memory;

import java.util.LinkedList;

/**
 * This memory model allocates memory cells based on the first-fit method.
 * deluppgift 1
 *
 * Väljer att jobba "direkt" i minnet iställer för att använda Pointer.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {
	private int totalMemoryCells; //totalt antal celler i minnet (size ir originalfil) = this.cells.length (?)
	private int segmentLength; //storlek på segmentet av lediga blockplatser (ska använas för att lagra det bästa alternativet hitills)
	private int freeList; //freelist blir en int som pekar på första lediga plats
	private LinkedList<Pointer> occupiedList;

	/**
	 * Initializes an instance of a first fit-based memory.
	 *
	 * @param totalMemoryCells The number of cells.
	 */
	public FirstFit(int totalMemoryCells) {
		super(totalMemoryCells);
		freeList = 0;
		occupiedList = new LinkedList<>();
		// TODO Implement this!
	}

	/**
	 * Allocates a number of memory cells.
	 *
	 * @param sizeToAllocate the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int sizeToAllocate) {

		for(int i = freeList; i < this.cells.length; i++) { //search list after free space
			pointer = freeList.get(i); //keep track of the object in the list of the current iteration
			segmentsLength = pointer.read(); //?? vill ta reda på segmentets längd
			if(segmentsLength > sizeToAllocate) {
				//block = needed part of the found segment
				//update freeList to reflect the allocation
				//block[-1] = size + 1;, remember block size, needed for deallocation (= release-method)
				//return block
			}
		}

		//uppdatera freeList! (ska peka på den första lediga cellen)

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

		//uppdatera freeList! (ska peka på den första lediga cellen)

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
