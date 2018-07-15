package usst.cqk.petshop.entities.repositories;

import org.springframework.data.repository.CrudRepository;
import usst.cqk.petshop.entities.Pet;

import java.util.List;

public interface PetRepository extends CrudRepository<Pet, Long> {
    List<Pet> findPetsByAccountId(Long accountId);
}
