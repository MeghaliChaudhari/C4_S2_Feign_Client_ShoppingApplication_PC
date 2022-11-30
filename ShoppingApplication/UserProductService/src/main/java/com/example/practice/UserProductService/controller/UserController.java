package com.example.practice.UserProductService.controller;

import com.example.practice.UserProductService.exception.ProductNotFoundException;
import com.example.practice.UserProductService.exception.UserAlreadyExistsException;
import com.example.practice.UserProductService.exception.UserNotFoundException;
import com.example.practice.UserProductService.model.Product;
import com.example.practice.UserProductService.model.User;
import com.example.practice.UserProductService.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/userproduct/app")
public class UserController {
    private UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws UserAlreadyExistsException {
        try {
            user.setProductList(new ArrayList<>());
            return new ResponseEntity<>(userServiceImpl.addUser(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException();
        }
    }

    @PutMapping("/addproduct/{userId}")
    public ResponseEntity<?> addProduct(@PathVariable int userId, @RequestBody Product product) throws UserNotFoundException {
        try {
            return new ResponseEntity<>(userServiceImpl.addProductForUser(userId,product),HttpStatus.CREATED);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }
    }

    @DeleteMapping("/deleteproduct/{productId}/{userId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int userId,@PathVariable int productId) throws ProductNotFoundException, UserNotFoundException {
        try {
            return new ResponseEntity<>(userServiceImpl.deleteProductForUser(userId, productId),HttpStatus.OK);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (ProductNotFoundException e){
            throw new ProductNotFoundException();
        }
    }

    @GetMapping("/getproduct/{userId}")
    public ResponseEntity<?> getProduct(@PathVariable int userId) throws UserNotFoundException {
        try {
            return new ResponseEntity<>(userServiceImpl.getAllProductForUser(userId),HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
