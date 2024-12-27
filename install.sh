#!/bin/bash

# Note: 
# Ensure that the BitcoinZ node is fully synchronized before running the BitcoinZ Wallet GUI

sudo apt update -y

sudo apt install -y default-jdk openjdk-11-jdk

wget https://github.com/btcz/bitcoinz-wallet/releases/download/v2.0.10/bitcoinz-wallet_2.0.10.deb

dpkg-deb -x bitcoinz-wallet_2.0.10.deb ./bitcoinz-temp
#here we extract .jar files from the package (no need to install it)

sudo mkdir /usr/lib/bitcoinz-wallet
sudo mv ./bitcoinz-temp/usr/lib/bitcoinz-wallet/* /usr/lib/bitcoinz-wallet/

rm -f bitcoinz-wallet_2.0.10.deb
rm -r ./bitcoinz-temp

wget https://github.com/btcz/bitcoinz/releases/download/2.1.0/bitcoinz-c73d5cdb2b70-x86_64-linux-gnu.tar.gz

tar -xvzf bitcoinz-c73d5cdb2b70-x86_64-linux-gnu.tar.gz

sudo mv ./bitcoinz-c73d5cdb2b70/bin/bitcoinzd /usr/lib/bitcoinz-wallet/
sudo mv ./bitcoinz-c73d5cdb2b70/bin/bitcoinz-cli /usr/lib/bitcoinz-wallet/
sudo mv ./bitcoinz-c73d5cdb2b70/bin/bitcoinz-tx /usr/lib/bitcoinz-wallet/

rm -rf bitcoinz-c73d5cdb2b70-x86_64-linux-gnu.tar.gz


echo "Creating shortcut command for BitcoinZ Wallet..."

echo '#!/bin/bash' | sudo tee /usr/local/bin/bitcoinz-wallet > /dev/null
echo 'java -jar /usr/lib/bitcoinz-wallet/BitcoinZWallet.jar' | sudo tee -a /usr/local/bin/bitcoinz-wallet > /dev/null

sudo chmod +x /usr/local/bin/bitcoinz-wallet

echo "Shortcut command 'bitcoinz-wallet'"
