package org.dromara.demo.domain;

import javax.annotation.processing.Generated;
import org.dromara.demo.domain.vo.TestDemoVo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:45+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class TestDemoToTestDemoVoMapperImpl implements TestDemoToTestDemoVoMapper {

    @Override
    public TestDemoVo convert(TestDemo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        TestDemoVo testDemoVo = new TestDemoVo();

        testDemoVo.setId( arg0.getId() );
        testDemoVo.setDeptId( arg0.getDeptId() );
        testDemoVo.setUserId( arg0.getUserId() );
        testDemoVo.setOrderNum( arg0.getOrderNum() );
        testDemoVo.setTestKey( arg0.getTestKey() );
        testDemoVo.setValue( arg0.getValue() );
        testDemoVo.setCreateTime( arg0.getCreateTime() );
        if ( arg0.getCreateBy() != null ) {
            testDemoVo.setCreateBy( Long.parseLong( arg0.getCreateBy() ) );
        }
        testDemoVo.setUpdateTime( arg0.getUpdateTime() );
        if ( arg0.getUpdateBy() != null ) {
            testDemoVo.setUpdateBy( Long.parseLong( arg0.getUpdateBy() ) );
        }
        testDemoVo.setVersion( arg0.getVersion() );

        return testDemoVo;
    }

    @Override
    public TestDemoVo convert(TestDemo arg0, TestDemoVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setId( arg0.getId() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setUserId( arg0.getUserId() );
        arg1.setOrderNum( arg0.getOrderNum() );
        arg1.setTestKey( arg0.getTestKey() );
        arg1.setValue( arg0.getValue() );
        arg1.setCreateTime( arg0.getCreateTime() );
        if ( arg0.getCreateBy() != null ) {
            arg1.setCreateBy( Long.parseLong( arg0.getCreateBy() ) );
        }
        else {
            arg1.setCreateBy( null );
        }
        arg1.setUpdateTime( arg0.getUpdateTime() );
        if ( arg0.getUpdateBy() != null ) {
            arg1.setUpdateBy( Long.parseLong( arg0.getUpdateBy() ) );
        }
        else {
            arg1.setUpdateBy( null );
        }
        arg1.setVersion( arg0.getVersion() );

        return arg1;
    }
}
