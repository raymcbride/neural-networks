package com.raymcbride.neural;

/**
 * The Neuron class is the abstract base class for all Neurons
 *
 * @see InputNeuron
 * @see HiddenNeuron
 * @see OutputNeuron
 * @see BiasNeuron
 * @see ContextNeuron
 *
 * @author Ray McBride
 */
abstract class Neuron{

    private double inputValue;
    private double outputValue;
    private double[] inputValues;
    private double bias;
    private int slope;
    private int nextFree;

	/**
	 * This constructor for the <code>Neuron</code> sets the slope of it's activation function to 1
	 */
	public Neuron(){
		this(1);
	}

	/**
	 * This constructor for the <code>Neuron</code> sets the slope of it's activation function
	 *
	 * @param slope The slope of the activation function
	 */
	public Neuron(int slope){
		this.slope = slope;
	}

	/**
	 * This constructor for the <code>Neuron</code> sets the slope of it's activation function
	 * and the number of it's inputs
	 *
	 * @param slope The slope of the activation function
	 * @param numberOfInputs The number of inputs
	 */
	public Neuron(int slope, int numberOfInputs){
        this(slope);
        inputValues = new double[numberOfInputs];
        bias = 0;
        nextFree = 0;
    }

    /**
	 * Adds a new input value
	 *
	 * @param input The new input value
	 */
	public void input(double input){
		inputValues[nextFree] = input;
		nextFree = (nextFree + 1)%inputValues.length;
	}

    /**
	 * Sets the biased input
	 *
	 * @param input The new input value
	 */
	public void setBias(double input){
        bias = input;
    }

    /**
	 * Calculates the output
	 */
	public void calculateOutput(){
		outputValue = sigmoidActivation(this.summation());
	}

	/**
	 * Calculates the summation of the delayed output values
	 */
	public void calculateDelayedOutput(){}

    /**
	 * Calculates the summation of the inputs
	 */
	private double summation(){
        double temp = 0;
        for(int i = 0; i < inputValues.length; i++)
            temp += inputValues[i];
        return temp + bias;

    }

    /**
	 * The Sigmoid Activation Function
	 *
	 * @return the activated value
	 */
	protected double sigmoidActivation(double summation){
        return 1/(1 + Math.exp(-slope*summation));
    }

    /**
	 * Gets the input value
	 *
	 * @return the input value
	 */
	protected double getInputValue(){
		return inputValue;
	}

	/**
	 * Sets the input value
	 *
	 * @param inputValue the input value
	 */
	protected void setInputValue(double inputValue){
		this.inputValue = inputValue;
	}

	/**
	 * Gets the output value
	 *
	 * @return the output value
	 */
	protected double getOutputValue(){
		return outputValue;
	}

	/**
	 * Sets the output value
	 *
	 * @param outputValue the output value
	 */
	protected void setOutputValue(double outputValue){
		this.outputValue = outputValue;
	}
}
