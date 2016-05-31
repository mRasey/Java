package JunitTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

public class TestTest {
    private JunitTest.Test test = new JunitTest.Test();

    @Before
    public void setUp() throws Exception {
        test.helpInputJudge();

    }

    @After
    public void tearDown() throws Exception {


    }

    @org.junit.Test
    public void judge() throws Exception {
        test.judge(true);
        test.judge(false);
    }

    @Test
    public void judge1() throws Exception {
        test.judge1(true);
        test.judge1(false);
    }

    @Test
    public void judge2() throws Exception {
        test.judge2(true);
        test.judge2(false);
    }

    @Test
    public void inputJudge() throws Exception {
        test.helpInputJudge();
    }

}