package facade.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Text File Handler - Subsystem component for Facade pattern
 * Handles plain text file read/write operations
 */
public class TextFileHandler {

    /**
     * Read entire file as string
     *
     * @param filePath Path to text file
     * @return File content as string
     * @throws IOException if file operation fails
     */
    public String readFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        System.out.println("✓ Text file read successfully: " + filePath);
        System.out.println("  Content length: " + content.length() + " characters");
        return content;
    }

    /**
     * Read file as list of lines
     *
     * @param filePath Path to text file
     * @return List of lines
     * @throws IOException if file operation fails
     */
    public List<String> readLines(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        System.out.println("✓ Text file read as lines: " + filePath);
        System.out.println("  Lines read: " + lines.size());
        return lines;
    }

    /**
     * Write string to file
     *
     * @param filePath Path to text file
     * @param content Content to write
     * @throws IOException if file operation fails
     */
    public void writeFile(String filePath, String content) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print(content);
        }

        System.out.println("✓ Text file written successfully: " + filePath);
        System.out.println("  Content length: " + content.length() + " characters");
    }

    /**
     * Write lines to file
     *
     * @param filePath Path to text file
     * @param lines Lines to write
     * @throws IOException if file operation fails
     */
    public void writeLines(String filePath, List<String> lines) throws IOException {
        Files.write(Paths.get(filePath), lines);

        System.out.println("✓ Text file written with lines: " + filePath);
        System.out.println("  Lines written: " + lines.size());
    }

    /**
     * Append string to file
     *
     * @param filePath Path to text file
     * @param content Content to append
     * @throws IOException if file operation fails
     */
    public void appendFile(String filePath, String content) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.print(content);
        }

        System.out.println("✓ Content appended to file: " + filePath);
    }
}

