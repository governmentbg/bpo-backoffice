package com.duosoft.ipas.mapper.rest;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import com.duosoft.ipas.controller.rest.mapper.RPatentMapper;
import com.duosoft.ipas.controller.rest.mapper.RPatentMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
public class RPatentMapperTest {
    private RPatent restPatent;
    private RPatent revertedRestPatent;

    @Before
    public void setUp() throws Exception {
        RPatentMapper rPatentMapper = new RPatentMapperImpl();
        restPatent = RestObjectMapperTestUtils.createAndFill(RPatent.class);
        CPatent corePatent = rPatentMapper.toCore(restPatent);
        revertedRestPatent = rPatentMapper.toRest(corePatent);
    }

    @Test
    public void restPatentMapperTest() {
        DiffGenerator diffGenerator = DiffGenerator.create(restPatent, revertedRestPatent);
        assertFalse(diffGenerator.isChanged());
    }

}