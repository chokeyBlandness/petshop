package usst.cqk.petshop.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trolley {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    private Long trolleyId;
    private Long accountId;
    private Long merchandiseId;
    private Integer number;

    public Long getTrolleyId() {
        return trolleyId;
    }

    public void setTrolleyId(Long trolleyId) {
        this.trolleyId = trolleyId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getMerchandiseId() {
        return merchandiseId;
    }

    public void setMerchandiseId(Long merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
