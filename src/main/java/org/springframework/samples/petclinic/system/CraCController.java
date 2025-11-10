package org.springframework.samples.petclinic.system;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crac")
public class CraCController implements Resource {

	@PostMapping("/checkpoint")
	public String createSnapshot(){
		try{
			Core.checkpointRestore();
			return "Snapshot created successfully.";
		}catch (Exception e){
			//e.printStackTrace();
			return "Error during creating a snapshot" + e.getMessage();
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
