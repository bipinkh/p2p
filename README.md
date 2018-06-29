# Blockchain p2p messaging

**DISCLAIMER!** This is a work in progress implementation of Blockchain p2p messaging application.  Not recommended to be used in production yet.

# Steps to run the application
- Default configuration options are present in `src/main/resources/application.properties`
- Run command: `mvn spring-boot:run`



KademliaDHT class:
kademliaDHT provides access to the dht features. The components required to make a DHT are:
-- Contact Bucket : It stores information about available peers.
-- Kademlia Server: Responsible for sending and receiving messages to and from peers.
-- Value Store    : Stores (Key->Value) pairs in a HashTable

Setting up Kademlia DHT
// create the identifier key
 Key key=new Key("ab1245")

 // create a bucket Instance
 ContactBucket bucket=



