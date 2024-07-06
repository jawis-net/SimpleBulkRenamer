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

package net.jawis.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Redbad
 */
public class Resources {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Resources.class);
    
    public static String getValueFromBundle(String propertiesName, String key) {
        try {
            return getBundle(propertiesName).getString(key);
        } catch (Exception e) {
            LOGGER.error("Can not find key ".concat(key).concat(" for bundle ").concat(propertiesName));
            return null;
        }
    }
    
    public static String getValueFromBundle(String propertiesName, String key, Locale locale) {
        LOGGER.info("Locale country: ".concat(locale.getCountry()));
        LOGGER.info("Locale language: ".concat(locale.getLanguage()));
        try {
            return getBundle(propertiesName, locale).getString(key);
        } catch (Exception e) {
            LOGGER.error("Can not find key ".concat(key).concat(" for bundle ").concat(propertiesName));
            return null;
        }
    }
    
    public static ResourceBundle getBundle(String propertiesName) {
        Locale locale = Locale.getDefault();
        return getBundle(propertiesName, locale);
    }
    
    public static ResourceBundle getBundle(String propertiesName, Locale locale) {
        try {
            return ResourceBundle.getBundle(propertiesName, locale);
        } catch (MissingResourceException e) {
            LOGGER.error("Can not find bundle ".concat(propertiesName));
            return ResourceBundle.getBundle(propertiesName);
        }
    }
    
}
