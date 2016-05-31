package Numvc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Converter {
	private static BufferedReader reader;

	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[] convertEdgeList(String filename)
			throws IOException {
		reader = new BufferedReader(new FileReader(filename));
		String line;
		ArrayList<Integer> list[] = null;
		while ((line = reader.readLine()) != null) {
			String[] ls = line.split(" ");
			if (ls[0].equals("p")) {
				list = new ArrayList[Integer.parseInt(ls[2])];
				for (int i = 0; i < list.length; i++) {
					list[i] = new ArrayList<Integer>();
				}
			} else if (ls[0].equals("e")) {
				int from = Integer.parseInt(ls[1]);
				int to = Integer.parseInt(ls[2]);
				list[from - 1].add(to);
				list[to - 1].add(from);
			}
		}
		return list;
	}

	public static void main(String[] args) throws IOException {
		Converter.convertEdgeList("src/benchworks/frb30-15-1.mis");
	}

	public static ArrayList<Integer>[] convertEdgeListClique(String string)
			throws IOException {
		ArrayList<Integer>[] list = convertEdgeList(string);
		for (int i = 0; i < list.length; i++) {
			ArrayList<Integer> newL = new ArrayList<Integer>();
			boolean b[] = new boolean[list.length];
			for (int j = 0; j < list.length; j++) {
				b[j] = true;
			}
			b[i] = false;
			Iterator<Integer> it = list[i].iterator();
			while (it.hasNext())
				b[it.next() - 1] = false;
			for (int j = 0; j < list.length; j++) {
				if (b[j])
					newL.add(j + 1);
			}
			list[i] = newL;
		}
		return list;
	}
}
