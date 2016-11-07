package vn.edu.nuce.datn.dao;

import java.io.Serializable;

import vn.edu.nuce.datn.entity.GraduationPeriod;

@SuppressWarnings("serial")
public class GraduationPeriodDAO extends BaseDAO<GraduationPeriod> implements Serializable{
	
	@Override
	protected Class<GraduationPeriod> getEntityClass() {
		return GraduationPeriod.class;
	}

}
