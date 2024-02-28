package com.duosoft.ipas;


import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.service.structure.OfficeDivisionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@PropertySource("classpath:application.properties")
@SpringBootTest
public class IpasUiApplicationTests {

    @Autowired
    OfficeDivisionService officeDivisionService;
    @Autowired
    UserService userService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
//        List<OfficeDivision> divisionList = officeDivisionService.getAll();
//        System.out.println();
    }

    @Test
    public void deleteDivision() {
//        OfficeDivision division = officeDivisionService.findByDivisionCode("02");
//        System.out.println(officeDivisionService.archiveDivision(division));
    }


}
