package com.scottrade.datagovernance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.scottrade.datagovernance.domain.Person;
import com.scottrade.datagovernance.dto.PersonDto;
import com.scottrade.datagovernance.dto.save.SavePersonRequest;
import com.scottrade.datagovernance.exception.NotFoundException;
import com.scottrade.datagovernance.service.PersonService;
import com.scottrade.datagovernance.util.DtoFactory;

/**
 * REST layer for managing people.
 * 
 * @author Adapted from http://codetutr.com/2013/04/09/spring-mvc-easy-rest-based-json-services-with-responsebody/
 */
@Controller
public class PersonController {

	private PersonService personService;
	private DtoFactory personDtoFactory;

	@Autowired
	public PersonController(PersonService personService, DtoFactory personDtoFactory) {
		this.personService = personService;
		this.personDtoFactory = personDtoFactory;
	}

	/**
	 * @param id
	 * @return Returns the person with the given id.
	 */
	@RequestMapping("person/{id}")
	@ResponseBody
	public PersonDto getPersonById(@PathVariable Integer id) {
		return personDtoFactory.createPerson(personService.getPersonById(id));
	}

	/**
	 * Same as above method, just showing different URL mapping
	 * @param id
	 * @return Returns the person with the given id.
	 */
	@RequestMapping(value = "person", params = "id")
	@ResponseBody
	public PersonDto getPersonByIdFromParam(@RequestParam Integer id) {
		return personDtoFactory.createPerson(personService.getPersonById(id));
	}

	/**
	 * Creates a new person.
	 * @param request
	 * @return Returns the id for the new person.
	 */
	@RequestMapping(value = "person", method = RequestMethod.POST)
	@ResponseBody
	public Integer createPerson(@RequestBody SavePersonRequest request) {
		Person person = new Person();
		person.setFirstName(request.getFirstName());
		person.setLastName(request.getLastName());
		person.setUserName(request.getUserName());
		personService.savePerson(person);
		return person.getId();
	}
	
	// --- Error handlers
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleNotFoundException(NotFoundException e) {
		return e.getMessage();
	}

}
