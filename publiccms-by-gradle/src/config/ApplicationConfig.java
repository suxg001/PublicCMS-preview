package config;

import java.beans.PropertyVetoException;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.publiccms.common.view.InitializeFreeMarkerView;
import com.publiccms.logic.component.SiteComponent;
import com.publiccms.logic.component.TemplateComponent;
import com.sanluan.common.base.Base;
import com.sanluan.common.datasource.MultiDataSource;

/**
 * 
 * ApplicationConfig Spring配置类
 *
 */
@Configuration
@ComponentScan(basePackages = "com.publiccms", excludeFilters = { @ComponentScan.Filter(value = { Controller.class }) })
@PropertySource({ "classpath:config/properties/dbconfig.properties", "classpath:config/properties/freemarker.properties",
        "classpath:config/properties/other.properties" })
@EnableTransactionManagement
@EnableScheduling
public class ApplicationConfig extends Base {
    @Autowired
    private Environment env;
    public static String basePath;
    public static WebApplicationContext webApplicationContext;

    /**
     * 数据源
     * 
     * @return
     * @throws PropertyVetoException
     */
    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        MultiDataSource bean = new MultiDataSource();
        HashMap<Object, Object> targetDataSources = new HashMap<Object, Object>();
        ComboPooledDataSource database = new ComboPooledDataSource();
        database.setDriverClass(env.getProperty("jdbc.driverClassName"));
        database.setJdbcUrl(env.getProperty("jdbc.url"));
        database.setUser(env.getProperty("jdbc.username"));
        database.setPassword(env.getProperty("jdbc.password"));
        database.setAutoCommitOnClose(Boolean.parseBoolean(env.getProperty("cpool.autoCommitOnClose")));
        database.setCheckoutTimeout(Integer.parseInt(env.getProperty("cpool.checkoutTimeout")));
        database.setInitialPoolSize(Integer.parseInt(env.getProperty("cpool.minPoolSize")));
        database.setMinPoolSize(Integer.parseInt(env.getProperty("cpool.minPoolSize")));
        database.setMaxPoolSize(Integer.parseInt(env.getProperty("cpool.maxPoolSize")));
        database.setMaxIdleTime(Integer.parseInt(env.getProperty("cpool.maxIdleTime")));
        database.setAcquireIncrement(Integer.parseInt(env.getProperty("cpool.acquireIncrement")));
        database.setMaxIdleTimeExcessConnections(Integer.parseInt(env.getProperty("cpool.maxIdleTimeExcessConnections")));
        targetDataSources.put("default", database);
        bean.setTargetDataSources(targetDataSources, database);
        return bean;
    }

    /**
     * Hibernate事务管理
     * 
     * @return
     */
    @Bean
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager bean = new HibernateTransactionManager();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    /**
     * 持久层会话工厂类
     * 
     * @return
     * @throws PropertyVetoException
     */
    @Bean
    public FactoryBean<SessionFactory> sessionFactory() throws PropertyVetoException {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan(new String[] { "com.publiccms.entities" });
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.query.substitutions", env.getProperty("hibernate.query.substitutions"));
        properties.setProperty("hibernate.jdbc.fetch_size", env.getProperty("hibernate.jdbc.fetch_size"));
        properties.setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
        properties.setProperty("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory_class"));
        properties.setProperty("hibernate.cache.use_second_level_cache",
                env.getProperty("hibernate.cache.use_second_level_cache"));
        properties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
        properties.setProperty("hibernate.transaction.coordinator_class",
                env.getProperty("hibernate.transaction.coordinator_class"));
        properties.setProperty("hibernate.cache.provider_configuration_file_resource_path",
                env.getProperty("hibernate.cache.provider_configuration_file_resource_path"));
        properties.setProperty("hibernate.search.default.directory_provider",
                env.getProperty("hibernate.search.default.directory_provider"));
        properties.setProperty("hibernate.search.default.indexBase", getDirPath("/indexes/"));
        bean.setHibernateProperties(properties);
        return bean;
    }

    /**
     * 国际化处理
     * 
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
        bean.setBasenames(new String[] { "config.language.message", "config.language.config", "config.language.plugin" });
        bean.setCacheSeconds(300);
        bean.setUseCodeAsDefaultMessage(true);
        return bean;
    }

    /**
     * 模板操作组件
     * 
     * @return
     */
    @Bean
    public TemplateComponent templateComponent() {
        TemplateComponent bean = new TemplateComponent();
        bean.setDirectivePrefix(env.getProperty("freeMarkerExtendHandler.directivePrefix"));
        bean.setDirectiveRemoveRegex(env.getProperty("freeMarkerExtendHandler.directiveRemoveRegex"));
        bean.setMethodRemoveRegex(env.getProperty("freeMarkerExtendHandler.methodRemoveRegex"));
        return bean;
    }

    /**
     * 文件操作组件
     * 
     * @return
     */
    @Bean
    public SiteComponent siteComponent() {
        SiteComponent bean = new SiteComponent();
        bean.setRootPath(getDirPath(""));
        bean.setSiteMasters(env.getProperty("site.masterSiteIds"));
        bean.setDefaultSiteId(Integer.parseInt(env.getProperty("site.defaultSiteId")));
        return InitializeFreeMarkerView.siteComponent = bean;
    }

    /**
     * FreeMarker配置工厂
     * 
     * @return
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer bean = new FreeMarkerConfigurer();
        bean.setTemplateLoaderPath("/WEB-INF/");
        Properties properties = new Properties();
        properties.setProperty("new_builtin_class_resolver", env.getProperty("freemarkerSettings.new_builtin_class_resolver"));
        properties.setProperty("template_update_delay", env.getProperty("freemarkerSettings.template_update_delay"));
        properties.setProperty("defaultEncoding", env.getProperty("freemarkerSettings.defaultEncoding"));
        properties.setProperty("url_escaping_charset", env.getProperty("freemarkerSettings.url_escaping_charset"));
        properties.setProperty("locale", env.getProperty("freemarkerSettings.locale"));
        properties.setProperty("boolean_format", env.getProperty("freemarkerSettings.boolean_format"));
        properties.setProperty("datetime_format", env.getProperty("freemarkerSettings.datetime_format"));
        properties.setProperty("date_format", env.getProperty("freemarkerSettings.date_format"));
        properties.setProperty("time_format", env.getProperty("freemarkerSettings.time_format"));
        properties.setProperty("number_format", env.getProperty("freemarkerSettings.number_format"));
        properties.setProperty("auto_import", env.getProperty("freemarkerSettings.auto_import"));
        properties.setProperty("auto_include", env.getProperty("freemarkerSettings.auto_include"));
        properties.setProperty("template_exception_handler", env.getProperty("freemarkerSettings.template_exception_handler"));
        bean.setFreemarkerSettings(properties);
        return bean;
    }

    /**
     * 附件Multipart解决方案
     * 
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver bean = new CommonsMultipartResolver();
        bean.setDefaultEncoding(DEFAULT_CHARSET);
        bean.setMaxUploadSize(102400000);
        return bean;
    }

    /**
     * 任务计划工厂类配置
     * 
     * @return
     */
    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadCount", env.getProperty("task.threadCount"));
        bean.setQuartzProperties(properties);
        return bean;
    }

    /**
     * json消息转换适配器，用于支持RequestBody、ResponseBody
     * 
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    private String getDirPath(String path) {
        String filePath = env.getProperty("site.filePath", basePath) + path;
        File dir = new File(filePath);
        dir.mkdirs();
        return dir.getAbsolutePath();
    }
}