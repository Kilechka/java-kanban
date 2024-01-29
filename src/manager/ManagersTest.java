package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    private Managers manager;

    @Test
    void ShouldGetDefault() {
        manager = new Managers();
        assertNotNull(manager.getDefault());
    }

    @Test
    void ShouldGetDefaultHistory() {
        manager = new Managers();
        assertNotNull(manager.getDefaultHistory());
    }
}