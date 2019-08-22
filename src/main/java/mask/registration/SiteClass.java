package mask.registration;

public enum SiteClass {
	
	REG_MARK, 
	ALIGN_MARK, 
	INFO_ONLY;

	public static SiteClass fromString(String value) {
		
		if (null == value) {
			return REG_MARK;
		}
		
		String description = value.toLowerCase();
		
		if (description.contains("align")) {
			return ALIGN_MARK;
		}
		
		if (description.contains("info")) {
			return SiteClass.INFO_ONLY;
		}
		
		return REG_MARK;
	}

}
