package com.kitri.meto.m.metour;


public class Member {
	private int mem_num;
	private String id;
	private String pwd;
	private String name;
	private String birth;
	private String phone;
	private String mem_status;
	private String gender;
	
	public Member() {
	}
	
	public Member(int mem_num, String id, String pwd, String name, String birth, String phone, String mem_status,
			String gender) {
		super();
		this.mem_num = mem_num;
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.birth = birth;
		this.phone = phone;
		this.mem_status = mem_status;
		this.gender = gender;
	}


	public int getMem_num() {
		return mem_num;
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

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMem_status() {
		return mem_status;
	}

	public void setMem_status(String mem_status) {
		this.mem_status = mem_status;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
