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

import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;

/**
 *
 * @author Redbad
 */
public class Localization {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Localization.class);
    
    private static final String DEFAULT_LANGUAGE_TAG = "en-US";
    
    /**
     * This method sets the locale for this instance of the JVM.
     */
    public static void setDefaultLocale(String languageTag) {
        Locale.setDefault(Locale.forLanguageTag(languageTag));
    }
    /**
     * This method sets the locale for this instance of the JVM.
     */
    public static void setDefaultLocale(String language, String Country) {
        Locale locale = new Locale(language, Country);
        Locale.setDefault(locale);
    }
    /**
     * This method sets the locale for this instance of the JVM.
     */
    public static void setDefaultLocale() {
//        System.out.println(Locale.getDefault().getCountry());
        Locale.setDefault(Locale.forLanguageTag(DEFAULT_LANGUAGE_TAG));
        System.out.println(Locale.getDefault().getCountry());
        System.out.println(Locale.getDefault().getLanguage());
    }
    
    
    public static void setLanguageForMenus(List<JMenu> components) {
        String text;
        String textPropertiesName = "menu_text";
        String tooltipPropertiesName = "menu_tooltip";
        for (JMenu component : components) {
            text = Resources.getValueFromBundle(textPropertiesName, component.getName());
            if (text != null) {
                component.setText(text);
            }
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    public static void setLanguageForMenuItems(List<JMenuItem> components) {
        String text;
        String textPropertiesName = "menuItem_text";
        String tooltipPropertiesName = "menuItem_tooltip";
        for (JMenuItem component : components) {
            text = Resources.getValueFromBundle(textPropertiesName, component.getName());
            if (text != null) {
                component.setText(text);
            }
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    public static void setLanguageForButtons(List<JButton> components) {
        String text;
        String textPropertiesName = "button_text";
        String tooltipPropertiesName = "button_tooltip";
        for (JButton component : components) {
            text = Resources.getValueFromBundle(textPropertiesName, component.getName());
            if (text != null) {
                component.setText(text);
            }
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    public static void setLanguageForLabels(List<JLabel> components) {
        String text;
        String textPropertiesName = "label_text";
        String tooltipPropertiesName = "label_tooltip";
        for (JLabel component : components) {
            text = Resources.getValueFromBundle(textPropertiesName, component.getName());
            if (text != null) {
                component.setText(text);
            }
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    public static void setLanguageForComboboxes(List<JComboBox> components) {
        String tooltipPropertiesName = "combobox_tooltip";
        for (JComboBox component : components) {
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    public static void setLanguageForListboxes(List<JList> components) {
        String tooltipPropertiesName = "listbox_tooltip";
        for (JList component : components) {
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    public static void setLanguageForCheckboxes(List<JCheckBox> components) {
        String text;
        String textPropertiesName = "checkbox_text";
        String tooltipPropertiesName = "checkbox_tooltip";
        for (JCheckBox component : components) {
            text = Resources.getValueFromBundle(textPropertiesName, component.getName());
            if (text != null) {
                component.setText(text);
            }
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    public static void setLanguageForRadiobuttons(List<JRadioButton> components) {
        String text;
        String textPropertiesName = "radiobutton_text";
        String tooltipPropertiesName = "radiobutton_tooltip";
        for (JRadioButton component : components) {
            text = Resources.getValueFromBundle(textPropertiesName, component.getName());
            if (text != null) {
                component.setText(text);
            }
            component.setToolTipText(Resources.getValueFromBundle(tooltipPropertiesName, component.getName()));
        }
    }
    
}
