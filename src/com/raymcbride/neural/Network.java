package com.raymcbride.neural;

import java.text.*;
import java.util.*;
import java.io.*;

/**
 * The Network class is the abstract base class for all networks
 *
 * @see MLP
 * @see TDNN
 * @see RNN
 *
 * @author Ray McBride
 */
abstract class Network{

    private Neuron[] inputNeurons;
    private Neuron[] hiddenNeurons;
    private Neuron outputNeuron;
    private double learningRate;
    private double momentum;
    private int epoch;
    private int slope;
    private double targetOutput;
    private double outputErrorTerm;
    private double[] hiddenErrorTerm;
    private double[] inputData;
    private BiasNeuron biasHidden;
    private BiasNeuron biasOutput;
    private Synapse[][] inputToHidden;
    private Synapse[] hiddenToOutput;
    private Synapse[] biasToHidden;
    private Synapse biasToOutput;
    private int nextInput;
    private double totalNetworkError;
    private OutputFile detailFile;
    private int totalEpochs;
    private String fileID;

    /**
	 * This constructor for the <code>MLP</code> specifies the number of Input Neurons and Hidden Neurons
	 * the slope of their activation functions, the learning rate, the momentum, the number of training
	 * epochs and set a network topology id for use with the log file.
	 *
	 * @param inputs The number of Input Neurons
	 * @param hiddens The number of Hidden Neurons
	 * @param slope The slope of their activation functions
	 * @param learningRate The learning rate
	 * @param momentum The momentum
	 * @param totalEpochs The number of training epochs
	 * @param fileID The network topology id
	 */
	public Network(int inputs, int hiddens, int slope, double learningRate, double momentum, int totalEpochs, String fileID){
        inputNeurons = new Neuron[inputs];
		hiddenNeurons = new Neuron[hiddens];
		inputToHidden = new Synapse[inputs][hiddens];
		hiddenToOutput = new Synapse[hiddens];
		hiddenErrorTerm = new double[hiddens];
		biasToHidden = new Synapse[hiddens];
		this.slope = slope;
		this.learningRate = learningRate;
		this.momentum = momentum;
        nextInput = 0;
        totalNetworkError = 0;
        this.totalEpochs = totalEpochs;
        this.fileID = fileID;
    }

    /**
     * Creates the Neurons
     */
    protected void createNeurons(){
		for(int i = 0; i < inputNeurons.length; i++)
			inputNeurons[i] = new InputNeuron(slope);
		for(int i = 0; i < hiddenNeurons.length; i++)
			hiddenNeurons[i] = new HiddenNeuron(slope, inputNeurons.length);
		outputNeuron = new OutputNeuron(slope, hiddenNeurons.length);
		biasHidden = new BiasNeuron();
		biasOutput = new BiasNeuron();
    }

	/**
     * Connects the Neurons
     */
    protected void connectNeurons(){
		for(int i = 0; i < inputNeurons.length; i++){
			for(int j = 0; j < hiddenNeurons.length; j++){
				inputToHidden[i][j] = new Synapse(inputNeurons[i], hiddenNeurons[j]);
			}
		}
		for(int i = 0; i < hiddenNeurons.length; i++){
			hiddenToOutput[i] = new Synapse(hiddenNeurons[i], outputNeuron);
			biasToHidden[i] = new Synapse(biasHidden, hiddenNeurons[i]);
		}
		biasToOutput = new Synapse(biasOutput, outputNeuron);
	}

	/**
     * Abstract method to initialise the network
     */
    protected abstract void initialise();

	/**
     * Calculates the output error term
     */
    protected void calculateOutputError(){
		outputErrorTerm = slope*outputNeuron.getOutputValue()*(1-outputNeuron.getOutputValue())*(targetOutput - outputNeuron.getOutputValue());
	}

	/**
     * Calculates the hidden error term
     */
    protected void calculateHiddenError(){
		for(int i = 0; i < hiddenNeurons.length; i++){
			hiddenErrorTerm[i] = slope * hiddenNeurons[i].getOutputValue() * (1 - hiddenNeurons[i].getOutputValue()) * outputErrorTerm * hiddenToOutput[i].getWeight();
		}
	}

	/**
     * Calculates the weight change to be made to the Synapses between the Output and Hidden Neurons
     * and Bias Neurons
     */
    protected void calculateOutputWeightChange(){
		for (int i = 0; i < hiddenToOutput.length; i++){
			hiddenToOutput[i].calculateWeightChange(learningRate, momentum, outputErrorTerm);
		}
		biasToOutput.calculateWeightChange(learningRate, momentum, outputErrorTerm);
	}

	/**
     * Adjusts the weights of the Synapses between the Output and Hidden Neurons and Bias Neurons
     */
    protected void adjustOutputWeights(){
	    for (int i = 0; i < hiddenToOutput.length; i++){
	    	hiddenToOutput[i].adjustWeight();
		}
	    biasToOutput.adjustWeight();
	}

	/**
     * Calculates the weight change to be made to the Synapses between the Hidden and Input Neurons
     * and Bias Neurons
     */
    protected void calculateHiddenWeightChange(){
		for (int i = 0; i < inputToHidden.length; i++){
		    for(int j = 0; j < inputToHidden[i].length; j++){
		        inputToHidden[i][j].calculateWeightChange(learningRate, momentum, hiddenErrorTerm[j]);
			}
		}
		for (int i = 0; i < biasToHidden.length; i++){
		biasToHidden[i].calculateWeightChange(learningRate, momentum, hiddenErrorTerm[i]);
		}
	}

	/**
     * Adjusts the weights of the Synapses between the Hidden and Input Neurons and Bias Neurons
     */
    protected void adjustHiddenWeights(){
		for (int i = 0; i < inputToHidden.length; i++){
			for(int j = 0; j < inputToHidden[i].length; j++){
				inputToHidden[i][j].adjustWeight();
			}
		}
		for (int i = 0; i < biasToHidden.length; i++){
			biasToHidden[i].adjustWeight();
		}
	}

	/**
     * Transfers the weighted values to the HiddenNeurons
     */
    protected void sendToHidden(){
	    for(int i = 0; i < inputToHidden.length; i++){
	        for(int j = 0; j < inputToHidden[i].length; j++)
	            inputToHidden[i][j].transferValue();
	    }
	   for (int i = 0; i < biasToHidden.length; i++)
	        biasToHidden[i].transferValue();
	}

	/**
     * Calculates the output of the HiddenNeurons
     */
    protected void calculateHiddenOutput(){
	    for(int i = 0; i < hiddenNeurons.length; i++){
	        hiddenNeurons[i].calculateOutput();
		}
	}

	/**
     * Calculates the output error
     */
    protected double calculateError(){
		double error = targetOutput - outputNeuron.getOutputValue();
		return 0.5 * error * error;
    }

	/**
     * Transfers the weighted values to the OutputNeuron
     */
    protected void sendToOutput(){
	    for(int i = 0; i < hiddenToOutput.length; i++)
	    	hiddenToOutput[i].transferValue();
	    biasToOutput.transferValue();
    }

    /**
     * Trains the network
     */
    public void train(double[] data){
		inputData = data;
		epoch = 0;
		double error;
		while((epoch < totalEpochs)){
			initialise();
			sendToHidden();
			calculateHiddenOutput();
			sendToOutput();
			outputNeuron.calculateOutput();
			totalNetworkError += calculateError();
			calculateOutputError();
			calculateOutputWeightChange();
			calculateHiddenError();
			calculateHiddenWeightChange();
			adjustOutputWeights();
			adjustHiddenWeights();
			if (nextInput >= (inputData.length - 1)){
				nextInput = 0;
				totalNetworkError = 0;
		        epoch++;
			}
			else nextInput++;
		}
    }

    /**
     * Tests the network with the required test type
     */
    private void beginTest(double[] data, String testType){
		detailFile = new OutputFile("../output/"+ fileID + testType + "_" +"details.csv");
		inputData = data;
		totalEpochs = 1;
		nextInput = 0;
		while(nextInput <= (inputData.length - 1)){
			initialise();
			sendToHidden();
			calculateHiddenOutput();
			sendToOutput();
			outputNeuron.calculateOutput();
			totalNetworkError = calculateError();
			detailFile.writeToFile("Error" + "," + totalNetworkError);
			nextInput++;
		}
		detailFile.closeFile();
    }

    /**
	 * Starts testing the network
	 */
	 public void test(double[] data){
		beginTest(data, "T");
	}

    /**
     * Starts validating the network
     */
    public void validate(double[] data){
		beginTest(data, "V");
	}

	/**
	 * Gets the slope
	 *
	 * @return slope
	 */
	protected int getSlope(){
		return slope;
	}

	/**
	 * Gets the learning rate
	 *
	 * @return learningRate
	 */
	protected double getLearningRate(){
		return learningRate;
	}

	/**
	 * Gets the momentum
	 *
	 * @return momentum
	 */
	protected double getMomentum(){
		return momentum;
	}

	/**
	 * Sets the target output
	 *
	 * @param targetOutput the target output
	 */
	protected void setTargetOutput(double targetOutput){
		this.targetOutput = targetOutput;
	}

	/**
	 * Sets the OutputNeuron
	 *
	 * @param outputNeuron the OutputNeuron
	 */
	protected void setOutputNeuron(OutputNeuron outputNeuron){
		this.outputNeuron = outputNeuron;
	}

	/**
	 * Gets the OutputErrorTerm
	 *
	 * @return outputErrorTerm
	 */
	protected double getOutputErrorTerm(){
		return outputErrorTerm;
	}

	/**
	 * Gets a hiddenErrorTerm
	 *
	 * @param i its position
	 *
	 * @return hiddenErrorTerm an array of error terms
	 */
	protected double getHiddenErrorTerm(int i){
		return hiddenErrorTerm[i];
	}

	/**
	 * Sets a hiddenErrorTerm
	 *
	 * @param errorTerm the new error term
	 * @param i its position
	 */
	protected void setHiddenErrorTerm(double errorTerm, int i){
		hiddenErrorTerm[i] = errorTerm;
	}

	/**
	 * Gets the next input position
	 *
	 * @return nextInput
	 */
	protected int getNextInput(){
		return nextInput;
	}

	/**
	 * Gets the Synapse connecting the BiasNeuron to the OutputNeuron
	 *
	 * @return biasToOutput
	 */
	protected Synapse getBiasToOutput(){
		return biasToOutput;
	}

	/**
	 * Sets the BiasNeuron to the HiddenNeurons
	 *
	 * @param biasHidden the BiasNeuron to the HiddenNeurons
	 */
	protected void setBiasHidden(BiasNeuron biasHidden){
		this.biasHidden = biasHidden;
	}

	/**
	 * Gets the BiasNeuron to the HiddenNeurons
	 *
	 * @return biasHidden the BiasNeuron to the HiddenNeurons
	 */
	protected BiasNeuron getBiasHidden(){
		return biasHidden;
	}

	/**
	 * Sets the BiasNeuron to the OutputNeuron
	 *
	 * @param biasOutput the BiasNeuron to the OutputNeuron
	 */
	protected void setBiasOutput(BiasNeuron biasOutput){
		this.biasOutput = biasOutput;
	}

	/**
	 * Gets the BiasNeuron to the OutputNeuron
	 *
	 * @return biasOutput the BiasNeuron to the OutputNeuron
	 */
	protected BiasNeuron getBiasOutput(){
		return biasOutput;
	}

	/**
	 * Gets the Synapses between the BiasNeuron and the HiddenNeurons
	 *
	 * @return biasToHidden the array of Synapses
	 */
	protected Synapse[] getBiasToHidden(){
		return biasToHidden;
	}

	/**
	 * Gets a Synapse between the BiasNeuron and a HiddenNeuron
	 *
	 * @param i its position
	 *
	 * @return the Synapse at that position
	 */
	protected Synapse getBiasToHidden(int i){
		return biasToHidden[i];
	}

	/**
	 * Gets a Synapse between a HiddenNeuron and the OutputNeuron
	 *
	 * @param i its position
	 *
	 * @return the Synapse at the position
	 */
	protected Synapse getHiddenToOutput(int i){
		return hiddenToOutput[i];
	}

	/**
	 * Gets the Synapses between the HiddenNeurons and the OutputNeuron
	 *
	 * @return hiddenToOutput the array of Synapses
	 */
	protected Synapse[] getHiddenToOutput(){
		return hiddenToOutput;
	}

	/**
	 * Gets all the Synapses between the InputNeurons and the HiddenNeurons
	 *
	 * @return inputToHidden the 2D array of Synapses
	 */
	protected Synapse[][] getInputToHidden(){
		return inputToHidden;
	}

	/**
	 * Gets the Synapses between an InputNeuron and the HiddenNeurons
	 *
	 * @param i its position
	 *
	 * @return the array of Synapses at that position
	 */
	protected Synapse[] getInputToHidden(int i){
		return inputToHidden[i];
	}

	/**
	 * Gets the Synapse between an InputNeuron and a HiddenNeuron
	 *
	 * @param i its x position
	 * @param j its y position
	 *
	 * @return the Synapse at that position
	 */
	protected Synapse getInputToHidden(int i, int j){
	  return inputToHidden[i][j];
	}

	/**
	 * Gets an InputNeuron
	 *
	 * @param i its position
	 *
	 * @return the InputNeuron
	 */
	protected Neuron getInputNeuron(int i){
		return inputNeurons[i];
	}

	/**
	 * Gets the InputNeurons
	 *
	 * @return inputNeurons the array of InputNeurons
	 */
	protected Neuron[] getInputNeurons(){
		return inputNeurons;
	}

	/**
	 * Sets an InputNeuron
	 *
	 * @param inputNeuron the new InputNeuron
	 * @param i its position
	 */
	protected void setInputNeuron(InputNeuron inputNeuron, int i){
		inputNeurons[i] = inputNeuron;
	}

	/**
	 * Gets a HiddenNeuron
	 *
	 * @param i its position
	 *
	 * @return the HiddenNeuron
	 */
	protected Neuron getHiddenNeuron(int i){
		return hiddenNeurons[i];
	}

	/**
	 * Gets the HiddenNeurons
	 *
	 * @return hiddenNeurons the array of HiddenNeurons
	 */
	protected Neuron[] getHiddenNeurons(){
		return hiddenNeurons;
	}

	/**
	 * Sets a HiddenNeuron
	 *
	 * @param hiddenNeuron the new HiddenNeuron
	 * @param i its position
	 */
	protected void setHiddenNeuron(HiddenNeuron hiddenNeuron, int i){
		hiddenNeurons[i] = hiddenNeuron;
	}

	/**
	 * Gets input data at a given position
	 *
	 * @param i its position
	 *
	 * @return the double at that position
	 */
	protected double getInputData(int i){
		return inputData[i];
	}

	/**
	 * Gets the input data
	 *
	 * @return inputData
	 */
	protected double[] getInputData(){
		return inputData;
	}

}
