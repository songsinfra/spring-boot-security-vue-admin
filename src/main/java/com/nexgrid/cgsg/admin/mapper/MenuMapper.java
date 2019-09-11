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

	List<MenuInfo> getMenuList(MenuInfo menuInfo);

	List<MenuInfo> getUpMenuList();
	
	int setMenuAdd(MenuInfo menuInfo);
	int setMenuUpd(MenuInfo menuInfo);
	
	int getMenuCnt(MenuInfo menuInfo);
	int getUpMenuCnt(MenuInfo menuInfo);
	
	int setUpMenuAdd(MenuInfo menuInfo);
	
	MenuInfo getMenuInfo(MenuInfo menuInfo);
	
	List<MenuInfo> getRoleMenuList(MenuInfo menuInfo);

}
