package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.service.FileReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileReaderImplTest {
    private static final String VALID_FILE = "src/test/resources/validInput.csv";
    private static final String EMPTY_FILE = "src/test/resources/emptyInput.csv";
    private static final String MISSING_FILE = "src/test/resources/noSuchFile.csv";
    private FileReader fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
    }

    @Test
    void read_validFile_ok() {
        List<String> expected = List.of(
                "type,fruit,quantity",
                "b,banana,20",
                "b,apple,100",
                "s,banana,100",
                "p,banana,13",
                "r,apple,10");
        assertEquals(expected, fileReader.read(VALID_FILE));
    }

    @Test
    void read_emptyFile_ok() {
        assertTrue(fileReader.read(EMPTY_FILE).isEmpty());
    }

    @Test
    void read_missingFile_notOk() {
        assertThrows(RuntimeException.class, () -> fileReader.read(MISSING_FILE));
    }

    @Test
    void read_nullPath_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileReader.read(null));
    }

    @Test
    void read_blankPath_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileReader.read("  "));
    }
}
