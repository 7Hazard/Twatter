package solutions.desati.twatter.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.models.Conversation;
import solutions.desati.twatter.models.Message;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.services.ConversationService;
import solutions.desati.twatter.services.MessageService;

@RestController
@RequestMapping("/conversations")
public class ConversationController {
    @Autowired
    ConversationService conversationService;
    @Autowired
    MessageService messageService;

    @GetMapping
    public ResponseEntity get(@RequestAttribute User user) {
        return new ResponseEntity(Conversation.getView(conversationService.getAll(user)), HttpStatus.OK);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity getMessages(@RequestAttribute User user, @PathVariable Long id) {
        var conv = conversationService.getByUserAndId(user, id);
        if(conv == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(Message.getView(conversationService.getAllMessages(conv)), HttpStatus.OK);
    }

    @Data
    static class SendMessage { private String content; private boolean isImage = false; }
    @PostMapping("/{id}/messages")
    public ResponseEntity sendMessage(@RequestAttribute User user, @PathVariable Long id, @RequestBody SendMessage body) {
        if(body.content.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        var conv = conversationService.getByUserAndId(user, id);
        if(conv == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(messageService.send(conv, user, body.content, body.isImage).getView(), HttpStatus.OK);
    }
}
