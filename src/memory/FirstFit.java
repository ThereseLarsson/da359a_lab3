package memory;

/**
 * This memory model allocates memory cells based on the first-fit method.
 * deluppgift 1
 *
 * Väljer att jobba "direkt" i minnet.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {
	private int  freeList;
	private int size; //antalet celler i rad som är lediga
	private int current; //addressen där vi är nu just nu
	private int next; //addressen till nästa lediga plats

	/**
	 * Initializes an instance of a first fit-based memory.
	 *
	 * @param totalNbrMemoryCells The (total) number of cells.
	 */
	public FirstFit(int totalNbrMemoryCells) {
		super(totalNbrMemoryCells);
		freeList = 0;
		this.cells[0] = totalNbrMemoryCells;
		this.cells[1] = -1;
		//System.out.println(this.cells[627]);
	}

	/**
	 * Allocates a number of memory cells.
	 * i den här deluppgiften ska vi "bara" peka om freeList.
	 *
	 * @param sizeToAllocate the number of cells to allocate.
	 * @return The address of the first cell (to be allocated).
	 */
	@Override
	public Pointer alloc(int sizeToAllocate) { //använd pointer.address (får en int)
		size++; //eller var det sizeToAllocate som ska ++?
		int newCurrent;
		Pointer pointer = new Pointer(freeList, this); //POINTER SKA BÖRJA MED ATT PEKA PÅ NOLL
		current = freeList; //nuvarande cell-adress
		//next = this.cells[current + 1]; //får adressen till nästa "hop" lediga celler - BLIR 0

		System.out.println("\n LETAR PLATS... freeList är: " + freeList);
		System.out.println("LETAR PLATS... next är: " + next);
        System.out.println("LETAR PLATS... current är: " + current);

        //size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga

		do {
			size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga
			next = this.cells[current + 1]; //VAD SKA next VARA EFTER FÖRSTA ALLOKERINGEN???? --> -1

			//här pekar vi om freeList (dvs pekaren till den första lediga cellen i minnet (dvs. this.cells))
			if(size >= sizeToAllocate) {
				if(size == sizeToAllocate) { //betyder att det inte blir någon lucka i den lediga "hopen" av lediga minnesceller
					//UPPDATERAR FREELIST: om current == freeList --> peka om freeList till nästa lediga "hop":s första adress, blir det freeList = next; ?
					if(current == freeList) {
						freeList = next;
                    }

				} else if(size > sizeToAllocate) { //betyder att det blir en lucka med lediga celler, peka om freeList till första lediga cell i luckan om det inte finns ledig plats innan den allokerade platsen
					//UPPDATERAR FREELIST: om current == freeList --> peka om freeList till: current + sizeToAllocate
					if(current == freeList) {
						freeList = current + sizeToAllocate;
						System.out.println("PLATS HITTAD! freeList är nu: " + freeList);
                    }
				}

				//det är här som allokeringen sker
				size = this.cells[freeList];
				newCurrent = current + sizeToAllocate;
				this.cells[newCurrent] = this.cells[current] + size;
				this.cells[newCurrent + 1] = this.cells[current + 1];
				this.cells[current + 1] = this.cells[newCurrent + 1];
				this.cells[current] = sizeToAllocate;

				//next = this.cells[current + 1]; //VAD SKA next VARA EFTER FÖRSTA ALLOKERINGEN????
				System.out.println("next är: " + next);
                System.out.println("ALLOKERING DONE");

				pointer.pointAt(current); //adressen (= första indexet) där sekvensen av de lediga cellerna (i rad) börjar
				return pointer;

				//om inget lämplig plats hittas att allokera på för denna iteration
			} else {
				current = next; //vill ha kvar den nuvarande cellen vi är på
				next = this.cells[current + 1]; //får adressen till nästa "hop" lediga celler
			}

		} while(next > -1); //searches list (this.cells) after free space, starts with the first free space. Om indexet = -1 så har vi nått this.cells slut

		return null; //kommer vi hit så har allokeringen misslyckats, vad returnerar vi då? - svar: null
	}

	/**
	 * Releases a number of data cells
	 * 
	 * @param pointer The pointer to release.
	 */
	@Override
	public void release(Pointer pointer) {
	    int beginningAddress = pointer.pointsAt(); //returnerar (begynnelse)adressen som pekaren (pointer som fås i metodhuvudet) pekar på
        int rCurrent;
        int rNewCurrent;

		/*
		om freeList kommer EFTER adressen för det som ska deallokeras så vill vi peka om freeList till begynnelseadressen
		(detta eftersom freeList alltid ska peka på första lediga cellen i minnet (this.cells))
		(dvs. om freeList är STÖRRE än beginningAddress)
		 */
		if(freeList > beginningAddress) {
			freeList = beginningAddress; //pekar om freeList till begynnelseAdressen för det som ska deallokeras

			//peka om current
			//this.cells[rNewCurrent | begynnelseadressen?] = ... längden på gamla freeList + deallokeringsblockets längd
			//this.cells[rNewCurrent + 1] = this.cells[rCurrent + 1];
			//this.cells[rCurrent] = ... ska inte längre peka på något, men hur?
			//this.cells[rCurrent + 1] = ... ska inte längre peka på något, men hur?

			//peka om next

        //om freeList kommer INNAN adressen för det som ska deallokeras
		} else {

        }
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
