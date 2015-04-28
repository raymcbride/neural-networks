package com.raymcbride.neural;

/**
 * The BiasNeuron class creates Bias Neurons which have a fixed value of -1
 *
 * @author Ray McBride
 */
public class BiasNeuron extends Neuron{

	/**
	 * This constructor for the <code>BiasNeuron</code>initialises it's input value to -1
	 */
	public BiasNeuron(){
		super();
        setInputValue(-1.0);
    }

    /**
	 * Calculates the output value
	 */
	public void calculateOutput(){
		setOutputValue(getInputValue());
	}
}
