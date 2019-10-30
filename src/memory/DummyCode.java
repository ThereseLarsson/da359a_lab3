package memory;

public class DummyCode extends Memory {
    private int freeList;

    public DummyCode(int totalNbrMemoryCells) {super(totalNbrMemoryCells);}

    @Override
    public Pointer alloc(int sizeToAllocate) {return null;}

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
            while(beginningAddressReached == false) {
                if(rCurrent < beginningAddress && rNext > beginningAddress) {
                    beginningAddressReached = true;
                }
                rCurrent = rNext;
                rNext = this.cells[rCurrent + 1];
            }


            if((rCurrent + this.cells[rCurrent]) == beginningAddress) { // Scenario 2.1 + 2.2 - det finns ett ledigt block celler precis INNAN det som ska deallokeras

                if(beginningAddress + this.cells[beginningAddress] == rNext) { // Scenario 2.1 - det finns ett ledigt block celler direkt EFTER det som ska deallokeras


                } else { // Scenario 2.2 - det finns INTE ett ledigt block celler direkt EFTER det som ska deallokeras


                }

            } else { // Scenario 2.3 + 2.4 - det finns INTE ett ledigt block celler precis INNAN det som ska deallokeras

                if(beginningAddress + this.cells[beginningAddress] == rNext) { //Scenario 2.3 - det finns ett ledigt block celler direkt EFTER det som ska deallokeras


                } else { // Scenario 2.4 - det finns INTE ett ledigt block celler direkt EFTER det som ska deallokeras


                }
            }
        }
    }

    @Override
    public void printLayout() {}
}
