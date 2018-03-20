import java.util.ArrayList;
import java.util.Random;

public class Heuristics {
	int objective_value = 0;
	public int num_agents;
	public int num_resources;
	public int[] b;
	public int[][] c;
	public int[][] a;
	public int[][] a_copy;
	public int[] b_original;
	public int[] allocated;
	public int[] min_cl_val = new int[3];
	public int[][] smallest;
	public int x[][];
	public int all = 0;
	public int optimum;
	public ArrayList<assigned_costs> allocated_costs=new ArrayList<assigned_costs>();
	public Heuristics(ReadData r,int optimum) { // constructor
		num_agents = r.num_agents;
		num_resources = r.num_resources;
		b = new int[num_agents];
		b = r.b;
		this.optimum=optimum;
		c = new int[num_agents][num_resources];
		c = r.c;
		a = new int[num_agents][num_resources];
		a = r.a;
		a_copy = new int[num_agents][num_resources];
		b_original = new int[num_agents];

		x = new int[num_agents][num_resources];
		a_copy = copy(a);
		b_original = copy(b);
		allocated = new int[num_resources];
	}

	public void constructive_heuristic() { // constructive heuristic
		while (all < num_resources) {
			smallest = new int[num_resources][3];
			find_min(a_copy);// finds the min of all aijs
			int[] max_min_value = max_min(); // finds the max of minimums
			int mm_i = max_min_value[1];
			int mm_j = max_min_value[2];
			if (a_copy[mm_i][mm_j] < b[mm_i]) {// if it was feasible
				allocated[mm_j] = -1; // allocate it somewhere to show that column j was visited
				all++;
				x[mm_i][mm_j] = 1;
				b[mm_i] -= a_copy[mm_i][mm_j];
				objective_value += c[mm_i][mm_j];
				/*System.out.println(
						"Heuristic value:" + objective_value + " the gap is" + (12681.0 / objective_value) * 100);*/
			} else
				a_copy[mm_i][mm_j] = Integer.MAX_VALUE; // if the answer was infeasible just don`t add it
		}
		//check_Feas(); //
		Sort_assigned_costs();
	/*	int sum=0;
		for(int i=0;i<allocated_costs.size();i++) {
			sum+=allocated_costs.get(i).value;
		}
		System.out.println(sum);*/
		
		//System.out.println(allocated_costs.size()+" "+ allocated_costs.size()/3);
		//Random rn=new Random();
	//	int r1=rn.nextInt(allocated_costs.size()/3);
	//	int r2=rn.nextInt(allocated_costs.size()/3)+allocated_costs.size()/3;
	//	int r3=rn.nextInt(allocated_costs.size()/3)+allocated_costs.size()*2/3;
		///////////////Local_Search l1=new Local_Search(a, objective_value, r1, x, c, b, allocated_costs,optimum);
		//Local_Search l2=new Local_Search(a, objective_value, r2, x, c, b, allocated_costs,optimum);
		//Local_Search l3=new Local_Search(a, objective_value, r3, x, c, b, allocated_costs,optimum);	
		/////////////l1.search();
		//int M_Search=l2.search();
		//int H_Search=l3.search();
		//int best=Math.min(L_Search, M_Search,H_Search);
	/*	for(int i=0;i<allocated_costs.size();i++) {
			System.out.print(allocated_costs.get(i).value + " ");
		}*/
		//System.out.println(r1+ " "+ r2+ " "+ r3);
	//	System.out.println(optimum);
		//System.out.println(L_Search);
		//System.out.println(M_Search);
		//System.out.println(H_Search);
	}

	public void find_min(int[][] m) {// finds the minimum of elements in a column of a
		int min = Integer.MAX_VALUE;
		int min_i = 0;
		int min_j = 0;
		for (int j = 0; j < m[0].length; j++) {
			/*
			 * if (j==172) { System.err.println(allocated[j]); }
			 */
			if (allocated[j] != -1) {// checks if the resource was not allocated before
				min = Integer.MAX_VALUE;
				min_i = 0;
				min_j = 0;
				for (int i = 0; i < m.length; i++) {
					if (m[i][j] < min) {
						min = m[i][j];
						min_i = i;
						min_j = j;
					}
				}
				smallest[j][0] = min;
				smallest[j][1] = min_i;
				smallest[j][2] = min_j;
			}
		}
		/*
		 * for(int i=0;i<smallest.length;i++) { System.out.println(smallest[i][0]); }
		 */
	}

	public int[] max_min() {
		int max = Integer.MIN_VALUE;
		int max_i = 0;
		int max_j = 0;
		for (int i = 0; i < smallest.length; i++) {
			if (smallest[i][0] > max) {
				max = smallest[i][0];
				max_i = smallest[i][1];
				max_j = smallest[i][2];
			}
		}
		int[] max_of_min = { max, max_i, max_j };
		/*
		 * System.out.println(max+ " "+ max_i+" " +max_j);
		 * System.out.println(a[max_i][max_j]);
		 */
		return max_of_min;
	}

	private int[][] copy(int[][] m) {
		int[][] n = new int[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				n[i][j] = m[i][j];
			}
		}
		return n;
	}

	private int[] copy(int[] m) {
		int[] n = new int[m.length];
		for (int i = 0; i < m.length; i++) {
			n[i] = m[i];
		}
		return n;
	}

	public void check_Feas() {// this function checks feasibility
		System.out.println("feaschecking");
		int flag = 0;
		int zer0_flag = 0;
		for (int j = 0; j < num_resources; j++) {
			for (int i = 0; i < num_agents; i++) {
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
			if (flag > 1 || zer0_flag == num_agents) {
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
		for (int i = 0; i < num_agents; i++) {
			for (int j = 0; j < num_resources; j++) {
				System.out.print(x[i][j]);
			}
			System.out.println();
		}
	}

	public void Sort_assigned_costs(){
		assigned_costs as;
		for(int i=0;i<num_agents;i++) {
			for(int j=0;j<num_resources;j++) {
				if(x[i][j]==1) {
					as=new assigned_costs(c[i][j], i, j);
					allocated_costs.add(as);
				}
			}
		}

		mergeSort(allocated_costs, 0, allocated_costs.size()-1);
	}
	void merge(ArrayList<assigned_costs> arr, int l, int m, int r)
	{
	    int i, j, k;
	    int n1 = m - l + 1;
	    int n2 =  r - m;
	 
	    /* create temp arrays */
	    ArrayList<assigned_costs> L=new ArrayList<assigned_costs>();
	    ArrayList<assigned_costs>R=new ArrayList<assigned_costs>();
	 
	    /* Copy data to temp arrays L[] and R[] */
	    for (i = 0; i < n1; i++)
	        L.add(i, arr.get(l+i));
	    for (j = 0; j < n2; j++)
	        R.add(j,arr.get(m + 1+ j));
	 
	    /* Merge the temp arrays back into arr[l..r]*/
	    i = 0; // Initial index of first subarray
	    j = 0; // Initial index of second subarray
	    k = l; // Initial index of merged subarray
	    while (i < n1 && j < n2)
	    {
	        if (L.get(i).value <= R.get(j).value)
	        {
	            arr.set(k,L.get(i));
	            i++;
	        }
	        else
	        {
	        	arr.set(k,R.get(j));
	            j++;
	        }
	        k++;
	    }
	 
	    /* Copy the remaining elements of L[], if there
	       are any */
	    while (i < n1)
	    {
	        arr.set(k,L.get(i));
	        i++;
	        k++;
	    }
	 
	    /* Copy the remaining elements of R[], if there
	       are any */
	    while (j < n2)
	    {
	    	arr.set(k,R.get(j));
	        j++;
	        k++;
	    }
	}
	 
	/* l is for left index and r is right index of the
	   sub-array of arr to be sorted */
	void mergeSort(ArrayList<assigned_costs> arr, int l, int r)
	{
	    if (l < r)
	    {
	        // Same as (l+r)/2, but avoids overflow for
	        // large l and h
	        int m = l+(r-l)/2;
	 
	        // Sort first and second halves
	        mergeSort(arr, l, m);
	        mergeSort(arr, m+1, r);
	 
	        merge(arr, l, m, r);
	    }
	}
}
