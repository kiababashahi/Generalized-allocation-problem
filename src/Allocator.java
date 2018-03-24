import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.DefaultEditorKit.CopyAction;

public class Allocator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadData rd = new ReadData();
		rd.readFile("e05100.txt");
		Nap_Const h1=new Nap_Const(rd,12681);
		h1.assign();
		
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
		while(k<100 && best-h1.optimum>= 0.000001) {
		Meta m1=new Meta(x, h1.a, h1.c, b, h1.allocated_costs, h1.objective_value, h1.optimum);
		m1.set_candidate();
		Local_Search l2 = new Local_Search(m1.a, m1.objective, m1.x, m1.c, m1.b, m1.assignments, m1.optimal);
		l2.search();
		if(l2.objective<best) {
			best=l2.objective;
		
			copy(best_x,l2.assignments );
			copy(best_b, l2.b);
		}
		System.out.println(k);

		k++;
		}
		System.out.println(" the best value found is"+ best + "the gap is "+ (best-h1.optimum)*100.0/best);
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
		for(int i=0;i<b.size();i++) {
			temp1=new assigned_costs(b.get(i).value, b.get(i).i_val,b.get(i).j_val);
			a.add(i,temp1);
		}
	}
}
