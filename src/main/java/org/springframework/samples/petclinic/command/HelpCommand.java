package org.springframework.samples.petclinic.command;

import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

	@Override
	public String name() {
		return "/help";
	}

	@Override
	public String description() {
		return "Help for using CLI";
	}

	@Override
	public void execute(String[] args) {
		System.out.println("Available commands:");
		System.out.println("  /create-snapshot   - Creates a new snapshot in a separate JVM instance");
		System.out.println("  /help     - Displays this help information");
		System.out.println("  exit     - Exits the CLI tool");
	}

}
