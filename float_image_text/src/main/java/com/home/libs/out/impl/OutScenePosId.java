package com.home.libs.out.impl;

public class OutScenePosId {

    private static OutScenePosId outScenePosId;
    private String rewardVideoIdForHomeKeyWIFI;
    private String rewardVideoIdForHomeKeyClear;
    private String rewardVideoIdForHomeKeyMobileManager;
    private String rewardVideoIdForNetwork;
    private String rewardVideoIdForPackage;
    private String rewardVideoIdForRecharge;
    private String fullVideoIdForHomeKeyWIFI;
    private String fullVideoIdForHomeKeyClear;
    private String fullVideoIdForHomeKeyMobileManager;
    private String fullVideoIdForNetwork;
    private String fullVideoIdForPackage;
    private String fullVideoIdForRecharge;
    private String nativeId;
    private long KsSceneId;
    private boolean isAutoExecute;
    private boolean popNativeShow;
    private String outPopNativeId;
    private VideoAdType videoIdType = VideoAdType.FULL_VIDEO;

    public static OutScenePosId getInstance() {
        synchronized (OutScenePosId.class) {
            if (outScenePosId == null) {
                outScenePosId = new OutScenePosId();
            }
        }
        return outScenePosId;
    }

    private OutScenePosId() {
    }

    public String getRewardVideoIdForHomeKeyWIFI() {
        return rewardVideoIdForHomeKeyWIFI;
    }

    public void setRewardVideoIdForHomeKeyWIFI(String rewardVideoIdForHomeKeyWIFI) {
        this.rewardVideoIdForHomeKeyWIFI = rewardVideoIdForHomeKeyWIFI;
    }

    public String getRewardVideoIdForHomeKeyClear() {
        return rewardVideoIdForHomeKeyClear;
    }

    public void setRewardVideoIdForHomeKeyClear(String rewardVideoIdForHomeKeyClear) {
        this.rewardVideoIdForHomeKeyClear = rewardVideoIdForHomeKeyClear;
    }

    public String getRewardVideoIdForHomeKeyMobileManager() {
        return rewardVideoIdForHomeKeyMobileManager;
    }

    public void setRewardVideoIdForHomeKeyMobileManager(String rewardVideoIdForHomeKeyMobileManager) {
        this.rewardVideoIdForHomeKeyMobileManager = rewardVideoIdForHomeKeyMobileManager;
    }

    public String getRewardVideoIdForNetwork() {
        return rewardVideoIdForNetwork;
    }

    public void setRewardVideoIdForNetwork(String rewardVideoIdForNetwork) {
        this.rewardVideoIdForNetwork = rewardVideoIdForNetwork;
    }

    public String getRewardVideoIdForPackage() {
        return rewardVideoIdForPackage;
    }

    public void setRewardVideoIdForPackage(String rewardVideoIdForPackage) {
        this.rewardVideoIdForPackage = rewardVideoIdForPackage;
    }

    public String getFullVideoIdForHomeKeyWIFI() {
        return fullVideoIdForHomeKeyWIFI;
    }

    public void setFullVideoIdForHomeKeyWIFI(String fullVideoIdForHomeKeyWIFI) {
        this.fullVideoIdForHomeKeyWIFI = fullVideoIdForHomeKeyWIFI;
    }

    public String getFullVideoIdForHomeKeyClear() {
        return fullVideoIdForHomeKeyClear;
    }

    public void setFullVideoIdForHomeKeyClear(String fullVideoIdForHomeKeyClear) {
        this.fullVideoIdForHomeKeyClear = fullVideoIdForHomeKeyClear;
    }

    public String getFullVideoIdForHomeKeyMobileManager() {
        return fullVideoIdForHomeKeyMobileManager;
    }

    public void setFullVideoIdForHomeKeyMobileManager(String fullVideoIdForHomeKeyMobileManager) {
        this.fullVideoIdForHomeKeyMobileManager = fullVideoIdForHomeKeyMobileManager;
    }

    public String getFullVideoIdForNetwork() {
        return fullVideoIdForNetwork;
    }

    public void setFullVideoIdForNetwork(String fullVideoIdForNetwork) {
        this.fullVideoIdForNetwork = fullVideoIdForNetwork;
    }

    public String getFullVideoIdForPackage() {
        return fullVideoIdForPackage;
    }

    public void setFullVideoIdForPackage(String fullVideoIdForPackage) {
        this.fullVideoIdForPackage = fullVideoIdForPackage;
    }

    public String getRewardVideoIdForRecharge() {
        return rewardVideoIdForRecharge;
    }

    public void setRewardVideoIdForRecharge(String rewardVideoIdForRecharge) {
        this.rewardVideoIdForRecharge = rewardVideoIdForRecharge;
    }

    public String getFullVideoIdForRecharge() {
        return fullVideoIdForRecharge;
    }

    public void setFullVideoIdForRecharge(String fullVideoIdForRecharge) {
        this.fullVideoIdForRecharge = fullVideoIdForRecharge;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public VideoAdType getVideoIdType() {
        return videoIdType;
    }

    public void setVideoIdType(VideoAdType videoIdType) {
        this.videoIdType = videoIdType;
    }

    public long getKsSceneId() {
        return KsSceneId;
    }

    public void setKsSceneId(long ksSceneId) {
        KsSceneId = ksSceneId;
    }

    public boolean isAutoExecute() {
        return isAutoExecute;
    }

    public void setAutoExecute(boolean autoExecute) {
        isAutoExecute = autoExecute;
    }

    public boolean isPopNativeShow() {
        return popNativeShow;
    }

    public void setPopNativeShow(boolean popNativeShow) {
        this.popNativeShow = popNativeShow;
    }

    public String getOutPopNativeId() {
        return outPopNativeId;
    }

    public void setOutPopNativeId(String outPopNativeId) {
        this.outPopNativeId = outPopNativeId;
    }
}
