package com.sanluan.common.source;

import static com.sanluan.common.tools.FreeMarkerUtils.makeFileByFile;
import static com.sanluan.common.tools.ScanClassUtils.getClasses;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sanluan.common.base.Base;
import com.sanluan.common.source.entity.EntityColumn;
import com.sanluan.common.source.entity.EntityCondition;
import com.sanluan.common.source.entity.MyColumn;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 
 * SourceMaker 代码生成工具
 *
 */
public class SourceMaker extends Base {
    public static final String ENTITY_BASE_PACKAGE = "entities";
    public static final String DAO_BASE_PACKAGE = "dao";
    public static final String DAO_SUFFIX = "Dao";
    public static final String SERVICE_BASE_PACKAGE = "service";
    public static final String SERVICE_SUFFIX = "Service";
    public static final String DIRECTIVE_BASE_PACKAGE = "views.directive";
    public static final String DIRECTIVE_SUFFIX = "Directive";
    public static final String CONTROLLER_BASE_PACKAGE = "views.controller.admin";
    public static final String CONTROLLER_SUFFIX = "AdminController";
    public static final String JAVA_BASE_PATH = "src/new/";
    public static final String WEB_BASE_PATH = "WebContent/WEB-INF/admin/new/";

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        SourceMaker sourceMaker = new SourceMaker();
        boolean overwrite = false;// 是否覆盖已有代码
        String basePackage = "com.publiccms";// 基础包名
        // 生成所有实体类的代码
        // sourceMaker.make(basePackage, overwrite);
        // 生成某个包所有实体类的代码
        sourceMaker.make(basePackage, "home", overwrite);
        // 生成某个实体类的代码
        // sourceMaker.make(Class.forName("com.publiccms.entities.sys.SysCluster"),
        // basePackage, overwrite);
    }

    /**
     * 生成所有实体类的代码
     * 
     * @param basePackage
     * @param overwrite
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void make(String basePackage, boolean overwrite) throws ClassNotFoundException, IOException {
        String entitiesFullPackage = basePackage + "." + ENTITY_BASE_PACKAGE;
        for (Class<?> c : getClasses(new String[] { entitiesFullPackage })) {
            make(c, basePackage, overwrite);
        }
    }

    /**
     * 生成某个包所有实体类的代码
     * 
     * @param basePackage
     * @param entityPackage
     * @param overwrite
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void make(String basePackage, String entityPackage, boolean overwrite) throws ClassNotFoundException, IOException {
        String entitiesFullPackage = basePackage + "." + ENTITY_BASE_PACKAGE + "." + entityPackage;
        for (Class<?> c : getClasses(new String[] { entitiesFullPackage })) {
            make(c, basePackage, overwrite);
        }
    }

    /**
     * 生成某个实体类的代码
     * 
     * @param c
     * @param basePackage
     * @param overwrite
     */
    public void make(Class<?> c, String basePackage, boolean overwrite) {
        String name = c.getSimpleName();
        String base = basePackage + "." + ENTITY_BASE_PACKAGE + ".";
        String entitiesPackage = c.getPackage().getName().substring((base).length(), c.getPackage().getName().length());

        System.out.println("entity:" + base + entitiesPackage + "." + name);
        Map<String, Object> model = new HashMap<String, Object>();

        String entityPack = ENTITY_BASE_PACKAGE + "." + entitiesPackage;
        String daoPack = DAO_BASE_PACKAGE + "." + entitiesPackage;
        String servicePack = SERVICE_BASE_PACKAGE + "." + entitiesPackage;
        String directivePack = DIRECTIVE_BASE_PACKAGE + "." + entitiesPackage;
        String controllerPack = CONTROLLER_BASE_PACKAGE + "." + entitiesPackage;

        model.put("base", basePackage);
        model.put("entityPack", entityPack);
        model.put("daoPack", daoPack);
        model.put("daoSuffix", DAO_SUFFIX);
        model.put("servicePack", servicePack);
        model.put("serviceSuffix", SERVICE_SUFFIX);
        model.put("directivePack", directivePack);
        model.put("directiveSuffix", DIRECTIVE_SUFFIX);
        model.put("controllerPack", controllerPack);
        model.put("controllerSuffix", CONTROLLER_SUFFIX);
        model.put("entityName", name);

        Set<String> imports = new HashSet<String>();
        Map<String, EntityCondition> conditionMap = new LinkedHashMap<String, EntityCondition>();
        List<EntityColumn> columnList = new ArrayList<EntityColumn>();

        for (Field field : c.getDeclaredFields()) {
            if (0 != (field.getModifiers() & Modifier.PRIVATE) && 0 == (field.getModifiers() & Modifier.FINAL)) {
                String typeName = field.getType().getName();
                switch (typeName) {
                case "int":
                    typeName = "java.lang.Integer";
                    break;
                case "short":
                    typeName = "java.lang.Short";
                    break;
                case "long":
                    typeName = "java.lang.Long";
                    break;
                case "byte":
                    typeName = "java.lang.Byte";
                    break;
                case "char":
                    typeName = "java.lang.Character";
                    break;
                case "double":
                    typeName = "java.lang.Double";
                    break;
                case "float":
                    typeName = "java.lang.Float";
                    break;
                case "boolean":
                    typeName = "java.lang.Boolean";
                    break;
                }

                MyColumn column = field.getAnnotation(MyColumn.class);
                if (null != column) {
                    String shortTypeName = typeName.substring(typeName.lastIndexOf(".") + 1, typeName.length());
                    columnList.add(new EntityColumn(field.getName(), shortTypeName, column.order(), column.title()));
                    if (column.condition()) {
                        if (!typeName.startsWith("java.lang")) {
                            imports.add(typeName);
                        }
                        String key = isNotBlank(column.name()) ? column.name() : field.getName();
                        EntityCondition condition = conditionMap.get(key);
                        if (null == condition) {
                            condition = new EntityCondition(key, shortTypeName, column.title(), column.or(), column.like());
                        }
                        condition.getNameList().add(field.getName());
                        conditionMap.put(key, condition);
                    }
                }
            }
        }
        model.put("imports", imports);
        model.put("columnList", columnList);
        model.put("conditionList", conditionMap.values());

        String daoPath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + daoPack.replace('.', '/') + "/";
        String servicePath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + servicePack.replace('.', '/') + "/";
        String directivePath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + directivePack.replace('.', '/') + "/";
        String controllerPath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + controllerPack.replace('.', '/') + "/";

        try {
            makeFileByFile("java/dao.ftl", daoPath + name + DAO_SUFFIX + ".java", config, model, overwrite);
            makeFileByFile("java/service.ftl", servicePath + name + SERVICE_SUFFIX + ".java", config, model, overwrite);
            makeFileByFile("java/directive.ftl", directivePath + name + DIRECTIVE_SUFFIX + ".java", config, model, overwrite);
            makeFileByFile("java/directiveList.ftl", directivePath + name + "List" + DIRECTIVE_SUFFIX + ".java", config, model,
                    overwrite);
            makeFileByFile("java/controller.ftl", controllerPath + name + CONTROLLER_SUFFIX + ".java", config, model, overwrite);
            makeFileByFile("html/list.ftl", WEB_BASE_PATH + uncapitalize(name) + "/list.html", config, model, overwrite);
            makeFileByFile("html/add.ftl", WEB_BASE_PATH + uncapitalize(name) + "/add.html", config, model, overwrite);
            makeFileByFile("html/doc.ftl", WEB_BASE_PATH + "doc.txt", config, model, overwrite, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (TemplateException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("-------------------------------");
    }

    private Configuration config;

    /**
     * 
     */
    public SourceMaker() {
        config = new freemarker.template.Configuration(Configuration.getVersion());
        config.setClassForTemplateLoading(this.getClass(), "");
        config.setDefaultEncoding("utf-8");
    }

}
