package com.raymcbride.neural;

/**
 * The MLP class is used to create Multilayer Perceptron networks
 *
 * @author Raymond McBride
 */
public class MLP extends Network{

	/**
	 * This constructor for the <code>MLP</code> specifies the number of Input Neurons and Hidden Neurons
	 * the slope of their activation functions, the learning rate, the momentum, the number of training
	 * epochs and set a network topology id for use with log files.
	 *
	 * @param inputs The number of Input Neurons
	 * @param hiddens The number of Hidden Neurons
	 * @param slope The slope of their activation functions
	 * @param learningRate The learning rate
	 * @param momentum The momentum
	 * @param totalEpochs The number of training epochs
	 * @param fileID The network topology id
	 */
	public MLP(int inputs, int hiddens, int slope, double learningRate, double momentum, int totalEpochs, String fileID){
		super(inputs, hiddens, slope, learningRate, momentum, totalEpochs, fileID);
		createNeurons();
        connectNeurons();
	}

	/**
	 * Initialises the network with data
	 */
	protected void initialise(){
		for(int i = 0; i < getInputNeurons().length; i++){
			getInputNeuron(i).input(getInputData((getNextInput() + i + getInputData().length)%getInputData().length));
			getInputNeuron(i).calculateOutput();
		}
		getBiasHidden().calculateOutput();
    	getBiasOutput().calculateOutput();
		setTargetOutput(getInputData((getNextInput() + getInputNeurons().length - 1)%getInputData().length));
	}
}