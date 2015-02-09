package edu.ufl.cise.cnt5106c.cnt5106c.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 *
 * @author Giacomo Benincasa    (giacomo@cise.ufl.edu)
 */
public class Handshake implements Serializable {
    private final static String _protocolId = "P2PFILESHARINGPROJ";
    private final byte[] _zeroBits = new byte[10];
    private final byte[] _peerId = new byte[4];

    Handshake (byte[] peerId) {
        if (peerId.length > 4) {
            throw new ArrayIndexOutOfBoundsException("peerId max length is 4, while "
                    + peerId + "'s length is "+ peerId.length);
        }
        int i = 0;
        for (byte b : peerId) {
            _peerId[i++] = b;    
        }
    }

    private void writeObject(ObjectOutputStream oos)
        throws IOException {
        byte[] peerId = _protocolId.getBytes(Charset.forName("US-ASCII"));
        if (peerId.length > _protocolId.length()) {
            throw new IOException("protocol id length is " + peerId.length + " instead of " + _protocolId.length());
        }
        oos.write(peerId, 0, peerId.length);
        oos.write(_zeroBits, 0, _zeroBits.length);
        oos.write(_peerId, 0, _peerId.length);
    }

    private void readObject(ObjectInputStream ois)
        throws ClassNotFoundException, IOException {
        // Read and check protocol Id
        byte[] protocolId = new byte[_protocolId.length()];
        if (ois.read(protocolId, 0, _protocolId.length()) < _protocolId.length()) {
            throw new IOException("protocol id is " + protocolId + " instead of " + _protocolId);
        }
        if (!_protocolId.equals(new String(protocolId, "US-ASCII"))) {
            throw new IOException("protocol id is " + protocolId + " instead of " + _protocolId);
        }

        // Read and check zero bits
        if (ois.read(_zeroBits, 0, _zeroBits.length) <  _zeroBits.length) {
            throw new IOException("zero bit bytes read are less than " + _zeroBits.length);
        }

        // Read and check peer id
        if (ois.read(_zeroBits, 0, _peerId.length) <  _peerId.length) {
            throw new IOException("peer id bytes read are less than " + _peerId.length);
        }
    }
}