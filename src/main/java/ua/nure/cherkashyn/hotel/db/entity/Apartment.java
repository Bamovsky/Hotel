package ua.nure.cherkashyn.hotel.db.entity;

import java.math.BigDecimal;


/**
 * Apartment entity
 *
 * @author Vladimir Cherkashyn
 */
public class Apartment extends Entity {

    private static final long serialVersionUID = -2913351768655004854L;

    private String name;
    private String shortDescription;
    private String description;
    private BigDecimal price;
    private int quantityOfRooms;
    private String img;
    private long classId;
    private long statusId;
    private String statusI18N;
    private String classI18N;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantityOfRooms() {
        return quantityOfRooms;
    }

    public void setQuantityOfRooms(int quantityOfRooms) {
        this.quantityOfRooms = quantityOfRooms;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String getStatusInString() {
        ApartmentStatus status = ApartmentStatus.getStatus(this);
        return status.getName();
    }

    public String getClassInString() {
        ApartmentClass apartmentClass = ApartmentClass.getClass(this);
        return apartmentClass.getName();
    }

    public String getStatusI18N() {
        return statusI18N;
    }

    public void setStatusI18N(String statusI18N) {
        this.statusI18N = statusI18N;
    }

    public String getClassI18N() {
        return classI18N;
    }

    public void setClassI18N(String classI18N) {
        this.classI18N = classI18N;
    }
}
