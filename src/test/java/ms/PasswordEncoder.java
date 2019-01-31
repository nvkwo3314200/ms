package ms;

import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class PasswordEncoder {
	
	@Test
	public void encoder() {
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		String pwd = md5PasswordEncoder.encodePassword("Aa12345","admin");
		System.out.println(pwd);
		/**
		 * a-1 68473ad50150b264f38483cd64122095
		 * b-1 2282bd72f2bc4be6d45abe5beb33dcb7
		 */
	}
	
}
