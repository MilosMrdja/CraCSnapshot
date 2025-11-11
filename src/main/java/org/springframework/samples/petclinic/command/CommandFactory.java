package org.springframework.samples.petclinic.command;

import org.springframework.samples.petclinic.CRaC.ICRaCService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandFactory {

	private final Map<String, Command> commands = new HashMap<>();

	public CommandFactory(List<Command> commandList) {
		for (Command c : commandList) {
			commands.put(c.name(), c);
		}
	}

	public Command get(String name) {
		return commands.get(name);
	}

	public void printAvailableCommands() {
		System.out.println("Available commands:");
		commands.values().forEach(c -> System.out.println("  " + c.description()));
		System.out.println("  /exit");
	}

}
