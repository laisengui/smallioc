package cn.lsg.smallioc.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.lsg.smallioc.util.SUtil;

/**
 * 日志管理
 * 
 * @author magic282
 */
public class LoggerImpl implements Logger {

    // 日期格式化对象
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    // FileWriter
    private BufferedWriter logWriter = null;

    // *****************************可设置的属性
    // 是否在记录日志的同时打印到控制台
    private static boolean print2Console = true;
    // 是否打印日志到文件
    private static boolean print2File = false;

    // 日志记录等级
    private static LEVEL logLevel = LEVEL.error;
    // 日志如果记录到文件 的文件夹路径
    private static String dirUrl;

    /**
     * 构造
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午4:16:08
     */
    public LoggerImpl() {
        super();
        initLogger();
    }

    /**
     * 初始化 日志记录的流和设置
     * 
     * @return
     * @author lsg
     * @2017-8-3 @上午9:15:32
     */
    private void initLogger() {

        if (logLevel == LEVEL.sile) {// 如果是静默 不初始化
            return;
        }
        if (!print2File) {// 如果不需要打印到文件 不初始化
            return;
        }

        // 创建文件夹
        if (SUtil.isEmp(dirUrl)) {
            dirUrl = System.getProperty("user.dir");// 如果未指定日志位置 默认 运行的根目录
        }
        String logDirectoryPath = null;
        logDirectoryPath = dirUrl + java.io.File.separatorChar + "log";
        if (!new File(logDirectoryPath).exists()) {
            new File(logDirectoryPath).mkdir();
        }

        // 创建日志文件
        String logFilePath = logDirectoryPath + java.io.File.separatorChar + dateFormat.format(new Date()) + ".log";
        try {
            logWriter = new BufferedWriter(new FileWriter(logFilePath, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 返回日志记录者的信息
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午4:25:37
     * @return
     */
    private String retLogClassInfo(boolean detail) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement s : stackTrace) {
            if ("java.lang.Thread".equals(s.getClassName())
                    || "cn.lsg.smallioc.log.LoggerImpl".equals(s.getClassName())) {
                continue;
            }
            if (detail) {
                return s.getClassName() + "." + s.getMethodName() + "()" + s.getLineNumber() + " 行";
            } else {
                return s.getClassName();
            }
        }
        return null;
    }

    /**
     * 记录日志信息
     * 
     * @param message
     * @author lsg
     * @2017-8-3 @上午9:15:15
     */
    private void log(String message, boolean detail) {
        String callingClassName = retLogClassInfo(detail);
        String log = String.format("[%s] @ [%s]: %s\n", callingClassName, dateFormat.format(new Date()), message);

        printConsole(log);

        print2File(log);
    }

    /**
     * 记录异常堆栈
     * 
     * @param exception
     * @author lsg
     * @2017-8-3 @上午9:15:05
     */
    private void log(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String log = sw.toString();

        printConsole(log);

        print2File(log);

    }

    /**
     * 记录到控制台
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午4:36:53
     * @param log
     */
    private void printConsole(String log) {
        if (print2Console) {
            System.out.printf("[log]:%s", log);
        }
    }

    /**
     * 输出到文件
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午4:18:08
     * @param log
     */
    private void print2File(String log) {
        if (print2File) {
            synchronized (logWriter) {
                try {
                    logWriter.write(log);
//                    logWriter.flush();
                } catch (IOException e) {
                    System.err.println("Write log to file %s error.");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void flush() {
        if (logWriter != null) {
            try {
                logWriter.flush();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void debug(String msg) {
        if (LEVEL.debug == logLevel) {
            log(msg, false);
        }
    }

    @Override
    public void debug(String msg, Throwable e) {
        if (LEVEL.debug == logLevel) {
            log(msg, false);
            log(e);
        }
    }

    @Override
    public void debug(Throwable e) {
        if (LEVEL.debug == logLevel) {
            log(e.getMessage(), false);
            log(e);
        }
    }

    @Override
    public void error(String msg) {
        if (LEVEL.debug == logLevel || LEVEL.error == logLevel) {
            log(msg, true);
        }
    }

    @Override
    public void error(String msg, Throwable e) {
        if (LEVEL.debug == logLevel || LEVEL.error == logLevel) {
            log(msg, true);
            log(e);
        }
    }

    @Override
    public void error(Throwable e) {
        if (LEVEL.debug == logLevel || LEVEL.error == logLevel) {
            log(e.getMessage(), true);
            log(e);
        }
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 下午4:55:58
     * @param print2Console
     */
    public static void setPrint2Console(boolean print2Console) {
        LoggerImpl.print2Console = print2Console;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 下午4:55:58
     * @param print2File
     */
    public static void setPrint2File(boolean print2File) {
        LoggerImpl.print2File = print2File;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 下午4:55:58
     * @param logLevel
     */
    public static void setLogLevel(LEVEL logLevel) {
        LoggerImpl.logLevel = logLevel;
    }

    /**
     * @author Norton Lai
     * @created 2017-12-24 下午4:55:58
     * @param dirUrl
     */
    public static void setDirUrl(String dirUrl) {
        LoggerImpl.dirUrl = dirUrl;
    }

    /**
     * 返回日志配置
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午5:07:09
     * @return
     */
    public static String retParams() {
        StringBuilder sb = new StringBuilder();
        sb.append("打印到控制台: ");
        sb.append(print2Console);
        sb.append("\n\r");
        sb.append("输出到文件: ");
        sb.append(print2File);
        sb.append("\n\r");
        sb.append("日志等级: ");
        sb.append(logLevel.name());
        sb.append("\n\r");
        sb.append("文件夹路径: ");
        sb.append(dirUrl);
        sb.append("\n\r");
        return sb.toString();
    }

    /**
     * 描述
     * @author Norton Lai
     * @created 2017-12-24 下午6:52:33
     * @throws Throwable
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        flush();
    }
    
}