package com.sbadc.survivalera.models;

public class ModelTournament {

    String uid,phone,email,
            tourID,
            tourImage,
            tourName,tourDate,tourTime,
            tourWinningPrize,tourPerKillPrize,tourEntryFee,
            tourVersion,tourType,tourMap,
            tourCreatedTime,
            tourVerified,
            totalPlayer,
            extraAdvice,
            playerJoined,
            perfectTime;

    public ModelTournament() {
    }

    public ModelTournament(String uid, String phone, String email, String tourID, String tourImage, String tourName, String tourDate, String tourTime, String tourWinningPrize, String tourPerKillPrize, String tourEntryFee, String tourVersion, String tourType, String tourMap, String tourCreatedTime, String tourVerified, String totalPlayer, String extraAdvice, String playerJoined, String perfectTime) {
        this.uid = uid;
        this.phone = phone;
        this.email = email;
        this.tourID = tourID;
        this.tourImage = tourImage;
        this.tourName = tourName;
        this.tourDate = tourDate;
        this.tourTime = tourTime;
        this.tourWinningPrize = tourWinningPrize;
        this.tourPerKillPrize = tourPerKillPrize;
        this.tourEntryFee = tourEntryFee;
        this.tourVersion = tourVersion;
        this.tourType = tourType;
        this.tourMap = tourMap;
        this.tourCreatedTime = tourCreatedTime;
        this.tourVerified = tourVerified;
        this.totalPlayer = totalPlayer;
        this.extraAdvice = extraAdvice;
        this.playerJoined = playerJoined;
        this.perfectTime = perfectTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTourID() {
        return tourID;
    }

    public void setTourID(String tourID) {
        this.tourID = tourID;
    }

    public String getTourImage() {
        return tourImage;
    }

    public void setTourImage(String tourImage) {
        this.tourImage = tourImage;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourDate() {
        return tourDate;
    }

    public void setTourDate(String tourDate) {
        this.tourDate = tourDate;
    }

    public String getTourTime() {
        return tourTime;
    }

    public void setTourTime(String tourTime) {
        this.tourTime = tourTime;
    }

    public String getTourWinningPrize() {
        return tourWinningPrize;
    }

    public void setTourWinningPrize(String tourWinningPrize) {
        this.tourWinningPrize = tourWinningPrize;
    }

    public String getTourPerKillPrize() {
        return tourPerKillPrize;
    }

    public void setTourPerKillPrize(String tourPerKillPrize) {
        this.tourPerKillPrize = tourPerKillPrize;
    }

    public String getTourEntryFee() {
        return tourEntryFee;
    }

    public void setTourEntryFee(String tourEntryFee) {
        this.tourEntryFee = tourEntryFee;
    }

    public String getTourVersion() {
        return tourVersion;
    }

    public void setTourVersion(String tourVersion) {
        this.tourVersion = tourVersion;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public String getTourMap() {
        return tourMap;
    }

    public void setTourMap(String tourMap) {
        this.tourMap = tourMap;
    }

    public String getTourCreatedTime() {
        return tourCreatedTime;
    }

    public void setTourCreatedTime(String tourCreatedTime) {
        this.tourCreatedTime = tourCreatedTime;
    }

    public String getTourVerified() {
        return tourVerified;
    }

    public void setTourVerified(String tourVerified) {
        this.tourVerified = tourVerified;
    }

    public String getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(String totalPlayer) {
        this.totalPlayer = totalPlayer;
    }

    public String getExtraAdvice() {
        return extraAdvice;
    }

    public void setExtraAdvice(String extraAdvice) {
        this.extraAdvice = extraAdvice;
    }

    public String getPlayerJoined() {
        return playerJoined;
    }

    public void setPlayerJoined(String playerJoined) {
        this.playerJoined = playerJoined;
    }

    public String getPerfectTime() {
        return perfectTime;
    }

    public void setPerfectTime(String perfectTime) {
        this.perfectTime = perfectTime;
    }
}
