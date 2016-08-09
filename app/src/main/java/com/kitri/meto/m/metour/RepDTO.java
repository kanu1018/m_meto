package com.kitri.meto.m.metour;

/**
 * Created by Administrator on 2016-08-08.
 */
public class RepDTO {
    private int rep_num;
    private String rep_content;
    private int rep_writer;
    private int share_num;

    public RepDTO(){}
    public RepDTO(int rep_num, String rep_content, int rep_writer, int share_num) {
        super();
        this.rep_num = rep_num;
        this.rep_content = rep_content;
        this.rep_writer = rep_writer;
        this.share_num = share_num;
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
    public int getShare_num() {
        return share_num;
    }
    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }
}

