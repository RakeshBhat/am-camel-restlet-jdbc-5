package student;

public class Util {
	public static String replace(String s) {
		while (s.contains("%%"))
			s = s.replace("%%", "$");
		return s;
	}
}
