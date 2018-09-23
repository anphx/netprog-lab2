package netprog.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StrHelper {
    public static byte[] concatBytes(byte[] str1, byte[] str2) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write( str1 );
        byteOut.write( str2 );
        byte conBytes[] = byteOut.toByteArray( );

        // AnP: another approach using System.arraycopy
//			byte[] conBytes = new byte[newBuff.length + myName.length];

//			System.arraycopy(newBuff, 0, conBytes, 0, newBuff.length);
//			System.arraycopy(myName, 0, conBytes, newBuff.length, myName.length);
        return conBytes;
    }
}
