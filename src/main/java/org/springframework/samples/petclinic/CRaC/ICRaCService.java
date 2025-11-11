package org.springframework.samples.petclinic.CRaC;

public interface ICRaCService {

	/**
	 * Triggers a CRaC checkpoint and restore.
	 * @return message with the result of the operation.
	 */
	String createSnapshot();

}
