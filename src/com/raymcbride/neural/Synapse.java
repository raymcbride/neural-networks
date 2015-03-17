package com.raymcbride.neural;

/**
 * The Synapse class is used to connect Neurons together. Synapses are created with
 * adjustable random weights.
 *
 * @see ContextSynapse
 *
 * @author Raymond McBride
 */
public class Synapse{

    private double weight;
    private Neuron inputNeuron;
    private Neuron outputNeuron;
    private double weightChange;

    /**
	 * This constructor for the <code>Synapse</code> connects two Neurons together,
	 * and sets it's weight to a random value
	 *
	 * @param inputNeuron The input <code>Neuron</code>
	 * @param outputNeuron The output <code>Neuron</code>
	 */
	public Synapse(Neuron inputNeuron, Neuron outputNeuron){
        weightChange = 0;
        weight = Math.random();
        this.inputNeuron = inputNeuron;
        this.outputNeuron = outputNeuron;
    }

    /**
	 * Gets the Synapse weight
	 *
	 * @return The weight
	 */
	public double getWeight(){
	    return weight;
	}

	/**
	 * Sets the Synapse weight
	 *
	 * @param newWeight The new weight
	 */
	protected void setWeight(int newWeight){
		weight = newWeight;
	}

	/**
	 * Calculates the weight change
	 */
	public void calculateWeightChange(double learningRate, double momentum, double errorTerm){
	    weightChange = (learningRate * errorTerm * inputNeuron.getOutputValue()) + (momentum * weightChange);
    }

    /**
	 * Calculates the average weight change for use with time delays
	 */
	public void calculateWeightChange(double learningRate, double momentum, double errorTerm, int delays){
		double[] delayedOutputs;
		if(inputNeuron instanceof InputNeuron){
			delayedOutputs = ((InputNeuron)inputNeuron).getOutputs();
		}
		else{
			delayedOutputs = ((HiddenNeuron)inputNeuron).getOutputs();
		}
		double previousWeight = weightChange;
		weightChange = 0;
		for (int i = 0; i < delays; i++)
			weightChange += (learningRate * errorTerm * delayedOutputs[i]) + (momentum * previousWeight);
		weightChange = weightChange/delays;
    }

    /**
	 * Adjusts the weight
	 */
	public void adjustWeight(){
		weight = weight + weightChange;
	}

    /**
	 * Transfers a weighted value from the input Neuron to the output Neuron
	 */
	public void transferValue(){
        if(inputNeuron instanceof BiasNeuron){
        	outputNeuron.setBias(inputNeuron.getOutputValue() * weight);
		}
        else{
			outputNeuron.input(inputNeuron.getOutputValue() * weight);
        }
    }
}