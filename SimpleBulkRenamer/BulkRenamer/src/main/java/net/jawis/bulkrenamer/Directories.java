/*
 * Copyright (c) 2024 jawis.net
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/MIT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the “Software”), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR 
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.jawis.bulkrenamer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.jawis.config.Resources;
//import net.jawis.my.config.Resources;

/**
 *
 * @author Redbad
 */
public class Directories {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Directories .class);
    
    private static final String PROP_DIALOG = "dialog";
    
    public static void openFile(Path filePath) {
        if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isRegularFile(filePath, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    Desktop.getDesktop().open(filePath.toFile());
                } catch (IOException ex) {
                    LOGGER.info("Could not open file: ".concat(ex.getMessage()));
                }
            } else {
                LOGGER.info("Path does not respresent a file: ".concat(filePath.toString()));
            }
        } else {
            LOGGER.info("File does not exist: ".concat(filePath.toString()));
        }
    }
    
    public static void openDirectory(Path directoryPath) {
        if (Files.exists(directoryPath, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isDirectory(directoryPath, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    Desktop.getDesktop().open(directoryPath.toFile());
                } catch (IOException ex) {
                    LOGGER.info("Could not open directory: ".concat(ex.getMessage()));
                }
            } else {
                LOGGER.info("Path does not respresent a directory: ".concat(directoryPath.toString()));
            }
        } else {
            LOGGER.info("Directory does not exist: ".concat(directoryPath.toString()));
        }
    }
    
    public static void openFile(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Can not open file: ".concat(filePath), "Open file", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public static void openDirectory(String directoryPath) {
        if (directoryPath != null) {
            File file = new File(directoryPath);
            if (file.exists() && file.isDirectory()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Can not open directory: ".concat(directoryPath), "Open directory", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public static String chooseDirectory() {
        return chooseDirectory(null);
    }
    
    public static String chooseDirectory(String directoryPath) {
       return openDirectoryChooser(Paths.get(directoryPath)).toString();
    }
    
    public static Path openDirectoryChooser() {
        return openDirectoryChooser(null);
    }
    
    public static Path openDirectoryChooser(Path path) {
        File file = null;
        if (path != null) {
            file = path.toFile();
        }
        Path directory = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(Resources.getValueFromBundle(PROP_DIALOG, "titleOpenDirectoryChooser"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(file);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            directory = selectedDirectory.toPath();
        }
        return directory;
    }
    
    public static List<String> listDirectories(Path path) {
        List<String> directories = new ArrayList<>();
        try {
            Collections.sort(directories = Files.list(path)
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList()));
        } catch (IOException ex) {
            Logger.getLogger(Directories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return directories;
    }
    
    public static List<String> listFiles(Path path, List<String> extensions) {
        List<String> files = new ArrayList<>();
        try {
            if (extensions == null) {
                Collections.sort(files = Files.list(path)
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.toList()));
            } else {
                Collections.sort(files = Files.list(path)
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .filter(file -> {
                            String fileName = file.toString();
                            String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                            return extensions.contains(extension);
                        })
                        .map(Path::toString)
                        .collect(Collectors.toList()));
            }
        } catch (IOException ex) {
            Logger.getLogger(Directories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return files;
    }
    
    public static List<String> listExtensions(Path path) {
        List<String> extensions = new ArrayList<>();
        try {
            Collections.sort(extensions = Files.list(path)
                    .filter(Files::isRegularFile)
                    .map(file -> {
                            String fileName = file.toString();
                            return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                    })
                    .distinct()
                    .collect(Collectors.toList()));
        } catch (IOException ex) {
            Logger.getLogger(Directories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return extensions;
    }
    
    public static List<String> listDirectories(String directoryPath) {
        List<String> directories = new ArrayList<>();
        File file = new File(directoryPath);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();

            for (File item : listFiles) {
                if (item.isDirectory()) {
                    directories.add(item.getName());
                }
            }
            Collections.sort(directories);
        }
        return directories;
    }
    
    public static List<String> listFiles(String directoryPath, List<String> fileExtensions) {
        List<String> files = new ArrayList<>();
        File file = new File(directoryPath);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File item : listFiles) {
                if (item.isFile()) {
                    if (fileExtensions == null) {
                        files.add(item.getName());
                    } else {
                        for (String extension : fileExtensions) {
                            if (item.getName().toLowerCase().endsWith(extension)) {
                                files.add(item.getName());
                            }
                        }
                    }
                }
            }
            Collections.sort(files);
        }
        return files;
    }
    
    public static boolean renameDirectory(Path path) {
        String dialogResult = JOptionPane.showInputDialog(null, Resources.getValueFromBundle(PROP_DIALOG, "renameFolder"), Resources.getValueFromBundle(PROP_DIALOG, "titleRenameFolder"), JOptionPane.QUESTION_MESSAGE);
        if (!dialogResult.isEmpty()) {
            Path newPath = Path.of(path.getParent().toString(), dialogResult);
            return renameDirectory(path, newPath);
        } else {
            return false;
        }
    }
    
    public static boolean renameFile(Path path) {
        String dialogResult = JOptionPane.showInputDialog(null, Resources.getValueFromBundle(PROP_DIALOG, "renameFile"), Resources.getValueFromBundle(PROP_DIALOG, "titleRenameFile"), JOptionPane.QUESTION_MESSAGE);
        if (!dialogResult.isEmpty()) {
            String extension = path.toString().substring(path.toString().lastIndexOf("."));
            Path newPath = Path.of(path.getParent().toString(), dialogResult.concat(extension));
            return renameFile(path, newPath);
        } else {
            return false;
        }
    }
    
    public static boolean renameDirectory(Path oldPath, Path newPath) {
        if (Files.exists(oldPath, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isDirectory(oldPath, LinkOption.NOFOLLOW_LINKS)) {
                if (!Files.exists(newPath, LinkOption.NOFOLLOW_LINKS)) {
                    return oldPath.toFile().renameTo(newPath.toFile());
                } else {
                    LOGGER.info("Directory already exists: ".concat(newPath.toString()));
                }
            } else {
                LOGGER.info("Path does not represent a directory: ".concat(oldPath.toString()));
            }
        } else {
            LOGGER.info("Directory does not exist: ".concat(oldPath.toString()));
        }
        return false;
    }
    
    public static boolean renameFile(Path oldPath, Path newPath) {
        if (Files.exists(oldPath, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isRegularFile(oldPath, LinkOption.NOFOLLOW_LINKS)) {
                if (!Files.exists(newPath, LinkOption.NOFOLLOW_LINKS)) {
                    return oldPath.toFile().renameTo(newPath.toFile());
                } else {
                    LOGGER.info("File already exists: ".concat(newPath.toString()));
                }
            } else {
                LOGGER.info("Path does not represent a file: ".concat(oldPath.toString()));
            }
        } else {
            LOGGER.info("File does not exist: ".concat(oldPath.toString()));
        }
        return false;
    }
    
    public static boolean deleteDirectory(Path path, boolean promptForDeleting) {
        if (promptForDeleting) {
            int dialogResult = JOptionPane.showConfirmDialog(null, Resources.getValueFromBundle(PROP_DIALOG, "deleteFolder").concat(path.getFileName().toString()).concat(Resources.getValueFromBundle(PROP_DIALOG, "deleteFolder2")).concat(" ?"), Resources.getValueFromBundle(PROP_DIALOG, "titleDeleteFolder"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION) {
                return deleteDirectory(path);
            } else {
                return false;
            }
        } else {
            return deleteDirectory(path);
        }
    }
    
    public static void deleteAllFilesInDirectory(Path path) {
        List<String> files = Directories.listFiles(path, null);
        for (String file : files) {
            try {
                Files.delete(Path.of(path.toString(), file));
            } catch (IOException ex) {
                LOGGER.error("Could not delete file ".concat(file));
            }
        }
    }
    
    public static boolean deleteDirectory(Path path) {
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    return Files.deleteIfExists(path);
                } catch (DirectoryNotEmptyException ex) {
                    LOGGER.info("Could not delete directory, because it is not empty: ".concat(path.toString()));
                    int dialogResult = JOptionPane.showConfirmDialog(null, Resources.getValueFromBundle(PROP_DIALOG, "deleteAllFilesInDirectory"), Resources.getValueFromBundle(PROP_DIALOG, "titleDeleteFolder"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        deleteAllFilesInDirectory(path);
                        deleteDirectory(path);
                    }
                } catch (IOException ex) {
                    LOGGER.info("IOException occurred while deleting directory: ".concat(path.toString()));
                    LOGGER.info(ex.getMessage());
                }
            } else {
                LOGGER.info("Path does not represent a directory: ".concat(path.toString()));
            }
        } else {
            LOGGER.info("Directory does not exist: ".concat(path.toString()));
        }
        return false;
    }
    
    public static boolean deleteFile(Path path, boolean promptForDeleting) {
        if (promptForDeleting) {
            int dialogResult = JOptionPane.showConfirmDialog(null, Resources.getValueFromBundle(PROP_DIALOG, "deleteFile").concat(path.getFileName().toString()).concat(Resources.getValueFromBundle(PROP_DIALOG, "deleteFile2")).concat(" ?"), Resources.getValueFromBundle(PROP_DIALOG, "titleDeleteFile"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION) {
                return deleteFile(path);
            } else {
                return false;
            }
        } else {
            return deleteFile(path);
        }
    }
    
    public static boolean deleteFile(Path path) {
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    return Files.deleteIfExists(path);
                } catch (IOException ex) {
                    LOGGER.info("IOException occurred while deleting file: ".concat(path.toString()));
                    LOGGER.info(ex.getMessage());
                }
            } else {
                LOGGER.info("Path does not represent a file: ".concat(path.toString()));
            }
        } else {
            LOGGER.info("File does not exist: ".concat(path.toString()));
        }
        return false;
    }
    
}
