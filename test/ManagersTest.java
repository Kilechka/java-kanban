import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldGetDefault() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void shouldGetDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }
}