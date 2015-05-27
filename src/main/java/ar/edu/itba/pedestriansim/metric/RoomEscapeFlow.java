package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class RoomEscapeFlow {

	public static void main(String[] args) throws IOException {
//		int N = Integer.valueOf(args[0]);
//		String fileName = args[1];
		List<String> doors = Arrays.asList("1.3", "1.5", "1.8");
		List<Integer> Ns = Arrays.asList(100, 150, 200);
		for (String door : doors) {
			for (int N : Ns) {
				for (int index = 1; index <= 5; index++) {
					String fileName = String.format("/home/gcastigl/Desktop/tmp/pedestriansim/room/room-%d/dynamic-%s-%s.txt", index, N, door);
					System.out.println(fileName);
					process(new File(fileName), N);
				}
			}
		}
//		String door = "1.3";
//		int N = 200;
	}
	
	public static void process(File dynamicFile, int N) throws IOException {
		String numericRegex = "^[0-9]+\\.[0-9]+";
		float yLimit = 21;
		Scanner scanner = new Scanner(dynamicFile);
		List<String> ts = Lists.newLinkedList();
		List<Integer> flows = Lists.newLinkedList();
		List<Integer> remaning = Lists.newLinkedList();
		Set<String> ids = Sets.newHashSet();
		try {
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
		} catch (Exception e) {
			remaning.add(N);
		}
		scanner.close();
//		Iterator<String> tsit = ts.iterator();
//		Iterator<Integer> flowsit = flows.iterator();
//		LinkedHashMap<Integer, Integer> flowBySecond = new LinkedHashMap<Integer, Integer>();
//		int size = ts.size();
//		for (int i = 0; i < size; i++) {
//			int t = Float.valueOf(tsit.next()).intValue();
//			int flow = flowsit.next();
//			Integer acumFlow = flowBySecond.get(t);
//			if (acumFlow == null) {
//				acumFlow = 0;
//			}
//			flowBySecond.put(t, acumFlow + flow);
//		}
//		System.out.println(String.format("x = %s;", flowBySecond.keySet()));
//		System.out.println(String.format("y = %s;", flowBySecond.values()));
//		System.out.println(String.format("t = %s;", ts));
//		System.out.println(String.format("N = %s;", remaning));
		FileWriter writer = new FileWriter(new File(
			dynamicFile.getParent() + "/" + "flow_" + dynamicFile.getName()
		));
		int size = ts.size();
		Iterator<String> tsit = ts.iterator();
		Iterator<Integer> remaningit = remaning.iterator();
		for (int i = 0; i < size; i++) {
			writer.write(String.format("%s\t%s\n", tsit.next(), remaningit.next()));
		}
		writer.close();
		System.out.println("OK!");
	}
}
