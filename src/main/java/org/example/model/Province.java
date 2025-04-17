package main.java.org.example.model;

public class Province {
    private int idProvince;
    private String nameProvince;

    // Constructor mặc định
    public Province() {
    }

    // Constructor với tham số
    public Province(int idProvince, String nameProvince) {
        this.idProvince = idProvince;
        this.nameProvince = nameProvince;
    }

    // Getter và Setter cho idProvince
    public int getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(int idProvince) {
        this.idProvince = idProvince;
    }

    // Getter và Setter cho nameProvince
    public String getNameProvince() {
        return nameProvince;
    }

    public void setNameProvince(String nameProvince) {
        this.nameProvince = nameProvince;
    }
}