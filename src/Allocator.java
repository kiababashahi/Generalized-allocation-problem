import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.DefaultEditorKit.CopyAction;

public class Allocator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadData rd = new ReadData();
		rd.readFile("e10400.txt");
		/*
		 * for(int i=0;i<rd.num_agents;i++) { for(int j=0;j<rd.num_resources;j++) {
		 * System.out.print(rd.c[i][j] +" ");F } System.out.println(); }
		 * System.out.println("*************"); for(int i=0;i<rd.num_agents;i++) {
		 * for(int j=0;j<rd.num_resources;j++) { System.out.print(rd.a[i][j] +" "); }
		 * System.out.println(); } System.out.println("*****"); for(int
		 * i=0;i<rd.num_agents;i++) { System.out.print(rd.b[i]+ " "); }
		 */
		Heuristics h1 = new Heuristics(rd, 45746);
		h1.constructive_heuristic();
		//Random rn = new Random();// 123
		//int r1 = rn.nextInt(h.allocated_costs.size() / 3);
		// for(int i=0;i<h.allocated_costs.size();i++)
		// System.out.println(h.allocated_costs.get(i).value);
		int best_gap;
		int[][]x=new int[h1.x.length][h1.x[0].length];
		int[]b=new int[h1.b.length];
		copy(x, h1.x);
		copy(b, h1.b);
		ArrayList<assigned_costs> allocated_costs=new ArrayList<assigned_costs>();
		copy(allocated_costs, h1.allocated_costs);
		Local_Search l1 = new Local_Search(h1.a, h1.objective_value, x, rd.c, b, h1.allocated_costs, h1.optimum);
		l1.search();
		int best=l1.objective;
		int k=0;
		int[][]best_x=new int[x.length][x[0].length];
		int[] best_b=new int[b.length];
		while(k<1000 && best-h1.optimum>= 10) {
		Meta m1=new Meta(x, h1.a, h1.c, b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m1.set_candidate();
		/*Meta m2=new Meta(x, h1.a, h1.c, b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m2.set_candidate();
		Meta m3=new Meta(x, h1.a, h1.c, b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m3.set_candidate();
		Meta m4=new Meta(x, h1.a, h1.c,b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m4.set_candidate();
		Meta m5=new Meta(x, h1.a, h1.c, b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m5.set_candidate();
		Meta m6=new Meta(x, h1.a, h1.c, b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m6.set_candidate();
		Meta m7=new Meta(x, h1.a, h1.c, b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m7.set_candidate();*/
		Local_Search l2 = new Local_Search(m1.a, m1.objective, m1.x, m1.c, m1.b, m1.assignments, m1.optimal);
		l2.search();
		if(l2.objective<best) {
			best=l2.objective;
		
			copy(best_x,l2.assignments );
			copy(best_b, l2.b);
		}
		k++;
		}
		System.out.println("the best value found is"+ best + "the gap is "+ (best-h1.optimum)*100.0/best);
/*
		System.out.println();
		for(int i=0;i<m1.b.length;i++)
			System.out.print(m1.b[i] + " ");
		System.out.println();
		for(int i=0;i<m2.b.length;i++)
			System.out.print(m2.b[i] + " ");
		System.out.println();
		for(int i=0;i<m3.b.length;i++)
			System.out.print(m3.b[i] + " ");
		System.out.println();
		for(int i=0;i<m4.b.length;i++)
			System.out.print(m4.b[i] + " ");
		System.out.println();
		for(int i=0;i<m5.b.length;i++)
			System.out.print(m5.b[i] + " ");
		System.out.println();
		for(int i=0;i<m5.b.length;i++)
			System.out.print(m6.b[i] + " ");
		System.out.println();
		for(int i=0;i<m5.b.length;i++)
			System.out.print(m7.b[i] + " ");*/
		/*
		 * Random rn1=new Random();//1245 //System.out.println(h.optimum); //Random
		 * ran=new Random(); int k=0; 
		 * Meta m=new Meta(l1.assignments, l1.a, l1.c, l1.b,l1.costs_Assigned, l1.objective, l1.optimal); 
		 * while(m.objective-m.optimal>10
		 * && k<1000) { //System.err.println("hereeee"); int
		 * cn=rn1.nextInt(l1.costs_Assigned.size()); m.set_candidate(cn);
		 * m.new_assignment(); k++; }
		 */
	}
	public static void check_x_equality(int[][]x,int[][]y) {
		int flag=0;
		for(int i=0;i<x.length;i++ ) {
			for(int j=0;j<x[0].length;j++) {
				if(x[i][j]!=y[i][j]) {
					System.err.println("its cool");
					}
			}
		}
	}
	public static void copy (int[][]x,int y[][]) {
		int temp=0;
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x[0].length;j++) {
				temp=y[i][j];
				x[i][j]=temp;
			}
		}
	}
	public static void copy(int[] x,int[]y) {
		int temp;
		for(int i=0;i<x.length;i++) {
			temp=y[i];
			x[i]=temp;
	}}
	public static void copy(ArrayList<assigned_costs>a, ArrayList<assigned_costs>b) {
		assigned_costs temp1;
		//if(a.size()!=b.size()) System.err.println("dangeeerrrrrrrrrrrrrrrrrr");
		for(int i=0;i<b.size();i++) {
			temp1=new assigned_costs(b.get(i).value, b.get(i).i_val,b.get(i).j_val);
			a.add(i,temp1);
		}
	}
}
