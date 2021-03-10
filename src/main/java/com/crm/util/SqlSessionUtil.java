package com.crm.util;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {
    private static SqlSessionFactory sqlSessionFactory;
    private static ThreadLocal<SqlSession> tl = new ThreadLocal<>();
    static {
        try {
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession getSqlSession(){
        //获取ThreadLocal中保存的sqlSession对象
        SqlSession sqlSession = tl.get();
        if(sqlSession == null){
            //如果没有，进行创建并保存到ThreadLocal中
            sqlSession = sqlSessionFactory.openSession();
            tl.set(sqlSession);
        }
        return sqlSession;
    }
    public static void closeSqlSession(SqlSession sqlSession){
        if(sqlSession != null){
            sqlSession.close();
            //关闭sqlSession后一定要移除这条线程
            tl.remove();
        }
    }
}
