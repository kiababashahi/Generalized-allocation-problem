import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.print.attribute.Size2DSyntax;

public class Local_Search {
	public int[][] assignments;
	public int[][] c;
	public int[][] a;
	public int[] b;
	public int objective;
	public int num_resources;
	public int num_agents;
	public ArrayList<assigned_costs> costs_Assigned;
	public int optimal;
	public assigned_costs new_cost1;
	public assigned_costs new_cost2;

	public Local_Search(int[][] a, int objective, int[][] x, int[][] c, int[] b,
			ArrayList<assigned_costs> costs_Assigned, int opt) {
		assignments=new int[x.length][x[0].length];
		copy(assignments,x);
		this.a = a;
		num_agents = a.length;
		num_resources = a[0].length;
		this.c = c;
		this.b=new int[b.length];
		copy(this.b, b);
		this.costs_Assigned =new ArrayList<assigned_costs>();
		copy(this.costs_Assigned, costs_Assigned);
		this.objective = objective;
		optimal = opt;
	}

	public void search() {
		boolean flag = true;
		int delta = 0;
		int improvement;
		int best_i = 0;
		int best_j = 0;// error khiz
		int temp_obj = 0;
		while (flag == true) {
			delta=0;
			best_i=0;
			best_j=0;
			flag = false;
			for (int i = 0; i < costs_Assigned.size(); i++) {
				for (int j = i + 1; j < costs_Assigned.size(); j++) {
					if (check_swap_feas(i, j)) {
						temp_obj = objective - c[i(i)][j(i)] - c[i(j)][j(j)];
						if ((temp_obj + c[i(i)][j(j)] + c[i(j)][j(i)]) < objective) {
							//System.out.println("here");
							if (objective - (temp_obj + c[i(i)][j(j)] + c[i(j)][j(i)]) > delta) {
								//System.out.println("here");
								delta = objective - (temp_obj + c[i(i)][j(j)] + c[i(j)][j(i)]);
								best_i = i;
								best_j = j;
							}
						}
					}
				}
			}
	
				int temp_obj1=objective;
				swap(best_i, best_j);
			
				check_Feas();
				if(temp_obj1-objective>0) flag=true;
		
		}
		System.out.println("the obj without met is: " + objective + "and the opt is " + optimal + " "
				+ (find_objective_value() - optimal) * 100 / (find_objective_value() * 1.0));
		
		
	}

	public boolean check_swap_feas(int x, int candiate) {
		int temp_obj;
		int temp_x = b[i(x)] + a[i(x)][j(x)];
		int temp_cand = b[i(candiate)] + a[i(candiate)][j(candiate)];
		if ((temp_x - a[i(x)][j(candiate)] >= 0) && (temp_cand - a[i(candiate)][j(x)] >= 0)) {
			return true;
		} else {
			return false;
		}
	}

	public int find_objective_value() {
		int sum = 0;
		for (int i = 0; i < costs_Assigned.size(); i++) {
			sum += costs_Assigned.get(i).value;
		}
		return sum;
	}

	public void swap(int x, int candiate) {
		int temp_obj;
		int temp_x = b[i(x)] + a[i(x)][j(x)];
		int temp_cand = b[i(candiate)] + a[i(candiate)][j(candiate)];
		if ((temp_x - a[i(x)][j(candiate)] >= 0) && (temp_cand - a[i(candiate)][j(x)] >= 0)) {
			temp_obj = objective - c[i(x)][j(x)] - c[i(candiate)][j(candiate)];
			if ((temp_obj + c[i(x)][j(candiate)] + c[i(candiate)][j(x)]) < objective) {
				objective = temp_obj + c[i(x)][j(candiate)] + c[i(candiate)][j(x)];
				//System.err.println(objective);
				if (assignments[i(x)][j(x)] != 1) {
					System.err.println("stoppp");
				}
				
				assignments[i(x)][j(x)] = 0;
				assignments[i(candiate)][j(candiate)] = 0;
				assignments[i(x)][j(candiate)] = 1;
				assignments[i(candiate)][j(x)] = 1;
				b[i(x)] = temp_x - a[i(x)][j(candiate)];
				b[i(candiate)] = temp_cand - a[i(candiate)][j(x)];

				int p1 = c[i(x)][j(candiate)];
				int p2 = i(x);
				int p3 = j(candiate);

				new_cost1 = new assigned_costs(p1, p2, p3);
				int q1 = c[i(candiate)][j(x)];
				int q2 = i(candiate);
				int q3 = j(x);
				new_cost2 = new assigned_costs(q1, q2, q3);

				costs_Assigned.set(x, new_cost1);
				costs_Assigned.set(candiate, new_cost2);
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
		// System.out.println("feaschecking");
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
			if (flag > 1 || zer0_flag == num_agents) {
				System.err.println("infeasiblehlocal");
			}
			flag = 0;
			zer0_flag = 0;
		}
		for (int i = 0; i < num_agents; i++) {
			if (b[i] < 0) {
				System.err.println("infeasible");
			}
		}
		/*
		 * for (int i = 0; i < num_agents; i++) { for (int j = 0; j < num_resources;
		 * j++) { System.out.print(assignments[i][j]); } System.out.println(); }
		 */
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
	public void copy (int[][]x,int y[][]) {
		int temp=0;
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x[0].length;j++) {
				temp=y[i][j];
				x[i][j]=temp;
			}
		}
	}
	public void copy(int[] x,int[]y) {
		int temp;
		for(int i=0;i<x.length;i++) {
			temp=y[i];
			x[i]=temp;
	}}
	public void copy(ArrayList<assigned_costs>a, ArrayList<assigned_costs>b) {
		assigned_costs temp1;
		//if(a.size()!=b.size()) System.err.println("dangeeerrrrrrrrrrrrrrrrrr");
		for(int i=0;i<b.size();i++) {
			temp1=new assigned_costs(b.get(i).value, b.get(i).i_val,b.get(i).j_val);
			a.add(i,temp1);
		}
	}
}
