package com.tfmapp.model.repository.data;

import java.util.List;

import com.tfmapp.model.common.Constants;

public class Teacher {

	private Long idUser;
	
	private String username;
	
	private List<Long> listIdGroup;
	
	private List<String> listSubject;
	
	private static final String rol = Constants.TEACHER_ROL;

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Long> getListIdGroup() {
		return listIdGroup;
	}

	public void setListIdGroup(List<Long> listIdGroup) {
		this.listIdGroup = listIdGroup;
	}

	public List<String> getListSubject() {
		return listSubject;
	}

	public void setListSubject(List<String> listSubject) {
		this.listSubject = listSubject;
	}

	public static String getRol() {
		return rol;
	}
	
	
}
