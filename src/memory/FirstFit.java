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
		this.setOffset(1);
	}

	/**
	 * Allocates a number of memory cells.
	 *
	 * @param sizeToAllocate the number of cells to allocate.
	 * @return The address of the first cell (to be allocated).
	 */
	@Override
	public Pointer alloc(int sizeToAllocate) { //använd pointer.address (får en int)
		int currentCellSize;
        int next;
		int newCurrentCellAddress;
		Pointer pointer = null; //POINTER SKA BÖRJA MED ATT PEKA PÅ NOLL
		int currentCellAddress = freeList; //nuvarande cell-adress
		int previous = 0;
		sizeToAllocate++; //size++; //eller var det sizeToAllocate som ska ++?
        //size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga

		do {
			currentCellSize = this.cells[currentCellAddress]; //antalet celler i rad som är lediga
			next = this.cells[currentCellAddress + 1]; //får adressen till nästa "hop" lediga celler

			//här pekar vi om freeList (dvs pekaren till den första lediga cellen i minnet (dvs. this.cells))
			if(currentCellSize >= sizeToAllocate) {
				if(currentCellSize == sizeToAllocate) { //betyder att det inte blir någon lucka i den lediga "hopen" av lediga minnesceller
					//UPPDATERAR FREELIST: om current == freeList --> peka om freeList till nästa lediga "hop":s första adress, blir det freeList = next; ?
					if(currentCellAddress == freeList) {
						freeList = next;
                    } else {
						this.cells[previous + 1] = next;
					}

				} else if(currentCellSize > sizeToAllocate) { //betyder att det blir en lucka med lediga celler, peka om freeList till första lediga cell i luckan om det inte finns ledig plats innan den allokerade platsen
					//UPPDATERAR FREELIST: om current == freeList --> peka om freeList till: current + sizeToAllocate
					if(currentCellAddress == freeList) {
						freeList = currentCellAddress + sizeToAllocate;
						System.out.println("PLATS HITTAD! freeList är nu: " + freeList);
                    }
				}

				//det är här som allokeringen sker
				//3e nov - borde inte det här endast ske för när det blir en lucka i minnet vid allokeringen??
				// + ändras, se anteckningar för Scenario 1.2
				newCurrentCellAddress = currentCellAddress + sizeToAllocate;
				this.cells[newCurrentCellAddress] = currentCellSize - sizeToAllocate;
				this.cells[newCurrentCellAddress + 1] = this.cells[currentCellAddress + 1];
				this.cells[currentCellAddress] = sizeToAllocate;

				System.out.println("ALLOKERING DONE");

                //System.out.println(this);
				return new Pointer(currentCellAddress, this);

				//om ingen lämplig plats hittas att allokera på för denna iteration
			} else {
				previous = currentCellAddress;
				currentCellAddress = next; //vill ha kvar den nuvarande cellen vi är på
				next = this.cells[currentCellAddress + 1]; //får adressen till nästa "hop" lediga celler
			}

		} while(next > -1); //searches list (this.cells) for free space, starts with the first free space. Om indexet = -1 så har vi nått this.cells slut

		return null; //kommer vi hit så har allokeringen misslyckats, då ska null returneras
	}

	/**
	 * Releases a number of data cells
	 *
	 * @param pointer The pointer to release.
	 */
	@Override
	public void release(Pointer pointer) {
	    int beginningAddress = pointer.pointsAt(); //returnerar (begynnelse)adressen som pekaren (pointer som fås i metodhuvudet) pekar på
        int rCurrent = freeList;
        int rNewCurrent = beginningAddress;
        int releaseLength = this.cells[beginningAddress]; //längden på det "block" som ska deallokeras

		/*
		om freeList kommer EFTER adressen för det som ska deallokeras så vill vi peka om freeList till begynnelseadressen
		(detta eftersom freeList alltid ska peka på första lediga cellen i minnet (this.cells))
		(dvs. om freeList är STÖRRE än beginningAddress)
		 */
		if(freeList > beginningAddress) {
			if(beginningAddress + 1 == freeList) { //om deallokeringen sker hela vägen fram till ett block av lediga celler

				//peka om current i minnet och pekare till nästa block lediga celler i minnet
				this.cells[beginningAddress] = this.cells[freeList] + releaseLength; //längden på gamla freeList + deallokeringsblockets längd
				this.cells[beginningAddress + 1] = this.cells[freeList + 1]; //pekare till nästa

			} else { //om deallokeringen INTE sker hela vägen fram till ett block av lediga celler

				//this.cells[rNewCurrent] behöver inte uppdateras i det här fallet! dock behöver denna uppdateras i fallet där freeList kommer INNAN begynnelseadressen för deallokeringen
				this.cells[beginningAddress + 1] = freeList;
			}

			freeList = beginningAddress; //pekar om freeList till begynnelseAdressen för det som ska deallokeras

			System.out.println("RELEASE - "   + "freeList är nu: " + freeList);

		//------------------------------------------------------------------------------------------------------------

        /*
        om freeList kommer INNAN adressen för det som ska deallokeras
        då behöver freeList INTE pekas om, dvs. freeList ska inte ändra värde i detta fall
         */
		} else if(freeList < beginningAddress) { //if (freeList/rCurrent < beginningAddress)
			boolean beginningAddressReached = false;
			int rNext = this.cells[freeList + 1];

			/*
			här vill vi fortsätta att stega igenom minnet (this.cells), med freeList som startpunkt, tills att
			vi har hittat de två fria blocken celler som ligger mellan beginningAddress (dvs. det som ska deallokeras)
			 */
			while(!beginningAddressReached) {
				if(rCurrent < beginningAddress && beginningAddress < rNext) {
					beginningAddressReached = true;
				}
				rCurrent = rNext;
				rNext = this.cells[rCurrent + 1];
			}

			/*
			 if-sats som hanterar om det finns/inte finns ledigt block celler PRECIS INNAN det som ska deallokeras
			 */
			if((rCurrent + this.cells[rCurrent]) == beginningAddress) { // det finns ett ledigt block celler PRECIS INNAN det som ska deallokeras
				//uppdatera längd på ledigt block  deallokering: längden på ledigt block precis innan + längden på det som ska deallokeras
				this.cells[rCurrent] = this.cells[rCurrent] + this.cells[beginningAddress];

			} else { // det finns INTE ett ledigt block celler PRECIS INNAN det som ska deallokeras
				this.cells[beginningAddress + 1] = this.cells[rCurrent + 1]; //peka this.cells[beginningAddress + 1] = det som m pekade på innan
				this.cells[rCurrent + 1] = this.cells[beginningAddress]; //peka om m till this.cells[beginningAddress]
			}

			/*
			 if-sats som hanterar om det finns/inte finns ledigt block celler PRECIS EFTER det som ska deallokeras
			 */
			if(beginningAddress + this.cells[beginningAddress] == rNext) { // det finns ett ledigt block celler PRECIS EFTER det som ska deallokeras
				//uppdatera längd på det block som är ledigt: beginningAddress längd + längden på det lediga cellblocket precis efter
				this.cells[beginningAddress] = this.cells[beginningAddress] + this.cells[rNext];
				this.cells[beginningAddress + 1] = this.cells[rNext + 1];

			} else { // det finns INTE ett ledigt block celler PRECIS EFTER det som ska deallokeras
				this.cells[beginningAddress + 1] = rNext; //peka beginningAddress + 1 till nästkommande ledigt block av celler
			}
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
		String stringAllocated = "", stringFree = "";
		int pCurrent = freeList;
		int pNext = this.cells[freeList + 1];

		System.out.println("... printing ...");

		if(pCurrent != 0) { //om det första cellblocket är allokerat så vill vi skriva ut det
            stringAllocated += "0 - " + (pCurrent - 1);
        }

		System.out.println("printLayout() - freelist är " + freeList);
		System.out.println(this);
		//stega igenom minnet (dvs. this.cells)
		while(false && pNext > -1) {

		    /*
		    skriver ut det lediga block vi befinner oss på just nu (dvs. pCurrent)
		     */
		    stringFree += "\n" + pCurrent + " - " + ((pCurrent + this.cells[pCurrent]) - 1);

		    /*
		    skriver ut det allokerade blocket som finns mellan pCurrent och pNext
		    this.cells[pCurrent] + pCurrent = var (dvs. vilken address) som det allokerade blocket STARTAR
		    pNext - 1 = var (dvs. vilken address) som det allokerade blocket SLUTAR
		     */
            stringAllocated += "\n" + (pCurrent + this.cells[pCurrent]) + " - " + (pNext - 1);

			//gå vidare till nästa lediga block av lediga celler i minnet
			pCurrent = pNext;
			pNext = this.cells[pCurrent + 1];

			System.out.println("NEXT ÄR: " + pNext);

			//om vi nått fram till det sista lediga cellblocket och det finns ett allokerat block därefter, så måste vi skriva ut detta
			if(pNext == -1 && ((pCurrent + this.cells[pCurrent]) - 1) != this.cells.length) {
                stringAllocated += "\n" + pCurrent + this.cells[pCurrent] + " - " + this.cells.length;
            }
		}

		System.out.println("Allocated" + "\n" + stringAllocated + "\n");
		System.out.println("Free" + "\n" + stringFree + "\n");
	}
}
