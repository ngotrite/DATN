package vn.edu.nuce.datn.dao;


import vn.edu.nuce.datn.entity.SysRole;

public class SysRoleDAO extends BaseDAO<SysRole> {

	@Override
	protected Class<SysRole> getEntityClass() {
		return SysRole.class;
	}

}
