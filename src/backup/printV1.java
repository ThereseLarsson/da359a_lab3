package backup;

public class printV1 {

   /* public void printLayout() {
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
			*//*
			UTSKRIFTER
			------------------------------------------------------------------------------------------
			 *//*
            // FRITT - skriver ut det lediga block vi befinner oss på JUST NU (dvs. pCurrentCellAddress)
            endAddress = (pCurrentCellAddress + this.cells[pCurrentCellAddress]) - 1;
            stringFree += "\n" + pCurrentCellAddress + " - " + endAddress;

		    *//*
		    ALLOKERAT - skriver ut det allokerade blocket som finns mellan pCurrent och pNext
		    this.cells[pCurrent] + pCurrent = var (dvs. vilken address) som det allokerade blocket STARTAR
		    pNext - 1 = var (dvs. vilken address) som det allokerade blocket SLUTAR
		     *//*
            startAddress = pCurrentCellAddress + this.cells[pCurrentCellAddress];
            endAddress = pNext - 1;
            stringAllocated += "\n" + startAddress + " - " + endAddress;

            *//*
            NÄSTA LEDIGA "HOP" AV CELLER
            går vidare till nästa lediga block av lediga celler i minnet
            ---------------------------------------------------------------------
             *//*
            pCurrentCellAddress = pNext;
            pNext = this.cells[pCurrentCellAddress + 1];

			*//*
			SPECIALFALL - om vi har kommit till slutet av listan (dvs. next == -1)
			---------------------------------------------------------------------
			 *//*
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
    }*/
}
