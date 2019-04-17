Set up client
########################################

.. toctree::
  :maxdepth: 3

.. mainchain-api:

Walk Through
=================================
The code is written in java . make sure you add jdk in your environment.

1. download the code 

* git clone https://github.com/ioeXNetwork/ioeX.ORG.Wallet.Service.git


2. modify application.properties 

* `node.prefix` change it to your own local mainchain node .

3. install project 

* mvn install -Dmaven.test.skip -Dgpg.skip

4. start your project

* java -jar base.api-*.jar

