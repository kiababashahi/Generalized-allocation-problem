import java.util.ArrayList;
import java.util.Random;

public class Local_Search {
	int[][]assignments;
	int[][]c;
	int[][]a;
	int[]b;
	int candiate;
	int objective;
	int num_resources;
	int num_agents;
	ArrayList<assigned_costs>costs_Assigned;
	int optimal;
	assigned_costs new_cost1;
	assigned_costs new_cost2;
public Local_Search(int[][] a,int objective,int candidate,int[][]x,int[][]c,int[]b,ArrayList<assigned_costs>costs_Assigned,int opt) {
	assignments=x;
	this.a=a;
	num_agents=a.length;
	num_resources=a[0].length;
	this.c=c;
	this.b=b;
	this.costs_Assigned=costs_Assigned;
	this.candiate=candidate;
	this.objective=objective;
	optimal=opt;
	}
public int search() {
	Random ran=new Random();
	int k=0;
	int flag=0;
	while(optimal-objective>0.000001|| k<(1000)) {
	if (flag!=0) {
		candiate=ran.nextInt(costs_Assigned.size());
	}
	int x=ran.nextInt(costs_Assigned.size());
	if(x!=candiate) {
		swap(x);
	}
	else {
		while(x!=candiate) {
			x=ran.nextInt(costs_Assigned.size());
			if(x!=candiate)
				swap(x);
		}
	}
	check_Feas();
	//System.out.println(objective);
	flag++;
	k++;
}
	//System.out.println((1.0*(optimal)/objective)*100);
	return objective;
	}


public void swap(int x) {
	int temp_x=b[i(x)]+a[i(x)][j(x)];
	int temp_cand=b[i(candiate)]+a[i(candiate)][j(candiate)];
	if((temp_x-a[i(x)][j(candiate)]>=0) && (temp_cand-a[i(candiate)][j(x)]>=0)) {
		int temp_obj=objective-c[i(x)][j(x)]-c[i(candiate)][j(candiate)];
		if((temp_obj+c[i(x)][j(candiate)]+c[i(candiate)][j(x)])<objective) {
			objective=temp_obj+c[i(x)][j(candiate)]+c[i(candiate)][j(x)];
			//if(assignments[i(x)][j(x)]==1) System.err.println("PIZZZZZZAAAAAAAAAAAAAAAa");
			if(assignments[i(x)][j(x)]!=1) {System.err.println("stoppp"); }
			assignments[i(x)][j(x)]=0;
			assignments[i(candiate)][j(candiate)]=0;
			assignments[i(x)][j(candiate)]=1;
			assignments[i(candiate)][j(x)]=1;
			b[i(x)]=temp_x-a[i(x)][j(candiate)];
			b[i(candiate)]=temp_cand-a[i(candiate)][j(x)];
			int p1=c[i(x)][j(candiate)];
			int p2=i(x);
			int p3=j(candiate);
			new_cost1=new assigned_costs(p1,p2,p3);
			int q1=c[i(candiate)][j(x)];
			int q2=i(candiate);
			int q3=j(x);
			new_cost2=new assigned_costs(q1,q2,q3);
			costs_Assigned.set(x,new_cost1);
			costs_Assigned.set(candiate,new_cost2);
			
		}
	}
}
public int i(int x) {
	return costs_Assigned.get(x).i_val;
}
public int j(int x) {
	return costs_Assigned.get(x).j_val;
}
public void check_Feas() {// this function checks feasibility
	//System.out.println("feaschecking");
	int flag = 0;
	int zer0_flag = 0;
	for (int j = 0; j < num_resources; j++) {
		for (int i = 0; i < num_agents; i++) {
			if (assignments[i][j] == 1) {
				flag++;
			}
			if (assignments[i][j] == 0) {
				zer0_flag++;
			}
			/*
			 * if(zer0_flag==4) { System.err.println(i + ""+ j); }
			 */
		}
		if ( flag > 1 || zer0_flag == num_agents) {
			System.err.println("infeasibleh");
		}
		flag = 0;
		zer0_flag = 0;
	}
	for (int i = 0; i < num_agents; i++) {
		if (b[i] < 0) {
			System.err.println("infeasible");
		}
	}
/*	for (int i = 0; i < num_agents; i++) {
		for (int j = 0; j < num_resources; j++) {
			System.out.print(assignments[i][j]);
		}
		System.out.println();
	}*/
}
public void Sort_assigned_costs(){
	costs_Assigned=new ArrayList<assigned_costs>();
	assigned_costs as;
	for(int i=0;i<num_agents;i++) {
		for(int j=0;j<num_resources;j++) {
			if(assignments[i][j]==1) {
				as=new assigned_costs(c[i][j], i, j);
				 costs_Assigned.add(as);
			}
		}
	}
}
}
