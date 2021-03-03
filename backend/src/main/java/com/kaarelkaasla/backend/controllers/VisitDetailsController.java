package com.kaarelkaasla.backend.controllers;

import com.kaarelkaasla.backend.entities.User;
import com.kaarelkaasla.backend.pojos.UserEnabled;
import com.kaarelkaasla.backend.pojos.UserRoles;
import com.kaarelkaasla.backend.entities.VisitDetails;
import com.kaarelkaasla.backend.messages.ResponseMessage;
import com.kaarelkaasla.backend.repositories.UserRepository;
import com.kaarelkaasla.backend.services.UserDetailsServiceImpl;
import com.kaarelkaasla.backend.services.VisitDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The methods for handling the requests related to search, upload, and user management
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class VisitDetailsController {

    private final VisitDetailsService visitDetailsService;

    private final UserRepository userRepository;

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public VisitDetailsController(VisitDetailsService visitDetailsService, UserRepository userRepository,
                                  UserDetailsServiceImpl userDetailsService) {
        this.visitDetailsService = visitDetailsService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('UPLOAD') or hasRole('ROOT')")
    public ResponseEntity<ResponseMessage>
    uploadFile(@RequestParam("file") MultipartFile file) {
        String message;

        try {
            visitDetailsService.save(file);
            message = "Successfully uploaded: " +
                    file.getOriginalFilename() + "!";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " +
                    file.getOriginalFilename() + "!";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('SEARCH') or hasRole('ROOT')")
    public ResponseEntity<List<VisitDetails>> getAllEntries(@RequestParam("search") String search) {
        try {
            List<VisitDetails> entries;
            if (search.endsWith("*")) { //the case where a partial match is searched
                entries = visitDetailsService.getEntriesStartingWith(
                        search.substring(0, search.length() - 1));
            } else {
                entries = visitDetailsService.getExactEntries(search);
            }

            if (entries.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(entries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usersearch")
    @PreAuthorize("hasRole('MANAGEMENT') or hasRole('ROOT')")
    public ResponseEntity<?> getUserInfo(@RequestParam("search") String search) {
        try {
            final Optional<User> byUsername = userRepository.findByUsername(search);
            if (byUsername.isPresent()) {
                return new ResponseEntity<>(byUsername, HttpStatus.OK);
            }
            return new ResponseEntity<>("Username not found!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateroles")
    @PreAuthorize("hasRole('MANAGEMENT') or hasRole('ROOT')")
    public ResponseEntity<ResponseMessage> updateRoles(@RequestBody UserRoles userRoles) {
        try {
            Integer userId = userRepository.getUserIdByUsername(userRoles.getUsername());
            userDetailsService.deleteRoles(userId); //delete previous roles
            userDetailsService.addRoles(userId, userRoles.getRoles()); //save the new roles
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseMessage("The user roles successfully modified!"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage("Request failed!"));
        }
    }

    @PostMapping("/setenabled")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<ResponseMessage> setEnabled(@RequestBody UserEnabled userEnabled) {
        try {
            Integer userId = userRepository.getUserIdByUsername(userEnabled.getUsername());
            userDetailsService.setEnabled(userId, userEnabled.getEnabled());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseMessage("The user successfully modified!"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage("Request failed!"));
        }
    }
}
