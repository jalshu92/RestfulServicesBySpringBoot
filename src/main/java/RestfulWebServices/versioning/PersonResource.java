package RestfulWebServices.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonResource {

	
	@GetMapping("/pv1")
	public PersonV1 personV1() {
		return new PersonV1("Jalaj Shukla");
	}
	
	@GetMapping("/pv2")
	public PersonV2 personV2() {
		return new PersonV2(new Name("Jalaj", "Shukla"));
	}
	
	@GetMapping(name="/pv",params="v=1")
	public PersonV1 paramPersonV1() {
		return new PersonV1("Jalaj Shukla");
	}
	
	@GetMapping(name="/pv",params="v=2")
	public PersonV2 paramPersonV2() {
		return new PersonV2(new Name("Jalaj", "Shukla"));
	}
	
	@GetMapping(name="/pv",headers="v=1")
	public PersonV1 headerPersonV1() {
		return new PersonV1("Jalaj Shukla");
	}
	
	@GetMapping(name="/pv2",headers="v=2")
	public PersonV2 headerPersonV2() {
		return new PersonV2(new Name("Jalaj", "Shukla"));
	}
	
	@GetMapping(name="/pv",produces="application/com.jalaj.app-v1+xml")
	public PersonV1 producesPersonV1() {
		return new PersonV1("Jalaj Shukla");
	}
	
	@GetMapping(name="/pv2",produces="application/com.jalaj.app-v2+xml")
	public PersonV2 producesPersonV2() {
		return new PersonV2(new Name("Jalaj", "Shukla"));
	}
}
