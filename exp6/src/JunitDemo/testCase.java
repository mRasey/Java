package JunitDemo;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.After;
import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testCase {
    private static PushBox pb=new PushBox();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Test
    public void testReadmap() throws Exception {
        assertEquals(false, pb.readmap());
    }

    @Test
    public void testCheck() throws Exception {
        assertEquals(false,pb.check(0, 0));
        assertEquals(true,pb.check(1, 2));
    }

    @Test
    public void testBfs_Man() throws Exception {
        assertEquals(false, pb.bfs_man(1,0,2,2,3,3));
        assertEquals(true, pb.bfs_man(0,1,2,2,3,3));
    }

    @Test
    public void testBfs() throws Exception {
        pb.bfs();
    }

    @Before
    public void setUp() throws Exception {
        pb.pre(pb.q);
        pb.readmap();
    }

    @After
    public void tearDown() throws Exception {

    }
}
