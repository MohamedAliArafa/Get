package com.zeowls.get.DataModel;

/**
 * Created by nora on 3/23/2016.
 */
public class ShopDataModel implements Comparable<ShopDataModel> {
    int id, ParentCatID;
    String name, description, owner, pictureUrl,latitude,longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getParentCatID() {
        return ParentCatID;
    }

    public void setParentCatID(int parentCatID) {
        ParentCatID = parentCatID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPictureUrl() {
        if (pictureUrl == null){
            return null;
        }else {
            return "http://bubble.zeowls.com/uploads/" + pictureUrl;
        }
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }


    @Override
    public int compareTo(ShopDataModel shop) {
        int id = shop.getId();
        return this.id - id;
    }
}
