package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class RoomEscapeFlow {

	public static void main(String[] args) throws FileNotFoundException {
		String numericRegex = "^[0-9]+\\.[0-9]+";
		int N = 100;
		float yLimit = 21;
		Scanner scanner = new Scanner(new File("dynamic.txt"));
		List<String> ts = Lists.newLinkedList();
		List<Integer> flows = Lists.newLinkedList();
		List<Integer> remaning = Lists.newLinkedList();
		Set<String> ids = Sets.newHashSet();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.matches(numericRegex)) {
				ts.add(line);
				if (scanner.hasNextFloat() || !scanner.hasNext()) {
					flows.add(0);
					remaning.add(N);
				}
			} else {
				int flow = 0;
				boolean hasNextFloat;
				do {
					String[] nextLine = line.split("/");
					String id = nextLine[0];
					float yPosition = Float.valueOf(nextLine[1].split(" ")[1]);
					if (yPosition > yLimit && ids.add(id)) {
						flow++;
						N--;
					}
					hasNextFloat = scanner.hasNextFloat();
					if (!hasNextFloat) {
						line = scanner.nextLine();
					}
				} while (!hasNextFloat);
				flows.add(flow);
				remaning.add(N);
			}
		}
		scanner.close();
		Iterator<String> tsit = ts.iterator();
		Iterator<Integer> flowsit = flows.iterator();
		LinkedHashMap<Integer, Integer> flowBySecond = new LinkedHashMap<Integer, Integer>();
		int size = ts.size();
		for (int i = 0; i < size; i++) {
			int t = Float.valueOf(tsit.next()).intValue();
			int flow = flowsit.next();
			Integer acumFlow = flowBySecond.get(t);
			if (acumFlow == null) {
				acumFlow = 0;
			}
			flowBySecond.put(t, acumFlow + flow);
		}
//		System.out.println(String.format("x = %s;", flowBySecond.keySet()));
//		System.out.println(String.format("y = %s;", flowBySecond.values()));
		System.out.println(String.format("t = %s;", ts));
		System.out.println(String.format("N = %s;", remaning));
	}
}
