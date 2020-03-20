package backup.FirstFit;

public class releaseV2 {

   /* public void release(Pointer pointer, boolean debug) {
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

        *//*
        om freeList kommer INNAN adressen för det som ska deallokeras
        då behöver freeList INTE pekas om, dvs. freeList ska inte ändra värde i detta fall
         *//*
        } else if(freeList < beginningAddress) { //if (freeList/rCurrent < beginningAddress)
            System.out.println("RELEASE - freeList kom INNAN adressen för det som ska deallokeras vid detta anrop");

            boolean beginningAddressReached = false;
            rCurrent = freeList; //NY
            int rNext = this.cells[freeList + 1];

            //System.out.println("RNEXT" + rNext);

			*//*
			här vill vi fortsätta att stega igenom minnet (this.cells), med freeList som startpunkt, tills att
			vi har hittat de två fria blocken celler som ligger mellan beginningAddress (dvs. det som ska deallokeras)
			 *//*
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

			*//*
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
    }*/
}
