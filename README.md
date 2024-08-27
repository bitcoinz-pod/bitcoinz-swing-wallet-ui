# BitcoinZ Wallet 2.0.10

[Download here](https://github.com/btcz/bitcoinz-wallet/releases)


Security Warnings
-----------------

**BitcoinZ is experimental and a work-in-progress.** Use at your own risk.   
*BitcoinZ Full Node Desktop GUI Wallet is based on ZENCash Desktop GUI Wallet*    

Code was originally written by developer : Ivan Vaklinov <ivan@vaklinov.com>
Taken from repository https://github.com/HorizenOfficial/zencash-swing-wallet-ui under an MIT license


# [BitcoinZ](https://getbtcz.com/) Desktop GUI Wallet

(See all BtcZ wallets here : https://getbtcz.com/btcz-wallets-analytical-information/)

## Graphical user interface wrapper for the [BTCZ-RPC](https://bitcoinz-dev-tools.github.io/BTCZ-RPC-DOC/) command line tools

This program provides a Graphical User Interface (GUI) for the BitcoinZ client tools that acts as a wrapper and
presents the information in a user-friendly manner.

#### New/Experimental: BitcoinZ Desktop GUI Wallet for [Mac OS](https://github.com/btcz/bitcoinz-wallet/blob/master/docs/Release_0.73.4.md) is available

#### New/Experimental: BitcoinZ Desktop GUI Wallet for [Windows](https://github.com/btcz/bitcoinz-wallet/blob/master/docs/Release_0.73.1.md) is available


## Building, installing and running the Wallet GUI

Before installing the Desktop GUI Wallet you need to have BitcoinZ up and running. The following
[guide](https://github.com/btcz/bitcoinz/wiki/Quick-guide-for-beginners)
explains how to set up [BitcoinZ](https://github.com/btcz/bitcoinz).

**For security reasons it is recommended to always build the GUI wallet program from GitHub**
**[source](https://github.com/btcz/bitcoinz-wallet/archive/master.zip).**
The details of how to build it are described below (easy to follow).


1. Operating system and tools

   As of June 2017 (ZENCash v1.0.9) this program is mostly tested on Linux and Mac OS X
   (same limitation as [ZENCash](https://zensystem.io/)) with experimental support for Windows.
   The Linux tools you need to build and run the Wallet GUI are Git, Java (JDK7 or later) and
   Ant. If using Ubuntu Linux, they may be installed via command:
   ```
   user@ubuntu:~/build-dir$ sudo apt-get install git default-jdk ant
   ```
   For RedHat/CentOS/Fedora-type Linux systems the command is (like):
   ```
   user@centos:~/build-dir$ sudo yum install java-1.8.0-openjdk git ant
   ```
   The name of the JDK package (`java-1.8.0-openjdk`) may vary depending on the Linux system, so you need to
   check it, if name `java-1.8.0-openjdk` is not accepted.
   If you have some other Linux distribution, please check your relevant documentation on installing Git,
   JDK and Ant. The commands `git`, `java`, `javac` and `ant` need to be startable from command line
   before proceeding with build.

2. Building from source code

   As a start you need to clone the BitcoinZ-Wallet Git repository:
   ```
   git clone https://github.com/btcz/bitcoinz-wallet.git
   ```
   Change the current directory:
   ```
   cd bitcoinz-wallet/
   ```
   Issue the build command:
   ```
   ant -buildfile ./src/build/build.xml
   ```
   This takes a few seconds and when it finishes, it builds a JAR file `./build/jars/BitcoinZWallet.jar`.
   You need to make this file executable:
   ```
   chmod u+x ./build/jars/Bitcoinzwallet.jar
   ```
   At this point the build process is finished the built GUI wallet program is the JAR
   file `./build/jars/BitcoinZWallet.jar`

3. Installing the built BitcoinZ GUI wallet

   3.1. If you have built BitcoinZ from source code:

     Assuming you have already built from source code [BitcoinZ](https://github.com/btcz/bitcoinz) in directory `/home/user/bitcoinz/src` (for example - this is the typical build dir. for bitcoinz v2.0.10) which contains the command line tools `bitcoinz-cli` and `bitcoinzd` you need to take the created file `./build/jars/BitcoinZWallet.jar` and copy it to directory `/home/user/bitcoinz/src` (the same dir. that contains `bitcoinz-cli` and `bitcoinzd`). Example copy command:
      ```
      cp ./build/jars/BitcoinZWallet.jar /home/user/zen/src    
      ```

4. Running the installed BitcoinZ GUI wallet

   It may be run from command line or started from another GUI tool (e.g. file manager).
   Assuming you have already installed [BitcoinZ](https://github.com/btcz/bitcoinz) and the GUI Wallet `BitcoinZWallet.jar` in
   directory `/home/user/bitcoinz/src` one way to run it from command line is:
   ```
   java -jar /home/user/bitcoinz/src/BitcoinZWallet.jar
   ```
   If you are using Ubuntu (or similar ;) Linux you may instead just use the file manager and
   right-click on the `BitcoinZWallet.jar` file and choose the option "Open with OpenJDK 8 Runtime".
   This will start the BitcoinZ GUI wallet.

   **Important:** the BitcoinZ configuration file `~/.bitcoinz/bitcoinz.conf` needs to be correctly set up for the GUI
   wallet to work. Specifically the RPC user and password need to be set in it like:
   ```
   rpcuser=username
   rpcpassword=wjQOHVDQFLwztWp1Ehs09q7gdjHAXjd4E

   ```


### License
This program is distributed under an [MIT License](https://github.com/ZencashOfficial/zencash-swing-wallet-ui/raw/master/LICENSE).

### Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

### Known issues and limitations

1. Limitation: if two users exchange text messages via the messaging UI TAB and one of them has a system clock, substantially running slow or fast by more than 1 minute, it is possible that this user will see text messages appearing out of order.
1. Limitation: if a messaging identity has been created (happens on first click on the messaging UI tab), then replacing the `wallet.dat` or changing the node configuration between mainnet and testnet will make the identity invalid. This will result in a wallet update error. To remove the error the directory `~/.ZENCashSwingWalletUI/messaging` may be manually renamed or deleted (when the wallet is stopped). **CAUTION: all messaging history will be lost in this case!**
1. Limitation: Wallet encryption has been temporarily disabled in ZENCash due to stability problems. A corresponding issue
[#1552](https://github.com/zcash/zcash/issues/1552) has been opened by the zcash developers. Correspondingly
wallet encryption has been temporarily disabled in the ZENCash Desktop GUI Wallet.
1. Issue: GUI data tables (transactions/addresses etc.) allow copying of data via double click but also allow editing.
The latter needs to be disabled.
1. Limitation: The list of transactions does not show all outgoing ones (specifically outgoing Z address
transactions). A corresponding issue [#1438](https://github.com/zcash/zcash/issues/1438) has been opened
for the zcash developers.
1. Limitation: The CPU percentage shown to be taken by zend on Linux is the average for the entire lifetime
of the process. This is not very useful. This will be improved in future versions.
1. Limitation: When using a natively compiled wallet version (e.g. `ZENCashSwingWalletUI.exe` for Windows) on a
very high resolution monitor with a specifically configured DPI scaling (enlargement) factor to make GUI
elements look larger, the GUI elements of the wallet actually do not scale as expected. To correct this on
Windows you need to right-click on `ZENCashSwingWalletUI.exe` and choose option:
```
Properties >> Compatibility >> Override High DPI scaling behavior >> Scaling Performed by (Application)
```
Example:

![DPI Scaling](https://github.com/ZencashOfficial/zencash-swing-wallet-ui/raw/master/docs/EXEScalingSettings.png "DPI Scaling")
