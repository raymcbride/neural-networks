package com.raymcbride.neural;

/**
 * The HiddenNeuron class is used to create Hidden Neurons
 *
 * @author Ray McBride
 */
public class HiddenNeuron extends Neuron{

	private double[] timeDelay;

    /**
	 * This constructor for the <code>HiddenNeuron</code> sets the slope of it's activation function
	 * and the number of it's inputs
	 *
	 * @param slope The slope of the activation function
	 * @param numberOfInputs The number of inputs
	 */
	public HiddenNeuron(int slope, int numberOfInputs){
    	super(slope, numberOfInputs);
    }

    /**
	 * This constructor for the <code>HiddenNeuron</code> sets the slope of it's activation function,
	 * the number of it's inputs and the number of delays
	 *
	 * @param slope The slope of the activation function
	 * @param numberOfInputs The number of inputs
	 * @param delays The number of delays
	 */
	public HiddenNeuron(int slope, int numberOfInputs, int delays){
		super(slope, numberOfInputs);
		timeDelay = new double[delays];
	}

	/**
	 * Calculates the summation of the delayed output values
	 */
	public void calculateDelayedOutput(){
		super.calculateOutput();
		for(int i = timeDelay.length - 1; i > 0; i--)
			timeDelay[i] = timeDelay[i-1];
		timeDelay[0] = getOutputValue();
		double tempOutput = 0;
		for(int i = 0; i < timeDelay.length; i++){
			tempOutput += timeDelay[i];
		}
		setOutputValue(tempOutput);
	}

	/**
	 * gets the delayed output values
	 *
	 * @return a double array of delayed output values
	 */
	public double[] getOutputs(){
		return timeDelay;
	}
}
