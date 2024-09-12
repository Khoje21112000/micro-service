package com.Icwd.user.service.controllers;

import java.util.List;

import com.Icwd.user.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Icwd.user.service.entities.User;
import com.Icwd.user.service.services.UserService;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "http://localhost:3000") // Allow your React app's origin
public class UserController {

	@Autowired
	private UserService userService;

	//create
	// http://localhost:8081/users
	@PostMapping("/register")
	public ResponseEntity<String> createUser(@RequestBody User user) {
		try {
			// Check if the email is already registered
			User existingUser = userService.findByEmail(user.getEmail());

			if (existingUser != null) {
				// Email already registered
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered!");
			} else {
				// Save the new user
				userService.saveUser(user);
				return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful!");
			}
		} catch (ResourceNotFoundException e) {
			// Handle the specific exception if necessary
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred. Please try again later.");
		} catch (Exception e) {
			// Handle any other exceptions
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred. Please try again later.");
		}
	}


	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		User existingUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
		if (existingUser != null) {
			// Return the existing user object, including the user type
			return ResponseEntity.ok(existingUser);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	//single user get
	@GetMapping("/{userId}")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId){
		User user=userService.getUserByUserId(Integer.parseInt(userId));
		return ResponseEntity.ok(user);
	}

	//get all user

	@GetMapping
	public ResponseEntity<List<User>> getAllUser(){
		List<User> allUser=userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}

	// Delete user
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable String userId){
		boolean isDeleted = userService.deleteUser(Integer.parseInt(userId));
		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, @RequestBody User userDetails) {
		try {
			// Retrieve the existing user by userId
			User existingUser = userService.getUserByUserId(Integer.parseInt(userId));

			if (existingUser == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// Update the fields of the existing user with details from the request
			existingUser.setName(userDetails.getName());
			existingUser.setEmail(userDetails.getEmail());
			existingUser.setAbout(userDetails.getAbout());
			existingUser.setPassword(userDetails.getPassword());

			// Save the updated user
			User updatedUser = userService.saveUser(existingUser);

			return ResponseEntity.ok(updatedUser);
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}


}
