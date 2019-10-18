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
		//System.out.println(this.cells[300]);
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
		Pointer pointer = new Pointer(freeList, this); //POINTER SKA BÖRJA MED ATT PEKA PÅ NOLL
		current = freeList; //nuvarande cell-adress
		next = this.cells[current + 1]; //får adressen till nästa "hop" lediga celler - BLIR 0

		System.out.println("\n LETAR PLATS... freeList är: " + freeList);
		System.out.println("LETAR PLATS... next är: " + next);
        System.out.println("LETAR PLATS... current är: " + current);
        //System.exit(0);

        //size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga

		do {
			size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga
			next = this.cells[current + 1]; //VAD SKA next VARA EFTER FÖRSTA ALLOKERINGEN???? --> -1

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

				//måste allokera
				size = this.cells[freeList];
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

		return null; //allokeringen misslyckades, vad returnerar vi då? - null
	}

	/**
	 * Releases a number of data cells
	 * 
	 * @param pointer The pointer to release.
	 */
	@Override
	public void release(Pointer pointer) {
	    int address = pointer.pointsAt(); //rätt?? returnerar (begynnelse)adressen som pekaren (pointer som fås i metodhuvudet) pekar på
		//int beginningAddress = address; //vill behålla begynnelseaddressen (behövs när vi ska uppdatera freeList)

		//om addressen som deallokeringen börjar på är mindre än freeList så vill vi peka om freeList till begynnelseadressen (detta eftersom freeList alltid ska peka på första lediga cellen i minnet (this.cells))
		if(freeList > address) { //om freeList > begynnelseadressen
			freeList = address;
		}

		while(this.cells[address] != -1) { //så länge som this.cells[address] är skilt från -1 (då -1 innebär att platsen är ledig), om vi får this.cells[address] == -1 så har vi nått slutet av det segments som ska deallokeras (?)
			this.cells[address] = -1; //sätt this.cells[address] nuvarande plats till -1;
			address++; //sätt address = -1 så vi hoppar till nästa plats i this.cells för att kolla om den är upptagen eller tom (dvs. -1)
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

	public Pointer alloc_whileLoop(int sizeToAllocate) {
		Pointer pointer = new Pointer(freeList, this); //POINTER SKA BÖRJA MED ATT PEKA PÅ NOLL
		int size; //antalet celler i rad som är lediga
		int current = freeList; //nuvarande cell-adress
		int next = this.cells[pointer.pointsAt() + 1]; //får adressen till nästa "hop" lediga celler

		while(next > -1)  { //searches list (this.cells) after free space, starts with the first free space. Om indexet = -1 så har vi nått this.cells slut

			size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga

			if(size >= sizeToAllocate) {
				if(size == sizeToAllocate) { //betyder att det inte blir någon lucka i den lediga "hopen" av lediga minnesceller
					//UPPDATERA FREELIST
					//om current = freeList --> peka om freeList till nästa lediga "hop":s första address, blir det freeList = next; ?

				} else if(size > sizeToAllocate) { //betyder att det blir en lucka med lediga celler, peka om freeList till första lediga cell i luckan om det inte finns ledig plats innan den allokerade platsen
					//UPPDATERA FREELIST
					//om current = freeList --> peka om freeList till: current + sizeToAllocate

				}
				pointer.pointAt(current); //adressen (= första indexet) där sekvensen av de lediga cellerna (i rad) börjar
				return pointer;

			} else {
				current = next; //vill ha kvar den nuvarande cellen vi är på
				next = this.cells[current + 1]; //får adressen till nästa "hop" lediga celler
			}
		}

		return null; //allokeringen misslyckades, vad returnerar vi då? - null
	}
}
