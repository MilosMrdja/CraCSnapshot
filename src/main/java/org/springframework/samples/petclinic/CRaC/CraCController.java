package org.springframework.samples.petclinic.CRaC;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/checkpoint")
public class CraCController {

	private final ICRaCService craCService;

	public CraCController(ICRaCService craCService) {
		this.craCService = craCService;
	}

	@GetMapping
	@ResponseBody
	public String createSnapshot() {
		return craCService.createSnapshot();
	}

}
