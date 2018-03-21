import weka.core.Instances;
import weka.core.Utils;
import weka.classifiers.*;
import weka.classifiers.functions.MultilayerPerceptron;
import java.util.ArrayList;
import java.util.Random;

public class MLP extends Individual{
	protected static int numGenes=4;
	private String options;
	public MultilayerPerceptron model;
	Instances data;
	
	private void read(GenderSource source) throws Exception {
		
		data = source.getGenderInstances();
		// setting class attribute
		data.setClassIndex(data.numAttributes() - 1);
		//data.randomize();
		
	}
	//recebe um array descrevendo o tamanho das camadas internas, e transforma na string 
	//passada como opção.
	public MLP (ArrayList<Integer> layers) throws Exception
	{
		genes=layers;
		read(GeneticAlgorithm.source);
		//genes=new ArrayList<Integer>();
		setOptions(genes);
		new Thread(this).start();
	}
	private void setOptions(ArrayList<Integer> layers) {
		options=new String("-L "+((float)(layers.get(0))/100) + " -M " +((float)(layers.get(1))/100) +" -H ");
		boolean first=true;
		for (Integer layerSize:layers.subList(2,layers.size())){
			if(!first)
				options+=",";
			options+=layerSize.toString();
			first=false;
		}
		if (options.equals("-H"))
			options="";
		//System.out.println(options);
	}
	//new MLP with Random layers
	public MLP () throws Exception
	{
		genes=new ArrayList<Integer>();
		read(GeneticAlgorithm.source);
		//options=new String("-H ");
		randomizeGenes();
		setOptions(genes);
		new Thread(this).start();
	}
	//mede taxa de acertos total da MLP
	public synchronized Float classify() throws Exception {
		int acertoTotal=0;
		int testeTotal=0;
		for (int i=0;i<10;i++)
		{
			data.randomize(new Random());
			//int acertosCamada=0;
			int j=0;
			Instances trainSet=data.trainCV(10,i);
			Instances testSet=data.testCV(10,i);
			model = new MultilayerPerceptron();
			((MultilayerPerceptron) model).setOptions(Utils.splitOptions(options));
			model.buildClassifier(trainSet);
			
			testeTotal+=testSet.size();
			for (j=0;j<testSet.size();j++)
			{
				if(model.classifyInstance(testSet.get(j))==testSet.get(j).classValue())
				{
					//acertosCamada++;
					acertoTotal++;
				}
			}
			
			//System.out.print("taxa de acerto no conjunto="+acertosCamada*100f/j+"%\n");
				
		}
		notify();
		return acertoTotal*100f/testeTotal;	
	}
	//puts all layers with 0 neurons to the end of the vector
	private void adjustLayers()
	{
		
		for (int i=numGenes;i>2;i--)
		{
			if (this.genes.get(i)==0)
				continue;
			for (int j=numGenes-1;j>=2;j--)
			{
				if (this.genes.get(j)==0)
				{
					this.genes.set(j, this.genes.get(i));
					this.genes.set(i, 0);
				}
				else
					continue;
			}
		}
	}
	@Override
	public Individual Crossover (Individual partner) throws Exception {
		this.adjustLayers();
		((MLP) partner).adjustLayers();
		Individual son=new MLP();
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
	@Override
	public Float getAptitude() {
		// TODO Auto-generated method stub
		if (this.aptitude<0)
		{
			try {
				this.aptitude=this.classify();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return 0f;
		}
		return this.aptitude;
	}
	/*public static void main(String[] args) {
		try {
			classify(read("iris.arff"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	//treina a rede
	@Override
	public void run() {
		try {
			this.aptitude=this.classify();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
