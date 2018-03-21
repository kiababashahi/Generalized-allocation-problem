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
	ArrayList<assigned_costs> cloned;
	ArrayList<assigned_costs> assignments;
	int[] ints;
	public Meta(int[][] x, int a[][], int[][] c, int[] b, ArrayList<assigned_costs> assignments, int objective,
			int optimal) {
		this.x = x;
		this.c = c;
		this.b = b;
		this.a = a;
		this.assignments = assignments;
		this.objective = objective;
		this.optimal = optimal;
		temp_b=new int[b.length];
		temp_x=new int[x.length][x[0].length];
	}
	public void random_generator() {
		 Random random = new Random();
	     Set<Integer> intSet = new HashSet<>();
	    while (intSet.size() < assignments.size()/10) {
	        intSet.add(random.nextInt(assignments.size()));
	    }
	    ints = new int[intSet.size()];
	    Iterator<Integer> iter = intSet.iterator();
	    for (int i = 0; iter.hasNext(); ++i) {
	        ints[i] = iter.next();
	    }
	}
	public assigned_costs set_candidate() {
		Random rn=new Random();
		assigned_costs temp;
		//int i=rn.nextInt(x.length);
		//int j=rn.nextInt(x[0].length);
		for(int k=0;k<ints.length;k++) {
			change(i)
		}
		while(x[i][j]!=0) {
			i=rn.nextInt(x.length);
			j=rn.nextInt(x[0].length);
		}
		temp=new assigned_costs(c[i][j], i, j);
		return temp;
	}
	public void change(int k) {//initialize temp_x
		int j=assignments.get(k).j_val;
		for(int i=0;i<x.length;i++) {
			if(temp_x[i][j]==0) {
				if(check_Feas(i,assignments.get(k).i_val),j) {
					temp_x[i][j]=1;
					temp_x[assignments.get(k).i_val][j]=0;
					changeb()
				}
			}
		}
	}
	public boolean check_Feas(int n,int m,int j) {
		int temp_b=b[n]+
	}
	public void new_assignment() {
		assigned_costs swapper=set_candidate();
		int new_i=swapper.i_val;
		int new_j=swapper.j_val;
		int old;
		int k=0;
		while(objective-optimal>0.01 && k<10000) {
			//System.out.println(x[new_i][new_j]);
		
			//System.out.println("here");
			copy(saved_b, b);
			old=to_be_swapped(new_j);
			b[old]+=a[old][new_j];
			b[new_i]=b[new_i]-a[new_i][new_j];
			if(b[new_i]-a[new_i][new_j]>=0) {
			copy(saved_x, x);
			x[new_i][new_j]=1;
			x[old][new_j]=0;
			ArrayList<assigned_costs> new_costs=new ArrayList<assigned_costs>();
			Sort_assigned_costs(new_costs);
			Random r=new Random();
			int can=r.nextInt(new_costs.size()/3);
			check_Feas();
			Local_Search ls=new Local_Search(a, objective, x, c, b, new_costs, optimal);
			ls.search();
			if(ls.objective<=objective) {
				//System.out.println("here");
				objective=ls.objective;
				//copythenew ass
				copy(assignments, ls.costs_Assigned);
			}
			else {
				copy(x, saved_x);
				copy(b, saved_b);
			}
		}	
		k++;
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
	public void Sort_assigned_costs(ArrayList< assigned_costs>new_costs){

		assigned_costs as;
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x[0].length;j++) {
				if(x[i][j]==1) {
					as=new assigned_costs(c[i][j], i, j);
					new_costs.add(as);
				}
			}
		}

		mergeSort(new_costs, 0, new_costs.size()-1);
	}
	
	
	public void copy(ArrayList<assigned_costs>a, ArrayList<assigned_costs>b) {
		assigned_costs temp1;
		//if(a.size()!=b.size()) System.err.println("dangeeerrrrrrrrrrrrrrrrrr");
		for(int i=0;i<a.size();i++) {
			temp1=new assigned_costs(b.get(i).value, b.get(i).i_val,b.get(i).j_val);
			a.set(i,temp1);
		}
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

	public void check_Feas() {// this function checks feasibility
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
		for(int i=0;i<x.length;i++)
			x[i]=y[i];
	}

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
	