package ISO2.Exe1;

import org.junit.jupiter.api.Test;
import service.InputService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testRunExit() {
        String input = "2\n"; // Select option 2 (Exit)
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        App app = new App();
        InputService inputService = new InputService();
        
        assertDoesNotThrow(() -> app.run(inputService));
    }

    @Test
    public void testRunCheckDate() {
        // 1 (Check Date), 29 (Day), 2 (Month), 2020 (Year), 2 (Exit)
        String input = "1\n29\n2\n2020\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        App app = new App();
        InputService inputService = new InputService();
        
        assertDoesNotThrow(() -> app.run(inputService));
    }

    @Test
    public void testRunInvalidOption() {
        // 3 (Invalid), 2 (Exit)
        String input = "3\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        App app = new App();
        InputService inputService = new InputService();
        
        assertDoesNotThrow(() -> app.run(inputService));
    }

    @Test
    public void testRunInvalidDate() {
        // 1 (Check Date), 32 (Day), 1 (Month), 2020 (Year), 2 (Exit)
        // This should trigger IllegalArgumentException in CustomDate constructor
        String input = "1\n32\n1\n2020\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        App app = new App();
        InputService inputService = new InputService();
        
        assertDoesNotThrow(() -> app.run(inputService));
    }

    @Test
    public void testRunInputException() {
        // 1 (Check Date), invalid (Day) x 4 (to exhaust retries), 2 (Exit)
        // Assuming retries=3, so 4th attempt throws InputException
        String input = "1\ninvalid\ninvalid\ninvalid\ninvalid\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        App app = new App();
        InputService inputService = new InputService();
        
        assertDoesNotThrow(() -> app.run(inputService));
    }

    @Test
    public void testMain() {
        // Test main method
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        assertDoesNotThrow(() -> App.main(new String[]{}));
    }
}
