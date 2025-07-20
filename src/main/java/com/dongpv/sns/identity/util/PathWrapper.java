package com.dongpv.sns.identity.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class PathWrapper {

    /**
     * @param pathStr
     * @return
     */
    public boolean doesNotExist(String pathStr) {
        return !doesExist(pathStr);
    }

    /**
     * @param pathStr
     * @return
     */
    public boolean doesExist(String pathStr) {
        final Path path = Paths.get(pathStr);
        return Files.isDirectory(path) || Files.isRegularFile(path);
    }

    /**
     * @param pathStr
     * @return
     */
    public boolean isNotReadable(String pathStr) {
        return !isReadable(pathStr);
    }

    /**
     * @param pathStr
     * @return
     */
    public boolean isReadable(String pathStr) {

        try {
            final Path path = Paths.get(pathStr);
            return Files.isReadable(path);

        } catch (final SecurityException ex) {
            // we must have no read access
            return false;
        }
    }
}
