package com.happiify.archive;
import com.happiify.utils.AESEncrypter;
import com.happiify.utils.CGEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	static final String uploadedFileFolder = "/Users/chenguoyan/Desktop/uploadedfile/";

	@Test
	public void contextLoads() {


	}
	@Test
	public void myTest(){
		String path = "/Users/chenguoyan/Desktop/uploadedfile/";

		try {
			AESEncrypter aes = new AESEncrypter("hello89078678");
			//String base64FromFile = fileToBase64(path+"test.txt");

			File file = new File(path, "test.txt");
			InputStream in = new FileInputStream(file);
			byte[] bytes = new byte[in.available()];

			String readStr = new String(bytes,"utf-8");

			String base64Str = Base64.getEncoder().encodeToString(bytes);
			//String plainStr = new String(plainData);
			byte[] plainData = Base64.getDecoder().decode(base64Str);

			String finalStr = new String(plainData);

			System.out.println(base64Str);


//			String test = "星期六123Abc";
//			byte[] encrypted = aes.encrypt(test.getBytes());
//			byte[] decrypted = aes.decrypt(encrypted);
//
//			System.out.println(new String(decrypted));

			//encryptFile(path,"test.txt","hello89078678");
			decryptFile(path,"test.txt","hello89078678");

		} catch (Exception e) {
			System.out.println("something wrong");
			e.printStackTrace();
		}
		System.out.println("this is test");
	}

	void decryptFile(String filePath, String fileName, String password) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		File file = new File(filePath,fileName);
		try {
			inputStream = new FileInputStream(file);
			byte[] data = new byte[inputStream.available()];
			AESEncrypter aesEncrypter = new AESEncrypter(password);
			String  encryptedContent = new String(data);

			String decryptedContent = aesEncrypter.decrypt(encryptedContent);
			//byte[] originalContent = Base64.getDecoder().decode(decryptedContent);

			outputStream = new FileOutputStream(file,false);
			outputStream.write(decryptedContent.getBytes());
			outputStream.flush();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void encryptFile(String filePath, String fileName, String password) {
		FileOutputStream fileOutputStream = null;
		String base64Content = fileToBase64(filePath + fileName);
		System.out.println(base64Content);
		try {
			AESEncrypter aesEncrypter = new AESEncrypter(password);
			String encryptedContent = aesEncrypter.encrypt(base64Content);
			File file = new File(filePath, fileName);
			fileOutputStream = new FileOutputStream(file, false);
			byte[] data = encryptedContent.getBytes();
			fileOutputStream.write(data);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public String fileToBase64(String path) {
		String base64 = null;
		InputStream in = null;
		try {
			File file = new File(path);
			in = new FileInputStream(file);
			byte[] bytes = new byte[in.available()];
			base64 = Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return base64;
	}

	@Test
	public void myTest02() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
		CGEncryptor encryptor = new CGEncryptor("hello1234");
		//encryptor.encrypt(uploadedFileFolder+"t03.zip");
		encryptor.decrypt(uploadedFileFolder+"t03.zip");
	}

}
