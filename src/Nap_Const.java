import java.util.ArrayList;

public class Nap_Const {
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
	public double[][]ratio_matrix;
	int[] min_cj;
	int[] min_aj;
	public int all = 0;
	public int optimum;
	public ArrayList<assigned_costs> allocated_costs=new ArrayList<assigned_costs>();
	ArrayList<assigned_costs> ratio_holder=new ArrayList<assigned_costs>();
	public Nap_Const(ReadData r,int optimum) {
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
		//b_original = new int[num_agents];

		x = new int[num_agents][num_resources];
		//a_copy = copy(a);
		//b_original = copy(b);
		allocated = new int[num_resources];
		min_cj=new int[c[0].length];
		min_aj=new int[a[0].length];
		ratio_matrix=new double[c.length][c[0].length];
	}
	
	public void assign() {
	//	for(int i=0;i<b.length;i++) System.out.print (b[i]+ " ");
		min_j(a, min_aj);
		min_j(c,min_cj);
		construct_ratios();
		for(int k=0;k<ratio_holder.size();k++) {
			if((ratio_holder.get(k).value!=Integer.MIN_VALUE) && b[ratio_holder.get(k).i_val]-a[ratio_holder.get(k).i_val][ratio_holder.get(k).j_val]>=0) {
				//System.out.println("here");
				x[ratio_holder.get(k).i_val][ratio_holder.get(k).j_val]=1;
				b[ratio_holder.get(k).i_val]-=a[ratio_holder.get(k).i_val][ratio_holder.get(k).j_val];
				remove_jitems(k);
			}
		}
		check_Feas();
		objective_value=calculate_objective();
		final_assignments();
		System.out.println("the objective value is:" + objective_value);
		/*for(int i=0;i<allocated_costs.size();i++) {
			System.out.println(allocated_costs.get(i).value);
		}*/
	}
	
	public void final_assignments() {
		assigned_costs as;
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x[0].length;j++) {
				if(x[i][j]==1) {
					//System.out.println("here");
					as=new assigned_costs(c[i][j], i, j);
					allocated_costs.add(as);
				}
			}
		}

	}
	
	
	
	
	public void check_Feas() {// this function checks feasibility
		//System.out.println("feaschecking");
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
	/*	for (int i = 0; i < num_agents; i++) {
			for (int j = 0; j < num_resources; j++) {
				System.out.print(x[i][j]);
			}
			System.out.println();
		}*/
	}

	
	
	
	
	
	
	
	
	
	
	
	public void remove_jitems(int k) {
		for(int i=0;i<ratio_holder.size();i++) {
			if(ratio_holder.get(i).j_val==ratio_holder.get(k).j_val && (i!=k) ){
				ratio_holder.get(i).value=Integer.MIN_VALUE;
			}
		}
	}
	
	
	
	
	
	public void min_j(int[][]y,int[]z) {//finds the minimum of each column
		int min=Integer.MAX_VALUE;
		for(int j=0;j<y[0].length;j++) {
			for(int i=0;i<y.length;i++) {
				if(y[i][j]<min) {
					min=y[i][j];
				}
			}
			z[j]=min;
		}
	}
	
	public void construct_ratios() {
		assigned_costs as;
		for(int j=0;j<c[0].length;j++) {
			for(int i=0;i<c.length;i++) {
				ratio_matrix[i][j]=(a[i][j]-min_aj[j]*1.0)/((c[i][j]-min_cj[j])+1);
				as=new assigned_costs(Integer.MAX_VALUE,i, j);
				as.set_ratios(ratio_matrix[i][j]);
				ratio_holder.add(as);
			}
		}
		mergeSort(ratio_holder, 0, ratio_holder.size()-1);
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
	        if (L.get(i).ratios <= R.get(j).ratios)
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
	
	public int calculate_objective() {
		int sum=0;
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x[0].length;j++) {
				if(x[i][j]==1) {
					sum+=c[i][j];
				}
			}
		}
		return sum;
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

}
