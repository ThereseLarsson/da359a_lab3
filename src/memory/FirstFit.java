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
        int startIndex;

		for(int i = firstFree; i < this.cells.length; i++) { //searches list (this.cells) after free space, starts with the first free space (firstFree)
			if(this.cells[i] == -1) {
                nbrOfFreeSpacesInRow++; //öka med 1 för varje iteration
            } else {
                nbrOfFreeSpacesInRow = 0; //nollställ
            }

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
	    int address = p.pointsAt(); //rätt?? returns the address that the pointer p is pointing at

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

		for(int i = 0; i < this.cells.length; i++) {
			if(this.cells[i] == -1 && i == 0 || this.cells[i] == -1 && this.cells[i - 1] != -1) { //if current free AND is the first element in the list || if current free AND previous not free
				resFree += i + " - ";
			} else if(this.cells[i] == -1 && i == this.cells.length - 1 || this.cells[i] == -1 && this.cells[i + 1] != -1) { //if current free AND is the last element in the list || if current free AND next not free
				if(i == this.cells.length - 1) { //if last element
					resFree += i;
				} else {
					resFree += i + "\n";
				}
			}

			if(this.cells[i] != -1 && i == 0 || this.cells[i] != -1 && this.cells[i - 1] == -1) { //if current occupied AND is the first element in the list || if current occupied AND previous not occupied
				resOccupied += i + " - ";
			} else if(this.cells[i] != -1 && i == this.cells.length - 1 || this.cells[i] != -1 && this.cells[i + 1] == -1) { //if current occupied AND is the last element in the list || if current occupied AND next not occupied
				if(i == this.cells.length - 1) { //if last element
					resOccupied += i;
				} else {
					resOccupied += i + "\n";
				}
			}
		}

		System.out.println("Allocated" + "\n" + resOccupied);
		System.out.println("Free" + "\n" + resFree + "\n");
	}
}
