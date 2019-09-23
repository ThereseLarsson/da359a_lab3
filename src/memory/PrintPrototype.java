package memory;

import java.util.Random;

public class PrintPrototype {
    private int[] nbrs;
    private Random rand;

    public PrintPrototype() {
        nbrs = new int[12];
        rand = new Random();
    }

    public void fillList() {
        for(int i = 0; i < nbrs.length; i++) {
            if(i == 0 || i == 1 || i == 9 || i == 10) {
                nbrs[i] = -1;
            } else {
                nbrs[i] = rand.nextInt(10) + 1;
            }
        }
    }

    public void printList() {
        for(int i = 0; i < nbrs.length; i++) {
            System.out.println("index " + i + ": " + nbrs[i]);
        }
    }

    public void printListWithInterval() {
        String resOccupied = "";
        String resFree = "";

        for(int i = 0; i < nbrs.length; i++) {
            if(nbrs[i] == -1 && i == 0 || nbrs[i] == -1 && nbrs[i - 1] != -1) { // if current free AND is the first element in the list || if current free AND previous not free
                resFree += i + " - ";
            } else if(nbrs[i] == -1 && i == nbrs.length - 1 || nbrs[i] == -1 && nbrs[i + 1] != -1) { //if current free AND is the last element in the list || if current free AND next not free
                resFree += i + "\n";
            }
        }

        System.out.println("Ledigt" + "\n" + resFree);
        //System.out.println("Upptaget" + "\n" + resOccupied);
    }

    public static void main(String[] args) {
        PrintPrototype p = new PrintPrototype();
        p.fillList();
        p.printListWithInterval();
    }
}
