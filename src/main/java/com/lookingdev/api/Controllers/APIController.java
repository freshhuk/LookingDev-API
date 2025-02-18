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
        String result = service.initGitDatabase();
        return result.equals(QueueStatus.DONE.toString()) ?
                ResponseEntity.ok("Init was started") :
                ResponseEntity.badRequest().body("Error with init");
    }
    @GetMapping("/initStack")
    public ResponseEntity<String> initStackDataBase() {
        String result = service.initStackDatabase();
        return result.equals(QueueStatus.DONE.toString()) ?
                ResponseEntity.ok("Init was started") :
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


    /* End-points for getting  init status */

    /**
     * Method returns initialization status of StackOverflow microservice
     * @return initialization status
     */
    @GetMapping("/getStatusStack")
    public ResponseEntity<String> getInitStatusStack(){
        String status = service.getInitStatusStackOverflow();
        return !status.equals(QueueStatus.BAD.toString()) ? ResponseEntity.ok(status)
                : ResponseEntity.badRequest().body("Error with getting status");
    }

    /**
     * Method returns initialization status of GitHub microservice
     * @return initialization status
     */
    @GetMapping("/getStatusGit")
    public ResponseEntity<String> getInitStatusGit(){
        String status = service.getInitStatusGitHub();
        return !status.equals(QueueStatus.BAD.toString()) ? ResponseEntity.ok(status)
                : ResponseEntity.badRequest().body("Error with getting status");
    }

}
