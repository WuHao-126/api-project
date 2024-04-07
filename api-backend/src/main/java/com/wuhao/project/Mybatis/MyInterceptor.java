package com.wuhao.project.Mybatis;

import com.baomidou.mybatisplus.core.override.MybatisMapperMethod;
import javassist.bytecode.analysis.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */

//拦截sql语句的sql处理器方法
//@Intercepts({
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
//})
//@Component
public class MyInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取StatementHandler对象（执行语句）
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        //MetaObject 是MyBatis提供一个反射帮助类，可以优雅访问对象的属性，这里是对statementHandler 对象进行反射处理
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        //mappedStatement
        String id = mappedStatement.getId();
        Class<?> aClass = Class.forName(id.substring(0, id.lastIndexOf(".")));
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里设置一些属性配置
    }
}
