/**
 * This class compresses the object and writes it through the output stream
 * @autor Dvir Simhon & Amit Damri
 * @since 16.5.2019
 * @version 1.0
 **/

package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.Deflater;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;

    /**
     * Constructor
     * @param out outputStream to write
     */
    public MyCompressorOutputStream(OutputStream out) {
        if (null == out)
            this.out = new PrintStream(System.out);
        else
            this.out = out;
    }

    /**
     * writes the given byte
     * @param b byte to write
     */
    @Override
    public synchronized void write(int b) {
        try {
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes the given byte array through the output stream
     * @param byteArray
     */
    @Override
    public synchronized void write(byte[] byteArray) {
        if (null != byteArray) {
            try {
                //compresses the array
                byte[] compressedArray = compressByteMazeWithDeflate(byteArray);
                out.flush();
                out.write(compressedArray);
                //end of file 00004
                for (int i=0;i<4;i++){
                    this.write(0);
                }
                this.write(4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * compresses the array with LZ77 AND HUFFMAN CODE - DEFALTER CLASS
     * @param byteArray non compressed array
     * @return compressed byte array
     */
    private byte[] compressByteMazeWithDeflate(byte[] byteArray) {

        Deflater compressor = new Deflater();
        compressor.setInput(byteArray);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteArray.length);
        compressor.finish();
        byte[] buffer = new byte[byteArray.length];
        while (!compressor.finished()) {
            int count = compressor.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] tempByteMaze = outputStream.toByteArray();
        return tempByteMaze;
    }
}
