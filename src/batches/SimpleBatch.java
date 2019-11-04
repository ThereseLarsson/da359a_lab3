package batches;

import memory.BestFit;
import memory.FirstFit;
import memory.Memory;
import memory.Pointer;

public class SimpleBatch {

	public static void main(String[] args) {
		SimpleBatch batch = new SimpleBatch();
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
		run(new FirstFit(110)); // Swap this for  your own implementation
		//System.out.println("\nBest fit");
		//run(new BestFit(110)); // Swap this for your own implementation
	}

	public void run(Memory m) {
		Pointer p1, p2, p3, p4, p5, p6;

		p1 = m.alloc(20);
		System.out.println("alloc p1 klar");

		p1.write(range(1, 20));
		System.out.println("write p1 klar");

		p2 = m.alloc(5);
		System.out.println("alloc p2 klar");

		p2.write(range(38, 42));
		System.out.println("write p2 klar");

		p3 = m.alloc(30);
		System.out.println("alloc p3 klar");

		p3.write(range(100, 129));
		System.out.println("write p3 klar");

		p4 = m.alloc(15);
		System.out.println("alloc p4 klar");

		p4.write(range(101, 115));
		System.out.println("write p4 klar");

		m.release(p3);
		System.out.println("release p3 klar");

		m.release(p1);
		System.out.println("release p1 klar");

		p5 = m.alloc(10);
		System.out.println("alloc p5 klar");
		//System.out.println(p5);

		p5.write(range(70, 79));
		System.out.println("write p5 klar");

		p6 = m.alloc(15);
		System.out.println("alloc p6 klar");

		p6.write(range(1, 15));
		System.out.println("write p6 klar");
		
		m.printLayout();

	}
	public void runOriginal(Memory m) {
		Pointer p1, p2, p3, p4, p5, p6;

		System.out.println("run anropades");
		p1 = m.alloc(20);
		System.out.println("alloc skedde!");

		p1.write(range(1, 20));
		p2 = m.alloc(5);
		p2.write(range(38, 42));
		p3 = m.alloc(30);
		p3.write(range(100, 129));
		p4 = m.alloc(15);
		p4.write(range(101, 115));
		m.release(p3);
		m.release(p1);
		p5 = m.alloc(10);
		p5.write(range(70, 79));
		p6 = m.alloc(15);
		p6.write(range(1, 15));

		m.printLayout();

		/*
		Upptaget - FirstFit

		 */

		/*
		Ledigt - FirstFit
		 */

		//ska inte göra den här deluppgiften
//		m.compact();
//		System.out.println("After compact()");
//		m.printLayout();
	}
}
