package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import vn.edu.nuce.datn.dao.SysRoleDAO;
import vn.edu.nuce.datn.entity.SysRole;
import vn.edu.nuce.datn.util.Setting;

//@Component
@ManagedBean(name="roleBean")
@ViewScoped
public class RoleBean extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1219556076115932261L;
	private SysRole sysRole = new SysRole();
	private List<SysRole> listSysRole;
	private boolean isEditing;
	
	// Spring DI
	//@Autowired
	private SysRoleDAO roleDao;
	private List<SysRole> selectedRoles = new ArrayList<SysRole>();
	
	public RoleBean() {
		
		roleDao = new SysRoleDAO();
		init();
		searchRole();
	}
	
	private void init() {
		sysRole = new SysRole();
	}
	
	public void searchRole() {
		
		listSysRole = roleDao.findAll();
	}
		
	public void btnNew() {
		
		init();
		isEditing = true;
	}

	public void btnCancel() {
		
		init();
		isEditing = false;
	}
	
	public void btnSave() {
	
		if(sysRole.getId() > 0 ) {
			
			roleDao.update(sysRole);
		} else {
			
			roleDao.save(sysRole);
			listSysRole.add(sysRole);
		}
		
		btnCancel();
	}
	
	public void onRowSelect(SelectEvent event) {
//		sysRole = (SysRole) event.getObject();
	}
	
	public void onRowEdit(SysRole role) {
		
		this.sysRole = role;
		isEditing = true;
	}
	
	public void onRowDelete(SysRole role) {
		
		if(this.sysRole.getId() == role.getId()) {
			btnCancel();
		}
		roleDao.delete(role);
		listSysRole.remove(role);
	}
	
	/** GET SET**/
	public SysRole getSysRole() {
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
		
	public List<SysRole> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<SysRole> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public Map<String, String> getRoleTypeList() {
		return Setting.RoleType.listType;
	}
	
	public String getRoleTypeLabel(String value) {
		return Setting.RoleType.listType.get(value);
	}
		
	public List<SysRole> getListSysRole() {
		return listSysRole;
	}

	public void setListSysRole(List<SysRole> listSysRole) {
		this.listSysRole = listSysRole;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
}
