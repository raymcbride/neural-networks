package com.raymcbride.neural;

/**
 * The RNN class is based on the Elman Network, which has recurrency at the Hidden Layer.
 * In addition, there is a self loop at the context layer
 *
 * @author Raymond McBride
 */
public class RNN extends Network{

	private Neuron[] contextNeurons;
	private ContextSynapse[] contextToHidden;
	private ContextSynapse[] hiddenToContext;

	/**
	 * This constructor for the <code>RNN</code> specifies the number of Input Neurons and Hidden Neurons,
	 * the memory depth, the slope of their activation functions, the learning rate, the momentum,
	 * the number of training, epochs and set a network topology id for use with log files.
	 *
	 * @param inputs The number of Input Neurons
	 * @param hiddens The number of Hidden Neurons
	 * @param memoryDepth The memory depth
	 * @param slope The slope of their activation functions
	 * @param learningRate The learning rate
	 * @param momentum The momentum
	 * @param totalEpochs The number of training epochs
	 * @param fileID The network topology id
	 */
	public RNN(int inputs, int hiddens, double memoryDepth, int slope, double learningRate, double momentum, int totalEpochs, String fileID){
		super(inputs, hiddens, slope, learningRate, momentum, totalEpochs, fileID);
		contextNeurons = new Neuron[hiddens];
		contextToHidden = new ContextSynapse[hiddens];
		hiddenToContext = new ContextSynapse[hiddens];
		createNeurons(memoryDepth);
        connectNeurons();
	}

	/**
     * Creates the Neurons
     *
     * @param memoryDepth Their memory depth
     */
    protected void createNeurons(double memoryDepth){
		super.createNeurons();
		for(int i = 0; i < contextNeurons.length; i++)
			contextNeurons[i] = new ContextNeuron(memoryDepth);
    }

    /**
     * Connects the Neurons
     */
    protected void connectNeurons(){
		super.connectNeurons();
		for(int i = 0; i < contextNeurons.length; i++){
			contextToHidden[i] = new ContextSynapse(contextNeurons[i], getHiddenNeuron(i));
			hiddenToContext[i] = new ContextSynapse(getHiddenNeuron(i), contextNeurons[i]);
		}
	}

	/**
     * Transfers the weighted values to the Hidden Neurons
     */
    protected void sendToHidden(){
		super.sendToHidden();
		for(int i = 0; i < contextToHidden.length; i++)
			contextToHidden[i].transferValue();
	}

	/**
     * Transfers the weighted values to the Output Neuron
     */
    protected void sendToOutput(){
		super.sendToOutput();
		for(int i = 0; i < hiddenToContext.length; i++)
			hiddenToContext[i].transferValue();
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
    	for(int i = 0; i < contextNeurons.length; i++){
			contextNeurons[i].calculateOutput();
		}
		setTargetOutput(getInputData((getNextInput() + getInputNeurons().length - 1)%getInputData().length));
	}
}