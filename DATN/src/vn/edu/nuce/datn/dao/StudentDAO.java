package vn.edu.nuce.datn.dao;

import java.io.Serializable;

import vn.edu.nuce.datn.entity.Student;

@SuppressWarnings("serial")
public class StudentDAO extends BaseDAO<Student> implements Serializable {
	
	@Override
	protected Class<Student> getEntityClass() {
		return Student.class;
	}
}
