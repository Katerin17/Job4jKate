import org.junit.Test;
import ru.job4j.email.EmailNotification;
import ru.job4j.email.User;

public class EmailNotificationTest {
    @Test
    public void emailShouldBeSent() {
        EmailNotification en = new EmailNotification();
        for (int i = 0; i < 10; i++) {
            en.addTask(() -> en.emailTo(new User("Kate", "mail.ru")));
        }
        en.close();
    }
}
