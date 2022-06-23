package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
//@Transactional
public class PetController {

    private final PetService petService;
    private final CustomerService customerService;


    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @GetMapping
    public List<PetDTO> getAllPets() {
        return convertListPetToListPetDTO(petService.findAllPets());
    }

    public Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet  = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        pet.setCustomer(customerService.findCustomerById(petDTO.getOwnerId()));
        return pet;
    }

    public PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

    public List<PetDTO> convertListPetToListPetDTO(List<Pet> pets) {
        List<PetDTO> result = new ArrayList<>();
        for (Pet pet: pets) {
            result.add(convertPetToPetDTO(pet));
        }
        return result;
    }
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return convertPetToPetDTO(petService.savePet(convertPetDTOToPet(petDTO)));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDTO(petService.findPetById(petId));
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Customer owner = customerService.findCustomerById(ownerId);
        return convertListPetToListPetDTO(owner.getPets());
    }
}
