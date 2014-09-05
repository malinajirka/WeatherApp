package cz.malinajiri.showcase.weatherapp.entity;


public class DrawerItemEntity {
    String name;
    int imgId;


    public DrawerItemEntity(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }


    public String getName() {
        return name;
    }


    public int getImgId() {
        return imgId;
    }

}
