package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    private final PetRepository petRepository;
    @Autowired
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet findPetById(long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();
            return pet;
        } else {
            throw new PetNotFoundException("Pet Not Found");
        }
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    public Pet savePet(Pet pet){
        Pet savedPet = petRepository.save(pet);
        Customer owner = savedPet.getCustomer();
        List<Pet> petsOwned = owner.getPets();
        if (petsOwned == null) {
            petsOwned = new ArrayList<>();
        }
        if (!petsOwned.contains(savedPet)) {
            petsOwned.add(savedPet);
            owner.setPets(petsOwned);
        }
        return savedPet;
    }

}
