package cn.lsg.smallioc.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.lsg.smallioc.core.ApplicationContext;
import cn.lsg.smallioc.log.Logger;

/**
 * 类扫描工具
 * 
 * @author Norton Lai
 * @created 2017-12-23 下午8:39:17
 */
public class ClassScanUtil {

    /**
     * 获得包下面的所有的class
     * 
     * @param pack
     *            package完整名称
     * @return List包含所有class的实例
     */
    public static List<Class> getClasssFromPackage(String pack) {
        List<Class> clazzs = new ArrayList<Class>();

        // 是否循环搜索子包
        boolean recursive = true;

        // 包名字
        String packageName = pack;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    getLog().debug("file类型的扫描");
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
                } else if ("jar".equals(protocol)) {
                    getLog().debug("jar类型的扫描");
                    String jarPaht = url.getPath();
                    String filePaht = null;
                    if (jarPaht.indexOf("!") > -1) {
                        String[] paht = jarPaht.split("!");
                        jarPaht = paht[0];
                        filePaht = paht[1] != null ? paht[1] : null;
                    }
                    jarPaht = jarPaht.replaceFirst("file:/", "");
                    filePaht = filePaht.startsWith("/") ? filePaht.substring(1) : filePaht;
                    getClasssFromJarFile(jarPaht, filePaht, clazzs);
                }
            }
        } catch (Exception e) {
            getLog().error(e);
        }

        return clazzs;
    }

    /**
     * 在package对应的路径下找到所有的class
     * 
     * @param packageName
     *            package名称
     * @param filePath
     *            package对应的路径
     * @param recursive
     *            是否查找子package
     * @param clazzs
     *            找到class以后存放的集合
     */
    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive,
            List<Class> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(ClassFileFilter.retBean());

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                String temp = SUtil.isEmp(packageName) ? file.getName() : (packageName + "." + file.getName());
                findClassInPackageByFile(temp, file.getAbsolutePath(), recursive, clazzs);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    getLog().debug("获取到类：" + packageName + "." + className);
                    clazzs.add(Thread.currentThread().getContextClassLoader()
                            .loadClass(packageName + "." + className));
                } catch (Exception e) {
                    getLog().error("加载类路径异常",e);
                }
            }
        }
    }

    /**
     * 从jar文件中读取指定目录下面的所有的class文件
     * 
     * @param jarPaht
     *            jar文件存放的位置
     * @param filePaht
     *            指定的文件目录
     * @return 所有的的class的对象
     */
    public static List<Class> getClasssFromJarFile(String jarPaht, String filePaht) {
        List<Class> clazzs = new ArrayList<Class>();

        getClasssFromJarFile(jarPaht, filePaht, clazzs);

        return clazzs;
    }

    /**
     * 从jar文件中读取指定目录下面的所有的class文件
     * 
     * @author Norton Lai
     * @created 2017-12-23 下午9:33:07
     * @param jarPaht
     *            jar文件存放的位置
     * @param filePaht
     *            指定的文件目录
     * @param clazzs
     *            class集合
     */
    public static void getClasssFromJarFile(String jarPaht, String filePaht, List<Class> clazzs) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPaht);
        } catch (IOException e1) {
            getLog().error(e1);
        }

        List<JarEntry> jarEntryList = new ArrayList<JarEntry>();

        Enumeration<JarEntry> ee = jarFile.entries();
        while (ee.hasMoreElements()) {
            JarEntry entry = (JarEntry) ee.nextElement();
            // getLog().debug(entry.getName());
            // 过滤我们出满足我们需求的东西
            if (entry.getName().startsWith(filePaht) && entry.getName().endsWith(".class")) {
                jarEntryList.add(entry);
            }
        }
        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);

            try {
                getLog().debug("获取到类：" + className);
                clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                getLog().error("加载类路径异常",e);
            }
        }
    }

    /**
     * 获取日志记录器
     * 
     * @author Norton Lai
     * @created 2017-12-24 下午5:48:11
     * @return
     */
    private static Logger getLog() {
        return ApplicationContext.getLog();
    }

}

/**
 * class文件名过滤器
 * 
 * @author Norton Lai
 * @created 2017-12-23 下午8:50:48
 */
class ClassFileFilter implements FileFilter {
    // bean
    private static ClassFileFilter bean;

    /**
     * 返回一个过滤器
     * 
     * @author Norton Lai
     * @created 2017-12-23 下午8:54:42
     * @return
     */
    public static ClassFileFilter retBean() {
        if (bean == null) {
            bean = new ClassFileFilter();
        }
        return bean;
    }

    /**
     * 过滤出文件名后缀为.class的文件或者文件夹
     * 
     * @author Norton Lai
     * @created 2017-12-23 下午10:47:03
     * @param pathname
     * @return
     * @see java.io.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().endsWith(".class");
    }

}
