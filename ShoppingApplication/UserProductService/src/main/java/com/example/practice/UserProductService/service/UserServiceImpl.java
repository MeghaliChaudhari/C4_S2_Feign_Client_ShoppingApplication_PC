package com.example.practice.UserProductService.service;

import com.example.practice.UserProductService.exception.ProductNotFoundException;
import com.example.practice.UserProductService.exception.UserAlreadyExistsException;
import com.example.practice.UserProductService.exception.UserNotFoundException;
import com.example.practice.UserProductService.model.Product;
import com.example.practice.UserProductService.model.User;
import com.example.practice.UserProductService.proxy.UserProxy;
import com.example.practice.UserProductService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private UserProxy userProxy;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,UserProxy userProxy) {

        this.userRepository = userRepository;
        this.userProxy = userProxy;
    }

    @Override
    public User addUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findById(user.getUserId()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        User addedUser = userRepository.save(user);
        if (!(addedUser.getUserId() == 0)){
            ResponseEntity re = userProxy.saveUser(user);
            System.out.println(re.getBody());
        }
        return addedUser;
    }

    @Override
    public User addProductForUser(int userId, Product product) throws UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user = userRepository.findByUserId(userId);
        if (user.getProductList() == null){
            user.setProductList(Arrays.asList(product));
        }else {
            List<Product> products = user.getProductList();
            products.add(product);
            user.setProductList(products);
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteProductForUser(int userId, int productId) throws UserNotFoundException, ProductNotFoundException {
        boolean flag = false;
        if (userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        List<Product> products = user.getProductList();
        flag = products.removeIf(x -> x.getProductId() == productId);
        if (!flag){
            throw new ProductNotFoundException();
        }
        user.setProductList(products);
        return userRepository.save(user);
    }

    @Override
    public List<Product> getAllProductForUser(int userId) throws UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        return userRepository.findById(userId).get().getProductList();
    }
}
