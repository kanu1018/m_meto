package com.kitri.meto.m.metour;

import java.io.Serializable;

public class ScheduleDTO implements Serializable {
	private int main_num;
	private String main_title;
	private int main_writer;
	private String main_date;
	private int point_num;
	private int year;
	private int month;
	private int day;
	
	public ScheduleDTO() {}
	public ScheduleDTO(int main_num, String main_title, int main_writer, String main_date, int point_num, int year,
			int month, int day) {
		super();
		this.main_num = main_num;
		this.main_title = main_title;
		this.main_writer = main_writer;
		this.main_date = main_date;
		this.point_num = point_num;
		this.year = year;
		this.month = month;
		this.day = day;
	}



	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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
	public int getPoint_num() {
		return point_num;
	}
	public void setPoint_num(int point_num) {
		this.point_num = point_num;
	}
	
	
}
