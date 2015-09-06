package com.wang.mobilesafe.domain;

public class ContactInfo {

	private String name;
	private String phono;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhono() {
		return phono;
	}

	public void setPhono(String phono) {
		this.phono = phono;
	}

	//为了测试
	@Override
	public String toString() {
		return "ContactInfo [name=" + name + ", phono=" + phono + "]";
	}
}
