package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /** 
     * User Registration Endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        Optional<Account> newAccount = accountService.registerUser(account);
        if (newAccount.isPresent()) {
            return ResponseEntity.ok(newAccount.get());
        }
        return ResponseEntity.status(409).build(); // Conflict if username exists or invalid input
    }

    /** 
     * User Login Endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account) {
        return accountService.loginUser(account.getUsername(), account.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build()); // Unauthorized
    }

    /** 
     * Create New Message Endpoint
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Optional<Message> newMessage = messageService.createMessage(message);
        if (newMessage.isPresent()) {
            return ResponseEntity.ok(newMessage.get());
        }
        return ResponseEntity.badRequest().build(); // 400 if validation fails
    }

    /** 
     * Get All Messages Endpoint
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    /** 
     * Get One Message Given Message ID Endpoint
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        return messageService.getMessageById(message_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build()); // Empty response if not found
    }

    /** 
     * Delete a Message Given Message ID Endpoint
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer message_id) {
        boolean deleted = messageService.deleteMessageById(message_id);
        return deleted ? ResponseEntity.ok(1) : ResponseEntity.ok().build(); // Idempotent delete
    }

    /** 
     * Update Message Given Message ID Endpoint
     */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer message_id, @RequestBody Message message) {
        Optional<Message> updatedMessage = messageService.updateMessage(message_id, message.getMessageText());
        return updatedMessage.isPresent() ? ResponseEntity.ok(1) : ResponseEntity.badRequest().build();
    }

    /** 
     * Get All Messages From User Given Account ID Endpoint
     */
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer account_id) {
        return ResponseEntity.ok(messageService.getMessagesByUserId(account_id));
    }
}

