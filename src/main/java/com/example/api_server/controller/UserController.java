package com.example.api_server.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.api_server.model.User;
import com.example.api_server.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController				// REST API
@Slf4j						// log 확인
@RequiredArgsConstructor	// 생성자 주입
public class UserController {
	private final UserRepository userRepository;
	
	@PostMapping	// "/" - 해당 컨트롤러와 같은 경로로 요청을 받게 됨
	public ResponseEntity<User> createUser(@RequestBody User user) {
		log.info("** creatUser ** ");
		log.debug(user.toString());
		User savedUser = userRepository.save(user);
		log.debug(savedUser.toString());
		// return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		log.info("** getAllUsers **");
		List<User> users = userRepository.findAll();
		log.debug(users.toString());
		
		// return ResponseEntity.status(HttpStatus.OK).body(users);
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
//		return userRepository.findById(id)
//				.map(ResponseEntity::ok)
//				.orElse(ResponseEntity.notFound().build());
		
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			// return new ResponseEntity<>(user.get(), HttpStatus.OK);
			return ResponseEntity.ok(user.get());
		}
		// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			userRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
		Optional<User> oldUser = userRepository.findById(id);
		if (oldUser.isPresent()) {
			user.setId(id);
			User newUser = userRepository.save(user);
			return ResponseEntity.ok(newUser);
		}
		return ResponseEntity.notFound().build();
	}
}
