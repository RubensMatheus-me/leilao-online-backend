package com.example.leilao.backend.controller;

import com.example.leilao.backend.model.Profile;
import com.example.leilao.backend.service.ProfileService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public Profile create(@RequestBody Profile profile) {
        return profileService.create(profile);
    }
    @PutMapping
    public Profile update(@RequestBody Profile profile) {
        return profileService.update(profile);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        profileService.delete(id);
    }

    @GetMapping
    public List<Profile> findAll() {
        return profileService.findAll();
    }
}
