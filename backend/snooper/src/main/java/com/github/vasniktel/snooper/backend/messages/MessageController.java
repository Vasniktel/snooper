package com.github.vasniktel.snooper.backend.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
  private final MessageRepository repository;

  @Autowired
  public MessageController(MessageRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/userMessages")
  public List<MessageDto> getUserMessages(@RequestParam("id") String id) {
    return repository.findAllByOwnerIdOrderByDateDesc(id);
  }

  @GetMapping("/userFeed")
  public List<MessageDto> getUserFeed(@RequestParam("id") String id) {
    return repository.getUserFeed(id);
  }

  @PostMapping("/create")
  public void createMessage(@RequestBody MessageDto message) {
    message.setDate(new Date());
    repository.save(message);
  }

  @PostMapping("/update")
  public void updateMessage(@RequestBody MessageDto message) {
    if (message.getDate() == null) message.setDate(new Date());
    repository.save(message);
  }

  @GetMapping("/search")
  public List<MessageDto> search(@RequestParam("query") String query) {
    return repository.search(Arrays.asList(query.split(" ")));
  }
}
