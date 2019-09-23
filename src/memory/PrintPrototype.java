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
            if(i == 5 || i == 6 || i == 10 || i == 11) {
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
            if(nbrs[i] == -1 && i == 0 || nbrs[i] == -1 && nbrs[i - 1] != -1) { //if current free AND is the first element in the list || if current free AND previous not free
                resFree += i + " - ";
            } else if(nbrs[i] == -1 && i == nbrs.length - 1 || nbrs[i] == -1 && nbrs[i + 1] != -1) { //if current free AND is the last element in the list || if current free AND next not free
                if(i == nbrs.length - 1) { //if last element
                    resFree += i;
                } else {
                    resFree += i + "\n";
                }
            }

            if(nbrs[i] != -1 && i == 0 || nbrs[i] != -1 && nbrs[i - 1] == -1) { //if current occupied AND is the first element in the list || if current occupied AND previous not occupied
                resOccupied += i + " - ";
            } else if(nbrs[i] != -1 && i == nbrs.length - 1 || nbrs[i] != -1 && nbrs[i + 1] == -1) { //if current occupied AND is the last element in the list || if current occupied AND next not occupied
                if(i == nbrs.length - 1) { //if last element
                    resOccupied += i;
                } else {
                    resOccupied += i + "\n";
                }
            }
        }

        System.out.println("Free" + "\n" + resFree + "\n");
        System.out.println("Allocated" + "\n" + resOccupied);
    }

    public static void main(String[] args) {
        PrintPrototype p = new PrintPrototype();
        p.fillList();
        p.printListWithInterval();
    }
}
