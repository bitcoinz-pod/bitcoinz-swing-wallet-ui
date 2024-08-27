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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;

import java.net.URL;
import java.io.UnsupportedEncodingException;


/**
 * Typical about box stuff...
 *
 * @author Ivan Vaklinov <ivan@vaklinov.com>
 */
public class AboutDialog extends JDialog{
	public AboutDialog(JFrame parent) throws UnsupportedEncodingException{


		this.setTitle("About...");
		this.setSize(620, 480);
	  this.setLocation(100, 100);
		this.setLocationRelativeTo(parent);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JTabbedPane tabs = new JTabbedPane();


		// ************ Licence Panel start here ************************
		JPanel licensePanel = new JPanel();
		licensePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		licensePanel.setLayout(new BorderLayout(3, 3));
		JLabel licenseLabel = new JLabel();
		licenseLabel.setText(
			"<html><body><pre>" +

			"  ____ _____ ______ ___   __ _____  __   _ _____ _        __    _ _      _   _   _ ___  \n" +
			" | __ \\_   _|_   __| __|/ _ \\_   _||   \\| |___  | \\      / /_ _| | | ___| |_| | | |_ _| \n" +
			" |____/ | |   | |/ /   / / \\ \\| |  | |\\ | |  / / \\ \\ /\\ / / _` | | |/ _ \\ __| | | || |  \n" +
			" | ___ \\| |_  | |\\ \\__ \\ \\_/ /| |_ | | \\  | / /_  \\ V  V / (_| | | |  __/ |_| |_| || |  \n" +
			" |_____/____| |_| \\____|\\___/_____||_|  \\_|/____|  \\_/\\_/ \\__,_|_|_|\\___|\\__|\\___/|___| \n" +
			"  Version 2.0.10 \n \n"  +

		    " Copyright (c) 2017-2022 BitcoinZ team \n" +
		    " Copyright (c) 2016-2018 Ivan Vaklinov &lt;ivan@vaklinov.com&gt; \n" +
			"\n" +
			" Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
			" of this software and associated documentation files (the \"Software\"), to deal\n" +
			" in the Software without restriction, including without limitation the rights\n" +
			" to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
			" copies of the Software, and to permit persons to whom the Software is\n" +
			" furnished to do so, subject to the following conditions:\n" +
			" \n" +
			" The above copyright notice and this permission notice shall be included in\n" +
			" all copies or substantial portions of the Software.\n" +
			" \n" +
			" THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
			" IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
			" FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
			" AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
			" LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
			" OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
			" THE SOFTWARE.		\n" +
			"</pre></body></html>");
		licenseLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		licensePanel.add(licenseLabel, BorderLayout.NORTH);




		// ************ About Panel start here ****************************
		// Logo
		URL iconUrl = this.getClass().getClassLoader().getResource("images/bitcoinz60x60.png");
		ImageIcon imageIcon = new ImageIcon(iconUrl);
		JLabel imageLabel = new JLabel();
		imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		imageLabel.setIcon(imageIcon);

		// Title and version
		JLabel aboutNORTH_CENTER = new JLabel();
		aboutNORTH_CENTER.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		aboutNORTH_CENTER.setText(
				"<html><b><span style='font-weight:bold;font-size:2.3em'>" +
				"BitcoinZ Wallet UI</span></b><br>&nbsp;Version 2.0.10</html>");

		// About description
		JLabel aboutCENTER = new JLabel();
		aboutCENTER.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		aboutCENTER.setText(
			"<html>" +
				"<div style='display: table; padding: 10px; height: 250px; max-width: 600px; overflow: hidden;'>  " +
		  		"<div style='display: table-cell; vertical-align: top;'>" +
		    		"<div>" +
				      "This program provides a Graphical User Interface (GUI) for the BitcoinZ client 			<br>" +
							"tools that acts as a wrapper and presents the information in a user-friendly manner. <br>" +
							"<br>  " +
							"BitcoinZ is experimental and a work-in-progress. Use at your own risk.  							<br>" +
							"The BitcoinZ Full Node Desktop GUI Wallet is based on ZENCash Desktop GUI Wallet. 		<br>" +
							"<br>  " +
							"Code was originally written by developer : Ivan Vaklinov ivan@vaklinov.com   				<br>" +
							"Taken from repository under an MIT license: 																					<br>"+
							"<a href='https://github.com/HorizenOfficial/zencash-swing-wallet-ui'>" +
							"https://github.com/HorizenOfficial/zencash-swing-wallet-ui</a> 								 <br><br>" +
							"<b>Disclaimer</b>  																														 <br><br>" +
							"THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,   <br>" +
							"INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A       <br>" +
							"PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT  <br>" +
							"HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION   <br>" +
							"OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH   			 <br>" +
							"THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.  											   <br>" +
				    "</div>" +
				  "</div>" +
				"</div>" +
		"</html>");

		JPanel aboutPanel = new JPanel();
		aboutPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		aboutPanel.setLayout(new BorderLayout(3, 3));

		JPanel aboutNORTH = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		aboutNORTH.add(imageLabel, BorderLayout.WEST);
		aboutNORTH.add(aboutNORTH_CENTER, BorderLayout.CENTER);

		aboutPanel.add(aboutNORTH, BorderLayout.NORTH);
		aboutPanel.add(aboutCENTER, BorderLayout.CENTER);





		// ************ Update Log Panel start here ****************************
		JLabel updateLogText = new JLabel();
		updateLogText.setText(
				"<html>" +
					"<div style='display: table; padding: 10px; height: 400px; max-width: 600px; overflow: hidden;'>  " +

					"<b><u>v2.0.10 (SEP-2022)</u></b><br>"+
					"- Added Viewing key import and usage. <br><br>"+

						"<b><u>v2.0.7-u3 (MAY-2022)</u></b><br>"+
						"- Added option to disable the (z) messaging. <br>"+
						"- Added Raw TX tab with data send capability. <br>"+
						"- Code tweak and refresh. <br><br>"+

						"<b><u>v2.0.7-u2 (FEB-2022)</u></b><br>"+
						"- Complete BTCZ'ify of the source code. <br>"+
						"- Refresh of the About dialog box. <br>"+
						"- Code tweak and refresh. <br>"+
						"- Activated wallet encryption. <br><br>"+

						"<b><u>v2.0.7-u1 (JAN-2022)</u></b><br>"+
						"- Added Merge to Address feature. <br>"+
						"- Added Shield Coinbase feature. <br>"+
						"- Added Address Message Sign deature. <br>"+
						"- Code tweak and refresh. <br><br>"+

						"<b><u>v2.0.7</u></b><br>"+
						"- BitcoinZ Yoda node update. <br>"+

					"</div>"+
				"</html>");

		JScrollPane	scrollPanel = new JScrollPane(updateLogText);
		scrollPanel.setPreferredSize(new Dimension(200, 250));
		scrollPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane	updatePanel = new JScrollPane(scrollPanel);


		// ************ System Panel start here ****************************
		JPanel systemInfoPanel = new JPanel();
		systemInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		systemInfoPanel.setLayout(new BorderLayout(3, 3));



		// Add the panels to the TAB panel
		tabs.add("About", aboutPanel);
		tabs.add("Update Log", updatePanel);
		tabs.add("License (MIT)", licensePanel);
		//tabs.add("System Informations", systemInfoPanel);



		this.getContentPane().setLayout(new BorderLayout(0, 0));
		this.getContentPane().add(tabs, BorderLayout.NORTH);

		JPanel closePanel = new JPanel();
		closePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		JButton closeButon = new JButton("Close");
		closePanel.add(closeButon);
		this.getContentPane().add(closePanel, BorderLayout.SOUTH);

		closeButon.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					AboutDialog.this.setVisible(false);
					AboutDialog.this.dispose();
				}
		});

		pack();
	}
}
