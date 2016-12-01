package gov.ca.cwds.rest.util.jni;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.compress.utils.IOUtils;
import org.flywaydb.core.internal.util.FileCopyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pkware.deflate.DeflateOutputStream;
import com.pkware.deflate.InflateInputStream;

/**
 * Compresses (deflates) and decompresses (inflates) PK archives created by the Windows PKWare
 * library.
 * 
 * <p>
 * <strong>NOTE: </strong>This class only works with PK-compressed docs, not the LZW variable 15-bit
 * algorithm. For the latter, see {@link LZWEncoder}.
 * </p>
 *
 * <p>
 * This class provides specialized methods to compress/decompress documents for various inputs,
 * including files, streams, byte arrays, base64-encoded strings, and hexadecimal strings.
 * </p>
 *
 * <p>
 * For some unknown reason, this particular compression algorithm (PK_COMPRESS_METHOD_DEFLATE, level
 * 6) does not require a current license key. For example, this initialization is not needed:
 * </p>
 * 
 * <pre>
 * <code>PKSession session = new PKSession(PKWARE_LICENSE_KEY);</code>
 * </pre>
 * 
 * <strong>Java PKWare SDK details:</strong>
 * <ul>
 * <li>Name: com/pkware/archive</li>
 * <li>Implementation-Vendor: PKWARE, Inc.</li>
 * <li>Specification-Title: SecureZIP Toolkit for Java</li>
 * <li>Implementation-Title: com.pkware.archive</li>
 * <li>Implementation-Version: 3.20</li>
 * <li>Specification-Version: 3.20</li>
 * <li>Specification-Vendor: PKWARE, Inc.</li>
 * </ul>
 * 
 * @author CWDS API Team
 * @see LZWEncoder
 */
public class CmsPKCompressor implements LicenseCWDS {

  private static final Logger LOGGER = LoggerFactory.getLogger(CmsPKCompressor.class);

  private static final int DEFAULT_COMPRESSION_LEVEL = 6;
  private static final int DEFAULT_OUTPUT_SIZE = 0x10000;

  /**
   * Constructor
   */
  public CmsPKCompressor() {}

  /**
   * Decompress (inflate) a CMS PKWare archive.
   * 
   * @param inputFileName file name of resulting decompressed output
   * @param outputFileName file name to decompress
   * @throws IOException If an I/O error occurs
   */
  public void decompressFile(String inputFileName, String outputFileName) throws IOException {
    FileInputStream fis = new FileInputStream(new File(inputFileName));
    InputStream iis = new InflateInputStream(fis, true);

    FileOutputStream fos = new FileOutputStream(new File(outputFileName));
    IOUtils.copy(iis, fos);

    fis.close();
    fos.close();
  }

  /**
   * Decompress (inflate) raw bytes of a PK-compressed document.
   * 
   * @param bytes raw bytes of PK-compressed document.
   * @return byte array of decompressed document
   * @throws IOException If an I/O error occurs
   */
  public byte[] decompressBytes(byte[] bytes) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    InputStream iis = new InflateInputStream(bis, true);
    ByteArrayOutputStream bos = new ByteArrayOutputStream(DEFAULT_OUTPUT_SIZE);
    IOUtils.copy(iis, bos);

    iis.close();
    bis.close();
    bos.flush();
    bos.close();

    final byte[] retval = bos.toByteArray();
    LOGGER.debug("CmsPKCompressor.decompress(byte[]): retval len=" + retval.length);

    return retval;
  }

  /**
   * Decompress (inflate) an InputStream of a PK-compressed document.
   * 
   * @param input InputStream of PK-compressed document.
   * @return byte array of decompressed document
   * @throws IOException If an I/O error occurs
   */
  public byte[] decompressStream(InputStream input) throws IOException {
    InputStream iis = new InflateInputStream(input, true);
    ByteArrayOutputStream bos = new ByteArrayOutputStream(DEFAULT_OUTPUT_SIZE);
    IOUtils.copy(iis, bos);

    iis.close();
    bos.flush();
    bos.close();
    return bos.toByteArray();
  }

  /**
   * Convenience method. Decompress (inflate) a base64-encoded string of a PK-compressed archive.
   * 
   * @param base64Doc base64-encoded, PK-compressed archive
   * @return raw byte array of decompressed document
   * @throws IOException If an I/O error occurs
   */
  public byte[] decompressBase64(String base64Doc) throws IOException {
    final byte[] bytes = decompressBytes(DatatypeConverter.parseBase64Binary(base64Doc.trim()));
    LOGGER.debug("CmsPKCompressor.decompressBase64(String): bytes len=" + bytes.length);
    return bytes;
  }

  /**
   * Convenience method. Decompress (inflate) a hexadecimal string of a PK-compressed archive.
   * 
   * @param hex base64-encoded, PK-compressed archive
   * @return raw byte array of decompressed document
   * @throws IOException If an I/O error occurs
   */
  public byte[] decompressHex(String hex) throws IOException {
    final byte[] bytes = decompressBytes(DatatypeConverter.parseHexBinary(hex.trim()));
    LOGGER.debug("CmsPKCompressor.decompressHex(String): bytes len=" + bytes.length);
    return bytes;
  }

  /**
   * Compress (deflate) a CMS PKWare archive and writes resulting compressed document to given
   * output file.
   * 
   * @param inputFileName file name to decompress
   * @param outputFileName file name of resulting decompressed output
   * @throws IOException If an I/O error occurs
   */
  public void compressFile(String inputFileName, String outputFileName) throws IOException {
    FileInputStream fis = new FileInputStream(new File(inputFileName));
    OutputStream fos = new DeflateOutputStream(new FileOutputStream(new File(outputFileName)),
        DEFAULT_COMPRESSION_LEVEL, true);
    IOUtils.copy(fis, fos);

    fis.close();
    fos.close();
  }

  /**
   * Convenience method. Compress (deflate) a document InputStream.
   * 
   * @param bytes raw bytes of the document to compress
   * @return raw byte array of compressed document
   * @throws IOException If an I/O error occurs
   */
  public byte[] compressBytes(byte[] bytes) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    ByteArrayOutputStream bos = new ByteArrayOutputStream(DEFAULT_OUTPUT_SIZE);

    OutputStream dos = new DeflateOutputStream(bos, DEFAULT_COMPRESSION_LEVEL, true);
    IOUtils.copy(bis, dos);

    bis.close();
    dos.close();

    return bos.toByteArray();
  }

  /**
   * Compress (deflate) a CMS PKWare archive and writes resulting decompressed document to given
   * output file.
   * 
   * <p>
   * EXAMPLE USAGE:
   * </p>
   * 
   * <pre>
   * {@code -d 6916351513091620_CWDST___00007.pk from_java_pk.doc}
   * </pre>
   *
   * <pre>
   * {@code -c from_java_pk.doc something.pk}
   * </pre>
   *
   * <pre>
   * {@code -d something.pk again.doc}
   * </pre>
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    try {
      CmsPKCompressor inst = new CmsPKCompressor();

      String mode = args[0];
      if ("-d".equals(mode)) { // Decompress
        inst.decompressFile(args[1], args[2]);
      } else if ("-h".equals(mode)) { // hex
        final String hex = FileCopyUtils.copyToString(new FileReader(new File(args[1]))).trim();
        System.out.println("hex len=" + hex.length());
        final byte[] bytes = inst.decompressHex(hex);
        System.out.println("bytes len = " + bytes.length);
      } else if ("-b".equals(mode)) { // Base64
        final String b64 = FileCopyUtils.copyToString(new FileReader(new File(args[1]))).trim();
        System.out.println("b64 len=" + b64.length());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
