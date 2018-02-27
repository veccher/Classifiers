import java.io.IOException;

public class GeneticAlgorithm {

	public static void main(String[] args) {
		//creates initial generation
		int input=1;
		Integer generationNumber=1;
		Generation current=new Generation();
		Generation next;
		System.out.println("generation " + generationNumber.toString() + " trained");
		current.printElite();
		while (input==1){
			next=new Generation(current);
			System.out.println("generation " + generationNumber.toString() + " trained");
			next.printElite();
			System.out.println("type 0 to quit, 1 to new generation");
			try {
				input=System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
