package org.springframework.samples.petclinic.CRaC;

import lombok.RequiredArgsConstructor;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class CRaCService implements ICRaCService, Resource {

	@Override
	public String createSnapshot() {
		String snapshotDir = "./crac-files";
		try {
			File dir = new File(snapshotDir);
			if (dir.exists()) {
				System.out.println("[CraCService] Cleaning existing snapshot folder: " + snapshotDir);
				deleteFolder(dir);
			}

			dir.mkdirs();
			System.out.println("[CraCService] Initiating snapshot...");
			Core.checkpointRestore();
			return "Snapshot created successfully.";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Error during snapshot creation: " + e.getMessage();
		}
	}

	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				}
				else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	@Override
	public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
		System.out.println("[CraCService] Preparing for checkpoint (closing resources, etc.)");
	}

	@Override
	public void afterRestore(Context<? extends Resource> context) throws Exception {
		System.out.println("[CraCService] Restoring from checkpoint...");
	}

}
