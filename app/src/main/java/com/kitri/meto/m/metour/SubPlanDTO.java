package com.kitri.meto.m.metour;

/**
 * Created by Administrator on 2016-08-08.
 */
public class SubPlanDTO {
    private int sub_num;
    private String sub_title;
    private String start_time;
    private String end_time;
    private String place;
    private String llh_x;
    private String llh_y;
    private String mission;
    private String memo;
    private String mission_yn;
    private String photo;
    private int main_num;

    public SubPlanDTO() {
        super();
    }

    public SubPlanDTO(int sub_num, String sub_title, String start_time, String end_time, String place, String llh_x,
                   String llh_y, String mission, String memo, String mission_yn, String photo, int main_num) {
        super();
        this.sub_num = sub_num;
        this.sub_title = sub_title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.place = place;
        this.llh_x = llh_x;
        this.llh_y = llh_y;
        this.mission = mission;
        this.memo = memo;
        this.mission_yn = mission_yn;
        this.photo = photo;
        this.main_num = main_num;
    }

    public int getSub_num() {
        return sub_num;
    }

    public void setSub_num(int sub_num) {
        this.sub_num = sub_num;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLlh_x() {
        return llh_x;
    }

    public void setLlh_x(String llh_x) {
        this.llh_x = llh_x;
    }

    public String getLlh_y() {
        return llh_y;
    }

    public void setLlh_y(String llh_y) {
        this.llh_y = llh_y;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMission_yn() {
        return mission_yn;
    }

    public void setMission_yn(String mission_yn) {
        this.mission_yn = mission_yn;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getMain_num() {
        return main_num;
    }

    public void setMain_num(int main_num) {
        this.main_num = main_num;
    }

}
