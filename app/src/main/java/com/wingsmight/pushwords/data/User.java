package com.wingsmight.pushwords.data;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static final String PLATFORM_NAME = "Android";


    private String email;
    private String documentID;
    private Date signUpDate;
    private Date lastOpeningDate;
    private boolean isSubscribed;
    private int overallLearnedWordCount;
    private int weeklyLearnedWordCount;


    public User(String email,
                String documentID,
                Date signUpDate,
                Date lastOpeningDate,
                boolean isSubscribed,
                int overallLearnedWordCount,
                int weeklyLearnedWordCount) {
        this.email = email;
        this.documentID = documentID;
        this.signUpDate = signUpDate;
        this.lastOpeningDate = lastOpeningDate;
        this.isSubscribed = isSubscribed;
        this.overallLearnedWordCount = overallLearnedWordCount;
        this.weeklyLearnedWordCount = weeklyLearnedWordCount;
    }
    public User(String email) {
        this(email,
                "",
                new Date(),
                new Date(),
                false,
                0,
                0);
    }
    public User(QueryDocumentSnapshot data) {
        this(data.getString("email"),
                data.getId(),
                data.getDate("signUpDate"),
                data.getDate("lastOpeningDate"),
                data.getBoolean("isSubscribed"),
                data.getLong("overallLearnedWordCount").intValue(),
                data.getLong("weeklyLearnedWordCount").intValue());
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDocumentID() {
        return documentID;
    }
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
    public Date getSignUpDate() {
        return signUpDate;
    }
    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }
    public String getPlatform() {
        return PLATFORM_NAME;
    }
    public Date getLastOpeningDate() {
        return lastOpeningDate;
    }
    public void setLastOpeningDate(Date lastOpeningDate) {
        this.lastOpeningDate = lastOpeningDate;
    }
    public Boolean getSubscribed() {
        return isSubscribed;
    }
    public void setSubscribed(Boolean subscribed) {
        isSubscribed = subscribed;
    }
    public int getOverallLearnedWordCount() {
        return overallLearnedWordCount;
    }
    public void setOverallLearnedWordCount(int overallLearnedWordCount) {
        this.overallLearnedWordCount = overallLearnedWordCount;
    }
    public int getWeeklyLearnedWordCount() {
        return weeklyLearnedWordCount;
    }
    public void setWeeklyLearnedWordCount(int weeklyLearnedWordCount) {
        this.weeklyLearnedWordCount = weeklyLearnedWordCount;
    }
    public Map<String, Object> getMapData() {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("email", email);
        mapData.put("signUpDate", signUpDate);
        mapData.put("lastOpeningDate", lastOpeningDate);
        mapData.put("isSubscribed", isSubscribed);
        mapData.put("overallLearnedWordCount", overallLearnedWordCount);
        mapData.put("weeklyLearnedWordCount", weeklyLearnedWordCount);
        mapData.put("platform", PLATFORM_NAME);

        return mapData;
    }
}
