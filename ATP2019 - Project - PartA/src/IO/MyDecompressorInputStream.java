/**
 * This class reads the object from the input stream and decompresses it
 * @autor Dvir Simhon & Amit Damri
 * @since 16.5.2019
 * @version 1.0
 **/
package IO;


import java.io.*;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MyDecompressorInputStream extends InputStream {

    private InputStream in;

    /**
     * Constructor
     * @param in input stream to read from
     */
    public MyDecompressorInputStream(InputStream in) {
        if (null == in)
            this.in = new BufferedInputStream(System.in);
        else
            this.in = in;
    }

    /**
     * reads one byte from the inputs stream if eof returns -1
     * @return int of the byte was read
     */
    @Override
    public synchronized int read() {
        int readByte = -1;
        try {
            readByte = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readByte;
    }

    /**
     * reads bytes form the input stream
     * @param byteArray read the bytes from the input stream to this byte array
     * @return number of bytes that was read
     */
    @Override
    public synchronized int read(byte[] byteArray)  {
        if (byteArray != null) {
            int numberOfBytesRead = 0;
            try {
                ArrayList<Byte> compressedArray = new ArrayList<>();
                int endOfFile = 0; // if read 4 zeros - eof
                while (4 != endOfFile) {
                    byte byteRead = (byte) in.read();
                    ++numberOfBytesRead;
                    if (byteRead == 0)
                        ++endOfFile;
                    else
                        endOfFile = 0;
                    compressedArray.add(byteRead);
                }
                while (in.read()!=4) {
                    compressedArray.add((byte)0);
                    ++numberOfBytesRead;
                }
                byte[] tempByteArray = copyArrayToByteArray(compressedArray);
                tempByteArray = decompressByteMazeInflater(tempByteArray);
                if (byteArray.length >= tempByteArray.length) {
                    for (int i = 0; i < tempByteArray.length; i++) {
                        byteArray[i] = tempByteArray[i];
                    }
                } else {
                    //knows that 1 is a bug
                    byteArray[1] = 1;
                    return -1;
                }
            } catch (IOException e) {
                e.printStackTrace();
                byteArray[1] = 1;
                return -1;
            }
            return numberOfBytesRead -4;
        } else
            return -1;
    }

    /**
     * Decompresses the byte array
     * @param byteMaze compressed array
     * @return decompressed byte array
     */
    private byte[] decompressByteMazeInflater(byte[] byteArray) {


        Inflater decompressor = new Inflater();
        decompressor.setInput(byteArray);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteArray.length);
        byte[] buffer = new byte[1000000];
        try {
            while (!decompressor.finished()) {
                if (decompressor.getRemaining() == 0)
                    throw new Exception("Problem while decompressing");
                int count = decompressor.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            byteArray[3] = 1;
            return byteArray;
        }
        byte[] compressedMaze = outputStream.toByteArray();
        return compressedMaze;
    }

    /**
     * Copy the bytes to byte array
     * @param compressedArray list of bytes that was read from the input stream
     * @return byte array
     */
    private byte[] copyArrayToByteArray(ArrayList<Byte> compressedArray) {
        //copy without the last zeros
        byte[] byteArray = new byte[compressedArray.size()-4];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = compressedArray.get(i);
        }
        return byteArray;
    }

}
