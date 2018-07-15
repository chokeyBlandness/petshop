package usst.cqk.petshop.entities.repositories;

import org.springframework.data.repository.CrudRepository;
import usst.cqk.petshop.entities.Indent;

import java.util.List;

public interface IndentRepository extends CrudRepository<Indent, Long> {
    List<Indent> findIndentsByAccountId(Long accountId);

    List<Indent> findIndentsBySellerIdAndStatus(Long sellerId, Integer status);

    Indent findIndentByIndentId(Long indentId);

    List<Indent> findIndentsByMerchandiseIdAndStatus(Long merchandiseId, Integer status);
}
