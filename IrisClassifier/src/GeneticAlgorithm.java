
import java.util.Scanner;

public class GeneticAlgorithm {
	public static GenderSource source;
	public static void main(String[] args) {
	
		//get data;
		source = new GenderSource();
		//creates initial generation
		int input=1;
		Scanner in = new Scanner(System.in);
		Integer generationNumber=1;
		Generation current=new Generation();
		Generation next;
		System.out.println("generation " + generationNumber.toString() + " trained");
		current.printElite();
		do{
			System.out.println("type 0 to quit, 1 to new generation, 2 to classify");
			input=in.nextInt();
			if (input!=2)
				break;
			System.out.println("digite o nome que deseja classificar");
			in.reset();
			String nome=in.next();
			try {
				System.out.println("genero: " + ((MLP)current.getBestIndividual()).model.classifyInstance(source.instantiateName(nome)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (input==2);
		while (input==1 || input=='1'){
			next=new Generation(current);
			System.out.println("generation " + generationNumber.toString() + " trained");
			next.printElite();
			System.out.println("type 0 to quit, 1 to new generation");
			input=in.nextInt();
		}
		in.close();
	}

}
