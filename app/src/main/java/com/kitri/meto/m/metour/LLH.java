package com.kitri.meto.m.metour;

/**
 * Created by Administrator on 2016-08-17.
 */
public class LLH {
    private String llh_x;
    private String llh_y;

    public LLH(String llh_x,String llh_y){
        super();
        this.llh_x = llh_x;
        this.llh_y = llh_y;
    }

    public String getLlh_x(){
        return this.llh_x;
    }

    public String getLlh_y(){
        return this.llh_y;
    }
}
