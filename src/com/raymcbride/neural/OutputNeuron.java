/**
 * The OutputNeuron class creates Output Neurons
 *
 * @author Raymond McBride
 */
public class OutputNeuron extends Neuron{

    /**
	 * This constructor for the <code>OutputNeuron</code> sets the slope of it's activation function
	 * and the number of it's inputs
	 *
	 * @param slope The slope of the activation function
	 * @param numberOfInputs The number of inputs
	 */
	public OutputNeuron(int slope, int numberOfInputs){
        super(slope, numberOfInputs);
    }
}
