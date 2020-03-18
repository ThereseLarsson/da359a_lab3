package backup;

/*
funkar nästan helt som den ska.
kan inte allokera om det sker på den sista lediga platsen i minnet.
 */
public class allocV1 {

    /*public Pointer alloc(int size) { //använd pointer.address (får en int)
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
				*//*
				SCENARIO 2 - lUCKA
				 *//*
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

				*//*
				SCENARIO 1  - INGEN LUCKA i den lediga "hopen" av lediga minnesceller
				 *//*
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

        } while(next > -1); //searches list (this.cells) for free space, starts with the first free space. Om indexet = -1 så har vi nått this.cells slut

        //printMemory();
        return null; //kommer vi hit så har allokeringen misslyckats, då ska null returneras
    }*/
}
