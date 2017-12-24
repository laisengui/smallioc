package cn.lsg.smallioc.log;

/**
 * 日志接口.如果不想要默认日志记录 可以实现这个接口
 * 
 * @author Norton Lai
 * @created 2017-12-24 下午3:42:53
 */
public interface Logger {
 // 记录登记 BUG、ERR 、静默
    public static enum LEVEL {
        debug, error, sile
    }

    /**
     * 刷新缓冲流
     * @author Norton Lai
     * @created 2017-12-24 下午6:46:15
     */
    public void flush();
    public void debug(String msg);

    public void debug(String msg, Throwable e);

    public void debug(Throwable e);
    
    public void error(String msg);

    public void error(String msg, Throwable e);

    public void error(Throwable e);
}
