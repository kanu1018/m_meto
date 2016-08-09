package com.kitri.meto.m.metour;

/**
 * Created by Administrator on 2016-08-08.
 */
public class MetooDTO {
    private int metoo_num;
    private int share_num;
    private int mem_num;
    private String metoo_yn;

    public MetooDTO(){}
    public MetooDTO(int metoo_num, int share_num, int mem_num, String metoo_yn) {
        super();
        this.metoo_num = metoo_num;
        this.share_num = share_num;
        this.mem_num = mem_num;
        this.metoo_yn = metoo_yn;
    }
    public int getMetoo_num() {
        return metoo_num;
    }
    public void setMetoo_num(int metoo_num) {
        this.metoo_num = metoo_num;
    }
    public int getShare_num() {
        return share_num;
    }
    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }
    public int getMem_num() {
        return mem_num;
    }
    public void setMem_num(int mem_num) {
        this.mem_num = mem_num;
    }
    public String getMetoo_yn() {
        return metoo_yn;
    }
    public void setMetoo_yn(String metoo_yn) {
        this.metoo_yn = metoo_yn;
    }
}
