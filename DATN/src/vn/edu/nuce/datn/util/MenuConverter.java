package vn.edu.nuce.datn.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import vn.edu.nuce.datn.entity.SysMenu;


@FacesConverter("menuConverter")
public class MenuConverter implements Converter {

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

		Object ret = null;
		if (uic instanceof PickList) {

			Object dualList = ((PickList) uic).getValue();
			DualListModel<SysMenu> dl = (DualListModel<SysMenu>) dualList;
			for (Object o : dl.getSource()) {

				SysMenu objMenu = (SysMenu) o;
				if (value.equals(String.valueOf(objMenu.getId()))) {
					ret = o;
					break;
				}
			}

			if (ret == null) {
				for (Object o : dl.getTarget()) {

					SysMenu objMenu = (SysMenu) o;
					if (value.equals(String.valueOf(objMenu.getId()))) {
						ret = o;
						break;
					}
				}
			}
		}

		return ret;
	}

	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return String.valueOf(((SysMenu) object).getId());
		} else {
			return null;
		}
	}
}
