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

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import net.jawis.config.Localization;
import net.jawis.config.Resources;

/**
 *
 * @author Redbad
 */
public class MenuBar extends JMenuBar {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MenuBar.class);
    
    private MainFrame parentFrame;
    
    private List<JMenu> menus = new ArrayList<>();
    private List<JMenuItem> menuItems = new ArrayList<>();
    
    private static final String PROP_MENU = "menu_text";
    private static final String PROP_MENU_ITEM = "menuItem_text";
    
    private JMenu menuFile = new JMenu(Resources.getValueFromBundle(PROP_MENU, "menuFile"));
    private JMenu menuSettings = new JMenu(Resources.getValueFromBundle(PROP_MENU, "menuSettings"));
    private JMenu menuLanguage = new JMenu(Resources.getValueFromBundle(PROP_MENU, "menuLanguage"));
    private JMenu menuInfo = new JMenu(Resources.getValueFromBundle(PROP_MENU, "menuInfo"));
    
    private Action changeLangENG = new AbstractAction(Resources.getValueFromBundle(PROP_MENU_ITEM, "langENG")) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Localization.setDefaultLocale("en", "US");
            changeLanguage();
        }
    };
    private Action changeLangNL = new AbstractAction(Resources.getValueFromBundle(PROP_MENU_ITEM, "langNL")) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Localization.setDefaultLocale("nl", "NL");
            changeLanguage();
        }
    };
    
    private Action actionExit = new AbstractAction(Resources.getValueFromBundle(PROP_MENU_ITEM, "fileExit")) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };
    
    private Action actionAbout = new AbstractAction(Resources.getValueFromBundle(PROP_MENU_ITEM, "infoAbout")) {
        @Override
        public void actionPerformed(ActionEvent e) {
            FrameAbout frame = new FrameAbout();
        }
    };
    
    
    private JMenuItem fileExit = new JMenuItem(actionExit);
    
    private JMenuItem langENG = new JMenuItem(changeLangENG);
    private JMenuItem langNL = new JMenuItem(changeLangNL);
    
    private JMenuItem infoAbout = new JMenuItem(actionAbout);
    
    
    public MenuBar(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        initialize();
        listComponents();
        changeLanguage();
    }
    
    private void initialize() {
        this.add(menuFile);
        this.add(menuSettings);
        this.add(menuInfo);
        
        menuFile.add(fileExit);
        menuSettings.add(menuLanguage);
        menuLanguage.add(langENG);
        menuLanguage.add(langNL);
        menuInfo.add(infoAbout);
        
        menuFile.setName("menuFile");
        menuSettings.setName("menuSettings");
        menuLanguage.setName("menuLanguage");
        menuInfo.setName("menuInfo");
        
        fileExit.setName("fileExit");
        langENG.setName("langENG");
        langNL.setName("langNL");
        
        infoAbout.setName("infoAbout");
        
    }
    
    public void listComponents() {
        menus.add(menuFile);
        menus.add(menuSettings);
        menus.add(menuLanguage);
        menus.add(menuInfo);
        menuItems.add(fileExit);
        menuItems.add(langENG);
        menuItems.add(langNL);
        menuItems.add(infoAbout);
    }
    
    public void changeLanguage() {
        LOGGER.info("change language");
        Localization.setLanguageForMenus(menus);
        Localization.setLanguageForMenuItems(menuItems);
        
        parentFrame.changeLanguage();
    }
    
}
