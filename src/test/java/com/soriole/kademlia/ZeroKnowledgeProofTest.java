package com.soriole.kademlia;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

public class ZeroKnowledgeProofTest {

    @Test
    public void ZeroKnowlddgeProof() {
        // bob holds a secret.
        Bob bob = new Bob("The secret passed to bob by his grandmother");

        // alice wants to verify that bob knows the secret.
        // so first alice asks a commitment from bob.
        Alice alice = new Alice(bob.publishSecret());

        // now that  Bob has committed his secret, he cannot change it.
        // to verify that bob has the secret
        // alice creates a random number
        BigInteger random = new BigInteger(100, 40, new Random());

        // alice sends the random number to bob.
        // bob verifies his secret with the random number.
        BigInteger reply = bob.generateVerification(random);

        // verify the reply is correct.
        assert (alice.verify(random, reply));

        // Now, Alice is assured that Bob knows the secret of his grandmother,
        // whereas alice still doesn't know that secret.
    }

    class Alice {

        BigInteger publicSecret;

        public Alice(BigInteger publicSecret) {
            this.publicSecret = publicSecret;

        }

        // returns g^r mod p
        public BigInteger limitRandom(BigInteger r) {
            return g.modPow(r, p);
        }

        public boolean verify(BigInteger random, BigInteger reply) {
            return limitRandom(random).multiply(publicSecret).mod(p).equals(
                    g.modPow(reply, p)
            );
        }

    }

    class Bob {
        BigInteger secret;

        public Bob(String secret) {
            this.secret = new BigInteger(1, secret.getBytes());
        }

        public Bob(BigInteger secret) {
            this.secret = secret;
        }

        // returns g^(secret) mod p
        public BigInteger publishSecret() {
            return g.modPow(secret, p);
        }

        // returns g^(secret+r) mod (p-1)
        public BigInteger generateVerification(BigInteger r) {
            return secret.add(r).mod(p.subtract(BigInteger.ONE));
        }
    }

    // generator
    BigInteger g = new BigInteger("17", 10);

    // generate a probably prime random number
    BigInteger p = new BigInteger(200, 40, new Random());
}


