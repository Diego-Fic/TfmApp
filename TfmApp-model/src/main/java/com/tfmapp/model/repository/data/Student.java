/*
 * Project TFM 2019-2020
 * Writed by Diego Otero González
 * 
 */
package com.tfmapp.model.repository.data;

import org.springframework.stereotype.Repository;

import com.tfmapp.model.common.Constants;

/**
 * The Class Student.
 */
@Repository
public class Student {

	/** The id user. */
	private Long idUser;

	/** The username. */
	private String username;

	/** The id group. */
	private Long idGroup;

	/** The subject. */
	private String subject;

	/** The Constant rol. */
	private final String rol = Constants.STUDENT_ROL;

	/**
	 * Gets the rol.
	 *
	 * @return the rol
	 */
	public String getRol() {
		return rol;
	}

	/**
	 * Gets the id user.
	 *
	 * @return the id user
	 */
	public Long getIdUser() {
		return idUser;
	}

	/**
	 * Sets the id user.
	 *
	 * @param idUser the new id user
	 */
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the id group.
	 *
	 * @return the id group
	 */
	public Long getIdGroup() {
		return idGroup;
	}

	/**
	 * Sets the id group.
	 *
	 * @param idGroup the new id group
	 */
	public void setIdGroup(Long idGroup) {
		this.idGroup = idGroup;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idGroup == null) ? 0 : idGroup.hashCode());
		result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (idGroup == null) {
			if (other.idGroup != null)
				return false;
		} else if (!idGroup.equals(other.idGroup))
			return false;
		if (idUser == null) {
			if (other.idUser != null)
				return false;
		} else if (!idUser.equals(other.idUser))
			return false;
		if (rol == null) {
			if (other.rol != null)
				return false;
		} else if (!rol.equals(other.rol))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [idUser=" + idUser + ", username=" + username + ", idGroup=" + idGroup + ", subject=" + subject
				+ ", rol=" + rol + "]";
	}

}