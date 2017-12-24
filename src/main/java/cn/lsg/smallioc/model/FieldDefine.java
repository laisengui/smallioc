package cn.lsg.smallioc.model;

import java.lang.reflect.Field;

/**
 * 属性的描述
 * @author Norton Lai
 * @created 2017-12-24 上午9:57:42
 */
public class FieldDefine {

    //ID
    private String id;
    
    //注入id
    private boolean injectById;
    //类型
    private Class type;
    //原始field类
    private Field field;

    
    
    
    
  

    public FieldDefine(String id, boolean injectById, Class type, Field field) {
        super();
        this.id = id;
        this.injectById = injectById;
        this.type = type;
        this.field = field;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:58:29
     * @return type
     */
    public String getId() {
        return id;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:58:29
     * @return type
     */
    public boolean isInjectById() {
        return injectById;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:58:29
     * @return type
     */
    public Class getType() {
        return type;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:58:29
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:58:29
     * @param injectById
     */
    public void setInjectById(boolean injectById) {
        this.injectById = injectById;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午9:58:29
     * @param type
     */
    public void setType(Class type) {
        this.type = type;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:03:55
     * @return type
     */
    public Field getField() {
        return field;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:03:55
     * @param field
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * 描述
     * @author Norton Lai
     * @created 2017-12-24 下午5:51:15
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FieldDefine [id=" + id + ", injectById=" + injectById + ", type=" + type + ", field=" + field + "]";
    }
    
    
}
