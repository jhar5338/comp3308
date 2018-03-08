import java.util.ArrayList;

public class Attribute {

	private ArrayList<Double> valuesY = new ArrayList<Double>();
	private ArrayList<Double> valuesN = new ArrayList<Double>();
	private int countY = 0;
	private int countN = 0;
	private double meanY = 0;
	private double meanN = 0;
	private double stdDevY = 0;
	private double stdDevN = 0;

	public void insertY(double val) {
		valuesY.add(Double.valueOf(val));
		countY++;
	}

	public void insertN(double val) {
		valuesN.add(Double.valueOf(val));
		countN++;
	}

	public void calcMean() {
		double sum = 0;

		// add all values
		for (Double d : valuesN) {
			sum += d.doubleValue();
		}
		meanN = sum / (double) countN;

		sum = 0;

		for (Double d : valuesY) {
			sum += d.doubleValue();
		}
		meanY = sum / (double) countY;
	}

	// after all values are added getMean must be invoked before getStdDev
	public void calcStdDev() {
		double difference = 0;
		double sum = 0;

		for (Double d : valuesY) {
			difference = meanY - d.doubleValue();
			sum += (difference * difference);
		}
		stdDevY = Math.sqrt(sum / (double) (countY-1));

		difference = 0;
		sum = 0;

		for (Double d : valuesN) {
			difference = meanN - d.doubleValue();
			sum += (difference * difference);
		}
		stdDevN = Math.sqrt(sum / (double) (countN-1));
	}

	public double getMeanY() {
		return meanY;
	}

	public double getMeanN() {
		return meanN;
	}

	public double getStdDevY() {
		return stdDevY;
	}

	public double getStdDevN() {
		return stdDevN;
	}

	public int getCountY() {
		return countY;
	}

	public int getCountN() {
		return countN;
	}

}
