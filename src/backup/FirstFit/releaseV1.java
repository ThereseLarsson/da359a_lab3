package backup.FirstFit;

public class releaseV1 {

/*    public void release(Pointer pointer, boolean debug) {
        int beginningAddress = pointer.pointsAt(); //returnerar (begynnelse)adressen som pekaren (pointer som fås i metodhuvudet) pekar på
        int rCurrent = freeList;
        //int rNewCurrent = beginningAddress;
        int releaseLength = this.cells[beginningAddress]; //längden på det "block" som ska deallokeras

        int fan = 0, attans = 0;

		*//*
		om freeList kommer EFTER adressen för det som ska deallokeras så vill vi peka om freeList till begynnelseadressen
		(detta eftersom freeList alltid ska peka på första lediga cellen i minnet (this.cells))
		(dvs. om freeList är STÖRRE än beginningAddress)
		 *//*
        if(freeList > beginningAddress) {
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

            System.out.println("RELEASE - "   + "freeList är nu: " + freeList);

            //------------------------------------------------------------------------------------------------------------

        *//*
        om freeList kommer INNAN adressen för det som ska deallokeras
        då behöver freeList INTE pekas om, dvs. freeList ska inte ändra värde i detta fall
         *//*
        } else if(freeList < beginningAddress) { //if (freeList/rCurrent < beginningAddress)
            boolean beginningAddressReached = false;
            int rNext = this.cells[freeList + 1];

            System.out.println("RNEXT" + rNext);

			*//*
			här vill vi fortsätta att stega igenom minnet (this.cells), med freeList som startpunkt, tills att
			vi har hittat de två fria blocken celler som ligger mellan beginningAddress (dvs. det som ska deallokeras)
			 *//*
            fan = rCurrent;
            attans = rNext;
            while(!beginningAddressReached) {
                if (debug) {
                    System.out.println("rCurrent: " + rCurrent + ", beginningAddress: " + beginningAddress + ", rNext: " + rNext);
                }
                if (debug && rNext < rCurrent) {
                    System.out.println(this);
                    //System.exit(0);
                }
                if(rCurrent < beginningAddress && beginningAddress < rNext) {
                    beginningAddressReached = true;
                    fan = rCurrent;
                    attans = rNext;
                    rCurrent = rNext;
                    rNext = this.cells[rCurrent + 1];

                } else if(rCurrent < beginningAddress && rNext == -1) {
                    beginningAddressReached = true;

                } else {
                    System.out.println("ERRORRR");
                    System.out.println("förra current: " + fan + ", förra next: " + attans);
                    System.out.println("rCurrent: " + rCurrent + ", beginningAddress: " + beginningAddress + ", rNext: " + rNext);
                    System.out.println("rNext + 1: " + this.cells[rNext]);
                    System.out.println("Hela minnet: ");
                    for(int i = 0; i < this.cells.length; i++) {
                        System.out.println(this.cells[i]);
                    }

                    System.exit(0);
                }
                //rCurrent = rNext;
                //rNext = this.cells[rCurrent + 1];
            }

            System.out.println("current: " + rCurrent + ", " + "this.cells[current]: " + this.cells[rCurrent] + ", " + "beginningAddress: " + beginningAddress);

			*//*
			 if-sats som hanterar om det finns/inte finns ledigt block celler PRECIS INNAN det som ska deallokeras
			 *//*
            if((rCurrent + this.cells[rCurrent]) == beginningAddress) { // det finns ett ledigt block celler PRECIS INNAN det som ska deallokeras
                //uppdatera längd på ledigt block  deallokering: längden på ledigt block precis innan + längden på det som ska deallokeras
                this.cells[rCurrent] = this.cells[rCurrent] + this.cells[beginningAddress];
                System.out.println(" Slog samman: current + this.cells");


            } else { // det finns INTE ett ledigt block celler PRECIS INNAN det som ska deallokeras
                this.cells[beginningAddress + 1] = this.cells[rCurrent + 1]; //peka this.cells[beginningAddress + 1] = det som m pekade på innan
                this.cells[rCurrent + 1] = this.cells[beginningAddress]; //peka om m till this.cells[beginningAddress]
                System.out.println(" Slog INTE samman något");
                //System.out.println("current: " + rCurrent + ", " + "this.cells[current]: " + this.cells[rCurrent] + ", " + "beginningAddress: " + beginningAddress);
            }

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
                this.cells[beginningAddress + 1] = rNext; //peka beginningAddress + 1 till nästkommande ledigt block av celler
                System.out.println(" Slog INTE samman något...");
            }
        }
        if (beginningAddress == 0) {
            System.out.println("Checking...");
            System.out.println(this.cells[fan]);
            System.out.println(this.cells[attans]);
        }
    }*/
}
