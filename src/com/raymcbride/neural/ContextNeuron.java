package com.raymcbride.neural;

/**
 * The ContextNeuron class creates Context Neuron which are used to provide recurrency
 * in the RNN network. They store the previous Hidden Neuron activated values and a variable
 * amount of their own previous values, which is fed back into the network
 *
 * @see RNN
 *
 * @author Raymond McBride
 */
public class ContextNeuron extends Neuron{

	private double memoryDepth;
	private double memoryContents;

	/**
	 * This constructor for the <code>ContextNeuron</code> specifies the memory depth
	 * and sets the input value to 0.5
	 *
	 * @param memoryDepth The memory depth
	 */
	public ContextNeuron(double memoryDepth){
		super();
		this.memoryDepth = memoryDepth;
		setInputValue(0.5);
		memoryContents = 0;
	}

	/**
	 * Set the new input value and adds the old value to the memory
	 * @param input The new input value
	 */
	public void input(double input){
		memoryContents = getInputValue();
		setInputValue(input);
    }

	/**
	 * Calculates the output value
	 */
	public void calculateOutput(){
		setOutputValue(getInputValue() + memoryContents*memoryDepth);
	}

}