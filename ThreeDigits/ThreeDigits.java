import java.io.*;
import java.util.*;

public class ThreeDigits {

	private static String goal;
	private static ArrayList<String> forbidden = new ArrayList<String>();
	private static ArrayList<Node> expanded = new ArrayList<Node>();
	private static StringBuilder output = new StringBuilder();
	private static int counter = 1;

	public static void main(String[] args) {

		if (args.length != 2) {
			error("Invalid number of args supplied. Expected: 2");
		}

		String fileName = args[1];
		BufferedReader br = null;
		FileReader fr = null;
		Node root = null;

		try {

			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			String currentLine;

			// read first line from text file
			currentLine = br.readLine();

			if (currentLine == null)
				error("Error in text file.");

			root = new Node(null, 1, 0);
			root.setValue(currentLine);

			// second line
			currentLine = br.readLine();

			if (currentLine == null)
				error("Error in text file.");

			goal = currentLine;

			// third line
			currentLine = br.readLine();

			if (currentLine != null) {
				StringTokenizer st = new StringTokenizer(currentLine, ",");
				while (st.hasMoreTokens()) {
					forbidden.add(st.nextToken());
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		// check first argument for algorithm selection
		if (args[0].equals("B")) {
			bfs(root);
		} else if (args[0].equals("D")) {
			dfs(root);
		} else if (args[0].equals("I")) {
			ids(root);
		} else if (args[0].equals("G")) {
			greedy(root);
		} else if (args[0].equals("A")) {
			star(root);
		} else if (args[0].equals("H")) {
			hill(root);
		} else {
			error("Invalid algorithm selected, first arg should be D,I,G,A or H for algorithm selection.");
		}
	}

	public static void bfs(Node root) {

		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);

		while (!queue.isEmpty()) {
			Node temp = queue.remove();

			// check if max nodes has been reached
			if (expanded.size() == 1000) {
				expanded.add(temp);
				noSolution();
			}

			// check if temp is goal node
			if (temp.getValue().equals(goal)) {
				expanded.add(temp);
				goalOutput(temp, root);
			}

			generateChildren(temp);

			if (!isCycle(temp)) {
				expanded.add(temp);
				for (Node child : temp.getChildren()) {
					queue.add(child);
				}
			}
		}

		// if run out of nodes then no solution found
		noSolution();
	}

	public static void dfs(Node root) {
		Stack<Node> stack = new Stack<Node>();
		Stack<Node> stack2 = new Stack<Node>();
		stack.push(root);

		while (!stack.isEmpty()) {
			Node temp = stack.peek();
			// check if max nodes has been reached
			if (expanded.size() == 1000) {
				expanded.add(temp);
				noSolution();
			}

			// check if temp is goal node
			if (temp.getValue().equals(goal)) {
				expanded.add(temp);
				goalOutput(temp, root);
			}

			if (!temp.hasChildren()) {
				generateChildren(temp);
				stack.push(temp);
			}

			expanded.add(temp);
			if (temp.hasChildren()) {
				boolean childFound = false;
				List<Node> children = new ArrayList<Node>(temp.getChildren());
				for (int i = 0; i < children.size(); i++) {
					Node child = children.get(i);
					generateChildren(child);
					if (!isCycle(child)) {
						stack2.push(child);
						childFound = true;
					}
				}
				while (!stack2.isEmpty()) {
					stack.push(stack2.pop());
				}
				if (!childFound) {
					stack.pop();
				}
			}
		}

		// if run out of nodes then no solution found
		noSolution();

	}

	public static void ids(Node root) {

	}

	public static void greedy(Node root) {

	}

	public static void star(Node root) {

	}

	public static void hill(Node root) {

	}

	public static void error(String message) {
		System.err.println(message);
		System.exit(1);
	}

	public static boolean isCycle(Node temp) {
		boolean cycle = false;
		for (Node ex : expanded) {
			cycle = false;
			if (ex.getValue().equals(temp.getValue())) {
				cycle = true;
				List<Node> list1 = new ArrayList<Node>(ex.getChildren());
				List<Node> list2 = new ArrayList<Node>(temp.getChildren());
				if (list1.size() != list2.size()) {
					continue;
				}
				for (int i = 0; i < list1.size() && i < list2.size(); i++) {
					if (!(list1.get(i).getValue().equals(list2.get(i).getValue()))) {
						cycle = false;
						break;
					}
				}
				if (cycle == true) {
					break;
				}
			}
		}
		return cycle;
	}

	public static void generateChildren(Node temp) {
		String val;
		val = Character.toString(temp.getValue().charAt(0));
		if (!val.equals("0") && temp.getPrev() != 1) {
			boolean skip = false;
			int a = Integer.parseInt(val);
			a--;
			val = Integer.toString(a);
			String str = val + Character.toString(temp.getValue().charAt(1))
					+ Character.toString(temp.getValue().charAt(2));
			for (String x : forbidden) {
				if (str.equals(x)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				counter++;
				Node child = new Node(temp, counter, 1);
				child.setValue(str);
				temp.addChild(child);
			}
		}
		val = Character.toString(temp.getValue().charAt(0));
		if (!val.equals("9") && temp.getPrev() != 1) {
			boolean skip = false;
			int a = Integer.parseInt(val);
			a++;
			val = Integer.toString(a);
			String str = val + Character.toString(temp.getValue().charAt(1))
					+ Character.toString(temp.getValue().charAt(2));
			for (String x : forbidden) {
				if (str.equals(x)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				counter++;
				Node child = new Node(temp, counter, 1);
				child.setValue(str);
				temp.addChild(child);
			}
		}

		// store value of second digit
		val = Character.toString(temp.getValue().charAt(1));
		if (!val.equals("0") && temp.getPrev() != 2) {
			boolean skip = false;
			int a = Integer.parseInt(val);
			a--;
			val = Integer.toString(a);
			String str = Character.toString(temp.getValue().charAt(0)) + val
					+ Character.toString(temp.getValue().charAt(2));
			for (String x : forbidden) {
				if (str.equals(x)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				counter++;
				Node child = new Node(temp, counter, 2);
				child.setValue(str);
				temp.addChild(child);
			}
		}
		val = Character.toString(temp.getValue().charAt(1));
		if (!val.equals("9") && temp.getPrev() != 2) {
			boolean skip = false;
			int a = Integer.parseInt(val);
			a++;
			val = Integer.toString(a);
			String str = Character.toString(temp.getValue().charAt(0)) + val
					+ Character.toString(temp.getValue().charAt(2));
			for (String x : forbidden) {
				if (str.equals(x)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				counter++;
				Node child = new Node(temp, counter, 2);
				child.setValue(str);
				temp.addChild(child);
			}
		}
		// store value of third digit
		val = Character.toString(temp.getValue().charAt(2));
		if (!val.equals("0") && temp.getPrev() != 3) {
			boolean skip = false;
			int a = Integer.parseInt(val);
			a--;
			val = Integer.toString(a);
			String str = Character.toString(temp.getValue().charAt(0)) + Character.toString(temp.getValue().charAt(1))
					+ val;
			for (String x : forbidden) {
				if (str.equals(x)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				counter++;
				Node child = new Node(temp, counter, 3);
				child.setValue(str);
				temp.addChild(child);
			}
		}
		val = Character.toString(temp.getValue().charAt(2));
		if (!val.equals("9") && temp.getPrev() != 3) {
			boolean skip = false;
			int a = Integer.parseInt(val);
			a++;
			val = Integer.toString(a);
			String str = Character.toString(temp.getValue().charAt(0)) + Character.toString(temp.getValue().charAt(1))
					+ val;
			for (String x : forbidden) {
				if (str.equals(x)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				counter++;
				Node child = new Node(temp, counter, 3);
				child.setValue(str);
				temp.addChild(child);
			}
		}
	}

	public static void noSolution() {
		System.out.println("No solution found.");
		for (Node x : expanded) {
			output.append(x.getValue() + ",");
		}
		output.setLength(output.length() - 1);
		System.out.println(output.toString());
		System.exit(0);
	}

	public static void goalOutput(Node temp, Node root) {
		// use stack to store path used to get to goal node
		Stack<String> stack = new Stack<String>();
		stack.push(goal);
		while (temp.getParent().getId() != 1) {
			temp = temp.getParent();
			stack.push(temp.getValue());
		}
		System.out.print(root.getValue());
		while (!stack.isEmpty()) {
			System.out.print("," + stack.pop());
		}
		System.out.print("\n");
		for (Node x : expanded) {
			output.append(x.getValue() + ",");
		}
		output.setLength(output.length() - 1);
		System.out.println(output.toString());
		System.exit(0);
	}

}
