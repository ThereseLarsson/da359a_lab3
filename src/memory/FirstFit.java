package memory;

import java.util.LinkedList;

/**
 * This memory model allocates memory cells based on the first-fit method.
 * deluppgift 1
 *
 * Väljer att jobba "direkt" i minnet istället för att använda Pointer.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {
	private int totalMemoryCells; //totalt antal celler i minnet (size ir originalfil) = this.cells.length (?)
	private int segmentLength; //storlek på segmentet av lediga blockplatser (ska använas för att lagra det bästa alternativet hitills)
	private int firstFree; //freelist blir en int som pekar på första lediga plats

	/**
	 * Initializes an instance of a first fit-based memory.
	 *
	 * @param totalMemoryCells The number of cells.
	 */
	public FirstFit(int totalMemoryCells) {
		super(totalMemoryCells);
		firstFree = 0;
		// TODO Implement this!
	}

	/**
	 * Allocates a number of memory cells.
	 *
	 * @param sizeToAllocate the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int sizeToAllocate) { //använd pointer.address
		int counter = 0; //keeps track of the longest segment of non-occupied blocks

		for(int i = firstFree; i < this.cells.length; i++) { //search list after free space

		}

		//uppdatera freeList! (ska peka på den första lediga cellen)

		//return Pointer.address;
		return null;
	}

	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release. //pointer.address
	 */
	@Override
	public void release(Pointer p) {

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
		for(int i = 0; i < this.cells.length; i++) {

			System.out.println("");

		}
		// TODO Implement this!
	}
}
