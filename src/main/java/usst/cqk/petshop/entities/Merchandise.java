package usst.cqk.petshop.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Merchandise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long merchandiseId;
    private Long accountId;
    private String merchandiseName;
    private Integer tag;
    private Float price;
    private Boolean pointMerchandise=false;

    public Boolean getPointMerchandise() {
        return pointMerchandise;
    }

    public void setPointMerchandise(Boolean pointMerchandise) {
        this.pointMerchandise = pointMerchandise;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Long getMerchandiseId() {
        return merchandiseId;
    }

    public void setMerchandiseId(Long merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
