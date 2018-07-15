package usst.cqk.petshop.entities;

public class TransMerchandise {
    private Long merchandiseId;
    private Long accountId;
    private String phoneNumber;
    private String sellerNickName;
    private String merchandiseName;
    private Integer tag;
    private Float price;
    private Boolean pointMerchandise;

    public Boolean getPointMerchandise() {
        return pointMerchandise;
    }

    public void setPointMerchandise(Boolean pointMerchandise) {
        this.pointMerchandise = pointMerchandise;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getMerchandiseId() {
        return merchandiseId;
    }

    public void setMerchandiseId(Long merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public String getSellerNickName() {
        return sellerNickName;
    }

    public void setSellerNickName(String sellerNickName) {
        this.sellerNickName = sellerNickName;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
