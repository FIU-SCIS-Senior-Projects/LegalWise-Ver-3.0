package security;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;

import javax.swing.JPopupMenu.Separator;

import res.StringUtils;

/**
 *
 * @author Michel Roger
 *
 */

public class PasswordUser {

	private static final String SEPARATOR = ":";

	public static String getHash(String userEamil) {
		String hash = null;
		Date date = new Date();
		Random random = new Random();

		// gethash for everything but userEmail
		try {
			String tmpHash = StringUtils.getHash(date.toString() + random.nextLong());
			// space , is the combination to add the userEmail
			hash = userEamil + SEPARATOR + tmpHash + SEPARATOR +date.getTime();
			// encrypt everything
			hash = StringUtils.encrypt(hash);
		} catch (Exception e) {
			System.out.println("ERRORR getting the hash!!!!");
			e.printStackTrace();
		}

		return hash;
	}

	public static String[] getEmailHash(String tmpHash) {
		String[] emailHash = null;

		try {
			// space , is the combination to add the userEmail
			// userEamil + SEPARATOR + tmpHash;
			// decrypt everything
			String emailhash = StringUtils.decrypt(tmpHash);
			emailHash = emailhash.split(SEPARATOR);
		} catch (Exception e) {
			System.out.println("ERRORR getting the hash!!!!");
			e.printStackTrace();
		}
		return emailHash;
	}
	
	public static  void   main (String[] args){
		String hash = getHash("test@test.edu");
		String[] emailhash = getEmailHash(hash);
		for (String string : emailhash) {
			System.out.println(string);
		}
	}

}
