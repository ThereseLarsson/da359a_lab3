package memory;

import java.awt.*;

/**
 * This memory model allocates memory cells based on the best-fit method.
 * deluppgift 1
 *
 * Väljer att jobba "direkt" i minnet.
 *
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class BestFit extends Memory {
	static public int  freeList;

	/**
	 * Initializes an instance of a first fit-based memory.
	 *
	 * @param totalNbrMemoryCells The (total) number of cells.
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
	 * Här pekar vi om freeList (dvs pekaren till den första lediga cellen i minnet (dvs. this.cells))
	 *
	 * this.cells[currentCellAddress] = antalet celler i rad som är lediga
	 *
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell (to be allocated).
	 */
	@Override
	public Pointer alloc(int size) {
		Pointer pointer = null;
		int previous = -1; //föregående cell-adress (för ledigt "hop")
		int current = freeList; //nuvarande cell-adress (för ledigt "hop")
		int next = 0; //nästkommande cell-adress (för ledigt "hop")
		int sizeToAllocate = size + this.offset; //storlek på det som ska allokeras

		//bestFit
		int bestAddressSoFar = -5;
		int previous_best = 0;
		int next_best = 0;
		//boolean allocSucceed = false;

		//-------------------------- LETAR BÄSTA PLATS (ADRESS) ATT GÖRA ALLOKERA PÅ -------------------------------------------------------------
		do {
			next = this.cells[current + 1]; //får adressen till nästa "hop" lediga celler

			//vi har hittat ett ställe där vi kan allokera
			if(this.cells[current] >= sizeToAllocate) {
				//allocSucceed = true;

				if(bestAddressSoFar == -5 || this.cells[current] <= this.cells[bestAddressSoFar]) { //jämförelse: spara den bästa lösningen (bäst addressen att allokera på) hittills
					bestAddressSoFar = current;
					previous_best = previous;
					next_best = next;
					System.out.println("next_best: " + next_best);
				}
			}

			//vi går vidare till nästa lediga "hop" i minnet
			previous = current;
			current = next; //vill ha kvar den nuvarande cellen vi är på

			/*if(current != -1) {
				next = this.cells[current + 1]; //får adressen till nästa "hop" lediga celler
			}*/

		} while(current > -1); //searches list (this.cells) for free space, starts with the first free space.

		System.out.println("Ute ur loop - Bästa address: " + bestAddressSoFar);
		System.out.println("previous_best: " + previous_best);
		System.out.println("next_best: " + next_best);

		//-------------------------- ALLOKERING -------------------------------------------------------------
		/*
		-------- SCENARIO 2 - LUCKA --------
		 */
		if(this.cells[bestAddressSoFar] > sizeToAllocate + 1) {

			// Scenario 2.1 - om allokeringen sker på den första lediga "hopen" i minnet (dvs. freeList)
			if(bestAddressSoFar == freeList) {
				freeList = bestAddressSoFar + sizeToAllocate;

			//Scenario 2.2
			} else {
				this.cells[previous_best + 1] = bestAddressSoFar + sizeToAllocate;
			}
			//gemensamm kod för Scenario 2.1 och 2.2
			this.cells[bestAddressSoFar + sizeToAllocate] = this.cells[bestAddressSoFar] - sizeToAllocate;
			this.cells[bestAddressSoFar + sizeToAllocate + 1] = next_best; //HÄR
			this.cells[bestAddressSoFar] = sizeToAllocate;
		}

		/*
		 -------- SCENARIO 1  - INGEN LUCKA i den lediga "hopen" av lediga minnesceller --------
		 */
		else if(this.cells[bestAddressSoFar] == sizeToAllocate) { //betyder att det inte blir någon lucka

			// Scenario 1.1
			if(bestAddressSoFar == freeList) {
				freeList = next;
				System.out.println("Scenario 1.1, FL");

			//Scenario 1.1
			} else if (previous >= 0){
				this.cells[previous + 1] = next;
				System.out.println("Scenario 1.1 ?");
			}

			//om det blir en lucka som är 1 cell stor
		} else if(this.cells[bestAddressSoFar] == sizeToAllocate + 1) {
			if(bestAddressSoFar == freeList) {
				freeList = next;

			} else if (previous >= 0){
				this.cells[previous + 1] = next;
			}
			this.cells[bestAddressSoFar] = sizeToAllocate + 1;
			System.out.println("Lucka: 1 cell stor");
		}


		//-------------- Allokering lyckad! -------------------
		if(bestAddressSoFar != -5) {
			pointer = new Pointer(bestAddressSoFar, this);
		}

		System.out.println("///////////////////////////////////////");
		return pointer;
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
		int releaseLength = this.cells[beginningAddress]; //längden på det "block" som ska deallokeras

		/*
		om freeList kommer EFTER adressen för det som ska deallokeras så vill vi peka om freeList till begynnelseadressen
		(detta eftersom freeList alltid ska peka på första lediga cellen i minnet (this.cells))
		(dvs. om freeList är STÖRRE än beginningAddress)
		 */
		if(freeList > beginningAddress) {
			//System.out.println("RELEASE - freeList kom EFTER adressen för det som ska deallokeras vid detta anrop");

			if(beginningAddress + releaseLength == freeList) { //om deallokeringen sker hela vägen fram till ett block av lediga celler

				//peka om current i minnet och pekare till nästa block lediga celler i minnet
				this.cells[beginningAddress] = this.cells[freeList] + releaseLength; //längden på gamla freeList + deallokeringsblockets längd
				this.cells[beginningAddress + 1] = this.cells[freeList + 1]; //pekare till nästa

			} else { //om deallokeringen INTE sker hela vägen fram till ett block av lediga celler
				//this.cells[rNewCurrent] behöver inte uppdateras i det här fallet! dock behöver denna uppdateras i fallet där freeList kommer INNAN begynnelseadressen för deallokeringen
				this.cells[beginningAddress + 1] = freeList;
			}

			freeList = beginningAddress; //pekar om freeList till begynnelseAdressen för det som ska deallokeras

			//------------------------------------------------------------------------------------------------------------

        /*
        om freeList kommer INNAN adressen för det som ska deallokeras
        då behöver freeList INTE pekas om, dvs. freeList ska inte ändra värde i detta fall
         */
		} else if(freeList < beginningAddress) { //if (freeList/rCurrent < beginningAddress)
			//System.out.println("RELEASE - freeList kom INNAN adressen för det som ska deallokeras vid detta anrop");

			boolean beginningAddressReached = false;
			rCurrent = freeList; //NY
			int rNext = this.cells[freeList + 1];

			/*
			här vill vi fortsätta att stega igenom minnet (this.cells), med freeList som startpunkt, tills att
			vi har hittat de två fria blocken celler som ligger mellan beginningAddress (dvs. det som ska deallokeras)
			 */
			while(!beginningAddressReached) {

				if(rCurrent < beginningAddress && rNext > beginningAddress) {
					beginningAddressReached = true;

				} else if(rCurrent < beginningAddress && rNext == -1) {
					beginningAddressReached = true;

				} else { //dvs. har inte hittat beginningAddress än
					rCurrent = rNext;
					rNext = this.cells[rCurrent + 1];
				}
			}

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
				//System.out.println("///// ledigt RELEASE ledigt /////");

			 /*
			  LEDIGT INNAN och
			  UPPTAGET EFTER
			  */
			} else if((((rCurrent + this.cells[rCurrent]) == beginningAddress)) && !(beginningAddress + this.cells[beginningAddress] == rNext)) {
				this.cells[rCurrent] = this.cells[rCurrent] + this.cells[beginningAddress]; //uppdatera längd på ledigt block  deallokering: längden på ledigt block precis innan + längden på det som ska deallokeras
				//System.out.println("///// ledigt RELEASE upptaget /////");

			 /*
			  UPPTAGET INNAN och
			  LEDIGT EFTER
			  */
			} else if((!((rCurrent + this.cells[rCurrent]) == beginningAddress)) && (beginningAddress + this.cells[beginningAddress] == rNext)) {
				this.cells[rCurrent + 1] = beginningAddress; //pekar om pekaren (för det förekommande lediga blocket) till beginningAddress
				this.cells[beginningAddress + 1] = this.cells[rNext + 1]; //skapar pekare för beginningAddress + 1 som pekar till rNext + 1
				this.cells[beginningAddress] = this.cells[beginningAddress] + this.cells[rNext]; //lägger ihop längden för det block som ska deallokeras med längden för det lediga blockets längd som kommer precis efter
				//System.out.println("///// upptaget RELEASE ledigt /////");

			 /*
			  UPPTAGET INNAN och
			  UPPTAGET EFTER
			  */
			} else if((!((rCurrent + this.cells[rCurrent]) == beginningAddress)) && !(beginningAddress + this.cells[beginningAddress] == rNext)) {
				this.cells[beginningAddress + 1] = rNext; //pekar beginningAddress + 1 till nästkommande ledigt block av celler
				this.cells[rCurrent + 1] = beginningAddress; //pekar om pekaren (för det förekommande lediga blocket) till beginningAddress
				//System.out.println("///// upptaget RELEASE upptaget /////");
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
		int pCurrent = freeList;
		int pNext = this.cells[pCurrent + 1];
		int startAddress, endAddress;

		//om det första cellblocket i minnet (dvs. address 0 - x) är UPPTAGET så vill vi skriva ut det
		if(pCurrent != 0) {
			stringAllocated += "\n" + "0 - " + (pCurrent - 1);
		}

		//stega igenom minnet, börjar på freeList
		while(pCurrent > -1) {
			startAddress = pCurrent + this.cells[pCurrent];

			//sista lediga "hopen" i minnet
			if(pNext == -1) {
				if((pCurrent + this.cells[pCurrent]) == this.cells.length) { //sista lediga "hopen": finns inget upptaget efter
					endAddress = (pCurrent + this.cells[pCurrent]) - 1;
					stringFree += "\n" + pCurrent + " - " + endAddress;

				} else if(!((pCurrent + this.cells[pCurrent]) == this.cells.length)) { //sista lediga "hopen": finns upptaget efter
					//startAddress = pCurrent + this.cells[pCurrent];
					stringAllocated += "\n" + startAddress + " - 1099";
				}

				if(stringAllocated.equals("Allocated")) {
					stringAllocated += "\n" + "- no allocated space -";
				}

				//inte sista lediga hopen i minnet
			} else if(pNext != -1) {
				//FRITT
				endAddress = (pCurrent + this.cells[pCurrent]) - 1;
				stringFree += "\n" + pCurrent + " - " + endAddress;

				//ALLOKERAT
				//startAddress = pCurrent + this.cells[pCurrent];
				endAddress = pNext - 1;
				stringAllocated += "\n" + startAddress + " - " + endAddress;
			}

			/*
            NÄSTA LEDIGA "HOP" AV CELLER
            går vidare till nästa lediga block av lediga celler i minnet
            -----------------------------------------------------------------------------------------
             */
			pCurrent = pNext;

			if(pCurrent != -1) {
				pNext = this.cells[pCurrent + 1];
			}
		}

		System.out.println("----------------------------");
		System.out.println(stringAllocated);
		System.out.println();
		System.out.println(stringFree);
		System.out.println("----------------------------");
	}

	public void printMemory() {
		System.out.println("HELA MINNET:");
		for(int i = 0; i < this.cells.length; i++) {
			System.out.println("i=" + i + ", " + this.cells[i]);
		}
	}
}
