package vn.edu.nuce.datn.dao;

import java.io.Serializable;

import vn.edu.nuce.datn.entity.Major;

public class MajorDAO extends BaseDAO<Major> implements Serializable {
	@Override
	protected Class<Major> getEntityClass() {
		return Major.class;
	}

}
