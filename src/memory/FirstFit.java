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
		//int newCurrentCellAddress;
		//Pointer pointer = null; //POINTER SKA BÖRJA MED ATT PEKA PÅ NOLL
		int currentCellAddress = freeList; //nuvarande cell-adress
		int previous = 0;
		sizeToAllocate++; //size++; //eller var det sizeToAllocate som ska ++?
        //size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga

		do {
			currentCellSize = this.cells[currentCellAddress]; //antalet celler i rad som är lediga
			next = this.cells[currentCellAddress + 1]; //får adressen till nästa "hop" lediga celler

			//här pekar vi om freeList (dvs pekaren till den första lediga cellen i minnet (dvs. this.cells))
			if(currentCellSize >= sizeToAllocate) {

				/*
				SCENARIO 1  - INGEN LUCKA i den lediga "hopen" av lediga minnesceller
				 */
				if(currentCellSize == sizeToAllocate) { //betyder att det inte blir någon lucka i
					// Scenario 1.1
					if(currentCellAddress == freeList) {
						freeList = next;

					// Scenario 1.1
                    } else {
						this.cells[previous + 1] = next;
					}

				/*
				SCENARIO 2 - lUCKA
				 */
				} else if(currentCellSize > sizeToAllocate) {
					// Scenario 2.1 - om allokeringen sker på den första lediga "hopen" i minnet (dvs. freeList)
					if(currentCellAddress == freeList) {
						freeList = currentCellAddress + sizeToAllocate;

					// Scenario 2.2
					} else {
						this.cells[previous + 1] = currentCellAddress + sizeToAllocate;
					}
					//gemensamm kod för Scenario 2.1 och 2.2
					this.cells[currentCellAddress + sizeToAllocate] = this.cells[currentCellAddress] - sizeToAllocate;
					this.cells[currentCellAddress + sizeToAllocate + 1] = next;
					this.cells[currentCellAddress] = sizeToAllocate;
				}

				//det är här som allokeringen sker
				//3e nov - borde inte det här endast ske för när det blir en lucka i minnet vid allokeringen??
				// + ändras, se anteckningar för Scenario 1.2
				//newCurrentCellAddress = currentCellAddress + sizeToAllocate;
				//this.cells[newCurrentCellAddress] = currentCellSize - sizeToAllocate;
				//this.cells[newCurrentCellAddress + 1] = this.cells[currentCellAddress + 1];
				//this.cells[currentCellAddress] = sizeToAllocate;

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
        //int rNewCurrent = beginningAddress;
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
		String stringAllocated = "Allocated";
		String stringFree = "Free";
		int pCurrentCellAddress = freeList;
		int pNext = this.cells[freeList + 1];
		int startAddress, endAddress;

		//System.out.println(this);
		//System.out.println("... printing ..." + "\n");

		//om det första cellblocket i minnet (dvs. address 0 - x) är allokerat så vill vi skriva ut det
		if(pCurrentCellAddress != 0) {
            stringAllocated += "\n" + "0 - " + (pCurrentCellAddress - 1);
        }

		//stega igenom minnet (dvs. this.cells)
		while(pNext > -1) {
			/*
			UTSKRIFTER
			------------------------------------------------------------------------------------------
			 */
		    // FRITT - skriver ut det lediga block vi befinner oss på JUST NU (dvs. pCurrentCellAddress)
			endAddress = (pCurrentCellAddress + this.cells[pCurrentCellAddress]) - 1;
		    stringFree += "\n" + pCurrentCellAddress + " - " + endAddress;

		    /*
		    ALLOKERAT - skriver ut det allokerade blocket som finns mellan pCurrent och pNext
		    this.cells[pCurrent] + pCurrent = var (dvs. vilken address) som det allokerade blocket STARTAR
		    pNext - 1 = var (dvs. vilken address) som det allokerade blocket SLUTAR
		     */
		    startAddress = pCurrentCellAddress + this.cells[pCurrentCellAddress];
		    endAddress = pNext - 1;
            stringAllocated += "\n" + startAddress + " - " + endAddress;

            /*
            NÄSTA LEDIGA "HOP" AV CELLER
            går vidare till nästa lediga block av lediga celler i minnet
            ---------------------------------------------------------------------
             */
			pCurrentCellAddress = pNext;
			pNext = this.cells[pCurrentCellAddress + 1];

			/*
			SPECIALFALL - om vi har kommit till slutet av listan och det bara finns
			1 ledigt block kvar (i det här fallet kommer next att bli -1 och därmed bryts loppen)
			---------------------------------------------------------------------
			 */
			int x = (pCurrentCellAddress + this.cells[pCurrentCellAddress]) - 1;
			//om vi nått fram till det sista lediga cellblocket och det finns ett allokerat block därefter, så måste vi skriva ut detta
			if(pNext == -1 && x != (this.cells.length - 1)) {
				startAddress = pCurrentCellAddress + this.cells[pCurrentCellAddress];
                stringAllocated += "\n" + startAddress + " - " + (this.cells.length - 1);
            }
		}

		System.out.println();
		System.out.println(stringAllocated);
		System.out.println();
		System.out.println(stringFree);
	}
}
