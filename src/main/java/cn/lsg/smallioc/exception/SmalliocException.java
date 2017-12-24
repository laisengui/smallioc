package cn.lsg.smallioc.exception;

/**
 * 微型IOC基类异常
 * @author Norton Lai
 * @created 2017-12-24 上午12:16:40
 */
public class SmalliocException extends RuntimeException {

   

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    public SmalliocException() {
        super();
    }

    public SmalliocException(String message, Throwable cause) {
        super(message, cause);
        
    }

    public SmalliocException(String message) {
        super(message);
        
    }

    public SmalliocException(Throwable cause) {
        super(cause);
    }

}
