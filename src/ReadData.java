import java.io.InputStream;
import java.util.Scanner;

public class ReadData {

	public int num_agents;
	public int num_resources;
	public int[] b;
	public int[][] c;
	public int[][] a;

	public void readFile(String fileName) {
		try {
			InputStream in = getClass().getResourceAsStream("/" + fileName);
			Scanner scan = new Scanner(in);

			num_agents = scan.nextInt();
			num_resources = scan.nextInt();
			// init
			b = new int[num_agents];
			c = new int[num_agents][num_resources];
			a = new int[num_agents][num_resources];
			/*
			 * for (int i = 0; i < num_agents; i++) { facilityCapacity[i] = scan.nextInt();
			 * facilityCost[i] = scan.nextDouble(); }
			 */

			for (int i = 0; i < num_agents; i++) {
				for (int j = 0; j < num_resources; j++) {
					c[i][j] = scan.nextInt();
				}
			}
			for (int i = 0; i < num_agents; i++) {
				for (int j = 0; j < num_resources; j++) {
					a[i][j] = scan.nextInt();
				}
			}
			for (int i = 0; i < num_agents; i++) {
				b[i] = scan.nextInt();
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

}
