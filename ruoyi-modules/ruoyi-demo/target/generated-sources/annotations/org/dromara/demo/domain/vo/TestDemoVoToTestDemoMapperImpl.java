package org.dromara.demo.domain.vo;

import javax.annotation.processing.Generated;
import org.dromara.demo.domain.TestDemo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:45+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class TestDemoVoToTestDemoMapperImpl implements TestDemoVoToTestDemoMapper {

    @Override
    public TestDemo convert(TestDemoVo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        TestDemo testDemo = new TestDemo();

        if ( arg0.getCreateBy() != null ) {
            testDemo.setCreateBy( String.valueOf( arg0.getCreateBy() ) );
        }
        testDemo.setCreateTime( arg0.getCreateTime() );
        if ( arg0.getUpdateBy() != null ) {
            testDemo.setUpdateBy( String.valueOf( arg0.getUpdateBy() ) );
        }
        testDemo.setUpdateTime( arg0.getUpdateTime() );
        testDemo.setId( arg0.getId() );
        testDemo.setDeptId( arg0.getDeptId() );
        testDemo.setUserId( arg0.getUserId() );
        testDemo.setOrderNum( arg0.getOrderNum() );
        testDemo.setTestKey( arg0.getTestKey() );
        testDemo.setValue( arg0.getValue() );
        testDemo.setVersion( arg0.getVersion() );

        return testDemo;
    }

    @Override
    public TestDemo convert(TestDemoVo arg0, TestDemo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        if ( arg0.getCreateBy() != null ) {
            arg1.setCreateBy( String.valueOf( arg0.getCreateBy() ) );
        }
        else {
            arg1.setCreateBy( null );
        }
        arg1.setCreateTime( arg0.getCreateTime() );
        if ( arg0.getUpdateBy() != null ) {
            arg1.setUpdateBy( String.valueOf( arg0.getUpdateBy() ) );
        }
        else {
            arg1.setUpdateBy( null );
        }
        arg1.setUpdateTime( arg0.getUpdateTime() );
        arg1.setId( arg0.getId() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setUserId( arg0.getUserId() );
        arg1.setOrderNum( arg0.getOrderNum() );
        arg1.setTestKey( arg0.getTestKey() );
        arg1.setValue( arg0.getValue() );
        arg1.setVersion( arg0.getVersion() );

        return arg1;
    }
}
