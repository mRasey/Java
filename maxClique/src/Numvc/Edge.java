package Numvc;

public class Edge {
	int id, from;
	int w;
	boolean covered;

	public Edge(int from, int id) {
		this.id = id;
		this.w = 1;
		this.covered = false;
		this.from = from;
	}

	@Override
	public String toString() {
		return " [" + id + " " + covered + "]";
	}

}
