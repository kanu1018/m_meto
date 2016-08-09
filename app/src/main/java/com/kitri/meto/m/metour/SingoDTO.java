package com.kitri.meto.m.metour;

/**
 * Created by Administrator on 2016-08-08.
 */
public class SingoDTO {
    private int singo_num;
    private String singo_kind;
    private String singo_content;
    private String singo_state;
    private int content_num;
    private int singo_mem;

    public SingoDTO(){}
    public SingoDTO(int singo_num, String singo_kind, String singo_content, String singo_state, int content_num,
                 int singo_mem) {
        super();
        this.singo_num = singo_num;
        this.singo_kind = singo_kind;
        this.singo_content = singo_content;
        this.singo_state = singo_state;
        this.content_num = content_num;
        this.singo_mem = singo_mem;
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
    public int getContent_num() {
        return content_num;
    }
    public void setContent_num(int content_num) {
        this.content_num = content_num;
    }
    public int getSingo_mem() {
        return singo_mem;
    }
    public void setSingo_mem(int singo_mem) {
        this.singo_mem = singo_mem;
    }
}
