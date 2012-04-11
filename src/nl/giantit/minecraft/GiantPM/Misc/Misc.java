package nl.giantit.minecraft.GiantPM.Misc;

import java.util.List;
/**
 *
 * @author Giant
 */
public class Misc {
	
	public static boolean isEither(String target, String is, String either) {
		if(target.equals(is) || target.equals(either))
			return true;
		return false;
	}
	
	public static boolean isEitherIgnoreCase(String target, String is, String either) {
		if(target.equalsIgnoreCase(is) || target.equalsIgnoreCase(either))
			return true;
		return false;
	}
	
	public static boolean isAny(String target, String ...test) {
		for(String t : test) {
			if(target.equals(t))
				return true;
		}
		return false;
	}
	
	public static boolean isAnyIgnoreCase(String target, String ...test) {
		for(String t : test) {
			if(target.equalsIgnoreCase(t))
				return true;
		}
		return false;
	}
	
	public static Boolean contains(List<String> haystack, String needle) {
		for(String hay : haystack) {
			hay = hay.replace("[", "");
			hay = hay.replace("]", "");
			if(hay.equals(needle)) {
				return true;
			}
		}
		return false;
	}
	
	public static Boolean containsIgnoreCase(List<String> haystack, String needle) {
		for(String hay : haystack) {
			hay = hay.replace("[", "");
			hay = hay.replace("]", "");
			if(hay.equalsIgnoreCase(needle)) {
				return true;
			}
		}
		return false;
	}
}
