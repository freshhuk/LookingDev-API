package com.lookingdev.api.Controllers;

import com.lookingdev.api.Domain.Enums.QueueStatus;
import com.lookingdev.api.Domain.Models.DeveloperDTOModel;
import com.lookingdev.api.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {

    private final MessageService service;

    @Autowired
    public APIController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/initGit")
    public ResponseEntity<String> initGitDataBase() {
        String result = service.initGitDataBase();
        return result.equals(QueueStatus.DONE.toString()) ?
                ResponseEntity.ok("Init was done") :
                ResponseEntity.badRequest().body("Error with init");
    }
    @GetMapping("/initStack")
    public ResponseEntity<String> initStackDataBase() {
        String result = service.initStackFataBase();
        return result.equals(QueueStatus.DONE.toString()) ?
                ResponseEntity.ok("Init was done") :
                ResponseEntity.badRequest().body("Error with init");
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<DeveloperDTOModel> result = service.getAllUsers();
        return result != null ? ResponseEntity.ok(result.toString())
                : ResponseEntity.badRequest().body("Ops... Something was wrong");
    }

    @GetMapping("/getGitHub/{page}")
    public ResponseEntity<String> getGitHubUsers(@PathVariable int page) {
        List<DeveloperDTOModel> result = service.getGitUsers(page);
        return result != null ? ResponseEntity.ok(result.toString())
                : ResponseEntity.badRequest().body("Ops... Something was wrong");
    }

    @GetMapping("/getStackOverflow/{page}")
    public ResponseEntity<?> getStackOverflowUsers(@PathVariable int page) {
        List<DeveloperDTOModel> result = service.getStackOverflowUsers(page);
        return result != null ? ResponseEntity.ok(result.toString())
                : ResponseEntity.badRequest().body("Ops... Something was wrong");
    }

}
