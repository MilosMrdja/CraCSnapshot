package org.springframework.samples.petclinic.command;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.CRaC.ICRaCService;
import org.springframework.stereotype.Component;

@Component
public class CreateSnapshotCommand implements Command {

	private final ICRaCService icraCService;

	public CreateSnapshotCommand(ICRaCService icraCService) {
		this.icraCService = icraCService;
	}

	@Override
	public String name() {
		return "/create-snapshot";
	}

	@Override
	public String description() {
		return "Create snapshot of the current application state";
	}

	@Override
	public void execute(String[] args) {
		System.out.println("Creating a new snapshot of the current application state");
		icraCService.createSnapshot();
	}

}
