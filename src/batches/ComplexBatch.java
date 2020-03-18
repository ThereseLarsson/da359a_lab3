package batches;

import memory.BestFit;
import memory.FirstFit;
import memory.Memory;
import memory.Pointer;

public class ComplexBatch {
	private FirstFit ff;

	public static void main(String[] args) {
		ComplexBatch batch = new ComplexBatch();
		batch.run();
	}
	
	public int[] range(int start, int stop) {
		int[] range = new int[stop - start + 1];
		
		for (int i = 0; i < range.length; i++) {
			range[i] = start + i;
		}
		
		return range;
	}

	public void run() {
		System.out.println("First fit");
		ff = new FirstFit(1100);
		run(ff); // Swap this for  your own implementation

		//run(new FirstFit(1100)); // Swap this for  your own implementation
		//System.out.println("\nBest fit");
		//run(new BestFit(1100)); // Swap this for  your own implementation
	}

	public void run(Memory m) {
		Pointer[] ps = new Pointer[20];
		int[] zeros = {0};

		ps[0] = m.alloc(100); //efter: freeList = 101
		//ps[0].write(range(1, 100));
		ps[0].write(zeros);
		//executeCheckPoint(true, 0);

		ps[1] = m.alloc(200);
		//ps[1].write(range(1001, 1200));
		ps[1].write(zeros);
		//executeCheckPoint(true, 1);

		ps[2] = m.alloc(500);
		//ps[2].write(range(101, 600));
		ps[2].write(zeros);
		//executeCheckPoint(true, 2);

		ps[3] = m.alloc(200);
		//ps[3].write(range(1, 200));
		ps[3].write(zeros);
		//executeCheckPoint(true, 3);

		m.release(ps[2]);
		//executeCheckPoint(false, 2);

		ps[4] = m.alloc(50);
		//ps[4].write(range(51, 100));
		ps[4].write(zeros);
		//executeCheckPoint(true, 4);

		ps[5] = m.alloc(5);
		//ps[5].write(range(42, 46));
		ps[5].write(zeros);
		//executeCheckPoint(true, 5);

		ps[6] = m.alloc(90);
		//ps[6].write(range(5000, 5089));
		ps[6].write(zeros);
		//executeCheckPoint(true, 6);

		m.release(ps[0]);
		//executeCheckPoint(false, 0);

		ps[7] = m.alloc(60);
		//ps[7].write(range(10, 69));
		ps[7].write(zeros);
		//executeCheckPoint(true, 7);

		m.release(ps[4]);
		//executeCheckPoint(false, 4);

		ps[8] = m.alloc(45);
		//ps[8].write(range(1, 45));
		ps[8].write(zeros);
		//executeCheckPoint(true, 8);

		m.release(ps[5]); //+ litet mindfuck (lite svårlästa värden i minnet som inte behöver ändras)
		//executeCheckPoint(false, 5);

		ps[9] = m.alloc(10);
		//ps[9].write(range(16, 25));
		ps[9].write(zeros);
		//executeCheckPoint(true, 9);

		m.release(ps[6]);
		//executeCheckPoint(false, 6);

		ps[10] = m.alloc(40);
		//ps[10].write(range(301, 340));
		ps[10].write(zeros);
		//executeCheckPoint(true, 10);

		ps[11] = m.alloc(200); //skriver inte 0 på alla ställen för denna allokering?
		//ps[11].write(range(1, 200));
		ps[11].write(zeros);
		//executeCheckPoint(true, 11);

		ps[12] = m.alloc(35);
		//ps[12].write(range(501, 535));
		ps[12].write(zeros);
		//executeCheckPoint(true, 12);

		ps[13] = m.alloc(25);
		//ps[13].write(range(1, 25));
		ps[13].write(zeros);
		//executeCheckPoint(true, 13);

		ps[14] = m.alloc(60);
		//ps[14].write(range(1, 60));
		ps[14].write(zeros);
		//executeCheckPoint(true, 14);

		ps[15] = m.alloc(60);
		//ps[15].write(range(1, 60));
		ps[15].write(zeros);
		//executeCheckPoint(true, 15);

		//((FirstFit) m).release(ps[3], true); //fastnar i oändlig loop här

		m.release(ps[3]);
		//executeCheckPoint(false, 3);

		m.release(ps[13]);
		//executeCheckPoint(false, 13);

		m.release(ps[12]);
		//executeCheckPoint(false, 12);

		//m.printLayout();
		//System.exit(0);

		ps[16] = m.alloc(170);
		//ps[16].write(range(10001, 10170));
		ps[16].write(zeros);
		//executeCheckPoint(true, 16);

//		m.compact(); //ska inte göra den här deluppgiften

		ps[17] = m.alloc(30);
		//ps[17].write(range(40, 65));
		ps[17].write(zeros);
		//executeCheckPoint(true, 17);

		//----------- CORRECT SO FAR ---------------

		ps[18] = m.alloc(40);
		//ps[18].write(range(1, 40));
		ps[18].write(zeros);
		executeCheckPoint(true, 18);

		ps[19] = m.alloc(5);
		//ps[19].write(range(11, 15));
		ps[19].write(zeros);
		//executeCheckPoint(true, 19);

		m.printLayout();
		
		// After these last releases, the memory table should be empty
		m.release(ps[1]);
		m.release(ps[7]);
		m.release(ps[8]);
		m.release(ps[9]);
		m.release(ps[10]);
		m.release(ps[11]);
		m.release(ps[14]);
		m.release(ps[15]);
		m.release(ps[16]);
		m.release(ps[17]);
		m.release(ps[18]);
		m.release(ps[19]);
		
		m.printLayout();

		/*
		Upptaget - FirstFit
		0-589
		620-949
		 */

		/*
		Ledigt - FirstFit
		590-619
		950-999
		 */
	}

    /**
     * used as checkpoint to print the contents of the memory, if its a allocation or release and then stops the execution
     * @param isAlloc, true if allocation, false if release
     * @param pointerNbr, which pointer the execution refers to
     */
	public void executeCheckPoint(boolean isAlloc, int pointerNbr) {
		System.out.println();

		//om alloc
		if(isAlloc) {
			System.out.println("------------------ efter alloc_p" + pointerNbr + " ------------------------------");

		//om release
		} else {
			System.out.println("------------------ efter release_p" + pointerNbr + " ------------------------------");
		}

		System.out.println("freeList = " + FirstFit.freeList);
		ff.printMemory();
		System.out.println();
		System.exit(0);
	}
}
