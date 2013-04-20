package org.ck.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.ck.anomalifinder.Cusum_VmaskApproch;
import org.ck.forecaster.nn.NeuralNetwork;
import org.ck.gui.Constants.DatasetOptions;
import org.ck.sample.DataHolder;
import org.ck.sample.Sample;
import org.ck.similarity.CommonSequenceFinder;
import org.ck.similarity.CommonSequenceFinder.Tuple;
import org.ck.similarity.DynamicTimeWarper;


public class MainClass 
{
	public static void main(String args[])
	{	
		DataHolder.setDataset(DatasetOptions.ECG_DATASET);
		//Sample seaSample = new Sample(DataHolder.TRAINING_FILE_NAME,DataHolder.SAMPLE_NAME);	
		
		//use your respective methods for testing
		satvik();
		//vaishakh();
		//samir();
		
		
	}
	
	private static void samir()
	{
		DataHolder.setDataset(DatasetOptions.SEA_LEVEL_DATASET);
		Sample seaSample = new Sample(DataHolder.TRAINING_FILE_NAME,DataHolder.SAMPLE_NAME);		
		
		DataHolder.setDataset(DatasetOptions.FINANCE_VIX_DATASET);
		Sample sample2 = new Sample(DataHolder.TRAINING_FILE_NAME,DataHolder.SAMPLE_NAME).getSeriesSubset(0, 1400);
		
		System.out.println(seaSample.getSaxString());
		System.out.println(sample2.getSaxString());
		System.out.println(seaSample.getDistanceUsingSAX(sample2));
	}

	private static void vaishakh()
	{
		DataHolder.setDataset(DatasetOptions.ECG_DATASET);
		Sample seaSample = new Sample(DataHolder.TRAINING_FILE_NAME,DataHolder.SAMPLE_NAME);	
		//System.out.println(getSortedSimilarSeries(seaSample));
		//testLineGraphDrawer(seaSample);
		//testCommonSequenceFinder(seaSample);
		//System.out.println(new MovingGeometricForecaster(DataHolder.TRAINING_FILE_NAME,DataHolder.SAMPLE_NAME).geometricMean());
				
		
	}

	private static void satvik()
	{
		DataHolder.setDataset(DatasetOptions.FINANCE_NIFTY_DATASET);
		Sample seaSample = new Sample(DataHolder.TRAINING_FILE_NAME,DataHolder.SAMPLE_NAME);	
		//testNeuralNetwork(seaSample);
		testCusum(seaSample);
		
		
	}

	private static void testCusum(Sample seaSample) {
		Cusum_VmaskApproch finder = new Cusum_VmaskApproch(seaSample);
		finder.setHval(0.5);
		finder.setSampleSize(15);
		finder.computeCusumSereis();
		List<Integer> defectiveList = finder.getDefectiveDataPoints();
		System.out.println(defectiveList);
		String output = "[";
		int i,j;
		for(i=0,j=0;i<seaSample.getNumOfValues();++i)
		{
			
			int k = -1;
			if(j<defectiveList.size())
				k = defectiveList.get(j);
			
			if(k==i)
			{
				output += "[ new Date("+i+"+100),0,"+seaSample.getValue(i)+"],";
				System.out.println("k==i"+i);
				++j;
			}
			else
				output += "[ new Date("+i+"+100),"+seaSample.getValue(i)+", 0],";
		}
		output += "[0,0,0]]";
		System.out.println(output);
	}

	private static void testNeuralNetwork(Sample seaSample) {
		NeuralNetwork nn = new NeuralNetwork(new int[]{10,15,15,1}, seaSample);
		nn.train();
		System.out.println("Actual Value : "+seaSample.getValue(11)+"\t Predicted Value : "+
		nn.predict((ArrayList<Double>) seaSample.getSeriesSubset(10, 20).getNormalizedTimeSeries()));
	}

	private static void testCommonSequenceFinder(Sample seaSample) {
		CommonSequenceFinder csFinder = new CommonSequenceFinder(seaSample);
		csFinder.findPatterns();
		List<Tuple> similarPatterns = csFinder.getSimilarPatternList();
		for(Tuple t : similarPatterns)
		{
			System.out.print(t.getValue(0)+"\t"+t.getValue(1)+"\t");
			seaSample.display(t.getValue(0), t.getValue(1));
		}
	}

	public static String testLineGraphDrawer(Sample sample)
	{
		GraphDrawer lgd = new GraphDrawer(sample);
		String exampleLink = lgd.example1();
		System.out.println(exampleLink);
		System.out.println("Open this link in a brower!");
		return exampleLink;
	}

	public static String getSortedSimilarSeries(Sample seaSample)
	{
		DynamicTimeWarper dtw = new DynamicTimeWarper(seaSample.getSeriesSubset(0, 12));	
		
		Map<Double, Integer> similarityMap = new TreeMap<Double, Integer>();
		for(int i=0; i<seaSample.getNumOfValues(); i+=12)
			similarityMap.put(dtw.getDistanceFrom(seaSample.getSeriesSubset(i, i + 12)), i / 12);
		
		String output = "";
		output += "Subset\tDTW Distance\n";
		for(Double i : similarityMap.keySet())
			output += similarityMap.get(i) + "\t" + i + "\n";
		
		return output;
	}
}
