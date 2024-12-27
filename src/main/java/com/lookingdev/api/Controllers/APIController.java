package com.lookingdev.api.Controllers;

import com.lookingdev.api.Domain.Enums.QueueStatus;
import com.lookingdev.api.Domain.Models.DeveloperDTOModel;
import com.lookingdev.api.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {

    private final MessageService service;

    @Autowired
    public APIController(MessageService service){
        this.service = service;
    }

    @PostMapping("/init")
    public ResponseEntity<String> initDataBase(){
       String result =  service.initDataBase();
       return result.equals(QueueStatus.DONE.toString()) ?
               ResponseEntity.ok("Init was done") :
               ResponseEntity.badRequest().body("Error with init");
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        return null;
    }
    @GetMapping("/getGitHub")
    public ResponseEntity<String> getGitHubUsers(){
        List<DeveloperDTOModel> result = service.getGitUsers();
        return !result.isEmpty() ? ResponseEntity.ok(result.toString())
                : ResponseEntity.badRequest().body("Ops... Something was wrong");
    }
    @GetMapping("/getStackOverflow")
    public ResponseEntity<?> getStackOverflowUsers(){
        return null;
    }

}
