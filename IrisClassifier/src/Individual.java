import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Individual implements Comparable<Individual>, Runnable{
	protected static int numGenes=5;
	protected static final int geneMutationChance=10;
	protected static List<Integer> lowerBounds=Arrays.asList(15,15,3,0,0);
	protected static List<Integer> upperBounds=Arrays.asList(100,100,15,15,10);
	protected ArrayList<Integer> genes;
	//since aptitude will be frequently asked, and calculate it may be expensive, i will store
	//it after calculed, it will start with a negative number to sinalize it wasn't calculed yet.
	protected Float aptitude=-1f;
	
	protected void randomizeGenes() {
		genes=new ArrayList<Integer>(numGenes);
		Random rand=new Random();
		for (int i=0;i<numGenes;i++) {
			genes.add(rand.nextInt(upperBounds.get(i)+1-lowerBounds.get(i))+lowerBounds.get(i));
		}
	}
	//returns a randomly generated individual
	/*public Individual() {
		genes=new ArrayList<Integer>(numGenes);
		Random rand=new Random();
		for (int i=0;i<numGenes;i++) {
			genes.add(rand.nextInt(upperBounds.get(i)+1-lowerBounds.get(i))+lowerBounds.get(i));
		}
	}*/
	//returns a new individual mixing genes and applying mutation
	public Individual Crossover (Individual partner) throws Exception {
		Individual son=this.getClass().newInstance();
		Random rand=new Random();
		for (int i=0;i<numGenes;i++) {
			//if it should be mutated, ignores parents and keeps the randomly generated gene.
			if (rand.nextInt(100)<geneMutationChance)
				continue;
			//else get gene from a random parent
			else if(rand.nextInt(100)<50)
				son.genes.set(i,this.genes.get(i));
			else
				son.genes.set(i,partner.genes.get(i));
		}
		return son;
	}
	public int compareTo(Individual other) {
		return other.getAptitude().compareTo(this.getAptitude());
	}
	public abstract Float getAptitude();
	public ArrayList<Integer> getGenes(){
		return this.genes;
	}
}

