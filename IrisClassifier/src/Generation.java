import java.util.ArrayList;
import java.util.Random;

public class Generation {
	public static final float eliteRate=0.1f;
	private ArrayList<Individual> population;
	private static final int generationSize=20;
	private Float generationAptitude;
	
	//sums the total aptitude for every Individual in the population,
	private void calcGenAptitude() {
		this.generationAptitude=0f;
		for(Individual i:population) {
			this.generationAptitude+=i.getAptitude();
		}
	}
	//returns a individual, each individual get a chance proportional to it's aptitude.
	private Individual selectIndividual(Generation gen) throws Exception{
		Random rand=new Random();
		float sum=0;
		//if there's no apt individuals
		if (gen.generationAptitude==0)
			//TODO create own exception.
			throw new Exception();
		float target=rand.nextFloat()*gen.generationAptitude;
		for(Individual i:gen.population){
			if (target<=i.getAptitude()+sum) {
				return i;
			}
			else
				sum+=i.getAptitude();
		}
		//if we got here it's because shit happened, should've returned something in the for loop
		throw new Exception();
	}
	//randomly generates a generation
	public Generation() {
		population=new ArrayList<Individual>();
		for (int i=0;i<generationSize;i++) {
			try {
				population.add(new MLP());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		calcGenAptitude();
	}
	//generates a new generation using crossover from old generation.
	public Generation(Generation old) {
		int i;
		population=new ArrayList<Individual>();
		if (old!=null) {
			old.population.sort(null);
			//Automatically adds old elite do this generation.
			for(i=0;i<generationSize*eliteRate;i++) {
				this.population.add(old.population.get(i));
			}
			//fill the rest of the population with crossovers;
			for(;i<generationSize;i++) {
				try {
					//get 2 parents
					Individual parent1=selectIndividual(old);
					Individual parent2=selectIndividual(old);
					//assure it's not "self love"
					while (parent1==parent2){
						parent2=selectIndividual(old);
					}
					//adds their son to the population.
					this.population.add(parent1.Crossover(parent2));
				} catch (Exception e) {
					//in case of shit happened sets old to null to use 
					//randomly generated generation
					old=null;
					break;
					//e.printStackTrace();
				}
				calcGenAptitude();
			}
		}
		if (old==null) {
			Generation g=new Generation();
			this.population=g.population;
			this.generationAptitude=g.generationAptitude;
		}
	}
	//prints genes and aptitude from elite.
	public void printElite()
	{
		System.out.println("Printing Generation Elite:");
		for (Integer i=0;i<generationSize;i++) {
			System.out.println(i.toString() +":Genes: " + this.population.get(i).getGenes().isEmpty()+ //this.population.get(i).getGenes().get(1).toString()+ this.population.get(i).getGenes().get(2).toString()+ this.population.get(i).getGenes().get(3).toString() +
					" Aptitude:" + this.population.get(i).getAptitude().toString());
		}
	}
	
}
