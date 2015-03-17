/**
 * The DataProcessor class is used to scale the input data between 0 and 1
 *
 * @author Raymond McBride
 */
public class DataProcessor{

    private double[] inputs;
    private double minimum;
    private double maximum;

    /**
	 * This constructor for the <code>DataProcessor</code> sets it's inputs
	 *
	 * @param inputs An array of input values
	 */
	public DataProcessor(double[] inputs){
        this.inputs = inputs;
    }

    /**
	 * Scales it's inputs array between 0 and 1
	 *
	 * @return a double array of it's scaled inputs
	 */
	public double[] scale(){
        double[] temp = new double [inputs.length];
        minimum = inputs[0];
        maximum = inputs[0];
        for(int i = 0; i < inputs.length; i++){
            if (inputs[i] < minimum)
                minimum = inputs[i];
            if (inputs[i] > maximum)
                maximum = inputs[i];
        }
        for(int i = 0; i < inputs.length; i++)
            temp[i] = (inputs[i] - minimum) / (maximum - minimum);
        return temp;
    }
}
