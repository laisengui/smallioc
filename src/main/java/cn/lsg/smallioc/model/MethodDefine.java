package cn.lsg.smallioc.model;

import java.lang.reflect.Method;

/**
 * 方法描述
 * 
 * @author Norton Lai
 * @created 2017-12-24 上午10:12:58
 */
public class MethodDefine {
    
    //ID
    private String id;
    //注入根据id
    private boolean injectById;

    //类型
    private Class type;

    //原始方法对象
    private Method method;

    /**
     * 构造 
     * @author Norton Lai
     * @created 2017-12-24 下午5:52:39
     * @param id
     * @param injectById
     * @param type
     * @param method
     */
    public MethodDefine(String id, boolean injectById, Class type, Method method) {
        super();
        this.id = id;
        this.injectById = injectById;
        this.type = type;
        this.method = method;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @return type
     */
    public String getId() {
        return id;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @return type
     */
    public boolean isInjectById() {
        return injectById;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @return type
     */
    public Class getType() {
        return type;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @return type
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @param injectById
     */
    public void setInjectById(boolean injectById) {
        this.injectById = injectById;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @param type
     */
    public void setType(Class type) {
        this.type = type;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 上午10:13:55
     * @param method
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * 描述
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午5:52:02
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MethodDefine [id=" + id + ", injectById=" + injectById + ", type=" + type + ", method=" + method
                + "]";
    }

}
