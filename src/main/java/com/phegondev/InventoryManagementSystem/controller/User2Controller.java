//package com.phegondev.InventoryManagementSystem.controller;
//
//import com.phegondev.InventoryManagementSystem.entity.User2;
//import com.phegondev.InventoryManagementSystem.service.impl.User2Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/user2")
//public class User2Controller {
//    @Autowired
//    User2Service user2Service;
//    @GetMapping("/getAll")
//    public List<User2> getAllUser(){
//        List<User2> users2= user2Service.getAllUsers();
//        return users2;
//    }
//    @PostMapping("/create")
//    public User2 createUser(@RequestBody User2 user) {
//        return user2Service.saveUser(user);
//    }
//}
