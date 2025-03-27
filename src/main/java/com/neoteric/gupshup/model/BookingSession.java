package com.neoteric.gupshup.model;

public class BookingSession {

    private String city;
    private String hotel;
    private String room;
    private String checkInDate;
    private String checkOutDate;
    private Boolean reviewConfirmed;

    private boolean editingField; // Tracks the field being edited


    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

    public Boolean getReviewConfirmed() {
        return reviewConfirmed;
    }

    public void setReviewConfirmed(Boolean reviewConfirmed) {
        this.reviewConfirmed = reviewConfirmed;
    }

    public boolean isEditingField() {
        return editingField;
    }

    public void setEditingField(boolean editingField) {
        this.editingField = editingField;
    }
}
