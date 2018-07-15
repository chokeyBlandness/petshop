package usst.cqk.petshop.entities.repositories;

import org.springframework.data.repository.CrudRepository;
import usst.cqk.petshop.entities.Account;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account,Long> {
    Account findAccountByPhoneNumber(String phoneNumber);

    List<Account> findAccountsByStatus(Integer status);

    Account findAccountByAccountId(Long accountId);
}
