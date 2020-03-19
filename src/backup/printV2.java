package backup;

//bara städad kod som skiljer från printV1
public class printV2 {

/*    public void printLayout() {
        String stringAllocated = "Allocated";
        String stringFree = "Free";
        int pCurrent = freeList;
        int pNext = this.cells[pCurrent + 1];
        int startAddress, endAddress;

        //om det första cellblocket i minnet (dvs. address 0 - x) är UPPTAGET så vill vi skriva ut det
        if(pCurrent != 0) {
            stringAllocated += "\n" + "0 - " + (pCurrent - 1);
        }

        //stega igenom minnet (dvs. this.cells)
        while(pNext > -1) {
			*//*
			UTSKRIFTER
			------------------------------------------------------------------------------------------
			 *//*
            // FRITT - skriver ut det lediga block vi befinner oss på JUST NU (dvs. pCurrent)
            endAddress = (pCurrent + this.cells[pCurrent]) - 1;
            stringFree += "\n" + pCurrent + " - " + endAddress;

		    *//*
		    ALLOKERAT - skriver ut det allokerade blocket som finns mellan pCurrent och pNext
		    this.cells[pCurrent] + pCurrent = var (dvs. vilken address) som det allokerade blocket STARTAR
		    pNext - 1 = var (dvs. vilken address) som det allokerade blocket SLUTAR
		     *//*
            startAddress = pCurrent + this.cells[pCurrent];
            endAddress = pNext - 1;
            stringAllocated += "\n" + startAddress + " - " + endAddress;

            *//*
            NÄSTA LEDIGA "HOP" AV CELLER
            går vidare till nästa lediga block av lediga celler i minnet
            -----------------------------------------------------------------------------------------
             *//*
            pCurrent = pNext;
            pNext = this.cells[pCurrent + 1];

			*//*
			SPECIALFALL - om vi har kommit till slutet av listan (dvs. pNext == -1)
			-----------------------------------------------------------------------------------------
			 *//*
            //om vi nått fram till det sista lediga cellblocket och det finns ett allokerat block därefter, så måste vi skriva ut detta
            if(pNext == -1) {
                //sista cellblocket är allokerat
                if(pNext == -1 && ((pCurrent + this.cells[pCurrent]) - 1) != (this.cells.length - 1)) {
                    startAddress = pCurrent + this.cells[pCurrent];
                    stringAllocated += "\n" + startAddress + " - " + (this.cells.length - 1);

                    //sista cellblocket är ledigt
                } else {
                    stringFree += "\n" + pCurrent + " - " + (this.cells.length - 1);
                }
            }
        }

        System.out.println();
        System.out.println(stringAllocated);
        System.out.println();
        System.out.println(stringFree);
    }*/

}
