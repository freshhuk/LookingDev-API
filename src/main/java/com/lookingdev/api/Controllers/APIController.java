package com.lookingdev.api.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        return null;
    }
    @GetMapping("/getGitHub")
    public ResponseEntity<?> getGitHubUsers(){
        return null;
    }
    @GetMapping("/getStackOverflow")
    public ResponseEntity<?> getStackOverflowUsers(){
        return null;
    }

}
