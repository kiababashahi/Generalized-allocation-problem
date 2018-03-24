import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Meta {
	int[][] x;
	int c[][];
	int b[];
	int a[][];
	int candidate;
	int objective;
	int optimal;
	int[]temp_b;
	int[][] temp_x;;
	assigned_costs as1;
	ArrayList<assigned_costs> new_ass;
	ArrayList<assigned_costs> assignments=new ArrayList<assigned_costs>();
	int[] ints;
	public Meta(int[][] hx, int a[][], int[][] c, int[] hb, ArrayList<assigned_costs> assignments, int objective,
			int optimal) {
		x = new int[hx.length][hx[0].length];
		this.c = c;
		b = new int[hb.length];
		this.a = a;
		copy(this.assignments,assignments);
		this.objective = objective;
		this.optimal = optimal;
		temp_b=new int[b.length];
		temp_x=new int[x.length][x[0].length];
		copy(b, hb);
		copy(x, hx);
	}
	public void random_generator() {
		 Random random = new Random();
	     Set<Integer> intSet = new HashSet<>();
	    while (intSet.size() < assignments.size()/3) {
	        intSet.add(random.nextInt(assignments.size()));
	    }
	    ints = new int[intSet.size()];
	    Iterator<Integer> iter = intSet.iterator();
	    for (int i = 0; iter.hasNext(); ++i) {
	        ints[i] = iter.next();
	    }
	}

	
	public void set_candidate() {
		//Random rn=new Random();
		assigned_costs temp;
		int temp_obj=objective;
		int count=0;
		//implement the iterative process check the assignments if they need to be coppied or not(i don`t think so) 
		//creat_new_list(new_ass);
			random_generator();
			for(int k=0;k<ints.length;k++) {
				change(ints[k]);
			}
		check_Feasi();
		objective=find_objective_value();
	}
	public void change(int k) {//initialize temp_x
		assigned_costs cs;
		int j=assignments.get(k).j_val;
		Random rn=new Random();
		int i=rn.nextInt(x.length);
		//boolean flag=false;
			//System.out.println(b[i]+ " "+ a[i][j]+ " "+ a[assignments.get(k).i_val][j]);
		while(true) {
			if(x[i][j]==0) {
				if(check_Feas(i,assignments.get(k).i_val,j)){
					x[i][j]=1;
					x[assignments.get(k).i_val][j]=0;
					cs=new assigned_costs(c[i][j], i, j);
					assignments.set(k,cs);
					break;
					}
				else i=rn.nextInt(x.length);
			}
			else i=rn.nextInt(x.length);
		}
	}
	public boolean check_Feas(int newe ,int old,int j) {
		int temp_changed=b[old];
		b[old]=b[old]+a[old][j];
		//System.out.println(b[m]);
		//int temp_added=b[n]+a[n][j];
		if(b[newe]-a[newe][j]>=0) {
			b[newe]=b[newe]-a[newe][j];
			//System.err.println("its feasible");
			return true;
		}
		else {
			b[old]=temp_changed;
			return false;
		}
	}
	
	public int to_be_swapped(int j) {
		int store=0;
		int flag=0;
		for(int i=0;i<x.length;i++) {
			if(x[i][j]==1) {
				store=i;
				flag=1;
				break;
			}
		}
		if(flag!=1) System.err.println("arret infeasible");
		return store;
				
	}
	public int j_(int n) {
		return assignments.get(n).j_val;
	}

	public int i_(int n) {
		return assignments.get(n).i_val;
	}
	
	public void copy(ArrayList<assigned_costs>a, ArrayList<assigned_costs>b) {
		assigned_costs temp1;
		for(int i=0;i<b.size();i++) {
			temp1=new assigned_costs(b.get(i).value, b.get(i).i_val,b.get(i).j_val);
			a.add(i,temp1);
		}
	}

	public int find_objective_value() {
		int sum = 0;
		for (int i = 0; i < assignments.size(); i++) {
			sum += assignments.get(i).value;
		}
		return sum;
	}
	public void copy (int[][]x,int y[][]) {
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x[0].length;j++) {
				x[i][j]=y[i][j];
			}
		}
	}
	
	public int value(int n) {
		return assignments.get(n).value;
	}
	public void creat_new_list(ArrayList<assigned_costs> l){
		l=new ArrayList<assigned_costs>();
		assigned_costs as;
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x[0].length;j++) {
				if(x[i][j]==1) {
					as=new assigned_costs(c[i][j], i, j);
					l.add(as);
				}
			}
		}	
	}
	public void check_Feasi() {// this function checks feasibility
		// System.out.println("feaschecking");
		int flag = 0;
		int zer0_flag = 0;
		for (int j = 0; j < x[0].length; j++) {
			for (int i = 0; i < x.length; i++) {
				if (x[i][j] == 1) {
					flag++;
				}
				if (x[i][j] == 0) {
					zer0_flag++;
				}
				/*
				 * if(zer0_flag==4) { System.err.println(i + ""+ j); }
				 */
			}
			if (flag > 1 || zer0_flag == x.length) {
				System.err.println("infeasibleh");
			}
			flag = 0;
			zer0_flag = 0;
		}
		for (int i = 0; i < x.length; i++) {
			if (b[i] < 0) {
				System.err.println("infeasible");
			}
		}
		/*
		 * for (int i = 0; i < x.length; i++) { for (int j = 0; j <x[0].length; j++) {
		 * System.out.print(x[i][j]); } System.out.println(); }
		 */
		int sum = 0;
		for (int i = 0; i < assignments.size(); i++) {
			sum += value(i);
		}
		//System.out.println("the meta objective is: " + " " + sum);
	}

	public void copy(int[] x,int[]y) {
		int temp;
		for(int i=0;i<x.length;i++) {
			temp=y[i];
			x[i]=temp;
	}}

	void merge(ArrayList<assigned_costs> arr, int l, int m, int r) {
		int i, j, k;
		int n1 = m - l + 1;
		int n2 = r - m;

		/* create temp arrays */
		ArrayList<assigned_costs> L = new ArrayList<assigned_costs>();
		ArrayList<assigned_costs> R = new ArrayList<assigned_costs>();

		/* Copy data to temp arrays L[] and R[] */
		for (i = 0; i < n1; i++)
			L.add(i, arr.get(l + i));
		for (j = 0; j < n2; j++)
			R.add(j, arr.get(m + 1 + j));

		/* Merge the temp arrays back into arr[l..r] */
		i = 0; // Initial index of first subarray
		j = 0; // Initial index of second subarray
		k = l; // Initial index of merged subarray
		while (i < n1 && j < n2) {
			if (L.get(i).value <= R.get(j).value) {
				arr.set(k, L.get(i));
				i++;
			} else {
				arr.set(k, R.get(j));
				j++;
			}
			k++;
		}

		/*
		 * Copy the remaining elements of L[], if there are any
		 */
		while (i < n1) {
			arr.set(k, L.get(i));
			i++;
			k++;
		}

		/*
		 * Copy the remaining elements of R[], if there are any
		 */
		while (j < n2) {
			arr.set(k, R.get(j));
			j++;
			k++;
		}
	}

	/*
	 * l is for left index and r is right index of the sub-array of arr to be sorted
	 */
	void mergeSort(ArrayList<assigned_costs> arr, int l, int r) {
		if (l < r) {
			// Same as (l+r)/2, but avoids overflow for
			// large l and h
			int m = l + (r - l) / 2;

			// Sort first and second halves
			mergeSort(arr, l, m);
			mergeSort(arr, m + 1, r);

			merge(arr, l, m, r);
		}
	}
}

	