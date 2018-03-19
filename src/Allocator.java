import java.util.Random;

public class Allocator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadData rd=new ReadData();
		rd.readFile("e05100.txt");
		/*for(int i=0;i<rd.num_agents;i++) {
			for(int j=0;j<rd.num_resources;j++) {
				System.out.print(rd.c[i][j] +" ");F
			}
			System.out.println();
		}
		System.out.println("*************");
		for(int i=0;i<rd.num_agents;i++) {
			for(int j=0;j<rd.num_resources;j++) {
				System.out.print(rd.a[i][j] +" ");
			}
			System.out.println();
		}
		System.out.println("*****");
		for(int i=0;i<rd.num_agents;i++) {
			System.out.print(rd.b[i]+ " ");
		}*/
		Heuristics h=new Heuristics(rd,12681);
		h.constructive_heuristic();
		Random rn=new Random(123);
		int r1=rn.nextInt(h.allocated_costs.size()/3);
		int r2=rn.nextInt(h.allocated_costs.size()/3)+h.allocated_costs.size()/3;
		int r3=rn.nextInt(h.allocated_costs.size()/3)+h.allocated_costs.size()*2/3;
		Local_Search l1=new Local_Search(h.a, h.objective_value, r1, h.x, h.c, h.b, h.allocated_costs,h.optimum);		
		l1.search();
		//System.out.println(h.optimum);
		//Random ran=new Random();
		int k=0;
		Meta m=new Meta(l1.assignments, l1.a, l1.c, l1.b, l1.costs_Assigned, l1.objective, l1.optimal);
		while(m.objective-m.optimal>10 && k<10) {
		int cn=rn.nextInt(l1.costs_Assigned.size());
		m.set_candidate(cn);
		m.new_assignment();
		k++;
	}
	}
}
