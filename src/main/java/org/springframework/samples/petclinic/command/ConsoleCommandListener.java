package org.springframework.samples.petclinic.command;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleCommandListener implements CommandLineRunner {

	private final CommandFactory commandFactory;

	private boolean running = true;

	public ConsoleCommandListener(CommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Snapshot CLI started. Type 'help' for available commands.");

		while (running) {
			System.out.print("> ");
			String input = scanner.nextLine().trim();

			if (input.equalsIgnoreCase("exit")) {
				System.out.println("Exiting CLI...");
				running = false;
				continue;
			}

			Command command = commandFactory.get(input);

			if (command != null) {
				command.execute(args);
			}
			else {
				System.out.println("[ERROR] Unknown command: " + input);
				System.out.println("Type '/help' for available commands.");
			}
		}
		scanner.close();
	}

}
