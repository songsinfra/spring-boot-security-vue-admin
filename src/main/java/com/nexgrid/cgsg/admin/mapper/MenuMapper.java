package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import javax.swing.event.MenuListener;
import java.util.List;

@Mapper
public interface MenuMapper {

	List<MenuInfo> getRootMenuList(String mbrId);

	List<MenuInfo> getSubMenuList(String mbrId);

	List<MenuInfo> getMenuList(String searchUseYn);

	List<MenuInfo> getUpMenuList();
	
	int setMenuAdd(MenuInfo menuInfo);
	int setMenuUpdate(MenuInfo menuInfo);
	
	int getMenuCnt(String upMenuId);
	int getUpMenuCnt();
	
	int setUpMenuAdd(MenuInfo menuInfo);
	
	MenuInfo getMenuInfo(String menuId);
	
	List<MenuInfo> getRoleMenuList();

}
