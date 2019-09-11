package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    public void getLayoutMenuListByRule() {
        String mbrId = "admin";

        List<MenuInfo> layoutMenuListByRule = menuService.getLayoutMenuListByRule(mbrId);

        assertThat(layoutMenuListByRule).size().isGreaterThan(0);
        assertThat(layoutMenuListByRule.get(0).getSubMenu()).size().isGreaterThan(0);
    }
}