package usst.cqk.petshop.entities.repositories;

import org.springframework.data.repository.CrudRepository;
import usst.cqk.petshop.entities.Merchandise;

import java.util.List;

public interface MerchandiseRepository extends CrudRepository<Merchandise,Long> {
    List<Merchandise> findMerchandisesByAccountId(Long accountdId);

    Merchandise findMerchandiseByMerchandiseId(Long merchandiseId);

    void deleteMerchandiseByMerchandiseId(Long merchandiseId);

    List<Merchandise> findMerchandisesByPointMerchandise(Boolean pointMerchandise);
}
