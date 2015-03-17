/**
 * InputNeuron class is used to create Input Neurons
 *
 * @author Raymond McBride
 */
public class InputNeuron extends Neuron{

	private double[] timeDelay;

	/**
	 * This constructor for the <code>InputNeuron</code> sets the slope of it's activation function
	 *
	 * @param slope The slope of the activation function
	 */
	public InputNeuron(int slope){
	    super(slope);
    }

    /**
	 * This constructor for the <code>InputNeuron</code> sets the slope of it's activation function
	 * and the number of delays
	 *
	 * @param slope The slope of the activation function
	 * @param delays The number of delays
	 */
	public InputNeuron(int slope, int delays){
		super(slope);
		timeDelay = new double[delays];
	}

	/**
	 * Sets the new input Value
	 *
	 * @param input The new input value
	 */
	public void input(double input){
	    setInputValue(input);
    }

	/**
	 * Calculates the output value
	 */
	public void calculateOutput(){
		setOutputValue(getInputValue());
	}

	/**
	 * Calculates the summation of the delayed output values
	 */
	public void calculateDelayedOutput(){
		for(int i = timeDelay.length - 1; i > 0; i--)
			timeDelay[i] = timeDelay[i-1];
		timeDelay[0] = getInputValue();
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
