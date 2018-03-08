import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MyClassifier {

	public static void main(String[] args) {
		String arg1 = args[0];
		String arg2 = args[1];

		if (args[2].equals("NB")) {
			naiveBayes(arg1, arg2);
		} else if (args[2].contains("NN")) {
			nearestNeighbour(arg1, arg2, Integer.parseInt(args[2].substring(0, args[2].length() - 2)));
		}

	}

	public static void naiveBayes(String arg1, String arg2) {

		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		int numAttr = 0;
		int yes = 0;
		int no = 0;
		// read file1
		try (BufferedReader file1 = new BufferedReader(new FileReader(arg1))) {

			/*
			 * read first line separately to create correct number of attribute
			 * objects
			 */
			String line = file1.readLine();
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == ',') {
					numAttr++;
				}
			}
			for (int j = 0; j < numAttr; j++) {
				Attribute a = new Attribute();
				attributes.add(a);
			}

			int temp = 0;
			StringTokenizer st = new StringTokenizer(line, ",");
			// if last character in string is s then assume "yes"
			if (line.charAt(line.length() - 1) == 's') {
				/*
				 * for each token insert it into the corresponding attribute
				 * values arraylist
				 */
				while (st.hasMoreTokens() && temp < numAttr) {
					String str = st.nextToken();
					attributes.get(temp).insertY(Double.parseDouble(str));
					temp++;
				}
				yes++;
			} else {
				while (st.hasMoreTokens() && temp < numAttr) {
					String str = st.nextToken();
					attributes.get(temp).insertN(Double.parseDouble(str));
					temp++;
				}
				no++;
			}

			// process rest of lines
			while ((line = file1.readLine()) != null) {
				int index = 0;
				StringTokenizer strTok = new StringTokenizer(line, ",");
				// if last character in string is s then assume "yes"
				if (line.charAt(line.length() - 1) == 's') {
					// for each token insert it into the corresponding attribute
					// values arraylist
					while (strTok.hasMoreTokens() && index < numAttr) {
						String str = strTok.nextToken();
						attributes.get(index).insertY(Double.parseDouble(str));
						index++;
					}
					yes++;
				} else {
					while (strTok.hasMoreTokens() && index < numAttr) {
						String str = strTok.nextToken();
						attributes.get(index).insertN(Double.parseDouble(str));
						index++;
					}
					no++;
				}
			}

			file1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * calculate mean and standard deviation for each attribute now that
		 * reading of values is finished
		 */
		for (int i = 0; i < numAttr; i++) {
			attributes.get(i).calcMean();
			attributes.get(i).calcStdDev();
		}

		// read file2
		try (BufferedReader file2 = new BufferedReader(new FileReader(arg2))) {

			String line;
			while ((line = file2.readLine()) != null) {
				int index = 0;
				StringTokenizer st = new StringTokenizer(line, ",");
				double probY = 0;
				double probN = 0;
				while (st.hasMoreTokens() && index < numAttr) {
					double val = Double.parseDouble(st.nextToken());

					// calculate yes probability and multiply probY
					double meanY = attributes.get(index).getMeanY();
					double stdDevY = attributes.get(index).getStdDevY();
					double baseY = 1 / (stdDevY * Math.sqrt(2 * Math.PI));
					double numeratorY = (val - meanY) * (val - meanY);
					double denominatorY = 2 * (stdDevY * stdDevY);
					double fractionY = -1 * (numeratorY / denominatorY);
					double probabilityY = baseY * Math.exp(fractionY);
					if (index == 0) {
						probY = probabilityY;
					} else {
						probY *= probabilityY;
					}
					// calculate no probability and multiply probN
					double meanN = attributes.get(index).getMeanN();
					double stdDevN = attributes.get(index).getStdDevN();
					double baseN = 1 / (stdDevN * Math.sqrt(2 * Math.PI));
					double numeratorN = (val - meanN) * (val - meanN);
					double denominatorN = 2 * (stdDevN * stdDevN);
					double fractionN = -1 * (numeratorN / denominatorN);
					double probabilityN = baseN * Math.exp(fractionN);
					
					if (index == 0) {
						probN = probabilityN;
					} else {
						probN *= probabilityN;
					}

					index++;
				}
				// p(yes) and p(no)
				double total = yes+no;
				probY *= (double)yes/total;
				probN *= (double)no/total;
				
			
				if (probY >= probN) {
					System.out.println("yes");
				} else {
					System.out.println("no");
				}
			}
			file2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void nearestNeighbour(String arg1, String arg2, int neighbours) {
		
		ArrayList<String[]> data = new ArrayList<String[]>();	//a variable size array of arrays
		int attrCount;
		
		// read training data
		try (BufferedReader file1 = new BufferedReader(new FileReader(arg1))) {
			
			String line;
			while ((line = file1.readLine()) != null) {
				data.add(line.split(","));	//store each line as an array of values
			}
			file1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		attrCount = data.get(0).length - 1;

		// read test data
		try (BufferedReader file2 = new BufferedReader(new FileReader(arg2))) {
			
			String line;
			while ((line = file2.readLine()) != null) {	// for each line of test data
				
				String[] test = line.split(",");
				
				double[] nearestDistance = new double[neighbours];	// contains k lowest distances
				for (int i = 0; i < neighbours; i++) {
					nearestDistance[i] = Double.MAX_VALUE;
				}
				
				int[] nearestIndex = new int[neighbours];	// contains indexes of training lines with lowest distances
				for (int i = 0; i < neighbours; i++) {
					nearestIndex[i] = -1;
				}
				
				for (int t = 0; t < data.size(); t++) {	// for each line of training data
					
					// calculate Euclidean distance from current line of test data
					double distance = 0;
					for (int i = 0; i < attrCount; i++) {	// for each attribute
						double diff = Double.parseDouble(test[i]) - Double.parseDouble(data.get(t)[i]);
						distance += (diff * diff);
					}
					distance = Math.sqrt(distance);	// square root of sum of squared differences
					
					for (int n = 0; n < neighbours; n++) {	// check if distance is one of the k lowest so far
						if (distance < nearestDistance[n]) {	// if less than current index
							for (int m = neighbours - 1; m > n; m--) {	// from end of array to current index
								nearestDistance[m] = nearestDistance[m - 1];	// move values up one index
								nearestIndex[m] = nearestIndex[m - 1];
							}
							nearestDistance[n] = distance;	// insert at current index
							nearestIndex[n] = t;
							break;	// once inserted, stop trying to insert it further up
						}
					}
				}
				
				// count number of 'yes' in nearest neighbours
				int yesCount = 0;
				for (int k = 0; k < neighbours; k++) {
					String cls = data.get(nearestIndex[k])[attrCount].toLowerCase();
					if (cls.equals("yes")) {
						yesCount++;
					}
				}
				
				if (yesCount >= neighbours - yesCount) { // assume all non-'yes' is 'no'
					System.out.println("yes");
				} else {
					System.out.println("no");
				}
			}
			file2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}