package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileWriterImplTest {
    private static final String CONTENT = "fruit,quantity" + System.lineSeparator() + "banana,107";
    @TempDir
    private Path tempDir;
    private FileWriter fileWriter;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriterImpl();
    }

    @Test
    void write_validPath_ok() throws IOException {
        Path file = tempDir.resolve("report.csv");
        fileWriter.write(CONTENT, file.toString());
        assertEquals(CONTENT, Files.readString(file));
    }

    @Test
    void write_existingFile_overwritesContent() throws IOException {
        Path file = tempDir.resolve("report.csv");
        Files.writeString(file, "old content that is longer than the new one");
        fileWriter.write(CONTENT, file.toString());
        assertEquals(CONTENT, Files.readString(file));
    }

    @Test
    void write_nonExistingDirectory_notOk() {
        String path = tempDir.resolve("missing").resolve("report.csv").toString();
        assertThrows(RuntimeException.class, () -> fileWriter.write(CONTENT, path));
    }

    @Test
    void write_nullPath_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write(CONTENT, null));
    }

    @Test
    void write_blankPath_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write(CONTENT, ""));
    }
}
