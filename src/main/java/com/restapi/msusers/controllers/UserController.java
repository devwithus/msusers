package com.restapi.msusers.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.restapi.msusers.exceptions.ResourceNotFoundException;
import com.restapi.msusers.models.User;
import com.restapi.msusers.repositories.UserRepository;


@RestController
@RequestMapping("/api")
@Validated
public class UserController {
	
	@Autowired
	UserRepository userrepo;
	
	@GetMapping(value="/users")
	List<User> getAll(){
		return userrepo.findAll();
	}
	
	@GetMapping(value="/users/{id}")
	ResponseEntity<User> getById(@PathVariable("id") @Min(1) int id) {
		
		User usr = userrepo.findById(id)
				           .orElseThrow(()->new ResourceNotFoundException("User with ID :"+id+" Not Found!"));
		
		return ResponseEntity.ok().body(usr);
	}
	
	@GetMapping(value="/user")
	ResponseEntity<User> getByUsername(@RequestParam(required=true) String username) {
		
		User usr = userrepo.findByUsername(username)
				           .orElseThrow(()->new ResourceNotFoundException(username+" NOT Found!"));
		
		return ResponseEntity.ok().body(usr);
	}
	
	@PostMapping(value="/users")
	ResponseEntity<?> create( @Valid @RequestBody User user) {
		
		User addeduser = userrepo.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			                .path("/{id}")
			                .buildAndExpand(addeduser.getId())
			                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(value="/users/{id}")
	ResponseEntity<User> update(@PathVariable("id")  @Min(1) int id, @Valid @RequestBody User user) {

		User puser = userrepo.findById(id)
                			 .orElseThrow(()->new ResourceNotFoundException("User with ID :"+id+" Not Found!"));
		
		user.setId(puser.getId());
		userrepo.save(user);
		return ResponseEntity.ok().body(user);
		
	}
	
	@DeleteMapping(value="/users/{id}")
	ResponseEntity<String> delete(@PathVariable("id") @Min(1) int id) {
		User user = userrepo.findById(id)
							.orElseThrow(()->new ResourceNotFoundException("User with ID :"+id+" Not Found!"));
		
		userrepo.deleteById(user.getId());
		return ResponseEntity.ok().body("Employee deleted with success!");
		
	}
	

}
