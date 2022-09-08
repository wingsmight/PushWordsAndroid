package com.wingsmight.pushwords.data;

import java.util.Date;

public class User {
   public String email;
   public String documentID;
   public Date signUpDate;
   public String platform;
   public Date lastOpeningDate;
   public Boolean isSubscribed;
   public int overallLearnedWordCount;
   public int weeklyLearnedWordCount;


    public User(String email,
                String documentID,
                Date signUpDate,
                String platform,
                Date lastOpeningDate,
                Boolean isSubscribed,
                int overallLearnedWordCount,
                int weeklyLearnedWordCount) {
        this.email = email;
        this.documentID = documentID;
        this.signUpDate = signUpDate;
        this.platform = platform;
        this.lastOpeningDate = lastOpeningDate;
        this.isSubscribed = isSubscribed;
        this.overallLearnedWordCount = overallLearnedWordCount;
        this.weeklyLearnedWordCount = weeklyLearnedWordCount;
    }
}
