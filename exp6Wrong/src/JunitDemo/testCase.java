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
        setUp();
        assertEquals(false, pb.readmap());
    }

    @Test
    public void testCheck() throws Exception {
        setUp();
        for(int i = 0; i < pb.m; i++){
            for(int j = 0; j < pb.n; j++)
                assertEquals(false,pb.check(pb.m, pb.n));
        }
    }

    @Test
    public void testBfs_Man() throws Exception {
        setUp();
        assertEquals(false, pb.readmap());

    }

    @Test
    public void testBfs() throws Exception {
        setUp();
        assertEquals(false, pb.readmap());

    }

    @Before
    public void setUp() throws Exception {
        pb.pre(pb.q);
    }

    @After
    public void tearDown() throws Exception {

    }
}
