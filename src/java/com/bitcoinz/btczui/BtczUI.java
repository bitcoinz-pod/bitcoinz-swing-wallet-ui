/************************************************************************************************
 *  ____ _____ ______ ___   __ _____  __   _ _____ _        __    _ _      _   _   _ ___
 * | __ \_   _|_   __| __|/ _ \_   _||   \| |___  | \      / /_ _| | | ___| |_| | | |_ _|
 * |____/ | |   | |/ /   / / \ \| |  | |\ | |  / / \ \ /\ / / _` | | |/ _ \ __| | | || |
 * | ___ \| |_  | |\ \__ \ \_/ /| |_ | | \  | / /_  \ V  V / (_| | | |  __/ |_| |_| || |
 * |_____/____| |_| \____|\___/_____||_|  \_|/____|  \_/\_/ \__,_|_|_|\___|\__|\___/|___|

 * Copyright (c) 2017-2022 BitcoinZ team
 * Copyright (c) 2016 Ivan Vaklinov <ivan@vaklinov.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 **********************************************************************************/
package com.bitcoinz.btczui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bitcoinz.btczui.OSUtil.OS_TYPE;
import com.bitcoinz.btczui.BTCZClientCaller.NetworkAndBlockchainInfo;
import com.bitcoinz.btczui.BTCZClientCaller.WalletCallException;
import com.bitcoinz.btczui.BTCZInstallationObserver.DAEMON_STATUS;
import com.bitcoinz.btczui.BTCZInstallationObserver.DaemonInfo;
import com.bitcoinz.btczui.BTCZInstallationObserver.InstallationDetectionException;
import com.bitcoinz.btczui.msg.MessagingPanel;
import com.bitcoinz.btczui.msg.MessagingStorage;
//import com.bitcoinz.btczui.msg.MessagingOptions;



/**
 * Main BitcoinZ Window.
 *
 * @author Ivan Vaklinov <ivan@vaklinov.com>
 */
public class BtczUI
    extends JFrame
{
    private BTCZInstallationObserver  installationObserver;
    private BTCZClientCaller          clientCaller;
    private StatusUpdateErrorReporter errorReporter;

    private WalletOperations walletOps;

    private JMenuItem menuItemExit;
    private JMenuItem menuItemAbout;
    private JMenuItem menuItemEncrypt;
    private JMenuItem menuItemBackup;
    private JMenuItem menuItemExportKeys;
    private JMenuItem menuItemImportKeys;
    private JMenuItem menuItemShowPrivateKey;
    private JMenuItem menuItemImportOnePrivateKey;
    private JMenuItem menuItemOwnIdentity;
    private JMenuItem menuItemExportOwnIdentity;
    private JMenuItem menuItemImportContactIdentity;
    private JMenuItem menuItemAddMessagingGroup;
    private JMenuItem menuItemRemoveContactIdentity;
    private JMenuItem menuItemMessagingOptions;
    private JMenuItem menuItemShareFileViaIPFS;

    private DashboardPanel   dashboard;
    private AddressesPanel   addresses;
    private SendCashPanel    sendPanel;
    private RawTXPanel       rawTXPanel;
    private AddressBookPanel addressBookPanel;
    private MessagingPanel   messagingPanel;
    private MessagingStorage messagingStorage;
    private List<Image>      imageList;

    JTabbedPane tabs;
    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";

    public BtczUI(StartupProgressDialog progressDialog)
        throws IOException, InterruptedException, WalletCallException
    {
        super("BitcoinZ Wallet 2.0.10");

        if (progressDialog != null)
        {
        	progressDialog.setProgressText("Starting GUI wallet...");
        }

        ClassLoader cl = this.getClass().getClassLoader();

        imageList = new ArrayList();
        imageList.add(new ImageIcon(cl.getResource("images/bitcoinz20x20.png")).getImage());
        imageList.add(new ImageIcon(cl.getResource("images/bitcoinz30x30.png")).getImage());
        imageList.add(new ImageIcon(cl.getResource("images/bitcoinz38x38.png")).getImage());
        imageList.add(new ImageIcon(cl.getResource("images/bitcoinz40x40.png")).getImage());
        imageList.add(new ImageIcon(cl.getResource("images/bitcoinz60x60.png")).getImage());
        imageList.add(new ImageIcon(cl.getResource("images/bitcoinz80x80.png")).getImage());
        imageList.add(new ImageIcon(cl.getResource("images/bitcoinz120x120.png")).getImage());
        imageList.add(new ImageIcon(cl.getResource("images/BitcoinZ.png")).getImage());
        this.setIconImages(imageList);

        Container contentPane = this.getContentPane();

        errorReporter = new StatusUpdateErrorReporter(this);
        installationObserver = new BTCZInstallationObserver(OSUtil.getProgramDirectory());
        clientCaller = new BTCZClientCaller(OSUtil.getProgramDirectory());

        if (installationObserver.isOnTestNet())
        {
        	this.setTitle(this.getTitle() + " [using TESTNET]");
        }

        // Build content
        tabs = new JTabbedPane();
        Font oldTabFont = tabs.getFont();
        Font newTabFont  = new Font(oldTabFont.getName(), Font.BOLD | Font.ITALIC, oldTabFont.getSize() * 57 / 50);
        tabs.setFont(newTabFont);
        BackupTracker backupTracker = new BackupTracker(this);

        tabs.addTab("Overview ",
        		    new ImageIcon(cl.getResource("images/overview.png")),
        		    dashboard = new DashboardPanel(this, installationObserver, clientCaller,
        		    		                       errorReporter, backupTracker));
        tabs.addTab("Own addresses ",
        		    new ImageIcon(cl.getResource("images/own-addresses.png")),
        		    addresses = new AddressesPanel(this, clientCaller, errorReporter));
        tabs.addTab("Send cash ",
        		    new ImageIcon(cl.getResource("images/send.png")),
        		    sendPanel = new SendCashPanel(clientCaller, errorReporter, installationObserver, backupTracker));

        tabs.addTab("Raw Transaction ",
        		    new ImageIcon(cl.getResource("images/send.png")),
        		    rawTXPanel = new RawTXPanel(clientCaller, errorReporter, installationObserver, backupTracker));

        tabs.addTab("Address book ",
    		        new ImageIcon(cl.getResource("images/address-book.png")),
    		        addressBookPanel = new AddressBookPanel(sendPanel, tabs));

        tabs.addTab("Messaging ",
                new ImageIcon(cl.getResource("images/messaging.png")),
                messagingPanel = new MessagingPanel(this, sendPanel, tabs, clientCaller, errorReporter));

        // Disable Messaging tab option
        messagingStorage = new MessagingStorage();
        boolean msgDisabled = messagingStorage.getMessagingOptions().isMessagingDisabled();
        if (msgDisabled) {tabs.setEnabledAt(5,false);}

        contentPane.add(tabs);

        this.walletOps = new WalletOperations(
            	this, tabs, dashboard, addresses, sendPanel,
            	installationObserver, clientCaller, errorReporter, backupTracker);

        int width = 1024;

        OS_TYPE os = OSUtil.getOSType();
        // Window needs to be larger on Mac/Windows - typically
    	if ((os == OS_TYPE.WINDOWS) || (os == OS_TYPE.MAC_OS))
    	{
    		width = 1024;
    	}

        this.setSize(new Dimension(width, 600));

        // Build menu
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("Main");
        file.setMnemonic(KeyEvent.VK_M);
        int accelaratorKeyMask = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask();
        file.add(menuItemAbout = new JMenuItem("About...", KeyEvent.VK_T));
        menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, accelaratorKeyMask));
        file.addSeparator();
        file.add(menuItemExit = new JMenuItem("Quit", KeyEvent.VK_Q));
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, accelaratorKeyMask));
        mb.add(file);

        JMenu wallet = new JMenu("Wallet");
        wallet.setMnemonic(KeyEvent.VK_W);
        wallet.add(menuItemBackup = new JMenuItem("Backup...", KeyEvent.VK_B));
        menuItemBackup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, accelaratorKeyMask));
        wallet.add(menuItemEncrypt = new JMenuItem("Encrypt...", KeyEvent.VK_E));
        menuItemEncrypt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, accelaratorKeyMask));
        wallet.add(menuItemExportKeys = new JMenuItem("Export private keys...", KeyEvent.VK_K));
        menuItemExportKeys.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, accelaratorKeyMask));
        wallet.add(menuItemImportKeys = new JMenuItem("Import private keys...", KeyEvent.VK_I));
        menuItemImportKeys.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, accelaratorKeyMask));
        wallet.add(menuItemShowPrivateKey = new JMenuItem("Show private key...", KeyEvent.VK_P));
        menuItemShowPrivateKey.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, accelaratorKeyMask));
        wallet.add(menuItemImportOnePrivateKey = new JMenuItem("Import one private key...", KeyEvent.VK_N));
        menuItemImportOnePrivateKey.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, accelaratorKeyMask));
        JMenuItem menuItemOpenWalletDirectory = new JMenuItem("Open wallet directory...");
        wallet.add(menuItemOpenWalletDirectory);

        mb.add(wallet);

        JMenu messaging = new JMenu("Messaging");
        messaging.setMnemonic(KeyEvent.VK_S);
        messaging.add(menuItemOwnIdentity = new JMenuItem("Own identity...", KeyEvent.VK_D));
        menuItemOwnIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, accelaratorKeyMask));
        messaging.add(menuItemExportOwnIdentity = new JMenuItem("Export own identity...", KeyEvent.VK_X));
        menuItemExportOwnIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, accelaratorKeyMask));
        messaging.add(menuItemAddMessagingGroup = new JMenuItem("Add messaging group...", KeyEvent.VK_G));
        menuItemAddMessagingGroup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, accelaratorKeyMask));
        messaging.add(menuItemImportContactIdentity = new JMenuItem("Import contact identity...", KeyEvent.VK_Y));
        menuItemImportContactIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, accelaratorKeyMask));
        messaging.add(menuItemRemoveContactIdentity = new JMenuItem("Remove contact...", KeyEvent.VK_R));
        menuItemRemoveContactIdentity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, accelaratorKeyMask));
        messaging.add(menuItemMessagingOptions = new JMenuItem("Options...", KeyEvent.VK_O));
        menuItemMessagingOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, accelaratorKeyMask));

        JMenu shareFileVia = new JMenu("Share file via:");
        shareFileVia.setMnemonic(KeyEvent.VK_V);
        // TODO: uncomment this for IPFS integration
        //messaging.add(shareFileVia);
        shareFileVia.add(menuItemShareFileViaIPFS = new JMenuItem("IPFS", KeyEvent.VK_F));
        menuItemShareFileViaIPFS.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, accelaratorKeyMask));

        // TODO: Move the zMessaging disable option somwhere else.
        // This will allows to also disable the menu.
        //if (!msgDisabled)  {
          mb.add(messaging);
        //}

        // TODO: Temporarily disable encryption until further notice - Oct 24 2016
        // Enabled as own user risk !
        //menuItemEncrypt.setEnabled(false);

        this.setJMenuBar(mb);

        // Add listeners etc.
        menuItemExit.addActionListener(
            new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    BtczUI.this.exitProgram();
                }
            }
        );

        menuItemAbout.addActionListener(
            new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                	try
                	{
                		AboutDialog ad = new AboutDialog(BtczUI.this);
                		ad.setVisible(true);
                	} catch (UnsupportedEncodingException uee)
                	{
                		Log.error("Unexpected error: ", uee);
                		BtczUI.this.errorReporter.reportError(uee);
                	}
                }
            }
        );

        menuItemBackup.addActionListener(
        	new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    BtczUI.this.walletOps.backupWallet();
                }
            }
        );

       menuItemEncrypt.addActionListener(
           new ActionListener()
           {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                   BtczUI.this.walletOps.encryptWallet();
               }
           }
       );

        menuItemExportKeys.addActionListener(
            new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    BtczUI.this.walletOps.exportWalletPrivateKeys();
                }
            }
       );

       menuItemImportKeys.addActionListener(
            new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    BtczUI.this.walletOps.importWalletPrivateKeys();
                }
            }
       );

       menuItemShowPrivateKey.addActionListener(
            new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    BtczUI.this.walletOps.showPrivateKey();
                }
            }
       );

       menuItemImportOnePrivateKey.addActionListener(
           new ActionListener()
           {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                   BtczUI.this.walletOps.importSinglePrivateKey();
               }
           }
       );

       menuItemOpenWalletDirectory.addActionListener(e -> {
                   try {
                       Desktop.getDesktop().open(new File(OSUtil.getBlockchainDirectory()));
                   } catch (IOException e1) {
                       Log.error("Open wallet directory exception.", e1);
                   }
               }
       );

       menuItemOwnIdentity.addActionListener(
               new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
            			BtczUI.this.messagingPanel.openOwnIdentityDialog();
                   }
               }
        );

       menuItemExportOwnIdentity.addActionListener(
               new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
            			BtczUI.this.messagingPanel.exportOwnIdentity();
                   }
               }
        );

       menuItemImportContactIdentity.addActionListener(
               new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
            			BtczUI.this.messagingPanel.importContactIdentity();
                   }
               }
        );

       menuItemAddMessagingGroup.addActionListener(
               new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
            			BtczUI.this.messagingPanel.addMessagingGroup();
                   }
               }
        );

       menuItemRemoveContactIdentity.addActionListener(
               new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
            			BtczUI.this.messagingPanel.removeSelectedContact();
                   }
               }
        );menuItemMessagingOptions.addActionListener(
               new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
            			BtczUI.this.messagingPanel.openOptionsDialog();
                   }
               }
       );

       menuItemShareFileViaIPFS.addActionListener(
               new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
            			BtczUI.this.messagingPanel.shareFileViaIPFS();
                   }
               }
       );

        // Close operation
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                BtczUI.this.exitProgram();
            }
        });

        // Show initial message
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    String userDir = OSUtil.getSettingsDirectory();
                    File warningFlagFile = new File(userDir + File.separator + "initialInfoShown_0.75.flag");
                    if (warningFlagFile.exists())
                    {
                        return;
                    } else
                    {
                        warningFlagFile.createNewFile();
                    }

                } catch (IOException ioe)
                {
                    /* TODO: report exceptions to the user */
                	Log.error("Unexpected error: ", ioe);
                }

                JOptionPane.showMessageDialog(
                    BtczUI.this.getRootPane().getParent(),
                    "The BitcoinZ GUI Wallet is currently considered experimental. Use of this software\n" +
                    "comes at your own risk! Be sure to read the list of known issues and limitations\n" +
                    "at this page: https://github.com/btcz/bitcoinz-wallet\n\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                    "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                    "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                    "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                    "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                    "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
                    "THE SOFTWARE.\n\n" +
                    "(This message will be shown only once, per release)",
                    "Disclaimer", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Finally dispose of the progress dialog
        if (progressDialog != null)
        {
        	progressDialog.doDispose();
        }

        // Notify the messaging TAB that it is being selected - every time
        tabs.addChangeListener(
            new ChangeListener()
            {
    			@Override
    			public void stateChanged(ChangeEvent e)
    			{
    				JTabbedPane tabs = (JTabbedPane)e.getSource();
    				if (tabs.getSelectedIndex() == 4)
    				{
    					BtczUI.this.messagingPanel.tabSelected();
    				}
    			}
    		}
        );

    }

    public void exitProgram()
    {
    	Log.info("Exiting ...");

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        this.dashboard.stopThreadsAndTimers();
        this.addresses.stopThreadsAndTimers();
        this.sendPanel.stopThreadsAndTimers();
        this.messagingPanel.stopThreadsAndTimers();

        BtczUI.this.setVisible(false);
        BtczUI.this.dispose();

        System.exit(0);
    }

    public static void main(String argv[])
        throws IOException
    {
        try
        {
        	OS_TYPE os = OSUtil.getOSType();

        	if ((os == OS_TYPE.WINDOWS) || (os == OS_TYPE.MAC_OS))
        	{
        		possiblyCreateZENConfigFile();
        	}

        	Log.info("Starting BitcoinZ Wallet ...");
        	Log.info("OS: " + System.getProperty("os.name") + " = " + os);
        	Log.info("Current directory: " + new File(".").getCanonicalPath());
        	Log.info("Class path: " + System.getProperty("java.class.path"));
        	Log.info("Environment PATH: " + System.getenv("PATH"));

            // Look and feel settings - a custom OS-look and feel is set for Windows
            if (os == OS_TYPE.WINDOWS)
            {
            	// Custom Windows L&F and font settings
            	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            	// This font looks good but on Windows 7 it misses some chars like the stars...
            	//FontUIResource font = new FontUIResource("Lucida Sans Unicode", Font.PLAIN, 11);
            	//UIManager.put("Table.font", font);
            } else if (os == OS_TYPE.MAC_OS)
            {
            	// The MacOS L&F is active by default - the property sets the menu bar Mac style
            	System.setProperty("apple.laf.useScreenMenuBar", "true");
            }
            else
            {
	            for (LookAndFeelInfo ui : UIManager.getInstalledLookAndFeels())
	            {
	            	Log.info("Available look and feel: " + ui.getName() + " " + ui.getClassName());
	                if (ui.getName().equals("Nimbus"))
	                {
	                	Log.info("Setting look and feel: {0}", ui.getClassName());
	                    UIManager.setLookAndFeel(ui.getClassName());
	                    break;
	                };
	            }
            }

            // If bitcoinzd is currently not running, do a startup of the daemon as a child process
            // It may be started but not ready - then also show dialog
            BTCZInstallationObserver initialInstallationObserver =
            	new BTCZInstallationObserver(OSUtil.getProgramDirectory());
            DaemonInfo bitcoinzdInfo = initialInstallationObserver.getDaemonInfo();
            initialInstallationObserver = null;

            BTCZClientCaller initialClientCaller = new BTCZClientCaller(OSUtil.getProgramDirectory());
            boolean daemonStartInProgress = false;
            try
            {
            	if (bitcoinzdInfo.status == DAEMON_STATUS.RUNNING)
            	{
            		NetworkAndBlockchainInfo info = initialClientCaller.getNetworkAndBlockchainInfo();
            		// If more than 20 minutes behind in the blockchain - startup in progress
            		if ((System.currentTimeMillis() - info.lastBlockDate.getTime()) > (20 * 60 * 1000))
            		{
            			Log.info("Current blockchain synchronization date is "  +
            		                       new Date(info.lastBlockDate.getTime()));
            			daemonStartInProgress = true;
            		}
            	}
            } catch (WalletCallException wce)
            {
                if ((wce.getMessage().indexOf("{\"code\":-28") != -1) || // Started but not ready
                	(wce.getMessage().indexOf("error code: -28") != -1))
                {
                	Log.info("bitcoinzd is currently starting...");
                	daemonStartInProgress = true;
                }
            }

            StartupProgressDialog startupBar = null;
            if ((bitcoinzdInfo.status != DAEMON_STATUS.RUNNING) || (daemonStartInProgress))
            {
            	Log.info(
            		"bitcoinzd is not runing at the moment or has not started/synchronized 100% - showing splash...");
	            startupBar = new StartupProgressDialog(initialClientCaller);
	            startupBar.setVisible(true);
	            startupBar.waitForStartup();
            }
            initialClientCaller = null;

            // Main GUI is created here
            BtczUI ui = new BtczUI(startupBar);
            ui.setVisible(true);

        } catch (InstallationDetectionException ide)
        {
        	Log.error("Unexpected error: ", ide);
            JOptionPane.showMessageDialog(
                null,
                "This program was started in directory: " + OSUtil.getProgramDirectory() + "\n" +
                ide.getMessage() + "\n" +
                "See the console/logfile output for more detailed error information!",
                "Installation error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (WalletCallException wce)
        {
        	Log.error("Unexpected error: ", wce);

            if ((wce.getMessage().indexOf("{\"code\":-28,\"message\"") != -1) ||
            	(wce.getMessage().indexOf("error code: -28") != -1))
            {
                JOptionPane.showMessageDialog(
                        null,
                        "It appears that bitcoinzd has been started but is not ready to accept wallet\n" +
                        "connections. It is still loading the wallet and blockchain. Please try to \n" +
                        "start the GUI wallet later...",
                        "Wallet communication error",
                        JOptionPane.ERROR_MESSAGE);
            } else
            {
                JOptionPane.showMessageDialog(
                    null,
                    "There was a problem communicating with the BitcoinZ daemon/wallet. \n" +
                    "Please ensure that the BitcoinZ server bitcoinzd is started (e.g. via \n" +
                    "command  \"bitcoinzd --daemon\"). Error message is: \n" +
                     wce.getMessage() +
                    "See the console/logfile output for more detailed error information!",
                    "Wallet communication error",
                    JOptionPane.ERROR_MESSAGE);
            }

            System.exit(2);
        } catch (Exception e)
        {
        	Log.error("Unexpected error: ", e);
            JOptionPane.showMessageDialog(
                null,
                "A general unexpected critical error has occurred: \n" + e.getMessage() + "\n" +
                "See the console/logfile output for more detailed error information!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(3);
        } catch (Error err)
        {
        	// Last resort catch for unexpected problems - just to inform the user
            err.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "A general unexpected critical/unrecoverable error has occurred: \n" + err.getMessage() + "\n" +
                "See the console/logfile output for more detailed error information!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(4);
        }
    }

    private static String getPassword(Random random) {
        StringBuilder pass = new StringBuilder();
        IntStream.range(0, 15)
                .forEach(i -> pass.append(Character.toString(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())))));
        return pass.toString();
    }

     public static void possiblyCreateZENConfigFile()
        throws IOException
    {
    	String blockchainDir = OSUtil.getBlockchainDirectory();
    	File dir = new File(blockchainDir);

		if (!dir.exists())
		{
			if (!dir.mkdirs())
			{
				Log.error("ERROR: Could not create settings directory: " + dir.getCanonicalPath());
				throw new IOException("Could not create settings directory: " + dir.getCanonicalPath());
			}
		}

		File zenConfigFile = new File(dir, "bitcoinz.conf");

		if (!zenConfigFile.exists())
		{
			Log.info("BitcoinZ configuration file " + zenConfigFile.getCanonicalPath() +
					 " does not exist. It will be created with default settings.");

			Random random = new SecureRandom();

			PrintStream configOut = new PrintStream(new FileOutputStream(zenConfigFile));

			configOut.println("#############################################################################");
			configOut.println("#                         BitcoinZ configuration file                            #");
			configOut.println("#############################################################################");
			configOut.println("# This file has been automatically generated by the BitcoinZ GUI wallet with #");
			configOut.println("# default settings. It may be further cutsomized by hand only.              #");
			configOut.println("#############################################################################");
			configOut.println("# Creation date: " + new Date().toString());
			configOut.println("#############################################################################");
			configOut.println("");
			configOut.println("# The rpcuser/rpcpassword are used for the local call to bitcoinzd");
			configOut.println("rpcuser=User" + getPassword(random));
			configOut.println("rpcpassword=Pass" + getPassword(random));
			configOut.println("");

			configOut.close();
		}
    }

}
