package com.duosoft.ipas.mapper.rest;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.rest.model.userdoc.RUserdoc;
import com.duosoft.ipas.controller.rest.mapper.RUserdocMapper;
import com.duosoft.ipas.controller.rest.mapper.RUserdocMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
public class RUserdocMapperTest {
    private RUserdoc restUserdoc;
    private RUserdoc revertedRestUserdoc;

    @Before
    public void setUp() throws Exception {
        RUserdocMapper rUserdocMapper = new RUserdocMapperImpl();
        restUserdoc = RestObjectMapperTestUtils.createAndFill(RUserdoc.class);
        CUserdoc coreUserdoc = rUserdocMapper.toCore(restUserdoc);
        revertedRestUserdoc = rUserdocMapper.toRest(coreUserdoc);
    }

    @Test
    public void restUserdocMapperTest() {
        DiffGenerator diffGenerator = DiffGenerator.create(restUserdoc, revertedRestUserdoc);
        assertFalse(diffGenerator.isChanged());
    }

}