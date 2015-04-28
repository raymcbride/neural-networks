package com.raymcbride.neural;

import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * The Test class is the main class. It creates, trains, tests and validates MLP, TDNN and
 * RNN networks with the specified parameters
 *
 * @author Ray McBride
 */
public class Test{

    private final static double[] MOMENTUM = {0.0, 0.1, 0.5, 0.9};
	private final static double[] LEARNING_RATE = {0.01, 0.2, 0.8};
	private final static double[] MEMORY_DEPTH = {0.1, 0.5, 0.9};
	private final static int[] DELAYS = {1, 2, 3};
	private final static int[] EPOCHS = {1000, 3000, 5000, 7000, 10000};
	private final static int[] INPUTS = {5, 10, 15};
    private final static int[] HIDDENS = {5, 10, 15};
    private double[] trainingData;
    private double[] testingData;
    private double[] validatingData;
    private Thread mlpThread;
	private Thread tdnnThread;
	private Thread rnnThread;

    /**
	 * This constructor for the <code>Test</code> creates training, testing and validating data sets
	 *
	 * @param trainPath The location of the training data
	 * @param trainField The XML node tag containing the training data
	 * @param testingPath The location of the testing
	 * @param testingField The XML node tag containing the testing data
	 * @param validatingPath The location of the validating data
	 * @param validatingField The XML node tag containing the validating data
	 */
	public Test(String trainPath, String trainField, String testingPath, String testingField, String validatingPath, String validatingField){
		trainingData = getData(trainPath, trainField);
		testingData = getData(testingPath, testingField);
		validatingData = getData(validatingPath, validatingField);
	}

	/**
	 * Gets the data from the <code>Document</code>
	 *
	 * @return a double array containing the data
	 */
	public double[] getData(String path, String field){
		XMLParser parser = new XMLParser(path);
		Document xmlDoc = parser.getDocument();
		NodeList elementNodes = xmlDoc.getElementsByTagName(field);
		ArrayList arrayList = new ArrayList();
		for (int i = 0; i < elementNodes.getLength(); i++) {
			NodeList textNodes = elementNodes.item(i).getChildNodes();
			for (int j = 0; j < textNodes.getLength(); j++) {
				Node node = textNodes.item(j);
				arrayList.add(node.getNodeValue());
			}
		}
		double[] data = new double[arrayList.size()];
		for(int i = 0; i < arrayList.size(); i++)
			data[i] = Double.parseDouble((String)arrayList.get(i));
		DataProcessor dataProcessor = new DataProcessor(data);
        data = dataProcessor.scale();
        return data;
	}

	/**
	 * Trains, tests and validates the MLP networks
	 */
	public void testMLP(){
		mlpThread = new Thread(){
			public void run(){
				for(int i = 0; i < INPUTS.length; i++){
					for(int j = 0; j < HIDDENS.length; j++){
						for(int k = 0; k < LEARNING_RATE.length; k++){
							for(int m = 0; m < MOMENTUM.length; m++){
								for(int n = 0; n < EPOCHS.length; n++){
									MLP mlp = new MLP(INPUTS[i], HIDDENS[j], 1, LEARNING_RATE[k], MOMENTUM[m], EPOCHS[n], "MLP_" + i + "_" + j + "_" + k + "_" + m + "_" + n + "_");
									mlp.train(trainingData);
									mlp.test(testingData);
									mlp.validate(validatingData);
								}
							}
						}
					}
	   			}
			}
		};
		mlpThread.start();
	}

	/**
	 * Trains, tests and validates the TDNN networks
	 */
	public void testTDNN(){
		tdnnThread = new Thread(){
			public void run(){
				for(int i = 0; i < INPUTS.length; i++){
					for(int j = 0; j < HIDDENS.length; j++){
						for(int k = 0; k < DELAYS.length; k++){
							for(int m = 0; m < LEARNING_RATE.length; m++){
								for(int n = 0; n < MOMENTUM.length; n++){
									for(int p = 0; p < EPOCHS.length; p++){
										TDNN tdnn = new TDNN(INPUTS[i], HIDDENS[j], DELAYS[k], 1, LEARNING_RATE[m], MOMENTUM[n], EPOCHS[p], "TDNN_" + i + "_" + j + "_" + k + "_" + m + "_" + n + "_" + p + "_");
										tdnn.train(trainingData);
										tdnn.test(testingData);
										tdnn.validate(validatingData);
						   			}
						   		}
							}
						}
					}
	   			}
			}
		};
		tdnnThread.start();
	}

	/**
	 * Trains, tests and validates the RNN networks
	 */
	public void testRNN(){
		rnnThread = new Thread(){
			public void run(){
				for(int i = 0; i < INPUTS.length; i++){
					for(int j = 0; j < HIDDENS.length; j++){
						for(int k = 0; k < MEMORY_DEPTH.length; k++){
							for(int m = 0; m < LEARNING_RATE.length; m++){
								for(int n = 0; n < MOMENTUM.length; n++){
									for(int p = 0; p < EPOCHS.length; p++){
										RNN rnn = new RNN(INPUTS[i], HIDDENS[j], MEMORY_DEPTH[k], 1, LEARNING_RATE[m], MOMENTUM[n], EPOCHS[p], "RNN_" + i + "_" + j + "_" + k + "_" + m + "_" + n + "_" + p + "_");
										rnn.train(trainingData);
										rnn.test(testingData);
										rnn.validate(validatingData);
						   			}
								}
						   	}
						}
					}
				}
			}
		};
		rnnThread.start();
	}

	public static void main(String[] args){
		Test test = new Test("data/Train500.xml", "indexValue", "data/Test100.xml", "indexValue", "data/Validate100.xml", "indexValue");
		test.testMLP();
		test.testTDNN();
		test.testRNN();
	}
}