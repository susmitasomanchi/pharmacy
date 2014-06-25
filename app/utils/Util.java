package utils;

public class Util {

	public static String simpleSlugify(final String string){
		final String slug = string
				.toLowerCase()
				.replaceAll("[^\\p{ASCII}]", "")
				.replaceAll("[^\\w+]", "-")
				.replaceAll("\\s+", "-")
				.replaceAll("[-]+", "-")
				.replaceAll("^-", "")
				.replaceAll("-$", "")
				.trim();
		return slug;
	}

}
