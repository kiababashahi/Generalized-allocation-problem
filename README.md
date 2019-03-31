The following is one of the first codes I wrote to solve a NP-Complete problem hence I was not that experienced in coding. Here, I have tried to add enough comments and explanations but there is a lot of room for improvement in the code and in the comments but I hope it will be useful for people who want to learn how to use the heuristics and meta-heuristics on such problems. 
# Problem statement 
The Generalized Assignment Problem (GAP) is a NP-Complete problem with a wide application domain. The problem is concerned with allocating a set m indivisible resources R={r<sub>1</sub>,r<sub>2</sub>,r<sub>3</sub>,...,<sub>m</sub>} among a set of n agents A={a<sub>1</sub>,a<sub>2</sub>,...,a<sub>n</sub>} under the following assumptions: 
* Each agent i has a limited budget  t<sub>i</sub> 
* Each agent i has some preference p<sub>ij</sub> towards every resource j which could be viewed as the profit it will gain by obtaining the resource.
* Each agent i has to pay a price to gain resource j which is described via w<sub>ij</sub>.  

The objective is to maximize the overall profit in the system with respect to each agents budget under the constraint that no agent can pay more than what it already has. , i.e., 

![](https://github.com/kiababashahi/Generalized-allocation-problem/blob/master/Untitled.png)

Here the x<sub>ij</sub>=1  if agent a<sub>i</sub> receives resource r<sub>j</sub> and zero otherwise. 

As stated before the following problem is NP-Complete and therefore not polynomial time solvable unless P=NP. One can use approximation algorithms to solve the problem or heuristics. I decided to solve the problem using heuristics and meta heuristics in three steps: 
1. By applying a greedy algorithm to find a feasible solution to the problem. 
2. Improving the solution by applying a Local Search heuristic.
3. Improving the solution obtained from the previous step by applying a multi-start local search meta heuristic.

## In this code
 I have used the following data structures to code the problem:
* The profit of agent i when given resource j, i.e., p<sub>ij</sub> , is described via a[i][j].
* The price or the cost that agent i has to pay to obtain resource j, i.e., w<sub>ij</sub>, is described by c[i][j].
* The budgets of agents the (t<sub>i</sub>)s are being stored in array b.
* The decision variables of the problem are stored in array x[][] for which x[i][j]=1 if agent i has resource j and zero 
otherwise. 
* The decision variables are also refereed to as assignments[][] in the meta Heuristic class.

I used two heuristics to solve the problem coded in classes Heuristics and Nap_const. However in the following wiki I will only describe the second greedy heuristic, i.e., Nap_const because the results obtained from it were much better. 

I already had the value of the optimal resource allocation for our test data-sets and the goal was to see how close I could get to it by applying my algorithms. 
## Heuristic Method 
1.For each resource j find the minimum profit an agent would gain, i.e., a[i][j] as well as the the minimum price they have to pay to get the resource, i.e., c[i][j] by searching through arrays a and c.
```ruby 
min_j(a, min_aj);
min_j(c,min_cj);
```
Which would result in calling 

```ruby
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
``` 
Then assign resource j to agent i for which the value of

![](https://github.com/kiababashahi/Generalized-allocation-problem/blob/master/Heuristic.png)

 is minimum. This was done via the construct_ratios() function: 
```ruby 
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
``` 
As you can see the ratios are being stored in a matrix and after the value of each allocation has been computed and object will be created that holds which resource belongs to which agent and how much would be the value of this allocation (for the initial step it will be initialized to the max value possible because I will use this in the future as you will see to update the minimum ). 

After generating the ratio matrix, I used merge sort to sort the ratios to access them quickly since the goal is to pick the allocation with the minimum ratio. Hence each time I assign a resource to an agent I will not only update the price that the agent has payed and the allocation for the decision variable array but also will remove the ratio from the system. 

bundling everything together will result in the following: 
```ruby
	public void assign() {//uses the heuristic to solve the problem
		min_j(a, min_aj);
		min_j(c,min_cj);
		construct_ratios();
		for(int k=0;k<ratio_holder.size();k++) {
			//is checking the constraints 
			if((ratio_holder.get(k).value!=Integer.MIN_VALUE) && b[ratio_holder.get(k).i_val]-a[ratio_holder.get(k).i_val][ratio_holder.get(k).j_val]>=0) {
				//System.out.println("here");
				//assigns  i to j 
				x[ratio_holder.get(k).i_val][ratio_holder.get(k).j_val]=1;
				//makes sure to update the price i had to pay
				b[ratio_holder.get(k).i_val]-=a[ratio_holder.get(k).i_val][ratio_holder.get(k).j_val];
				remove_jitems(k);
			}
		}
		check_Feas();
		objective_value=calculate_objective();
		final_assignments();
		System.out.println("the objective value is:" + objective_value);
		 
	}

```

In the block of code above the final assignment happens if and only if the resource allocation obtained via the heuristic method is feasible. The feasibility is checked in the following block of code: 
```ruby
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
``` 
The block above checks that each resource is allocated to only one agent 

The block below which is a continuation of the previous block checks that the budget of each agent stays greater or equal to zero.
```ruby 
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


```


Despite providing us with a better solution than the previous one, in terms of the initial solution, this Heuristic also provided a very bad optimality gap.  Hence I have used the local search technique to increase the value of the objective function. 
## Local Search
After obtaining the resource allocation generated by the previous algorithm, I had to find a resource allocation that would result in an increase in the objective value. But how can I reach it? most probably by defining  some moves(functions) that would let two agents swap or exchange their resources multiple times and keep the result  if it leads to and increase in the objective value. But what type of move should be allowed and how can it be defined? 

Lets look at each allocation as a node in a graph, in order to reach one allocation from another, one has to define edges and neighborhoods on the graph and solve the problem with respect to them. I have implemented three Ideas based on three different neighborhoods and at last decided to use the last one. I will provide the reasons and the methods below.
### First Method: Random Neighbourhood First improvement strategy 
At first I decided to define a neighbourhood which worked with a mechanism similar to the simulated annealing
method, therefore I picked two random resources that were assigned from the previous step and swapped them
using a first improvement strategy. I continued this process for half of the assignments or until there was no
improvement or I have reached optimality. Yet, there were two problems with this approach:
1. Sometimes the results I obtained were very bad in terms of improving the objective value acquired from
the previous step and also it even happened that not even the meta heuristic method could do more than
4% of improvement to those solutions which were far from optimality.
2. Since I was using randomness and a first improvement strategy, it happened a lot that two items were
swapped multiple times and therefore I entered a loop and it took the algorithm extra time to exit this
situation. However this was mostly the case with the smaller data sets not the bigger ones.

Swapping is defined as: If job j<sub>1</sub> has been assigned to agent i<sub>1</sub> and job j<sub>2</sub> has been assigned to agent i<sub>2</sub>, check if it would be feasible to allocate job j<sub>1</sub> to agent i<sub>2</sub> and job j<sub>2</sub> to agent i<sub>1</sub>. And is done via the following block of code: 

```ruby 
public void swap(int x, int candiate) {
		int temp_obj;
		int temp_x = b[i(x)] + a[i(x)][j(x)];
		int temp_cand = b[i(candiate)] + a[i(candiate)][j(candiate)]; 
```
these two temps keep the hypothetical changes since I first check if there is an increase and then apply it so I need to update the budget and the allocations updated.
```ruby

		if ((temp_x - a[i(x)][j(candiate)] >= 0) && (temp_cand - a[i(candiate)][j(x)] >= 0)) {
			temp_obj = objective - c[i(x)][j(x)] - c[i(candiate)][j(candiate)];
			if ((temp_obj + c[i(x)][j(candiate)] + c[i(candiate)][j(x)]) < objective) {
				objective = temp_obj + c[i(x)][j(candiate)] + c[i(candiate)][j(x)];
				//System.err.println(objective);
				if (assignments[i(x)][j(x)] != 1) {
					System.err.println("stoppp");
				}
		// an improvement has been spotted the swap the values		
				assignments[i(x)][j(x)] = 0;
				assignments[i(candiate)][j(candiate)] = 0;
				assignments[i(x)][j(candiate)] = 1;
				assignments[i(candiate)][j(x)] = 1;
				b[i(x)] = temp_x - a[i(x)][j(candiate)]; // subtract from the agents budget 
				b[i(candiate)] = temp_cand - a[i(candiate)][j(x)]; //update the agents budget 

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

```
### Best improvement Strategy 

The strategy is: For each of the current assignments check if swapping is feasible, if it is, then check if
there is an improvement in the objective value by a potential exchange in resources between the agents. If so, check if it is better than the best improvement obtained so far, if it was store the indexes of the resources and the agents
that lead to this improvement and also the best improvement amount so far but don‘t swap yet. 

Examine the swap possibilities for all of the agents. After checking the swap moves for all of the agents, then update the
assignment and the objective value, check if there was an improvement from the previous objective value
if there was loop again and continue until there is no improvement anymore. 
### Best swap and shift 
In the previous method our neighborhood was defined as a swap move between the resources allocated between
two agents. My new strategy was to define a bigger neighborhood which could be achieved by applying a
combination of swap moves and some moves called shift moves with the same strategy as before( a best improvement
strategy).

 With shift being defined as allocating the job j which was previously allocated to agent i<sub>1</sub> to
a new agent i<sub>2</sub> if feasible.
```ruby
	public void shift(int oldi,int newi,int j,int index) {
		if(b[newi]-a[newi][j]>0) {
		int temp_objs=objective-(c[oldi][j])+c[newi][j];
		if(objective-temp_objs>0) {
		b[newi]=b[newi]+a[newi][j];
		b[oldi]=b[oldi]-a[oldi][j];
		assignments[newi][j]=1;
		assignments[oldi][j]=0;
		int p1 = c[i(newi)][j];
		int p2 = newi;
		int p3 = j;
		new_cost1 = new assigned_costs(p1, p2, p3);
		costs_Assigned.set(index, new_cost1);
		objective=temp_objs;
		}}}

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
```
When applying both of the methods, the neighborhood would be the union of all of the possible swap moves and shifts moves applicable to the solution in hand. To obtain the best solution, I will select the best improvement
between all possible swaps and shifts in the old assignment and apply it to get the a new assignment then
repeat until there is no improvement in the objective value. By applying this approach, I was able to improve
the objective value more than the methods in the previous sections. And obtained a much better results for
my meta heuristic.
```ruby
public void search() {
		boolean flag = true; // used to check if we could improve or not 
		int delta = 0; //used to check the difference between the objectives 
		int improvement;
		int best_i = 0;
		int best_j = 0; 
		int temp_obj = 0;
		int shift_temp_obj=0;
		
		while(flag==true) {
			int shift_delta=0;
			int best_shift_i=0;
			int best_shift_j=0;
			int best_shift_oldi=0;
			int best_index=0;
			flag=false;
			delta=0;
			best_i=0;
			best_j=0;
			for(int i=0;i<costs_Assigned.size();i++) {
				for(int k=0;k<assignments.length;k++) {
					if(check_shift_feas(k,costs_Assigned.get(i).j_val) && (costs_Assigned.get(i).i_val!=k)) {
						shift_temp_obj=objective-c[i(i)][j(i)];
						if(shift_temp_obj+c[k][j(i)]<objective) {
							if(objective-(shift_temp_obj+c[k][j(i)])<shift_delta){
							best_shift_oldi=i(i);
							best_shift_i=k;
							best_shift_j=j(i);
							shift_delta=objective-(shift_temp_obj+c[k][j(i)]);
							best_index=i;
						}
							}
					}
				}
			}
			int temp_obj2=objective;
			//best swap
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
			if(delta>shift_delta) {
				swap(best_i, best_j);
			}
			else {
				shift(best_shift_oldi,best_shift_i,best_shift_j,best_index);
			}			
			check_Feas();
			if(temp_obj2-objective>0) flag=true;
		}
		
		System.out.println("the obj without met is: " + objective + "and the opt is " + optimal + " "
				+ (find_objective_value() - optimal) * 100 / (find_objective_value() * 1.0));
		
		
	}
```
### Meta-Heuristic: Multi-start local search
To reduce the optimality gap I still had to improve what was obtained from the local search. For that I have used the multi-start local search for which I took the assignment constructed by the constructive greedy Heuristic and applied a random change to it.
This change was done by re-assigning the resources to agents for at least 33% of the resources which could be achieved by
applying the shift procedure to the initial solution. After obtaining the new allocation I am feeding it to the
local search algorithm and looping until I reach the optimality gap of zero or 10<sup>6</sup> or I pass a threshold of
iterations.
The following block of code is used to decide where to make changes in the  assignments. I am accessing the assignments via the array assignment which is the equivalent of x coming from the meta class. 
```ruby
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
		assigned_costs temp;
		int temp_obj=objective;
		int count=0;
		//starts the iterative process of making changes conditioned to feasibility. 
			random_generator();
			for(int k=0;k<ints.length;k++) {
				change(ints[k]);
			}
		check_Feasi();
		objective=find_objective_value();
	}
```
The Change function creates a new assignment by applying a shift to those assignments which were randomly selected in the previous steps  
```ruby
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
```
By applying the heuristics and the meta-heuristics we could improve the solutions obtained by the greedy algorithm and get closer to the global optimum. 
