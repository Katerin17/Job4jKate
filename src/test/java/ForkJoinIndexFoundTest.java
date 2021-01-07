import org.junit.Assert;
import org.junit.Test;
import ru.job4j.ForkJoinIndexFound;

public class ForkJoinIndexFoundTest {

    @Test
    public void indexShouldBeSame() {
        int[] arr = {5, 7, 8, 3, 1, 34, 68, 9, 55, 66, 765, 77, 344, 65, 84, 2, 65, 90};
        int result = ForkJoinIndexFound.search(arr, 34);
        Assert.assertEquals(5, result);
    }

    @Test
    public void indexShouldBeMinusOne() {
        int[] arr = {5, 7, 8, 3, 1, 34, 68, 9, 55, 66, 765, 77, 344, 65, 84, 2, 65, 90};
        int result = ForkJoinIndexFound.search(arr, 43);
        Assert.assertEquals(-1, result);
    }
}
