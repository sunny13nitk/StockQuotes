package modelframework.utilities;

import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// Encryption Utility Class - to encrypt and decrypt using AES 128 bit encryption
public class EncryptionUtility
	{
		
		/*************************************************************************************************
		 * Encrypt the plain text and returns Encryption Key and Encrypted Value
		 * 
		 * @throws Exception
		 *************************************************************************************************/
		
		public static Map<String, String> Encrypt(String plainText) throws Exception
			{
				Map<String, String> Key_Encrypted_Pair = new HashMap<String, String>();
				
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(128);
				SecretKey secretKey = keyGenerator.generateKey();
				Cipher cipher = Cipher.getInstance("AES");
				
				byte[] plainTextByte = plainText.getBytes();
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				byte[] encryptedByte = cipher.doFinal(plainTextByte);
				Base64.Encoder encoder = Base64.getEncoder();
				String encryptedText = encoder.encodeToString(encryptedByte);
				String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
				
				Key_Encrypted_Pair.put(encodedKey, encryptedText);
				
				return Key_Encrypted_Pair;
			}
			
		/*************************************************************************************************
		 * Decrypt the Encrypted Text using Encryption Key
		 * 
		 * @throws Exception
		 *************************************************************************************************/
		
		public static String Decrypt(Map<String, String> Key_Text_Pair) throws Exception
			{
				String decrypted_text = null;
				if (Key_Text_Pair != null)
					{
						Set keys = Key_Text_Pair.keySet();
						Iterator itr = keys.iterator();
						Cipher cipher = Cipher.getInstance("AES");
						
						String key;
						String value;
						while (itr.hasNext())
							{
								key = (String) itr.next();
								// Convert Key String to SecretKey
								byte[] decodedKey = Base64.getDecoder().decode(key);
								SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
								
								value = Key_Text_Pair.get(key);
								Base64.Decoder decoder = Base64.getDecoder();
								byte[] encryptedTextByte = decoder.decode(value);
								cipher.init(Cipher.DECRYPT_MODE, originalKey);
								byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
								decrypted_text = new String(decryptedByte);
								
							}
							
					}
					
				return decrypted_text;
				
			}
			
	}
