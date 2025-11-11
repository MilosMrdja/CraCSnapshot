package org.springframework.samples.petclinic.system;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkpoint")
public class CraCController implements Resource {

	@GetMapping
	public String createSnapshot() {
		try {

			System.out.println("SSS");
			Core.checkpointRestore();

			return "Snapshot created successfully.";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Error during creating a snapshot: " + e.getMessage();
		}
	}

	@Override
	public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
		System.out.println("Preparing for checkpoint (closing resources, etc.)");
	}

	@Override
	public void afterRestore(Context<? extends Resource> context) throws Exception {
		System.out.println("Restoring from checkpoint...");
	}

}
