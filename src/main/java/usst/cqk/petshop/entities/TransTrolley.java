package usst.cqk.petshop.entities;

public class TransTrolley {
    private Long trolleyId;
    private Long merchandiseId;
    private String sellerNickName;
    private Integer number;
    private String merchandiseName;
    private Integer tag;
    private String phoneNumber;
    private Float price;

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSellerNickName() {
        return sellerNickName;
    }

    public void setSellerNickName(String sellerNickName) {
        this.sellerNickName = sellerNickName;
    }

    public Long getTrolleyId() {
        return trolleyId;
    }

    public void setTrolleyId(Long trolleyId) {
        this.trolleyId = trolleyId;
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

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }
}
