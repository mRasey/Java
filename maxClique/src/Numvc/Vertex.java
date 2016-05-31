package Numvc;

import java.util.Arrays;

public class Vertex {
	boolean isInC;
	int time;
	int dscore;
	int confChange;
	int id;
	Edge[] edges;

	Vertex(int id) {
		this.dscore = 0;
		this.isInC = false;
		this.time = 1;
		this.id = id;
		this.confChange = 1;
	}

	@Override
	public String toString() {
		return "\n" + id + " " + isInC + ", edges=" + Arrays.toString(edges)
				+ "";
	}

}
