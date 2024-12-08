package me.fliqq.claimyourday;

import java.util.Date;

public class PlayerData {
    private int lastRewardId;
    private Date lastClaimTime;
    public PlayerData(int lastRewardId, Date lastClaimTime){
        this.lastClaimTime=lastClaimTime;
        this.lastRewardId=lastRewardId;
    }

    public int getLastRewardId() { return lastRewardId; }
    public void setLastRewardId(int lastRewardId) { this.lastRewardId = lastRewardId; }
    public Date getLastClaimTime() { return lastClaimTime; }
    public void setLastClaimTime(Date lastClaimTime) { this.lastClaimTime = lastClaimTime; }
}
