package cn.lsg.smallioc;

import java.io.File;
import java.util.List;

import com.h.a.A;
import com.h.a.B;
import com.h.a.O;

import cn.lsg.smallioc.core.ApplicationContext;
import cn.lsg.smallioc.log.Logger.LEVEL;
import cn.lsg.smallioc.util.ClassScanUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
        System.out.println(ApplicationContext.retDefaultLoggerParams());
        ApplicationContext.setDefaultLoggerParams(true, false, LEVEL.debug, "C:/Users/Administrator/Desktop");
        System.out.println(ApplicationContext.retDefaultLoggerParams());
        ApplicationContext app = ApplicationContext.getContext("com");

        
        app.getbean(A.class).b.a = null;
        B o = (B) app.getbean("b");

        B o1 = (B) app.getbean("b");
        System.out.println(o1.a);

    }

}
