import org.junit.Assert;
import org.junit.Test;
import ru.job4j.pools.RolColSum;

import java.util.concurrent.ExecutionException;

public class RolColSumTest {
    @Test
    public void sumShouldBeRight() {
        int[][] arr = {{1, 2, 3, 10},
                       {4, 5, 6, 11},
                       {7, 8, 9, 12}};
        RolColSum.Sums[] result = RolColSum.sum(arr);
        Assert.assertEquals(18, result[2].getColSum());
        Assert.assertEquals(36, result[2].getRowSum());
    }

    @Test
    public void asyncSumShouldBeRight() throws ExecutionException, InterruptedException {
        int[][] arr = {{1, 2, 3, 10},
                       {4, 5, 6, 11},
                       {7, 8, 9, 12}};
        RolColSum.Sums[] result = RolColSum.asyncSum(arr);
        Assert.assertEquals(15, result[1].getColSum());
        Assert.assertEquals(26, result[1].getRowSum());
    }
}
