/**
 * The TDNN class is used to create Time Delay Neural Networks
 *
 * @author Raymond McBride
 */
public class TDNN extends Network{

	private int delays;

	/**
	 * This constructor for the <code>TDNN</code> specifies the number of Input Neurons and Hidden Neurons,
	 * the number of delays, the slope of their activation functions, the learning rate, the momentum,
	 * the number of training, epochs and set a network topology id for use with log files.
	 *
	 * @param inputs The number of Input Neurons
	 * @param hiddens The number of Hidden Neurons
	 * @param delays The number of delays
	 * @param slope The slope of their activation functions
	 * @param learningRate The learning rate
	 * @param momentum The momentum
	 * @param totalEpochs The number of training epochs
	 * @param fileID The network topology id
	 */
	public TDNN(int inputs, int hiddens, int delays, int slope, double learningRate, double momentum, int totalEpochs, String fileID){
		super(inputs, hiddens, slope, learningRate, momentum, totalEpochs, fileID);
		this.delays = delays;
		createNeurons(delays);
        connectNeurons();
	}

	/**
     * Creates the Neurons
     *
     * @param delays The number of delays
     */
    protected void createNeurons(int delays){
		for(int i = 0; i < getInputNeurons().length; i++)
			setInputNeuron((new InputNeuron(getSlope(), delays)), i);
		for(int i = 0; i < getHiddenNeurons().length; i++)
			setHiddenNeuron((new HiddenNeuron(getSlope(), getInputNeurons().length, delays)), i);
		setOutputNeuron(new OutputNeuron(getSlope(), getHiddenNeurons().length));
		setBiasHidden(new BiasNeuron());
		setBiasOutput(new BiasNeuron());
	}

	/**
     * Calculates the output of the Hidden Neurons
     */
    protected void calculateHiddenOutput(){
		for(int i = 0; i < getHiddenNeurons().length; i++){
			getHiddenNeuron(i).calculateDelayedOutput();
		}
	}

	/**
     * Calculates the weight change to be made to the Synapses between the Output and Hidden Neurons
     * and Bias Neurons
     */
    protected void calculateOutputWeightChange(){
		for (int i = 0; i < (getHiddenToOutput()).length; i++){
			getHiddenToOutput(i).calculateWeightChange(getLearningRate(), getMomentum(), getOutputErrorTerm(), delays);
		}
		getBiasToOutput().calculateWeightChange(getLearningRate(), getMomentum(), getOutputErrorTerm());
	}

	/**
     * Calculates the weight change to be made to the Synapses between the Hidden and Input Neurons
     * and Bias Neurons
     */
    protected void calculateHiddenWeightChange(){
		for (int i = 0; i < (getInputToHidden()).length; i++){
			for(int j = 0; j < (getInputToHidden(i)).length; j++){
				getInputToHidden(i, j).calculateWeightChange(getLearningRate(), getMomentum(), getHiddenErrorTerm(j), delays);
			}
		}
		for (int i = 0; i < (getBiasToHidden()).length; i++){
			getBiasToHidden(i).calculateWeightChange(getLearningRate(), getMomentum(), getHiddenErrorTerm(i));
		}
	}

	/**
     * Calculates the hidden error term
     */
    protected void calculateHiddenError(){
		for(int i = 0; i < getHiddenNeurons().length; i++){
			double errorTerm = 0;
			double[] temp = ((HiddenNeuron)getHiddenNeuron(i)).getOutputs();
			for(int j = 0; j < delays; j++)
				errorTerm += getSlope() * temp[j] * (1 - temp[j]) * getOutputErrorTerm() * (getHiddenToOutput(i)).getWeight();
			setHiddenErrorTerm(errorTerm/delays, i);
		}
	}

	/**
	 * Initialises the network with data
	 */
	protected void initialise(){
		int n = 0;
		for(int i = 0; i < getInputNeurons().length; i++){
			getInputNeuron(i).input(getInputData((getNextInput() + i + getInputData().length)%getInputData().length));
			getInputNeuron(i).calculateDelayedOutput();
		}
		getBiasHidden().calculateOutput();
		getBiasOutput().calculateOutput();
		setTargetOutput(getInputData((getNextInput() + getInputNeurons().length - 1)%getInputData().length));
	}
}