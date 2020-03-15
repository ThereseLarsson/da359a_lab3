package memory;

/**
 * This memory model allocates memory cells based on the best-fit method.
 * deluppgift 2
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class BestFit extends Memory {
	static public int freeList;

	/**
	 * Initializes an instance of a best fit-based memory.
	 * 
	 * @param totalNbrMemoryCells The number of cells.
	 */
	public BestFit(int totalNbrMemoryCells) {
		super(totalNbrMemoryCells);
		freeList = 0;
		this.cells[0] = totalNbrMemoryCells;
		this.cells[1] = -1;
		this.setOffset(1);
	}

	/**
	 * Allocates a number of memory cells. 
	 * 
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		int bestStartAddressSoFar = -1;
		int currentCellSize = 0;
		int currentCellAddress = freeList;
		int previous = -1;
		int next = 0;

		do {

		} while(next > -1);

		return null; //kommer vi hit så har allokeringen misslyckats, då ska null returneras
	}
	
	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		// TODO Implement this!
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
