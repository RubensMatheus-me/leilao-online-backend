package com.example.leilao.backend.service;

import com.example.leilao.backend.model.Profile;
import com.example.leilao.backend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile create(Profile profile) {//save
        return profileRepository.save(profile);
    }
    public Profile update(Profile profile) {
        Profile profileSaved = profileRepository.findById(profile.getId()).orElseThrow(() -> new NoSuchElementException("Objeto não encontrado"));
        profileSaved.setName(profile.getName());
        return profileRepository.save(profileSaved);
    }
    public void delete(long id) {
        Profile profileSaved = profileRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Objeto nâo encontrado"));
        profileRepository.delete(profileSaved);
    }
    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

}

