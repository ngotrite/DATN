package vn.edu.nuce.datn.dao;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SysUserGroupMapDAO extends BaseDAO<SysUserGroupMapDAO> implements Serializable {
	@Override
	protected Class<SysUserGroupMapDAO> getEntityClass() {
		return SysUserGroupMapDAO.class;
	}
}
