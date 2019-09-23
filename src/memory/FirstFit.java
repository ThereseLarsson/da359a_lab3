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
	private int segmentLength; //storlek på segmentet av lediga blockplatser (ska användas för att lagra det bästa alternativet hittills)
	private int firstFree; //freelist blir en int som pekar på första lediga plats

	/**
	 * Initializes an instance of a first fit-based memory.
	 *
	 * @param totalMemoryCells The (total) number of cells.
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
	public Pointer alloc(int sizeToAllocate) { //använd pointer.address (får en int)
		int nbrOfFreeSpacesInRow = 0; //keeps track of the longest segment of non-occupied blocks that occurs in this.cells in a row

		for(int i = firstFree; i < this.cells.length; i++) { //searches list (this.cells) after free space, starts with the first free space (firstFree)
			nbrOfFreeSpacesInRow++; //öka med 1 för varje iteration

			if(nbrOfFreeSpacesInRow == sizeToAllocate) {
				//uppdatera firstFree

			}
		}

		//-------------------------------------
		//i den här deluppgiften ska vi "bara" peka om firstFree
		//för det behöver vi ta reda på den plats där segmentet passar in
		//returnera begynnelse-adressen för det segmentet (+ uppdatera firstFree)
		//pointer.address (för vilket syfte??)
		//ska man sätta point.address = firstFree?

		return null;
	}

	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		//använd pointer.address för att få addressen för det som ska deallokeras
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
		String resOccupied = "";
		String resFree = "";
		boolean nextIsFree; //om nästa cell är fri, dvs. skilt från -1

		for(int i = 0; i < this.cells.length; i++) {
			//
			if(this.cells[i] != -1) { //om cellen är upptagen
				if(förra cellen också var upptagen OCH nästa cell är fri) {
					resOccupied += ;
				} else {
					//
				}

			} else { //om cellen är fri (dvs. -1)

			}

			System.out.println("Upptaget" + "\n");
			//+ skriv ut upptagna platser

			System.out.println("Upptaget" + "\n");
			//skriv ut lediga platser

		}
		// TODO Implement this!
	}
}
