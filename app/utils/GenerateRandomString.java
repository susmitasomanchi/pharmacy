package utils;

import java.util.Random;

public class GenerateRandomString {
	
	/**
	 * @author:  anand
	 * 
	 * @description : this method is used to generate random String value
	 * 
	 * */
	public static String generatePassword(){
		final String dCase = "abcdefghijklmnopqrstuvwxyz";
	    final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    final String sChar = "!@#$%^&*";
	    final String intChar = "0123456789";
	    Random r = new Random();
	    String pass = "";

	   
	        while (pass.length () != 6){
	            int rPick = r.nextInt(4);
	            if (rPick == 0){
	                int spot = r.nextInt(25);
	                pass += dCase.charAt(spot);
	            } else if (rPick == 1) {
	                int spot = r.nextInt (25);
	                pass += uCase.charAt(spot);
	            } else if (rPick == 2) {
	                int spot = r.nextInt (7);
	                pass += sChar.charAt(spot);
	            } else if (rPick == 3){
	                int spot = r.nextInt (9);
	                pass += intChar.charAt (spot);
	            }
	        }
	    return pass;
	}


}
