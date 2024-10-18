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

/*
* Thanks to Kai Tödter for providing the open source JCalendar package.
*      https://toedter.com/jcalendar/
*      https://github.com/toedter/jcalendar
*/

package net.jawis.bulkrenamer;

import com.toedter.calendar.JCalendar;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import net.jawis.bulkrenamer.Renamer.DateFormat;
import net.jawis.bulkrenamer.Renamer.DatePosition;
import net.jawis.bulkrenamer.Renamer.Separator;
import net.jawis.config.Localization;

/**
 *
 * @author Redbad
 */
public class MainPanel extends javax.swing.JPanel {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MainPanel.class);
    
    private MainFrame parentFrame;
    
    private boolean isInitializing = true;
    
    private List<JButton> buttons = new ArrayList<>();
    private List<JLabel> labels = new ArrayList<>();
    private List<JList> listBoxes = new ArrayList<>();
    private List<JComboBox> comboboxes = new ArrayList<>();
    private List<JCheckBox> checkboxes = new ArrayList<>();
    private List<JMenuItem> menuItems = new ArrayList<>();
    
    private Path directoryPath;
    private Path subDirectoryPath;
    private Path filePath;
    private String selectedFile;
    private List<String> extensions = null;
    
    private DefaultListModel<String> listFolders = new DefaultListModel<>();
    private DefaultListModel<String> listFiles = new DefaultListModel<>();
    private DefaultListModel<String> listExtensions = new DefaultListModel<>();
    
    private SimpleDateFormat dateFormatter = new SimpleDateFormat();
    private JCalendar calendar = new JCalendar();

    private boolean addDate;
    private Date date = Calendar.getInstance().getTime();
    private DateFormat dateFormat = DateFormat.YYYYMMDD;
    private DatePosition datePosition = DatePosition.BEFORE_PREFIX;
    private String dateFormatted;
    
    private Separator separator = Separator.UNDERSCORE;
    private String prefix;
    private String suffix;
    private String fileNamePreview;
    
    private List<String> listFilesToRename;
    
    private Stack<Path> stackNext = new Stack();
    private Stack<Path> stackPrev = new Stack();
    
    /**
     * Creates new form MainPanel
     */
    public MainPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
        initialize();
        listComponents();
        checkAddDate();
        dirVisibility();
        isInitializing = false;
    }
    
    private void initialize() {
        isInitializing = true;
        addCalendar();
        cbxDateFormat.removeAllItems();
        cbxDatePosition.removeAllItems();
        cbxSeparator.removeAllItems();
        for (DateFormat format : DateFormat.values()) {
            cbxDateFormat.addItem(format.getDescription());
        }
        for (DatePosition position : DatePosition.values()) {
            cbxDatePosition.addItem(position.getDescription());
        }
        for (Separator separator : Separator.values()) {
            cbxSeparator.addItem(separator.getDescription());
        }
        cbxDateFormat.setSelectedItem(dateFormat.getDescription());
        cbxDatePosition.setSelectedItem(datePosition.getDescription());
        cbxSeparator.setSelectedItem(separator.getDescription());
        dateFormatter.applyPattern(dateFormat.getFormat());
        isInitializing = false;
    }
    
    private void addCalendar() {
        pnlCalendar.setLayout(new FlowLayout());
        if (pnlCalendar.getComponentCount() == 1) {
            pnlCalendar.remove(calendar);
        }
        calendar = new JCalendar();
        calendar.setDate(date);
        pnlCalendar.add(calendar);
        calendar.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            dateChanged();
        });
    }
    
    private void listComponents() {
        LOGGER.info(Locale.getDefault().getCountry());
        
        menuItems.add(menuOpenFile);
        menuItems.add(menuOpenFolder);
        menuItems.add(menuRemoveFile);
        menuItems.add(menuRemoveFolder);
        menuItems.add(menuRenameFile);
        menuItems.add(menuRenameFolder);
        
        labels.add(lblFolder);
        labels.add(lblSettings);
        labels.add(lblPreviewFileName);
        labels.add(lblPrefix);
        labels.add(lblSuffix);
        labels.add(lblDirectoryPath);
        labels.add(lblSeparator);
        labels.add(lblDate);
        labels.add(lblDateFormat);
        labels.add(lblDatePosition);
        
        buttons.add(btnChooseFolder);
        buttons.add(btnNext);
        buttons.add(btnPrev);
        buttons.add(btnRenameFiles);
        buttons.add(btnRenameFileSelected);
        
        listBoxes.add(lbxFolders);
        listBoxes.add(lbxFiles);
        listBoxes.add(lbxExtensions);
        
        comboboxes.add(cbxDateFormat);
        comboboxes.add(cbxDatePosition);
        comboboxes.add(cbxSeparator);
        
        checkboxes.add(checkAddDate);
    }
    
    public void changeLanguage() {
        Localization.setLanguageForMenuItems(menuItems);
        Localization.setLanguageForLabels(labels);
        Localization.setLanguageForButtons(buttons);
        Localization.setLanguageForListboxes(listBoxes);
        Localization.setLanguageForComboboxes(comboboxes);
        Localization.setLanguageForCheckboxes(checkboxes);
        initialize();
    }
    
    private void chooseFolder() {
        try {
            directoryPath = Directories.openDirectoryChooser(directoryPath);
            tbxDirectoryPath.setText(directoryPath.toString());
            setDirectoryPath();
            stackNext = new Stack();
            stackPrev = new Stack();
            dirVisibility();
        } catch (Exception e) {
            
        }
    }
    
    private void setDirectoryPath() {
        if (!tbxDirectoryPath.getText().isEmpty()) {
            Path path;
            try {
                path = Path.of(tbxDirectoryPath.getText());
                if (Files.exists(path, LinkOption.NOFOLLOW_LINKS) && Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                    directoryPath = path;
                    listFolders();
                } else {
                    chooseFolder();
                }
            } catch (Exception e) {
                chooseFolder();
            }
        } else {
            chooseFolder();
        }
    }
    
    private void listFolders() {
        listFolders.clear();
        listFolders.addAll(Directories.listDirectories(directoryPath));
        lbxFolders.setModel(listFolders);
        if (!listFolders.isEmpty()) {
            lbxFolders.setSelectedIndex(0);
        }
        listExtensions();
        listFiles();
    }
    
    private void listFiles() {
        listFiles.clear();
        listFiles.addAll(Directories.listFiles(directoryPath, extensions));
        lbxFiles.setModel(listFiles);
        if (!listFiles.isEmpty()) {
            lbxFiles.setSelectedIndex(0);
        }
    }
    
    private void listExtensions() {
        isInitializing = true;
        listExtensions.clear();
        listExtensions.addAll(Directories.listExtensions(directoryPath));
        lbxExtensions.setModel(listExtensions);
        if (!listExtensions.isEmpty()) {
            int[] indices = new int[listExtensions.size()];
            for (int i = 0; i<listExtensions.size(); i++) {
                indices[i] = i;
            }
            lbxExtensions.setSelectedIndices(indices);
        }
        isInitializing = false;
    }
    
    private void filterExtensions() {
        if (!isInitializing) {
            extensions = lbxExtensions.getSelectedValuesList();
            listFiles();
        }
    }
    
    private void selectFolder() {
        if (lbxFolders.getSelectedValue() != null) {
            subDirectoryPath = Path.of(directoryPath.toString(), lbxFolders.getSelectedValue());
            extensions = null;
        }
    }
    
    private void selectFile() {
        if (lbxFiles.getSelectedValue() != null) {
            listFilesToRename = lbxFiles.getSelectedValuesList();
            filePath = Path.of(directoryPath.toString(), lbxFiles.getSelectedValue());
            selectedFile = lbxFiles.getSelectedValue();
            previewFileName();
        }
    }
    
    private void stepIntoFolder() {
        stackPrev.add(directoryPath);
        directoryPath = subDirectoryPath;
        tbxDirectoryPath.setText(directoryPath.toString());
        listFolders();
        dirVisibility();
    }
    
    private void prevDir() {
        stackNext.add(directoryPath);
        directoryPath = stackPrev.pop();
        tbxDirectoryPath.setText(directoryPath.toString());
        setDirectoryPath();
        dirVisibility();
    }
    
    private void nextDir() {
        stackPrev.add(directoryPath);
        directoryPath = stackNext.pop();
        tbxDirectoryPath.setText(directoryPath.toString());
        setDirectoryPath();
        dirVisibility();
    }
    
    private void dirVisibility() {
        btnPrev.setVisible(!stackPrev.empty());
        btnNext.setVisible(!stackNext.empty());
    }
    
    private void checkAddDate() {
        addDate = checkAddDate.isSelected();
        pnlDate.setVisible(addDate);
        previewFileName();
    }
    
    private void dateChanged() {
        date = calendar.getDate();
        dateFormatted = dateFormatter.format(date);
        tbxDate.setText(dateFormatted);
        previewFileName();
    }
    
    private void dateFormatChanged() {
        if (!isInitializing) {
            dateFormat = DateFormat.getValueByDescription(cbxDateFormat.getSelectedItem().toString());
            dateFormatter.applyPattern(dateFormat.getFormat());
            dateChanged();
        }
    }
    
    private void datePositionChanged() {
        if (!isInitializing) {
            datePosition = DatePosition.getValueByDescription(cbxDatePosition.getSelectedItem().toString());
            previewFileName();
        }
    }
    
    private void separatorChanged() {
        if (!isInitializing) {
            separator = Separator.getValueByDescription(cbxSeparator.getSelectedItem().toString());
            previewFileName();
        }
    }
    
    
    private void previewFileName() {
        prefix = tbxPrefix.getText();
        suffix = tbxSuffix.getText();
        if (lbxFiles.getSelectedValue() != null) {
            fileNamePreview = Renamer.newFileName(filePath, prefix, suffix, separator, addDate, date, dateFormat, datePosition);
        } else {
            fileNamePreview = "";
        }
        tbxPreviewFileName.setText(fileNamePreview);
    }
    
    private void renameFilesSelected() {
//        if (Renamer.renameFile(filePath, fileNamePreview)) {
//            listFiles();
//        }
        listFilesToRename = lbxFiles.getSelectedValuesList();
        ThisRenamerThread thread = new ThisRenamerThread();
        thread.start();
    }
    
    private void renameFiles() {
        listFilesToRename = Collections.list(listFiles.elements());
        ThisRenamerThread thread = new ThisRenamerThread();
        thread.start();
    }
    
    private class ThisRenamerThread extends Thread {
        
        @Override
        public void run() {
            RenamerThread thread = new RenamerThread();
            thread.setListFiles(listFilesToRename);
            thread.setAddDate(addDate);
            thread.setDate(date);
            thread.setDateFormat(dateFormat);
            thread.setDatePosition(datePosition);
            thread.setExtensions(extensions);
            thread.setPath(directoryPath);
            thread.setPrefix(prefix);
            thread.setSuffix(suffix);
            thread.setSeparator(separator);
            thread.start();
            try {
                thread.join();
                listFolders();
            } catch (InterruptedException ex) {
                LOGGER.error("InterruptedException: ", ex.getMessage());
            }
        }
        
    }
    
    private void openFolder() {
        Directories.openDirectory(subDirectoryPath);
    }
    
    private void openFile() {
        Directories.openFile(filePath);
    }
    
    private void renameFolder() {
        if (Directories.renameDirectory(subDirectoryPath)) {
            listFolders();
        }
    }
    
    private void renameFile() {
        if (Directories.renameFile(filePath)) {
            listFiles();
        }
    }
    
    private void deleteFolder() {
        if (Directories.deleteDirectory(subDirectoryPath, true)) {
            listFolders();
        }
    }
    
    private void deleteFile() {
        if (Directories.deleteFile(filePath, true)) {
            listFiles();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupFolders = new javax.swing.JPopupMenu();
        menuOpenFolder = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuRenameFolder = new javax.swing.JMenuItem();
        menuRemoveFolder = new javax.swing.JMenuItem();
        popupFiles = new javax.swing.JPopupMenu();
        menuOpenFile = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuRenameFile = new javax.swing.JMenuItem();
        menuRemoveFile = new javax.swing.JMenuItem();
        pnlDirectoryPath = new javax.swing.JPanel();
        lblDirectoryPath = new javax.swing.JLabel();
        tbxDirectoryPath = new javax.swing.JTextField();
        btnChooseFolder = new javax.swing.JButton();
        pnlLists = new javax.swing.JPanel();
        scrollPaneFolders = new javax.swing.JScrollPane();
        lbxFolders = new javax.swing.JList<>();
        scrollPaneFiles = new javax.swing.JScrollPane();
        lbxFiles = new javax.swing.JList<>();
        lblFolder = new javax.swing.JLabel();
        scrollPaneExtensions = new javax.swing.JScrollPane();
        lbxExtensions = new javax.swing.JList<>();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        pnlActions = new javax.swing.JPanel();
        lblPreviewFileName = new javax.swing.JLabel();
        tbxPreviewFileName = new javax.swing.JTextField();
        btnRenameFileSelected = new javax.swing.JButton();
        btnRenameFiles = new javax.swing.JButton();
        pnlSettings = new javax.swing.JPanel();
        lblSettings = new javax.swing.JLabel();
        lblPrefix = new javax.swing.JLabel();
        tbxPrefix = new javax.swing.JTextField();
        tbxSuffix = new javax.swing.JTextField();
        lblSuffix = new javax.swing.JLabel();
        checkAddDate = new javax.swing.JCheckBox();
        pnlDate = new javax.swing.JPanel();
        tbxDate = new javax.swing.JTextField();
        lblDate = new javax.swing.JLabel();
        cbxDateFormat = new javax.swing.JComboBox<>();
        lblDateFormat = new javax.swing.JLabel();
        cbxDatePosition = new javax.swing.JComboBox<>();
        lblDatePosition = new javax.swing.JLabel();
        pnlCalendar = new javax.swing.JPanel();
        cbxSeparator = new javax.swing.JComboBox<>();
        lblSeparator = new javax.swing.JLabel();

        menuOpenFolder.setText("Open folder");
        menuOpenFolder.setName("menuOpenFolder"); // NOI18N
        menuOpenFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenFolderActionPerformed(evt);
            }
        });
        popupFolders.add(menuOpenFolder);
        popupFolders.add(jSeparator1);

        menuRenameFolder.setText("Rename folder");
        menuRenameFolder.setName("menuRenameFolder"); // NOI18N
        menuRenameFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRenameFolderActionPerformed(evt);
            }
        });
        popupFolders.add(menuRenameFolder);

        menuRemoveFolder.setText("Delete folder");
        menuRemoveFolder.setName("menuRemoveFolder"); // NOI18N
        menuRemoveFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveFolderActionPerformed(evt);
            }
        });
        popupFolders.add(menuRemoveFolder);

        menuOpenFile.setText("Open file");
        menuOpenFile.setName("menuOpenFile"); // NOI18N
        menuOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenFileActionPerformed(evt);
            }
        });
        popupFiles.add(menuOpenFile);
        popupFiles.add(jSeparator2);

        menuRenameFile.setText("Rename file");
        menuRenameFile.setName("menuRenameFile"); // NOI18N
        menuRenameFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRenameFileActionPerformed(evt);
            }
        });
        popupFiles.add(menuRenameFile);

        menuRemoveFile.setText("Delete file");
        menuRemoveFile.setName("menuRemoveFile"); // NOI18N
        menuRemoveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveFileActionPerformed(evt);
            }
        });
        popupFiles.add(menuRemoveFile);

        setMinimumSize(new java.awt.Dimension(800, 615));
        setName("MainPanel"); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 615));

        lblDirectoryPath.setText("Directory");
        lblDirectoryPath.setName("lblDirectoryPath"); // NOI18N

        tbxDirectoryPath.setName("tbxDirectoryPath"); // NOI18N
        tbxDirectoryPath.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbxDirectoryPathKeyPressed(evt);
            }
        });

        btnChooseFolder.setText("Choose folder");
        btnChooseFolder.setName("btnChooseFolder"); // NOI18N
        btnChooseFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseFolderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDirectoryPathLayout = new javax.swing.GroupLayout(pnlDirectoryPath);
        pnlDirectoryPath.setLayout(pnlDirectoryPathLayout);
        pnlDirectoryPathLayout.setHorizontalGroup(
            pnlDirectoryPathLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDirectoryPathLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDirectoryPath, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbxDirectoryPath)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnChooseFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlDirectoryPathLayout.setVerticalGroup(
            pnlDirectoryPathLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDirectoryPathLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDirectoryPathLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDirectoryPath)
                    .addComponent(tbxDirectoryPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChooseFolder))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbxFolders.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lbxFolders.setComponentPopupMenu(popupFolders);
        lbxFolders.setName("lbxFolders"); // NOI18N
        lbxFolders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbxFoldersMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbxFoldersMousePressed(evt);
            }
        });
        lbxFolders.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lbxFoldersKeyPressed(evt);
            }
        });
        lbxFolders.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lbxFoldersValueChanged(evt);
            }
        });
        scrollPaneFolders.setViewportView(lbxFolders);

        lbxFiles.setComponentPopupMenu(popupFiles);
        lbxFiles.setName("lbxFiles"); // NOI18N
        lbxFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbxFilesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbxFilesMousePressed(evt);
            }
        });
        lbxFiles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lbxFilesKeyPressed(evt);
            }
        });
        lbxFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lbxFilesValueChanged(evt);
            }
        });
        scrollPaneFiles.setViewportView(lbxFiles);

        lblFolder.setText("Folder");
        lblFolder.setName("lblFolder"); // NOI18N

        lbxExtensions.setName("lbxExtensions"); // NOI18N
        lbxExtensions.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lbxExtensionsValueChanged(evt);
            }
        });
        scrollPaneExtensions.setViewportView(lbxExtensions);

        btnNext.setText(">");
        btnNext.setName("btnNextDir"); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setText("<");
        btnPrev.setName("btnPrevDir"); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlListsLayout = new javax.swing.GroupLayout(pnlLists);
        pnlLists.setLayout(pnlListsLayout);
        pnlListsLayout.setHorizontalGroup(
            pnlListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneExtensions, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(scrollPaneFolders, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(scrollPaneFiles, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnlListsLayout.createSequentialGroup()
                        .addComponent(lblFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlListsLayout.setVerticalGroup(
            pnlListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlListsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlListsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFolder)
                    .addComponent(btnNext)
                    .addComponent(btnPrev))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneFolders, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneFiles, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneExtensions, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblPreviewFileName.setText("Preview");
        lblPreviewFileName.setName("lblPreviewFileName"); // NOI18N

        tbxPreviewFileName.setEditable(false);
        tbxPreviewFileName.setName("tbxPreviewFileName"); // NOI18N

        btnRenameFileSelected.setText("Rename selected files");
        btnRenameFileSelected.setName("btnRenameFileSelected"); // NOI18N
        btnRenameFileSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameFileSelectedActionPerformed(evt);
            }
        });

        btnRenameFiles.setText("Rename files");
        btnRenameFiles.setName("btnRenameFiles"); // NOI18N
        btnRenameFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlActionsLayout = new javax.swing.GroupLayout(pnlActions);
        pnlActions.setLayout(pnlActionsLayout);
        pnlActionsLayout.setHorizontalGroup(
            pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlActionsLayout.createSequentialGroup()
                        .addComponent(btnRenameFileSelected, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRenameFiles, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                    .addGroup(pnlActionsLayout.createSequentialGroup()
                        .addComponent(lblPreviewFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbxPreviewFileName)))
                .addContainerGap())
        );
        pnlActionsLayout.setVerticalGroup(
            pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlActionsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPreviewFileName)
                    .addComponent(tbxPreviewFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRenameFileSelected)
                    .addComponent(btnRenameFiles))
                .addContainerGap())
        );

        lblSettings.setText("Settings");
        lblSettings.setName("lblSettings"); // NOI18N

        lblPrefix.setText("Prefix");
        lblPrefix.setName("lblPrefix"); // NOI18N

        tbxPrefix.setName("tbxPrefix"); // NOI18N
        tbxPrefix.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbxPrefixKeyReleased(evt);
            }
        });

        tbxSuffix.setName("tbxSuffix"); // NOI18N
        tbxSuffix.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbxSuffixKeyReleased(evt);
            }
        });

        lblSuffix.setText("Suffix");
        lblSuffix.setName("lblSuffix"); // NOI18N

        checkAddDate.setText("Add date");
        checkAddDate.setName("checkAddDate"); // NOI18N
        checkAddDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAddDateActionPerformed(evt);
            }
        });

        pnlDate.setName("pnlDate"); // NOI18N

        tbxDate.setEditable(false);
        tbxDate.setName("tbxDate"); // NOI18N

        lblDate.setText("Date");
        lblDate.setName("lblDate"); // NOI18N

        cbxDateFormat.setName("cbxDateFormat"); // NOI18N
        cbxDateFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxDateFormatActionPerformed(evt);
            }
        });

        lblDateFormat.setText("Format");
        lblDateFormat.setName("lblDateFormat"); // NOI18N

        cbxDatePosition.setName("cbxDatePosition"); // NOI18N
        cbxDatePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxDatePositionActionPerformed(evt);
            }
        });

        lblDatePosition.setText("Position");
        lblDatePosition.setName("lblDatePosition"); // NOI18N

        pnlCalendar.setName("pnlCalendar"); // NOI18N

        javax.swing.GroupLayout pnlCalendarLayout = new javax.swing.GroupLayout(pnlCalendar);
        pnlCalendar.setLayout(pnlCalendarLayout);
        pnlCalendarLayout.setHorizontalGroup(
            pnlCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        pnlCalendarLayout.setVerticalGroup(
            pnlCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlDateLayout = new javax.swing.GroupLayout(pnlDate);
        pnlDate.setLayout(pnlDateLayout);
        pnlDateLayout.setHorizontalGroup(
            pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblDateFormat, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblDatePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxDateFormat, 0, 120, Short.MAX_VALUE)
                    .addComponent(cbxDatePosition, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tbxDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlDateLayout.setVerticalGroup(
            pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDateLayout.createSequentialGroup()
                        .addGroup(pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbxDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxDateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDateFormat))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxDatePosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDatePosition))
                        .addGap(0, 90, Short.MAX_VALUE))
                    .addComponent(pnlCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        cbxSeparator.setName("cbxSeparator"); // NOI18N
        cbxSeparator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSeparatorActionPerformed(evt);
            }
        });

        lblSeparator.setText("Separator");
        lblSeparator.setName("lblSeparator"); // NOI18N

        javax.swing.GroupLayout pnlSettingsLayout = new javax.swing.GroupLayout(pnlSettings);
        pnlSettings.setLayout(pnlSettingsLayout);
        pnlSettingsLayout.setHorizontalGroup(
            pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkAddDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSettingsLayout.createSequentialGroup()
                        .addComponent(lblSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxSeparator, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlSettingsLayout.createSequentialGroup()
                        .addComponent(lblSuffix, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbxSuffix))
                    .addGroup(pnlSettingsLayout.createSequentialGroup()
                        .addComponent(lblPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbxPrefix)))
                .addContainerGap())
        );
        pnlSettingsLayout.setVerticalGroup(
            pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSettings)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSeparator))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrefix)
                    .addComponent(tbxPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSuffix)
                    .addComponent(tbxSuffix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(checkAddDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDirectoryPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlLists, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlActions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDirectoryPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlLists, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooseFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseFolderActionPerformed
        chooseFolder();
    }//GEN-LAST:event_btnChooseFolderActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        prevDir();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        nextDir();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnRenameFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameFilesActionPerformed
        renameFiles();
    }//GEN-LAST:event_btnRenameFilesActionPerformed

    private void lbxFoldersValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lbxFoldersValueChanged
        selectFolder();
    }//GEN-LAST:event_lbxFoldersValueChanged

    private void lbxFoldersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lbxFoldersKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            stepIntoFolder();
        }
    }//GEN-LAST:event_lbxFoldersKeyPressed

    private void lbxFoldersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbxFoldersMouseClicked
        if (evt.getClickCount() == 2) {
            stepIntoFolder();
        }
    }//GEN-LAST:event_lbxFoldersMouseClicked

    private void lbxFoldersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbxFoldersMousePressed
        if (evt.getButton() == MouseEvent.BUTTON3) {
            int index = lbxFolders.locationToIndex(evt.getPoint());
            if (index > -1) {
                lbxFolders.setSelectedIndex(index);
            }
        }
    }//GEN-LAST:event_lbxFoldersMousePressed

    private void lbxFilesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lbxFilesValueChanged
        selectFile();
    }//GEN-LAST:event_lbxFilesValueChanged

    private void lbxFilesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lbxFilesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            openFile();
        }
    }//GEN-LAST:event_lbxFilesKeyPressed

    private void lbxFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbxFilesMouseClicked
        if (evt.getClickCount() == 2) {
            openFile();
        }
    }//GEN-LAST:event_lbxFilesMouseClicked

    private void lbxFilesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbxFilesMousePressed
        if (evt.getButton() == MouseEvent.BUTTON3) {
            int index = lbxFiles.locationToIndex(evt.getPoint());
            if (index > -1) {
                lbxFiles.setSelectedIndex(index);
            }
        }
    }//GEN-LAST:event_lbxFilesMousePressed

    private void menuRenameFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRenameFolderActionPerformed
        renameFolder();
    }//GEN-LAST:event_menuRenameFolderActionPerformed

    private void menuRemoveFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveFolderActionPerformed
        deleteFolder();
    }//GEN-LAST:event_menuRemoveFolderActionPerformed

    private void menuRenameFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRenameFileActionPerformed
        renameFile();
    }//GEN-LAST:event_menuRenameFileActionPerformed

    private void menuRemoveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveFileActionPerformed
        deleteFile();
    }//GEN-LAST:event_menuRemoveFileActionPerformed

    private void checkAddDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAddDateActionPerformed
        checkAddDate();
    }//GEN-LAST:event_checkAddDateActionPerformed

    private void cbxDateFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxDateFormatActionPerformed
        dateFormatChanged();
    }//GEN-LAST:event_cbxDateFormatActionPerformed

    private void cbxDatePositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxDatePositionActionPerformed
        datePositionChanged();
    }//GEN-LAST:event_cbxDatePositionActionPerformed

    private void tbxDirectoryPathKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbxDirectoryPathKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setDirectoryPath();
        }
    }//GEN-LAST:event_tbxDirectoryPathKeyPressed

    private void menuOpenFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenFolderActionPerformed
        openFolder();
    }//GEN-LAST:event_menuOpenFolderActionPerformed

    private void menuOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenFileActionPerformed
        openFile();
    }//GEN-LAST:event_menuOpenFileActionPerformed

    private void lbxExtensionsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lbxExtensionsValueChanged
        filterExtensions();
    }//GEN-LAST:event_lbxExtensionsValueChanged

    private void btnRenameFileSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameFileSelectedActionPerformed
        renameFilesSelected();
    }//GEN-LAST:event_btnRenameFileSelectedActionPerformed

    private void tbxPrefixKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbxPrefixKeyReleased
        previewFileName();
    }//GEN-LAST:event_tbxPrefixKeyReleased

    private void tbxSuffixKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbxSuffixKeyReleased
        previewFileName();
    }//GEN-LAST:event_tbxSuffixKeyReleased

    private void cbxSeparatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSeparatorActionPerformed
        separatorChanged();
    }//GEN-LAST:event_cbxSeparatorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseFolder;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnRenameFileSelected;
    private javax.swing.JButton btnRenameFiles;
    private javax.swing.JComboBox<String> cbxDateFormat;
    private javax.swing.JComboBox<String> cbxDatePosition;
    private javax.swing.JComboBox<String> cbxSeparator;
    private javax.swing.JCheckBox checkAddDate;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateFormat;
    private javax.swing.JLabel lblDatePosition;
    private javax.swing.JLabel lblDirectoryPath;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JLabel lblPrefix;
    private javax.swing.JLabel lblPreviewFileName;
    private javax.swing.JLabel lblSeparator;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblSuffix;
    private javax.swing.JList<String> lbxExtensions;
    private javax.swing.JList<String> lbxFiles;
    private javax.swing.JList<String> lbxFolders;
    private javax.swing.JMenuItem menuOpenFile;
    private javax.swing.JMenuItem menuOpenFolder;
    private javax.swing.JMenuItem menuRemoveFile;
    private javax.swing.JMenuItem menuRemoveFolder;
    private javax.swing.JMenuItem menuRenameFile;
    private javax.swing.JMenuItem menuRenameFolder;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlCalendar;
    private javax.swing.JPanel pnlDate;
    private javax.swing.JPanel pnlDirectoryPath;
    private javax.swing.JPanel pnlLists;
    private javax.swing.JPanel pnlSettings;
    private javax.swing.JPopupMenu popupFiles;
    private javax.swing.JPopupMenu popupFolders;
    private javax.swing.JScrollPane scrollPaneExtensions;
    private javax.swing.JScrollPane scrollPaneFiles;
    private javax.swing.JScrollPane scrollPaneFolders;
    private javax.swing.JTextField tbxDate;
    private javax.swing.JTextField tbxDirectoryPath;
    private javax.swing.JTextField tbxPrefix;
    private javax.swing.JTextField tbxPreviewFileName;
    private javax.swing.JTextField tbxSuffix;
    // End of variables declaration//GEN-END:variables
}
