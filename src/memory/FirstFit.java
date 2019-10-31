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
	//private int current; //addressen där vi är nu just nu
	//private int next; //addressen till nästa lediga plats

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
		//size++; //eller var det sizeToAllocate som ska ++?

        int size;
        int next;
		int newCurrent;
		Pointer pointer = new Pointer(freeList, this); //POINTER SKA BÖRJA MED ATT PEKA PÅ NOLL
		int current = freeList; //nuvarande cell-adress
		//next = this.cells[current + 1]; //får adressen till nästa "hop" lediga celler - BLIR 0

		//System.out.println("\n LETAR PLATS... freeList är: " + freeList);
		//System.out.println("LETAR PLATS... next är: " + next);
        //System.out.println("LETAR PLATS... current är: " + current);

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
				//System.out.println("next är: " + next);
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
        int rCurrent = freeList;
        int rNewCurrent = beginningAddress;
        int releaseLength = this.cells[beginningAddress]; //längden på det "block" som ska deallokeras

		/*
		om freeList kommer EFTER adressen för det som ska deallokeras så vill vi peka om freeList till begynnelseadressen
		(detta eftersom freeList alltid ska peka på första lediga cellen i minnet (this.cells))
		(dvs. om freeList är STÖRRE än beginningAddress)
		 */
		if(rCurrent > beginningAddress) {
			if(beginningAddress + 1 == rCurrent) { //om deallokeringen sker hela vägen fram till ett block av lediga celler

				//peka om current i minnet och pekare till nästa block lediga celler i minnet
				this.cells[rNewCurrent] = this.cells[rCurrent] + releaseLength; //längden på gamla freeList + deallokeringsblockets längd
				this.cells[rNewCurrent + 1] = this.cells[rCurrent + 1]; //pekare till nästa

			} else { //om deallokeringen INTE sker hela vägen fram till ett block av lediga celler

				//this.cells[rNewCurrent] behöver inte uppdateras i det här fallet! dock behöver denna uppdateras i fallet där freeList kommer INNAN begynnelseadressen för deallokeringen
				this.cells[rNewCurrent + 1] = this.cells[rCurrent];
			}

			freeList = beginningAddress; //pekar om freeList till begynnelseAdressen för det som ska deallokeras

		//------------------------------------------------------------------------------------------------------------

        /*
        om freeList kommer INNAN adressen för det som ska deallokeras
        då behöver freeList INTE pekas om, dvs. freeList ska inte ändra värde i detta fall
         */
		} else if(rCurrent < beginningAddress) { //if (freeList/rCurrent < beginningAddress)
			boolean beginningAddressReached = false;
			int rNext = rCurrent + 1;

			/*
			här vill vi fortsätta att stega igenom minnet (this.cells), med freeList som startpunkt, tills att
			vi har hittat de två fria blocken celler som ligger mellan beginningAddress (dvs. det som ska deallokeras)
			 */
			while(!beginningAddressReached) {
				if(rCurrent < beginningAddress && rNext > beginningAddress) {
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

			} else { // det finns INTE ett ledigt block celler PRECIS EFTER det som ska deallokeras
				this.cells[beginningAddress + 1] = this.cells[rNext]; //peka beginningAddress + 1 till nästkommande ledigt block av celler

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
		String stringAllocated = "";
		String stringFree = "";
		int pCurrent = freeList;
		int pNext = this.cells[pCurrent + 1];

		/*
		om det första cellblocket är allokerat så vill vi skriva ut det
		 */
		if(pCurrent != 0) {
            stringAllocated += "0 - " + (pCurrent - 1);
        }

		//stega igenom minnet (dvs. this.cells)
		while(pNext > -1) {

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

			//om vi nått fram till det sista lediga cellblocket och det finns ett allokerat block därefter, så måste vi skriva ut detta
			if(pNext == -1 && ((pCurrent + this.cells[pCurrent]) - 1) != this.cells.length) {
                stringAllocated += "\n" + pCurrent + this.cells[pCurrent] + " - " + this.cells.length;
            }
		}

		System.out.println("Allocated" + "\n" + stringAllocated);
		System.out.println("Free" + "\n" + stringFree + "\n");
	}
}
