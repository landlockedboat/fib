package IA.ProbIA5;

/**
 * Created by bejar on 17/01/17.
 */
public class ProbIA5Board {
	/*
	 * Class independent from AIMA classes - It has to implement the state of
	 * the problem and its operators
	 *
	 * 
	 * /* State data structure vector with the parity of the coins (we can
	 * assume 0 = heads, 1 = tails
	 */

	private int[] board;
	private static int[] solution;

	/* Constructor */
	public ProbIA5Board(int[] init, int[] goal) {

		board = new int[init.length];
		solution = new int[init.length];

		for (int i = 0; i < init.length; i++) {
			board[i] = init[i];
			solution[i] = goal[i];
		}

	}

	public ProbIA5Board(ProbIA5Board copy) {

		board = new int[copy.board.length];
		for (int i = 0; i < copy.board.length; i++) {
			board[i] = copy.board[i];
		}
	}

	/* Operator */
	public void flip_it(int i) {
		// flip the coins i and i + 1
		int tmp = board[i];
		board[i] = board[i + 1];
		board[i + 1] = tmp;
	}

	/* Heuristic function */
	public double heuristic() {
		// compute the number of coins out of place respect to solution
		double noc = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i] != solution[i]) {
				noc += 1.0;
			}
		}
		return noc;
	}

	/* Goal test */
	public boolean is_goal() {
		// compute if board = solution
		for (int i = 0; i < board.length; i++) {
			if (board[i] != solution[i]) {
				return false;
			}
		}
		return true;
	}

	public int getLength() {
		return board.length;
	}

}
