package usst.cqk.petshop.entities.repositories;

import org.springframework.data.repository.CrudRepository;
import usst.cqk.petshop.entities.Trolley;

import java.util.List;

public interface TrolleyRepository extends CrudRepository<Trolley, Long> {
    Trolley findTrolleyByAccountIdAndMerchandiseId(Long accountId, Long merchandiseId);

    List<Trolley> findTrolleysByAccountId(Long accountId);

    void deleteTrolleysByAccountId(Long accountId);

    void deleteTrolleysByMerchandiseId(Long merchandiseId);
}
