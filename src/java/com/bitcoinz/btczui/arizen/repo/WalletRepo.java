package com.bitcoinz.btczui.arizen.repo;

import com.bitcoinz.btczui.arizen.models.Address;

import java.io.File;
import java.util.Set;

/**
 * 
 * @author Pier Stabilini <pier@zensystem.io>
 *
 */
public interface WalletRepo {
	
	void openWallet(File f) throws Exception;
	
	void createWallet(File f) throws Exception;
	
	void insertAddress(Address address) throws Exception;
	
	void insertAddressBatch(Set<Address> address) throws Exception;
	
	void deleteAddress(String addr) throws Exception;
	
	Set<Address> listAddresses(Address.ADDRESS_TYPE type) throws Exception;
	
	void close() throws Exception;
	
	boolean isOpen();
	
}
