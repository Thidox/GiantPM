package nl.giantit.minecraft.GiantPM.core.Tools.Muter;

/**
 *
 * @author Giant
 */
public class MuterResp {
	
	private final MuterRespType t;
	private final String reason;
	
	public static enum MuterRespType {
		SUCCESS(1),
		FAIL(2);
		
		private int id;
		
		MuterRespType(int id) {
			this.id = id;
		}
		
		int getId() {
			return this.id;
		}
	}
	
	public MuterResp(MuterRespType t, String reason) {
		this.t = t;
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public boolean getStatus() {
		switch(t) {
			case SUCCESS:
				return true;
			default:
				return false;
		}
	}
}
