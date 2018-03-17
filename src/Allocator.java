
public class Allocator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadData rd=new ReadData();
		rd.readFile("e05100.txt");
		/*for(int i=0;i<rd.num_agents;i++) {
			for(int j=0;j<rd.num_resources;j++) {
				System.out.print(rd.c[i][j] +" ");
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
		Heuristics h=new Heuristics(rd);
		h.constructive_heuristic();
	}
}
