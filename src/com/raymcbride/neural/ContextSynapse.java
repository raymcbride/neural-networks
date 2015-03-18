package com.raymcbride.neural;

/**
 * The ContextSynapse class is used to connect Context Neurons to Hidden Neurons. They
 * have a fixed weight of 1
 *
 * @author Ray McBride
 */
public class ContextSynapse extends Synapse{

	/**
	 * This constructor for the <code>ContextSynapse</code> connects two Neurons together,
	 * and sets it's weight to 1
	 *
	 * @param inputNeuron The input Neuron
	 * @param outputNeuron The output Neuron
	 */
	public ContextSynapse(Neuron inputNeuron, Neuron outputNeuron){
		super(inputNeuron, outputNeuron);
		setWeight(1);
    }

    /**
	 * Transfers a weighted value from the input Neuron to the output Neuron
	 */
	public void transferValue(){
		super.transferValue();
	}
}