import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {


    @Test
    void ShouldGetDefault() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void ShouldGetDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }
}