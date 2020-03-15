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
	static public int  freeList;

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
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell (to be allocated).
	 */
	@Override
	public Pointer alloc(int size) { //använd pointer.address (får en int)
		int currentCellSize = 0;
        int next = 0;
		//int newCurrentCellAddress;
		//Pointer pointer = null; //POINTER SKA BÖRJA MED ATT PEKA PÅ NOLL
		int currentCellAddress = freeList; //nuvarande cell-adress
		int previous = -1;
		int sizeToAllocate = size + this.offset; //size++; //eller var det sizeToAllocate som ska ++?
        //size = this.cells[pointer.pointsAt()]; //antalet celler i rad som är lediga
		int fan = 0, attans = 0;

		if (freeList == 0) {
			System.out.println("Checking w freelist...");
			fan = freeList;
			attans = fan + 1;
			System.out.println(this.cells[freeList]);
			System.out.println(this.cells[freeList + 1]);
		}

		do {
			currentCellSize = this.cells[currentCellAddress]; //antalet celler i rad som är lediga
			next = this.cells[currentCellAddress + 1]; //får adressen till nästa "hop" lediga celler

			//här pekar vi om freeList (dvs pekaren till den första lediga cellen i minnet (dvs. this.cells))
			if(currentCellSize >= sizeToAllocate) {
				/*
				SCENARIO 2 - lUCKA
				 */
				if(currentCellSize > sizeToAllocate + 1) {
					// Scenario 2.1 - om allokeringen sker på den första lediga "hopen" i minnet (dvs. freeList)
					if(currentCellAddress == freeList) {
						freeList = currentCellAddress + sizeToAllocate;
						if (freeList == 98) {
							System.out.println("Fel i alloc, scenario 2.1");
							//System.out.println(this);
							//System.out.println("Fel i alloc, scenario 2.1");
							//System.exit(0);
						}

						// Scenario 2.2
					} else {
						this.cells[previous + 1] = currentCellAddress + sizeToAllocate;
					}
					//gemensamm kod för Scenario 2.1 och 2.2
					this.cells[currentCellAddress + sizeToAllocate] = this.cells[currentCellAddress] - sizeToAllocate;
					this.cells[currentCellAddress + sizeToAllocate + 1] = next;
					this.cells[currentCellAddress] = sizeToAllocate;
				}

				/*
				SCENARIO 1  - INGEN LUCKA i den lediga "hopen" av lediga minnesceller
				 */
				else if(currentCellSize == sizeToAllocate) { //betyder att det inte blir någon lucka i
					// Scenario 1.1
					if(currentCellAddress == freeList) {
						freeList = next;
						if (freeList == 98) {
							System.out.println("Fel i alloc, scenario 1.1");
							//System.out.println(this);
							//System.out.println("Fel i alloc, scenario 1.1");
							System.exit(0);
						}

					// Scenario 1.1
                    } else if (previous >= 0){
						this.cells[previous + 1] = next;
					}

					//om det blir en lucka som är 1 cell stor
				} else if(currentCellSize == sizeToAllocate + 1) {

					if(currentCellAddress == freeList) {
						freeList = next;
						if (freeList == 98) {
							System.out.println("Fel i alloc, scenario 1.1");
							//System.out.println(this);
							//System.out.println("Fel i alloc, scenario 1.1");
							System.exit(0);
						}

					} else if (previous >= 0){
						this.cells[previous + 1] = next;
					}
					this.cells[currentCellAddress] = sizeToAllocate + 1;
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

			//kan byta ut om while(currentCellAddress > -1) ??
		} while(next > -1); //searches list (this.cells) for free space, starts with the first free space. Om indexet = -1 så har vi nått this.cells slut

		//printMemory();
		return null; //kommer vi hit så har allokeringen misslyckats, då ska null returneras
	}

	/**
	 * Releases a number of data cells
	 *
	 * @param pointer The pointer to release.
	 */
	@Override
	public void release(Pointer pointer) {
		release(pointer, false);
	}

	public void release(Pointer pointer, boolean debug) {
	    int beginningAddress = pointer.pointsAt(); //returnerar (begynnelse)adressen som pekaren (pointer som fås i metodhuvudet) pekar på
        int rCurrent = freeList;
        //int rNewCurrent = beginningAddress;
        int releaseLength = this.cells[beginningAddress]; //längden på det "block" som ska deallokeras

		int fan = 0, attans = 0;

		/*
		om freeList kommer EFTER adressen för det som ska deallokeras så vill vi peka om freeList till begynnelseadressen
		(detta eftersom freeList alltid ska peka på första lediga cellen i minnet (this.cells))
		(dvs. om freeList är STÖRRE än beginningAddress)
		 */
		if(freeList > beginningAddress) {
			System.out.println("RELEASE - freeList kom EFTER adressen för det som ska deallokeras vid detta anrop");

			if(beginningAddress + releaseLength == freeList) { //om deallokeringen sker hela vägen fram till ett block av lediga celler

				//peka om current i minnet och pekare till nästa block lediga celler i minnet
				this.cells[beginningAddress] = this.cells[freeList] + releaseLength; //längden på gamla freeList + deallokeringsblockets längd
				this.cells[beginningAddress + 1] = this.cells[freeList + 1]; //pekare till nästa

			} else { //om deallokeringen INTE sker hela vägen fram till ett block av lediga celler
				if (beginningAddress == 0) {
					System.out.println("Checking...");
					fan = freeList;
					attans = fan + 1;
					System.out.println(this.cells[freeList]);
					System.out.println(this.cells[freeList + 1]);
				}

				//this.cells[rNewCurrent] behöver inte uppdateras i det här fallet! dock behöver denna uppdateras i fallet där freeList kommer INNAN begynnelseadressen för deallokeringen
				this.cells[beginningAddress + 1] = freeList;
			}

			freeList = beginningAddress; //pekar om freeList till begynnelseAdressen för det som ska deallokeras
			if (freeList == 98) {
				System.out.println("Fel i release, freelist > beginningAddress");
				//System.out.println(this);
				//System.out.println("Fel i release, freelist > beginningAddress");
				System.exit(0);
			}

			//System.out.println("RELEASE - "   + "freeList är nu: " + freeList);

		//------------------------------------------------------------------------------------------------------------

        /*
        om freeList kommer INNAN adressen för det som ska deallokeras
        då behöver freeList INTE pekas om, dvs. freeList ska inte ändra värde i detta fall
         */
		} else if(freeList < beginningAddress) { //if (freeList/rCurrent < beginningAddress)
			System.out.println("RELEASE - freeList kom INNAN adressen för det som ska deallokeras vid detta anrop");

			boolean beginningAddressReached = false;
			rCurrent = freeList; //NY
			int rNext = this.cells[freeList + 1];

			//System.out.println("RNEXT" + rNext);

			/*
			här vill vi fortsätta att stega igenom minnet (this.cells), med freeList som startpunkt, tills att
			vi har hittat de två fria blocken celler som ligger mellan beginningAddress (dvs. det som ska deallokeras)
			 */
			while(!beginningAddressReached) {

				System.out.println();
				System.out.println("---------------- CHECK 1 ----------------");
				System.out.println("freeList: " + freeList);
				System.out.println("rCurrent (ledig adress INNAN beginningAddress): " + rCurrent);
				System.out.println("rNext (ledig adress EFTER beginningAddress): " + rNext);
				System.out.println("--------------------------------");
				System.out.println();

				if(rCurrent < beginningAddress && rNext > beginningAddress) {
					beginningAddressReached = true;
					//rCurrent = rNext; //ska väl inte göras här?
					//rNext = this.cells[rCurrent + 1]; //ska väl inte göras här?

				} else if(rCurrent < beginningAddress && rNext == -1) {
					beginningAddressReached = true;

				} else { //dvs. har inte hittat beginningAddress än
					rCurrent = rNext;
					rNext = this.cells[rCurrent + 1];
				}
			}

			System.out.println();
			System.out.println("---------------- CHECK 2 ----------------");
			System.out.println("rCurrent (ledig adress INNAN beginningAddress): " + rCurrent);
			System.out.println("beginningAddress: " + beginningAddress);
			System.out.println("rNext (ledig adress EFTER beginningAddress): " + rNext);
			System.out.println("--------------------------------");
			System.out.println();

			/*
			  LEDIGT INNAN: det FINNS ett ledigt block celler PRECIS INNAN det som ska deallokeras
			  UPPTAGET INNAN: det FINNS INTE ett ledigt block celler PRECIS INNAN det som ska deallokeras
			  ---
			  LEDIGT EFTER: det FINNS ett ledigt block celler PRECIS EFTER det som ska deallokeras
			  UPPTAGET EFTER: det FINNS INTE ett ledigt block celler PRECIS EFTER det som ska deallokeras
			 */


			 /*
			  LEDIGT INNAN och
			  LEDIGT EFTER
			  */
			if((((rCurrent + this.cells[rCurrent]) == beginningAddress)) && (beginningAddress + this.cells[beginningAddress] == rNext)) {
				this.cells[rCurrent] = this.cells[rCurrent] + this.cells[beginningAddress] + this.cells[rNext]; //lägger ihop längden för de 3 lediga blocken (rCurrent, det som ska deallokeras, rNext)
				this.cells[rCurrent + 1] = this.cells[rNext + 1]; //pekar om pekaren (för det förekommande lediga blocket) till nästa lediga block efter rNext
				System.out.println("///// ledigt RELEASE ledigt /////");

			 /*
			  LEDIGT INNAN och
			  UPPTAGET EFTER
			  */
			} else if((((rCurrent + this.cells[rCurrent]) == beginningAddress)) && !(beginningAddress + this.cells[beginningAddress] == rNext)) {
				this.cells[rCurrent] = this.cells[rCurrent] + this.cells[beginningAddress]; //uppdatera längd på ledigt block  deallokering: längden på ledigt block precis innan + längden på det som ska deallokeras
				System.out.println("///// ledigt RELEASE upptaget /////");

			 /*
			  UPPTAGET INNAN och
			  LEDIGT EFTER
			  */
			} else if((!((rCurrent + this.cells[rCurrent]) == beginningAddress)) && (beginningAddress + this.cells[beginningAddress] == rNext)) {
				this.cells[rCurrent + 1] = beginningAddress; //pekar om pekaren (för det förekommande lediga blocket) till beginningAddress
				this.cells[beginningAddress + 1] = this.cells[rNext + 1]; //skapar pekare för beginningAddress + 1 som pekar till rNext + 1
				this.cells[beginningAddress] = this.cells[beginningAddress] + this.cells[rNext]; //lägger ihop längden för det block som ska deallokeras med längden för det lediga blockets längd som kommer precis efter
				System.out.println("///// upptaget RELEASE ledigt /////");

			 /*
			  UPPTAGET INNAN och
			  UPPTAGET EFTER
			  */
			} else if((!((rCurrent + this.cells[rCurrent]) == beginningAddress)) && !(beginningAddress + this.cells[beginningAddress] == rNext)) {
				this.cells[beginningAddress + 1] = rNext; //pekar beginningAddress + 1 till nästkommande ledigt block av celler
				this.cells[rCurrent + 1] = beginningAddress; //pekar om pekaren (för det förekommande lediga blocket) till beginningAddress
				System.out.println("///// upptaget RELEASE upptaget /////");
			}

			//-----------------------------

			/*
			 if-sats som hanterar om det finns/inte finns ledigt block celler PRECIS INNAN det som ska deallokeras
			 *//*
			if((rCurrent + this.cells[rCurrent]) == beginningAddress) { // det finns ett ledigt block celler PRECIS INNAN det som ska deallokeras
				//uppdatera längd på ledigt block  deallokering: längden på ledigt block precis innan + längden på det som ska deallokeras
				this.cells[rCurrent] = this.cells[rCurrent] + this.cells[beginningAddress];
				System.out.println(" Slog samman: current + this.cells");


			} else { // det finns INTE ett ledigt block celler PRECIS INNAN det som ska deallokeras
				this.cells[beginningAddress + 1] = this.cells[rCurrent + 1]; //peka this.cells[beginningAddress + 1] = det som m pekade på innan
				this.cells[rCurrent + 1] = beginningAddress; //peka om m till beginningAddress
				System.out.println(" Slog INTE samman något");
				//System.out.println("current: " + rCurrent + ", " + "this.cells[current]: " + this.cells[rCurrent] + ", " + "beginningAddress: " + beginningAddress);
			}

			//-----------------------------------

			*//*
			 if-sats som hanterar om det finns/inte finns ledigt block celler PRECIS EFTER det som ska deallokeras
			 *//*
			if(beginningAddress + this.cells[beginningAddress] == rNext) { // det finns ett ledigt block celler PRECIS EFTER det som ska deallokeras
				//uppdatera längd på det block som är ledigt: beginningAddress längd + längden på det lediga cellblocket precis efter
				this.cells[beginningAddress] = this.cells[beginningAddress] + this.cells[rNext];
				this.cells[beginningAddress + 1] = this.cells[rNext + 1];
				System.out.println(" Slog samman: current + next");
				//System.out.println("beginningAddress: " + beginningAddress + ", " + "this.cells[beginningAddress]: " + this.cells[beginningAddress] + ", " + "next: " + rNext);

			} else { // det finns INTE ett ledigt block celler PRECIS EFTER det som ska deallokeras
				this.cells[beginningAddress + 1] = rNext; //pekar beginningAddress + 1 till nästkommande ledigt block av celler
				System.out.println(" Slog INTE samman något...");
			}*/
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
			SPECIALFALL - om vi har kommit till slutet av listan (dvs. next == -1)
			---------------------------------------------------------------------
			 */
			//om vi nått fram till det sista lediga cellblocket och det finns ett allokerat block därefter, så måste vi skriva ut detta
			if(pNext == -1) {
				//sista cellblocket är allokerat
				if(pNext == -1 && ((pCurrentCellAddress + this.cells[pCurrentCellAddress]) - 1) != (this.cells.length - 1)) {
					startAddress = pCurrentCellAddress + this.cells[pCurrentCellAddress];
					stringAllocated += "\n" + startAddress + " - " + (this.cells.length - 1);

				//sista cellblocket är ledigt
				} else {
					stringFree += "\n" + pCurrentCellAddress + " - " + (this.cells.length - 1);
				}
			}
		}

		System.out.println();
		System.out.println(stringAllocated);
		System.out.println();
		System.out.println(stringFree);
	}

	public void printMemory() {
		System.out.println("HELA MINNET:");
		for(int i = 0; i < this.cells.length; i++) {
			System.out.println("i=" + i + ", " + this.cells[i]);
		}
	}
}
