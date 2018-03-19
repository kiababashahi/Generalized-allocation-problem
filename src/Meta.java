import java.util.ArrayList;
import java.util.Random;

public class Meta {
int[][]x;
int c[][];
int b[];
int a[][];
int candidate;
int objective;
int optimal;
ArrayList<assigned_costs> assignments;
public Meta(int[][] x,int a[][],int[][]c,int[]b,ArrayList<assigned_costs> assignments,int objective,int optimal) {
	this.x=x;
	this.c=c;
	this.b=b;
	this.a=a;
	this.assignments=assignments;
	this.objective=objective;
	this.optimal=optimal;
}public void set_candidate(int candidate) {
	this.candidate=candidate;
}
public void new_assignment() {
	int temp_b=0;
	int temp_obj=objective;
	assigned_costs as;
	for(int i=0;i<c.length;i++) {
		if(temp_obj-(c[i_(candidate)][j_(candidate)]+c[i][j_(candidate)])>=0) {//if the value is better
			temp_b=b[i_(candidate)]+a[i_(candidate)][j_(candidate)];
			if(b[i]-a[i][j_(candidate)]>=0) {////wrong it should b with b
				x[i_(candidate)][j_(candidate)]=0;
				x[i][j_(candidate)]=1;
				b[i_(candidate)]=temp_b;
				b[i]=b[i]-a[i][j_(candidate)];
				//System.out.println(c[i_(candidate)][j_(candidate)]+ " "+ c[i][j_(candidate)] );
				objective-=(c[i_(candidate)][j_(candidate)]+c[i][j_(candidate)]);
				as=new assigned_costs(c[i][j_(candidate)], i, j_(candidate));
				assignments.set(candidate, as);
				temp_obj=objective;		
				}
		//}
	}
	check_Feas();
	Random rn=new Random();
	mergeSort(assignments, 0, assignments.size()-1);
	System.out.println("meta obj is"+ objective);
	Local_Search ls=new Local_Search(a, objective, rn.nextInt(assignments.size()/3), x, c, b, assignments, optimal);
	ls.search();
	assignments=ls.costs_Assigned;
	objective=ls.objective;
	//System.out.println("objective improvement is" + objective);
}}
public int j_(int n) {
	return assignments.get(n).j_val;
}
public int i_(int n) {
	return assignments.get(n).i_val;
}
public int value(int n) {
	return assignments.get(n).value;
}
public void check_Feas() {// this function checks feasibility
	//System.out.println("feaschecking");
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
		if ( flag > 1 || zer0_flag == x.length) {
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
/*	for (int i = 0; i < num_agents; i++) {
		for (int j = 0; j < num_resources; j++) {
			System.out.print(assignments[i][j]);
		}
		System.out.println();
	}*/
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
