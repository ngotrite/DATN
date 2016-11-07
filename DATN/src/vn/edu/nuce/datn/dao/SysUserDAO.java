package vn.edu.nuce.datn.dao;


import java.util.List;

import vn.edu.nuce.datn.entity.SysUser;

public class SysUserDAO extends BaseDAO<SysUser> {

	@Override
	protected Class<SysUser> getEntityClass() {
		return SysUser.class;
	}
	public List<SysUser> findAll() {
		return super.findAll();
	}
}
