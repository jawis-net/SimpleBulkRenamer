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
import java.util.Date;
import java.util.List;

/**
 *
 * @author Redbad
 */
public class RenamerThread extends Thread {
    
    private boolean addDate;
    private Date date;
    private Renamer.DateFormat dateFormat;
    private Renamer.DatePosition datePosition;
    
    private Renamer.Separator separator;
    private String prefix;
    private String suffix;
    
    private Path path;
    private List<String> extensions;
    private String strPath;
    
    public RenamerThread() {
        this.setDaemon(true);
    }
    
    public void setPath(Path path) {
        this.path = path;
        strPath = path.toString();
    }
    
    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateFormat(Renamer.DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setDatePosition(Renamer.DatePosition datePosition) {
        this.datePosition = datePosition;
    }

    public void setSeparator(Renamer.Separator separator) {
        this.separator = separator;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    @Override
    public void run() {
        List<String> files = Directories.listFiles(path, extensions);
        for (String file : files) {
            Path srcPath = Path.of(strPath, file);
            Renamer.renameFile(srcPath, Renamer.newFileName(srcPath, prefix, suffix, separator, addDate, date, dateFormat, datePosition));
        }
    }
    
}
