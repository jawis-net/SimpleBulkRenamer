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

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.jawis.config.Resources;

/**
 *
 * @author Redbad
 */
public class Renamer {
    
    
    public enum Separator {
        UNDERSCORE("_", "_"),
        SCORE("-", "-"),
        SPACE(" ", " "),
        POINT(".", "."),
        NONE("", Resources.getValueFromBundle("misc", "separatorNone"));
        
        private final String separator;
        private final String description;
        
        private Separator(String separator, String description) {
            this.separator = separator;
            this.description = description;
        }
        
        public String getName() {
            return separator;
        }
        
        public String getDescription() {
            if (this == NONE) {
                return Resources.getValueFromBundle("misc", "separatorNone");
            } else {
                return description;
            }
        }
        
        public static Separator getValueByDescription(String description) {
            for (Separator separator : Separator.values()) {
                if (separator.getDescription().equals(description)) {
                    return separator;
                }
            }
            return NONE;
        }
        
    }
    
    public enum DateFormat {
        YYYYMMDD("yyyyMMdd"),
        YYYYMMDD_SPACE("yyyy MM dd"),
        YYYYMMDD_SCORE("yyyy-MM-dd"),
        YYYYMMDD_UNDERSCORE("yyyy_MM_dd"),
        YYYYMMDD_POINT("yyyy.MM.dd"),
        DDMMYYYY("ddMMyyyy"),
        DDMMYYYY_SPACE("dd MM yyyy"),
        DDMMYYYY_SCORE("dd-MM-yyyy"),
        DDMMYYYY_UNDERSCORE("dd_MM_yyyy"),
        DDMMYYYY_POINT("dd.MM.yyyy");
        
        private final String format;
        private final String description;
        private final SimpleDateFormat dateFormatter;
        
        private DateFormat(String format) {
            this.format = format;
            this.description = format;
            this.dateFormatter = new SimpleDateFormat(format);
        }
        
        public String getFormat() {
            return format;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getFormattedDate(Date date) {
            return dateFormatter.format(date);
        }
        
        public static DateFormat getValueByDescription(String description) {
            for (DateFormat dateFormat : DateFormat.values()) {
                if (dateFormat.getDescription().equals(description)) {
                    return dateFormat;
                }
            }
            return null;
        }
        
    }
    
    public enum DatePosition {
        BEFORE_PREFIX(Resources.getValueFromBundle("misc", "datePositionBeforePrefix"), "datePositionBeforePrefix"),
        AFTER_PREFIX(Resources.getValueFromBundle("misc", "datePositionAfterPrefix"), "datePositionAfterPrefix"),
        BEFORE_SUFFIX(Resources.getValueFromBundle("misc", "datePositionBeforeSuffix"), "datePositionBeforeSuffix"),
        AFTER_SUFFIX(Resources.getValueFromBundle("misc", "datePositionAfterSuffix"), "datePositionAfterSuffix");
        
        private final String description;
        private final String key;
        
        private DatePosition(String description, String key) {
            this.description = description;
            this.key = key;
        }
        
        public String getDescription() {
            return Resources.getValueFromBundle("misc", key);
        }
        
        public static DatePosition getValueByDescription(String description) {
            for (DatePosition datePosition : DatePosition.values()) {
                if (datePosition.getDescription().equals(description)) {
                    return datePosition;
                }
            }
            return null;
        }
        
    }
    
    public static boolean renameFile(Path path, String newFileName) {
        Path newPath = Path.of(path.getParent().toString(), newFileName);
        return Directories.renameFile(path, newPath);
    }
    
    public static String newFileName(Path filePath, String prefix, String suffix, Separator separator, boolean addDate, Date date, DateFormat dateFormat, DatePosition datePosition) {
        String newFileName;
        String fileName = filePath.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String fileNameRaw = fileName.substring(0, fileName.lastIndexOf("."));
        StringBuilder sb = new StringBuilder();
        String dateFormatted = dateFormat.getFormattedDate(date);
        if (addDate && datePosition == DatePosition.BEFORE_PREFIX) {
            sb.append(dateFormatted);
            sb.append(separator.getName());
        }
        if (!prefix.isEmpty()) {
            sb.append(prefix);
            sb.append(separator.getName());
        }
        if (addDate && datePosition == DatePosition.AFTER_PREFIX) {
            sb.append(dateFormatted);
            sb.append(separator.getName());
        }
        sb.append(fileNameRaw);
        if (addDate && datePosition == DatePosition.BEFORE_SUFFIX) {
            sb.append(separator.getName());
            sb.append(dateFormatted);
        }
        if (!suffix.isEmpty()) {
            sb.append(separator.getName());
            sb.append(suffix);
        }
        if (addDate && datePosition == DatePosition.AFTER_SUFFIX) {
            sb.append(separator.getName());
            sb.append(dateFormatted);
        }
        sb.append(extension);
        newFileName = sb.toString();
        return newFileName;
    }
    
}
