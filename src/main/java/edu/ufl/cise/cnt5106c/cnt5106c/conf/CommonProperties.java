package edu.ufl.cise.cnt5106c.cnt5106c.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Properties;

/**
 *
 * @author Giacomo
 */
public enum CommonProperties {

    NumberOfPreferredNeighbors,
    UnchokingInterval,
    OptimisticUnchokingInterval,
    FileName,
    FileSize,
    PieceSize;

    public static final String CONFIG_FILE_NAME = "Common.cfg";

    public static Properties read (Reader reader) throws Exception {

        final Properties conf = new Properties () {
            @Override
            public synchronized void load(Reader reader)
                    throws IOException {
                BufferedReader in = new BufferedReader(reader);
                int i = 0;
                for (String line; (line = in.readLine()) != null;) {
                    // The defaul Properties class uses the '=' character to
                    // separate keys and value, while the project description
                    // requires keys and values being separated by a space.
                    String[] tokens = line.split("\\s+");
                    if (line.length() != 2) {
                        throw new IOException (new ParseException (line, i));
                    }
                    setProperty(tokens[0], tokens[1]);
                    i++;
                }
            }
        };

        conf.load (reader);

        // Check the config file contains all the needed properties
        for (CommonProperties prop : CommonProperties.values()) {
            if (!conf.containsKey(prop.toString())) {
                throw new Exception ("config file does not contain property " + prop);
            }
        }

        return conf;
    }
}
