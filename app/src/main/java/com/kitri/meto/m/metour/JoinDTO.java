package com.kitri.meto.m.metour;

/**
 * Created by Administrator on 2016-08-10.
 */
public class JoinDTO {
    //member
    private int mem_num;
    private String id;
    private String pwd;
    private String name;
    private String phone;
    private String gender;
    private String birth;
    private String mem_status;

    //main_plan
    private int main_num;
    private String main_title;
    private int main_writer;
    private String main_date;

    //sub_plan
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

    //share_plan
    private int share_num;
    private int writer;
    private String location;
    private String content;
    private int metoo;
    private String share_title;

    //rep
    private int rep_num;
    private String rep_content;
    private int rep_writer;

    //point
    private int point_num;
    private int point;

    //singo
    private int singo_num;
    private String singo_kind;
    private String singo_content;
    private String singo_state;
    private int content_num;
    private int singo_mem;


    //metoo
    private int metoo_num;
    private String metoo_yn;

    //constructor
    public JoinDTO(){}
    public JoinDTO(int mem_num, String id, String pwd, String name, String phone, String gender, String birth,
                   String mem_status, int main_num, String main_title, int main_writer, String main_date, int sub_num,
                   String sub_title, String start_time, String end_time, String place, String llh_x, String llh_y,
                   String mission, String memo, String mission_yn, String photo, int share_num, int writer, String location,
                   String content, int metoo, String share_title, int rep_num, String rep_content, int rep_writer,
                   int point_num, int point, int singo_num, String singo_kind, String singo_content, String singo_state,
                   int content_num, int singo_mem, int metoo_num, String metoo_yn) {
        super();
        this.mem_num = mem_num;
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birth = birth;
        this.mem_status = mem_status;
        this.main_num = main_num;
        this.main_title = main_title;
        this.main_writer = main_writer;
        this.main_date = main_date;
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
        this.share_num = share_num;
        this.writer = writer;
        this.location = location;
        this.content = content;
        this.metoo = metoo;
        this.share_title = share_title;
        this.rep_num = rep_num;
        this.rep_content = rep_content;
        this.rep_writer = rep_writer;
        this.point_num = point_num;
        this.point = point;
        this.singo_num = singo_num;
        this.singo_kind = singo_kind;
        this.singo_content = singo_content;
        this.singo_state = singo_state;
        this.content_num = content_num;
        this.singo_mem = singo_mem;
        this.metoo_num = metoo_num;
        this.metoo_yn = metoo_yn;
    }


    // setter, getter
    public int getMem_num() {
        return mem_num;
    }
    public String getShare_title() {
        return share_title;
    }
    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }
    public void setMem_num(int mem_num) {
        this.mem_num = mem_num;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getBirth() {
        return birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public String getMem_status() {
        return mem_status;
    }
    public void setMem_status(String mem_status) {
        this.mem_status = mem_status;
    }
    public int getMain_num() {
        return main_num;
    }
    public void setMain_num(int main_num) {
        this.main_num = main_num;
    }
    public String getMain_title() {
        return main_title;
    }
    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }
    public int getMain_writer() {
        return main_writer;
    }
    public void setMain_writer(int main_writer) {
        this.main_writer = main_writer;
    }
    public String getMain_date() {
        return main_date;
    }
    public void setMain_date(String main_date) {
        this.main_date = main_date;
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
    public int getShare_num() {
        return share_num;
    }
    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }
    public int getWriter() {
        return writer;
    }
    public void setWriter(int writer) {
        this.writer = writer;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getMetoo() {
        return metoo;
    }
    public void setMetoo(int metoo) {
        this.metoo = metoo;
    }
    public int getRep_num() {
        return rep_num;
    }
    public void setRep_num(int rep_num) {
        this.rep_num = rep_num;
    }
    public String getRep_content() {
        return rep_content;
    }
    public void setRep_content(String rep_content) {
        this.rep_content = rep_content;
    }
    public int getRep_writer() {
        return rep_writer;
    }
    public void setRep_writer(int rep_writer) {
        this.rep_writer = rep_writer;
    }
    public int getPoint_num() {
        return point_num;
    }
    public void setPoint_num(int point_num) {
        this.point_num = point_num;
    }
    public int getPoint() {
        return point;
    }
    public void setPoint(int point) {
        this.point = point;
    }
    public int getSingo_num() {
        return singo_num;
    }
    public void setSingo_num(int singo_num) {
        this.singo_num = singo_num;
    }
    public String getSingo_kind() {
        return singo_kind;
    }
    public void setSingo_kind(String singo_kind) {
        this.singo_kind = singo_kind;
    }
    public String getSingo_content() {
        return singo_content;
    }
    public void setSingo_content(String singo_content) {
        this.singo_content = singo_content;
    }
    public String getSingo_state() {
        return singo_state;
    }
    public void setSingo_state(String singo_state) {
        this.singo_state = singo_state;
    }
    public int getSingo_mem() {
        return singo_mem;
    }
    public void setSingo_mem(int singo_mem) {
        this.singo_mem = singo_mem;
    }
    public int getContent_num() {
        return content_num;
    }
    public void setContent_num(int content_num) {
        this.content_num = content_num;
    }
    public int getMetoo_num() {
        return metoo_num;
    }
    public void setMetoo_num(int metoo_num) {
        this.metoo_num = metoo_num;
    }
    public String getMetoo_yn() {
        return metoo_yn;
    }
    public void setMetoo_yn(String metoo_yn) {
        this.metoo_yn = metoo_yn;
    }
}
