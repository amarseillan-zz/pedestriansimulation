package ar.edu.itba;

import java.io.IOException;

import org.newdawn.slick.SlickException;

import ar.edu.itba.command.CommandParam;
import ar.edu.itba.command.CommandParser;
import ar.edu.itba.command.ParsedCommand;
import ar.edu.itba.pedestriansim.back.PedestrianSimApp;
import ar.edu.itba.pedestriansim.front.GUIPedestrianSim;
import ar.edu.itba.pedestriansim.metric.RunsGenerator;

public class App {

	private static final CommandParser parser;
	static {
		parser = new CommandParser()
			.param(new CommandParam("-sim").required().constrained("gui", "metrics", "noGui").message("Tipo de simulacion a correr"));
	}

	public static void main(String[] args) throws SlickException, IOException {
//		args = new String[] {
//			"-sim", "gui", "-map", "room", "-door", "1.5", "-amount", "100"
//			"-sim", "gui", "-map", "cross"
//			"-sim", "gui", "-map", "hallway"
//			"-sim", "gui", "-map", "hallway", "-static", "./static.txt", "-dynamic", "./dynamic.txt"
//		};
		if (args.length == 0) {
			System.out.println("USAGE: ");
			System.out.println(parser.getHelp());
			System.out.println("=> gui:");
			System.out.println(GUIPedestrianSim.parser.getHelp());
			System.out.println("=> metrics:");
			System.out.println(RunsGenerator.parser.getHelp());
			System.out.println("=> noGUi:");
			System.out.println(PedestrianSimApp.parser.getHelp());
			return;
		}
		ParsedCommand cmd = parser.parse(args);
		if (cmd.hasErrors()) {
			System.out.println(cmd.getErrorString());
			return;
		}
		String sim = cmd.param("-sim"); 
		if (sim.equals("gui")) {
			GUIPedestrianSim.main(args);
		} else if (sim.equals("metrics")) {
			RunsGenerator.main(args);
		} else {
			PedestrianSimApp.main(args);
		}
	}

}
