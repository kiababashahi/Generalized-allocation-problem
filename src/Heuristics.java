import java.util.ArrayList;

public class Heuristics {
	int objective_value=0;
	private int num_agents;
    private int num_resources;
    private int[] b;
    private int [][] c;
    private int [][] a;
    private int[][] a_copy;
    private int[]b_original;
    private int[] allocated;
    int[] min_cl_val=new int[3];
    int[][] smallest;
    int x[][];
    int all=0;
 public Heuristics(ReadData r) { //constructor 
	 num_agents=r.num_agents;
	  num_resources=r.num_resources;
	  b=new int[num_agents];
	  b=r.b;
	  c=new int[num_agents][num_resources];
	  c=r.c;
	  a=new int[num_agents][num_resources];
	  a=r.a;
	  a_copy=new int[num_agents][num_resources];
	  b_original=new int[num_agents];
	  
	  x=new int[num_agents][num_resources]; 
	  a_copy=copy(a);
 	  b_original=copy(b);
 	  allocated=new int[num_resources];
 }
 public void constructive_heuristic() { //constructive  heuristic
	 while(all<num_resources) {
	 smallest=new int[num_resources][3]; 
	 find_min(a_copy);// finds the min of all aijs
	 int[] max_min_value=max_min(); //finds the max of minimums 
	 int mm_i=max_min_value[1];
	 int mm_j=max_min_value[2];
	 if(a_copy[mm_i][mm_j]<b[mm_i]) {//if it was feasible
	 allocated[mm_j]=-1; //allocate it somewhere to show that column j was visited
	 all++;
	 x[mm_i][mm_j]=1;
	 b[mm_i]-=a_copy[mm_i][mm_j];
	 objective_value+=c[mm_i][mm_j];
	 System.out.println("Heuristic value:" + objective_value +" the gap is" +(12681.0/objective_value)*100);
 }
	 else a_copy[mm_i][mm_j]=Integer.MAX_VALUE; // if the answer was infeasible just don`t add it 
	 }
	 check_Feas(); // 
 }
 
 public void find_min(int[][] m) {// finds the minimum of elements in a column of a
	 int min=Integer.MAX_VALUE;
	 int min_i=0;
	 int min_j=0;
	 for(int j=0;j<m[0].length;j++) {
		/* if (j==172) {
			 System.err.println(allocated[j]);
		 }*/
		 if(allocated[j]!=-1) {//checks if the resource was not allocated before 
		  min=Integer.MAX_VALUE;
		  min_i=0;
		  min_j=0;
		 for(int i=0;i<m.length;i++) {
			 if(m[i][j]<min) {
				 min=m[i][j];
				 min_i=i;
				 min_j=j;
			 }
		 }
		smallest[j][0]=min;
		smallest[j][1]=min_i;
		smallest[j][2]=min_j;
	 }}
/*	 for(int i=0;i<smallest.length;i++) {
		 System.out.println(smallest[i][0]);
	 }*/
 }
 public int[] max_min() {
	 int max=Integer.MIN_VALUE;
	 int max_i=0;
	 int max_j=0;
	 for(int i=0;i<smallest.length;i++) {
		if(smallest[i][0]>max) {
			max=smallest[i][0];
			max_i=smallest[i][1];
			max_j=smallest[i][2];
		}
	 } 
	 int[] max_of_min= {max,max_i,max_j};
/*	 System.out.println(max+ " "+ max_i+" " +max_j);
	 System.out.println(a[max_i][max_j]);*/
	 return max_of_min;
 }
 private int[][] copy(int[][] m){
	 int[][] n=new int[m.length][m[0].length];
	 for(int i=0;i<m.length;i++ ) {
		 for(int j=0;j<m[0].length;j++) {
			 n[i][j]=m[i][j];
		 }		
	 }
	 return n;
 }
 private int[]copy(int[]m){
	 int[]n=new int[m.length];
	 for(int i=0;i<m.length;i++) {
		 n[i]=m[i];
	 }
	 return n;
 }
  public void check_Feas() {// this function checks feasibility 
	  System.out.println("feaschecking");
	  int flag=0;
	  int zer0_flag=0;
	  for(int j=0;j<num_resources;j++) {
		  for(int i=0;i<num_agents;i++) {
			  if(x[i][j]==1) {
				  flag++;
			  }
			  if(x[i][j]==0) {
				  zer0_flag++;
			  }
			 /* if(zer0_flag==4) {
				  System.err.println(i + ""+ j);
			  }*/
			 		  }
		  if(flag>1 || zer0_flag==num_agents) {
			  System.err.println("infeasibleh");
		  }
		  flag=0;
		  zer0_flag=0;
	  }
	  for(int i=0;i<num_agents;i++) {
		  if(b[i]<0) {
			  System.err.println("infeasible");
		  }
	  }
	  for(int i=0;i<num_agents;i++) {
		  for(int j=0;j<num_resources;j++) {
			  System.out.print(x[i][j]);
  }
		  System.out.println();}}
}
