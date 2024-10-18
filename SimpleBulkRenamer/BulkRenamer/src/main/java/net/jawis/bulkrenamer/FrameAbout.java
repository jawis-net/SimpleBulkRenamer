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
import java.awt.Image;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import net.jawis.config.Config;
import net.jawis.config.Resources;

/**
 *
 * @author Redbad
 */
public class FrameAbout extends javax.swing.JFrame {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FrameAbout.class);
    
    private static final String PROPERTIES_NAME = "label_text";
    
    private Config conf;
    
    private DefaultListModel<String> listModelApps = new DefaultListModel<>();
    
    /**
     * Creates new form FrameAbout
     */
    public FrameAbout() {
        this.conf = new Config();
        initComponents();
        initialize();
    }
    
    private void initialize() {
        Image icon = new ImageIcon(getClass().getResource("/SimpleBulkRenamer.png")).getImage();
        this.setIconImage(icon);
        
        lblInfoAboutTitle.setText(Resources.getValueFromBundle(PROPERTIES_NAME, "lblInfoAboutTitle"));
        lblInfoAboutExtra.setText(Resources.getValueFromBundle(PROPERTIES_NAME, "lblInfoAboutExtra"));
        lblMoreJawisApps.setText(Resources.getValueFromBundle(PROPERTIES_NAME, "lblMoreJawisApps"));
            
        if (conf.isFetched()) {
            List<String> apps = conf.getListOtherApps();
            listModelApps.addAll(apps);
            lbxMoreApps.setModel(listModelApps);
        }
        
        tbxWebsite.setText(conf.getUrl());
        tbxWebsiteGit.setText(conf.getUrlGit());
        tbxReleaseDateLatest.setText(conf.getReleaseDateLatest());
        tbxVersionLatest.setText(conf.getVersionLatest());
        tbxVersionCurrent.setText(conf.getVersionCurrent());
        tbxReleaseDateCurrent.setText(conf.getReleaseDateCurrent());
        
        this.setVisible(true);
    }
    
    private void appChanged() {
        tbxMoreJawisAppsWebsite.setText(conf.getOtherAppUrl(lbxMoreApps.getSelectedIndex()));
    }
    
    private void goToWebsite(String url) {
        url = prepareURL(url);
        if(isDesktopSupported()) {
            openURLinBrowserUsingDesktop(url);
        } else {
            openURLinBrowserUsingRuntime(url);
        }
    }
    private static boolean isDesktopSupported() {
        boolean isDesktopSupported = Desktop.isDesktopSupported();
        return isDesktopSupported;
    }
    private static void openURLinBrowserUsingDesktop(String url) {
        url = prepareURL(url);
        Desktop desktop = Desktop.getDesktop();
        if(desktop.isSupported(Desktop.Action.OPEN)) {
            try {
                desktop.browse(URI.create(url));
            } catch (IOException ex) {
                
            }
        }
    }
    private static void openURLinBrowserUsingRuntime(String url) {
        url = prepareURL(url);
        Process p;
        try {
            p = Runtime.getRuntime().exec("cmd /c start " + url);
        } catch (IOException ex) {
            
        }
    }
    private static String prepareURL(String url) {
            if(url.isEmpty()) {
                url = "www.jawis.net";
            } else {
                if(!url.startsWith("www.") && !url.startsWith("http://") && !url.startsWith("https://")
                        && !url.startsWith("http://www.") && !url.startsWith("https://www.")) {
                    url = "www.".concat(url);
                }
            }
            return url;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlInfoAbout = new javax.swing.JPanel();
        lblVersionCurrent = new javax.swing.JLabel();
        tbxVersionCurrent = new javax.swing.JTextField();
        lblReleaseDateCurrent = new javax.swing.JLabel();
        lblWebsite = new javax.swing.JLabel();
        lblWebsiteGit = new javax.swing.JLabel();
        lblInfoAboutTitle = new javax.swing.JLabel();
        tbxReleaseDateCurrent = new javax.swing.JTextField();
        tbxWebsite = new javax.swing.JTextField();
        tbxWebsiteGit = new javax.swing.JTextField();
        lblInfoAboutExtra = new javax.swing.JLabel();
        lblReleaseDateCurrentSuffix = new javax.swing.JLabel();
        lblVersionCurrentSuffix = new javax.swing.JLabel();
        lblReleaseDateLatestSuffix = new javax.swing.JLabel();
        lblVersionLatestSuffix = new javax.swing.JLabel();
        tbxReleaseDateLatest = new javax.swing.JTextField();
        lblVersionLatest = new javax.swing.JLabel();
        tbxVersionLatest = new javax.swing.JTextField();
        lblReleaseDateLatest = new javax.swing.JLabel();
        lblLicense = new javax.swing.JLabel();
        tbxLicense = new javax.swing.JTextField();
        pnlMoreJawisApps = new javax.swing.JPanel();
        lblMoreJawisApps = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lbxMoreApps = new javax.swing.JList<>();
        lblMoreJawisAppsWebsite = new javax.swing.JLabel();
        tbxMoreJawisAppsWebsite = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Info");
        setAlwaysOnTop(true);

        lblVersionCurrent.setText("Current version");

        tbxVersionCurrent.setEditable(false);

        lblReleaseDateCurrent.setText("Release date");

        lblWebsite.setText("Website");

        lblWebsiteGit.setText("GitHub");

        lblInfoAboutTitle.setText("SimpleBulkRenamer developed by jawis.net");

        tbxReleaseDateCurrent.setEditable(false);

        tbxWebsite.setEditable(false);
        tbxWebsite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbxWebsiteMouseClicked(evt);
            }
        });

        tbxWebsiteGit.setEditable(false);
        tbxWebsiteGit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbxWebsiteGitMouseClicked(evt);
            }
        });

        lblInfoAboutExtra.setText("Extra info");

        lblReleaseDateCurrentSuffix.setText(" ");

        lblVersionCurrentSuffix.setText(" ");

        lblReleaseDateLatestSuffix.setText(" ");

        lblVersionLatestSuffix.setText(" ");

        tbxReleaseDateLatest.setEditable(false);

        lblVersionLatest.setText("Latest version");

        tbxVersionLatest.setEditable(false);

        lblReleaseDateLatest.setText("Release date");

        lblLicense.setText("License");

        tbxLicense.setEditable(false);
        tbxLicense.setText("https://opensource.org/licenses/MIT");
        tbxLicense.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbxLicenseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlInfoAboutLayout = new javax.swing.GroupLayout(pnlInfoAbout);
        pnlInfoAbout.setLayout(pnlInfoAboutLayout);
        pnlInfoAboutLayout.setHorizontalGroup(
            pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInfoAboutTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                        .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblVersionCurrent, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(lblReleaseDateCurrent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                                .addComponent(tbxVersionCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVersionCurrentSuffix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                                .addComponent(tbxReleaseDateCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblReleaseDateCurrentSuffix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(lblInfoAboutExtra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                        .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblWebsite, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(lblWebsiteGit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tbxWebsite)
                            .addComponent(tbxWebsiteGit)))
                    .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                        .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblVersionLatest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblReleaseDateLatest, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                                .addComponent(tbxVersionLatest, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVersionLatestSuffix, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                            .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                                .addComponent(tbxReleaseDateLatest, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblReleaseDateLatestSuffix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                        .addComponent(lblLicense, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbxLicense)))
                .addContainerGap())
        );
        pnlInfoAboutLayout.setVerticalGroup(
            pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoAboutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoAboutTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVersionCurrent)
                    .addComponent(tbxVersionCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVersionCurrentSuffix))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbxReleaseDateCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReleaseDateCurrent)
                    .addComponent(lblReleaseDateCurrentSuffix))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVersionLatest)
                    .addComponent(tbxVersionLatest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVersionLatestSuffix))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbxReleaseDateLatest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReleaseDateLatest)
                    .addComponent(lblReleaseDateLatestSuffix))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbxLicense, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLicense))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbxWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWebsite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbxWebsiteGit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWebsiteGit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblInfoAboutExtra)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblMoreJawisApps.setText("More applications from jawis.net");

        lbxMoreApps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lbxMoreApps.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lbxMoreAppsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lbxMoreApps);

        lblMoreJawisAppsWebsite.setText("Website");

        tbxMoreJawisAppsWebsite.setEditable(false);
        tbxMoreJawisAppsWebsite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbxMoreJawisAppsWebsiteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlMoreJawisAppsLayout = new javax.swing.GroupLayout(pnlMoreJawisApps);
        pnlMoreJawisApps.setLayout(pnlMoreJawisAppsLayout);
        pnlMoreJawisAppsLayout.setHorizontalGroup(
            pnlMoreJawisAppsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMoreJawisAppsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMoreJawisAppsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMoreJawisAppsLayout.createSequentialGroup()
                        .addComponent(lblMoreJawisAppsWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbxMoreJawisAppsWebsite))
                    .addComponent(lblMoreJawisApps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        pnlMoreJawisAppsLayout.setVerticalGroup(
            pnlMoreJawisAppsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMoreJawisAppsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMoreJawisApps)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMoreJawisAppsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbxMoreJawisAppsWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMoreJawisAppsWebsite))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInfoAbout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMoreJawisApps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInfoAbout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlMoreJawisApps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tbxWebsiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbxWebsiteMouseClicked
        if (evt.getClickCount() == 1) {
            goToWebsite(tbxWebsite.getText());
        }
    }//GEN-LAST:event_tbxWebsiteMouseClicked

    private void tbxWebsiteGitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbxWebsiteGitMouseClicked
        if (evt.getClickCount() == 1) {
            goToWebsite(tbxWebsiteGit.getText());
        }
    }//GEN-LAST:event_tbxWebsiteGitMouseClicked

    private void tbxMoreJawisAppsWebsiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbxMoreJawisAppsWebsiteMouseClicked
        if (evt.getClickCount() == 1) {
            goToWebsite(tbxMoreJawisAppsWebsite.getText());
        }
    }//GEN-LAST:event_tbxMoreJawisAppsWebsiteMouseClicked

    private void lbxMoreAppsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lbxMoreAppsValueChanged
        appChanged();
    }//GEN-LAST:event_lbxMoreAppsValueChanged

    private void tbxLicenseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbxLicenseMouseClicked
        if (evt.getClickCount() == 1) {
            goToWebsite(tbxLicense.getText());
        }
    }//GEN-LAST:event_tbxLicenseMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameAbout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameAbout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameAbout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameAbout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameAbout().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfoAboutExtra;
    private javax.swing.JLabel lblInfoAboutTitle;
    private javax.swing.JLabel lblLicense;
    private javax.swing.JLabel lblMoreJawisApps;
    private javax.swing.JLabel lblMoreJawisAppsWebsite;
    private javax.swing.JLabel lblReleaseDateCurrent;
    private javax.swing.JLabel lblReleaseDateCurrentSuffix;
    private javax.swing.JLabel lblReleaseDateLatest;
    private javax.swing.JLabel lblReleaseDateLatestSuffix;
    private javax.swing.JLabel lblVersionCurrent;
    private javax.swing.JLabel lblVersionCurrentSuffix;
    private javax.swing.JLabel lblVersionLatest;
    private javax.swing.JLabel lblVersionLatestSuffix;
    private javax.swing.JLabel lblWebsite;
    private javax.swing.JLabel lblWebsiteGit;
    private javax.swing.JList<String> lbxMoreApps;
    private javax.swing.JPanel pnlInfoAbout;
    private javax.swing.JPanel pnlMoreJawisApps;
    private javax.swing.JTextField tbxLicense;
    private javax.swing.JTextField tbxMoreJawisAppsWebsite;
    private javax.swing.JTextField tbxReleaseDateCurrent;
    private javax.swing.JTextField tbxReleaseDateLatest;
    private javax.swing.JTextField tbxVersionCurrent;
    private javax.swing.JTextField tbxVersionLatest;
    private javax.swing.JTextField tbxWebsite;
    private javax.swing.JTextField tbxWebsiteGit;
    // End of variables declaration//GEN-END:variables
}
