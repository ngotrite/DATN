package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.List;

import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.entity.Student;

@SuppressWarnings("serial")
public class StudentDAO extends BaseDAO<Student> implements Serializable {
	
	public List<Student> findSTByGP(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" };
		Operator[] operators = { Operator.EQ };
		Object[] values = { graduationPeriodId };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
	@Override
	protected Class<Student> getEntityClass() {
		return Student.class;
	}
}
