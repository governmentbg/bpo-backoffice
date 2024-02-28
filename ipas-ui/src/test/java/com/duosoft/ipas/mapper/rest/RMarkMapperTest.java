package com.duosoft.ipas.mapper.rest;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.rest.model.mark.RMark;
import com.duosoft.ipas.controller.rest.mapper.RMarkMapper;
import com.duosoft.ipas.controller.rest.mapper.RMarkMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
public class RMarkMapperTest {
    private RMark restMark;
    private RMark revertedRestMark;

    @Before
    public void setUp() throws Exception {
        RMarkMapper rMarkMapper = new RMarkMapperImpl();
        restMark = RestObjectMapperTestUtils.createAndFill(RMark.class);
        CMark coreMark = rMarkMapper.toCore(restMark);
        revertedRestMark = rMarkMapper.toRest(coreMark);
    }

    @Test
    public void restMarkMapperTest() {
        DiffGenerator diffGenerator = DiffGenerator.create(restMark, revertedRestMark);
        assertFalse(diffGenerator.isChanged());
    }

}