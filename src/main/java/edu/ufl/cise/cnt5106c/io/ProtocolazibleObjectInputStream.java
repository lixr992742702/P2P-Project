package edu.ufl.cise.cnt5106c.io;

import edu.ufl.cise.cnt5106c.messages.Handshake;
import edu.ufl.cise.cnt5106c.messages.Message;
import edu.ufl.cise.cnt5106c.messages.Type;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;

/**
 * A ProtocolazibleObjectInputStream deserializes primitive data and objects
 * previously written using an ProtocolazibleObjectOutputStream.
 *
 * @author Giacomo Benincasa    (giacomo@cise.ufl.edu)
 */
public class ProtocolazibleObjectInputStream extends DataInputStream implements ObjectInput {

    private boolean _handshakeReceived = false;

    public ProtocolazibleObjectInputStream(InputStream in) {
        super(in);
    }

    @Override
    public Object readObject() throws ClassNotFoundException, IOException {
        if (_handshakeReceived) {
            int length = readInt();
            Message message = Message.getInstance (length - 1, Type.valueOf (readByte()));
            message.read(this);
            return message;
        }
        else {
            Handshake handshake = new Handshake();
            handshake.read(this);
            _handshakeReceived = true;
            return handshake;
        }
    }
}