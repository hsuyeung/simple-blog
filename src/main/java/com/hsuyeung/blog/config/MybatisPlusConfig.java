package com.hsuyeung.blog.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hsuyeung.blog.web.core.RequestUserHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static com.hsuyeung.blog.constant.enums.LogicDeleteEnum.NOT_DELETED;


/**
 * MyBatis Plus 配置
 *
 * @author hsuyeung
 * @date 2021/9/14
 */
@MapperScan("com.hsuyeung.blog.mapper")
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {
    /**
     * 分页插件
     *
     * @return {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 插入数据的时候自动填充的字段
     *
     * @param metaObject 字段原对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        // 这个与 setFieldValByName 的区别是后者不管你有没有设置值，
        // 都会被这里的填充值给覆盖掉，前者以自定义的值为准，如果为 null 才使用这里的填充值
        String currentUid = String.valueOf(RequestUserHolder.getCurrentUid());
        this.strictInsertFill(metaObject, "createBy", String.class, currentUid);
        setFieldValByName("createTime", now, metaObject);
        this.strictInsertFill(metaObject, "updateBy", String.class, currentUid);
        setFieldValByName("updateTime", now, metaObject);
        setFieldValByName("deleted", NOT_DELETED, metaObject);
    }

    /**
     * 更新数据的时候自动填充的字段
     *
     * @param metaObject 字段原对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        setFieldValByName("updateBy", String.valueOf(RequestUserHolder.getCurrentUid()), metaObject);
        setFieldValByName("updateTime", now, metaObject);
    }
}
