import weka.core.Instances;
import weka.core.Utils;
import weka.classifiers.*;
import weka.filters.supervised.instance.*;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.KStar;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.RandomTree;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MLP extends Individual{
	protected static int numGenes=4;
	private String options;
	Instances data;
	
	private void read(String dataPath) throws Exception {
		DataSource source = new DataSource(dataPath);
		data = source.getDataSet();
		// setting class attribute
		data.setClassIndex(data.numAttributes() - 1);
		//data.randomize();
		
	}
	//recebe um array descrevendo o tamanho das camadas internas, e transforma na string 
	//passada como opção.
	public MLP (ArrayList<Integer> layers) throws Exception
	{
		genes=layers;
		read("iris.arff");
		//genes=new ArrayList<Integer>();
		setOptions(genes);
	}
	private void setOptions(ArrayList<Integer> layers) {
		options=new String("-H ");
		boolean first=true;
		for (Integer layerSize:layers){
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
		read("iris.arff");
		//options=new String("-H ");
		randomizeGenes();
		setOptions(genes);
	}
	//mede taxa de acertos total da MLP
	public Float classify() throws Exception {
		Classifier model=null;
		int acertoTotal=0;
		int testeTotal=0;
		for (int i=0;i<10;i++)
		{
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
		return acertoTotal*100f/testeTotal;	
	}
	@Override
	public Float getAptitude() {
		// TODO Auto-generated method stub
		if (this.aptitude<0)
		{
			try {
				return this.classify();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0f;
		}
		else
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
}
