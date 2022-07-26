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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


/**
 * Table to be used for addresses - specifically.
 *
 * @author Ivan Vaklinov <ivan@vaklinov.com>
 */
public class AddressTable

extends DataTable {
	public AddressTable(final Object[][] rowData, final Object[] columnNames,
			            final BTCZClientCaller caller) {

		super(rowData, columnNames);
		int accelaratorKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();



		JMenuItem shieldCoinbase = new JMenuItem("Shield Coinbase");
		popupMenu.add(shieldCoinbase);

		shieldCoinbase.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e){
							if ((lastRow >= 0) && (lastColumn >= 0)){
								try{

									String address = AddressTable.this.getModel().getValueAt(lastRow, 3).toString();
									boolean isZAddress = Util.isZAddress(address);

									// Check for encrypted wallet
									final boolean bEncryptedWallet = caller.isWalletEncrypted();
									if (bEncryptedWallet)
									{
										PasswordDialog pd = new PasswordDialog((JFrame)(AddressTable.this.getRootPane().getParent()));
										pd.setVisible(true);

										if (!pd.isOKPressed()) {return;}

										caller.unlockWallet(pd.getPassword());
									}

									String shieldCoinbaseResult = caller.shieldCoinbase(address);


									JOptionPane.showMessageDialog(
										AddressTable.this.getRootPane().getParent(),
										"Message : "+shieldCoinbaseResult+ "\n" +
										"",
										"Shield Coinbase", JOptionPane.INFORMATION_MESSAGE);



								} catch (Exception ex) {
									Log.error("Unexpected error: ", ex);
						            JOptionPane.showMessageDialog(
						                AddressTable.this.getRootPane().getParent(),
								        "Error Shield Coinbase:" + "\n" +
								         ex.getMessage() + "\n\n",
								        "Error Shield Coinbase!",
								        JOptionPane.ERROR_MESSAGE);
								}
							}
					}
				});





		JMenuItem mergeToAddress = new JMenuItem("Merge all (t) UTXO");
		mergeToAddress.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, accelaratorKeyMask));
		popupMenu.add(mergeToAddress);

		mergeToAddress.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e){
							if ((lastRow >= 0) && (lastColumn >= 0)){
								try{

									String address = AddressTable.this.getModel().getValueAt(lastRow, 3).toString();
									boolean isZAddress = Util.isZAddress(address);

									// Check for encrypted wallet
									final boolean bEncryptedWallet = caller.isWalletEncrypted();
									if (bEncryptedWallet)
									{
										PasswordDialog pd = new PasswordDialog((JFrame)(AddressTable.this.getRootPane().getParent()));
										pd.setVisible(true);

										if (!pd.isOKPressed())
										{
											return;
										}

										caller.unlockWallet(pd.getPassword());
									}

									String mergeAddressResult = caller.mergeToAddress(address);


									JOptionPane.showMessageDialog(
										AddressTable.this.getRootPane().getParent(),
										"Message : "+mergeAddressResult+ "\n" +
										"",
										"Merge To Address", JOptionPane.INFORMATION_MESSAGE);



								} catch (Exception ex) {
									Log.error("Unexpected error: ", ex);
						            JOptionPane.showMessageDialog(
						                AddressTable.this.getRootPane().getParent(),
								        "Error merging UTXO:" + "\n" +
								         ex.getMessage() + "\n\n",
								        "Error merging UTXO!",
								        JOptionPane.ERROR_MESSAGE);
								}
							}
					}
				});



		JMenuItem signMessage = new JMenuItem("Sign message...");
		signMessage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, accelaratorKeyMask));
		popupMenu.add(signMessage);

		signMessage.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e){
							if ((lastRow >= 0) && (lastColumn >= 0)){
								try{

									String address = AddressTable.this.getModel().getValueAt(lastRow, 3).toString();
									boolean isZAddress = Util.isZAddress(address);

									// Check for encrypted wallet
									final boolean bEncryptedWallet = caller.isWalletEncrypted();
									if (bEncryptedWallet)
									{
										PasswordDialog pd = new PasswordDialog((JFrame)(AddressTable.this.getRootPane().getParent()));
										pd.setVisible(true);

										if (!pd.isOKPressed())
										{
											return;
										}

										caller.unlockWallet(pd.getPassword());
									}


									SignMessageDialog smd = new SignMessageDialog((JFrame)(AddressTable.this.getRootPane().getParent()));
									smd.setVisible(true);

									if (!smd.isOKPressed())
									{
										return;
									}

									String signedMessgae = caller.getSignedMessage(address, smd.getMessage());
									Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
									clipboard.setContents(new StringSelection(signedMessgae), null);

									JOptionPane.showMessageDialog(
										AddressTable.this.getRootPane().getParent(),
										"Message : "+smd.getMessage() + "\n" +
										"Address : "+address + "\n" +
										"Signature : "+ "\n" +signedMessgae+ "\n\n" +
										"The signature has also been copied to the clipboard.",
										"Sign message", JOptionPane.INFORMATION_MESSAGE);


								} catch (Exception ex) {
									Log.error("Unexpected error: ", ex);
						            JOptionPane.showMessageDialog(
						                AddressTable.this.getRootPane().getParent(),
								        "Error in signing message:" + "\n" +
								         ex.getMessage() + "\n\n",
								        "Error in signing message!",
								        JOptionPane.ERROR_MESSAGE);
								}
							}
					}
				});













		JMenuItem obtainPrivateKey = new JMenuItem("Obtain private key...");
		obtainPrivateKey.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, accelaratorKeyMask));
    popupMenu.add(obtainPrivateKey);

    obtainPrivateKey.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ((lastRow >= 0) && (lastColumn >= 0))
				{
					try
					{
						String address = AddressTable.this.getModel().getValueAt(lastRow, 3).toString();
						boolean isZAddress = Util.isZAddress(address);

						// Check for encrypted wallet
						final boolean bEncryptedWallet = caller.isWalletEncrypted();
						if (bEncryptedWallet)
						{
							PasswordDialog pd = new PasswordDialog((JFrame)(AddressTable.this.getRootPane().getParent()));
							pd.setVisible(true);

							if (!pd.isOKPressed())
							{
								return;
							}

							caller.unlockWallet(pd.getPassword());
						}

						String privateKey = isZAddress ?
							caller.getZPrivateKey(address) : caller.getTPrivateKey(address);

						// Lock the wallet again
						if (bEncryptedWallet)
						{
							caller.lockWallet();
						}

						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(new StringSelection(privateKey), null);

						JOptionPane.showMessageDialog(
							AddressTable.this.getRootPane().getParent(),
							(isZAddress ? "Z (Private)" : "T (Transparent)") +  " address:\n" +
							address + "\n" +
							"has private key:\n" +
							privateKey + "\n\n" +
							"The private key has also been copied to the clipboard.",
							"Private key information", JOptionPane.INFORMATION_MESSAGE);


					} catch (Exception ex)
					{
						Log.error("Unexpected error: ", ex);
			            JOptionPane.showMessageDialog(
			                AddressTable.this.getRootPane().getParent(),
					        "Error in obtaining private key:" + "\n" +
					         ex.getMessage() + "\n\n",
					        "Error in obtaining private key!",
					        JOptionPane.ERROR_MESSAGE);
					}
				} else
				{
					// Log perhaps
				}
			}
		});


	} // End constructor

}
