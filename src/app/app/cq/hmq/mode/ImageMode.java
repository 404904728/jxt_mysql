package app.cq.hmq.mode;
/**
 * 
 * @author Limit
 *
 */
public class ImageMode {
	
	private String imgId;
	private String imgPath;
	private String thumbPath;
	private String tagsOne;
	private String tagsTwo;
	private String tagsThree;
	private String tagsFour;
	private String descript;
	
	
	public ImageMode(String imgId, String imgPath, String thumbPath,
			String tagsOne, String tagsTwo, String tagsThree, String tagsFour,
			String descript) {
		super();
		this.imgId = imgId;
		this.imgPath = imgPath;
		this.thumbPath = thumbPath;
		this.tagsOne = tagsOne;
		this.tagsTwo = tagsTwo;
		this.tagsThree = tagsThree;
		this.tagsFour = tagsFour;
		this.descript = descript;
	}
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
	public String getImgPath() {
		return imgPath;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getTagsOne() {
		return tagsOne;
	}
	public void setTagsOne(String tagsOne) {
		this.tagsOne = tagsOne;
	}
	public String getTagsTwo() {
		return tagsTwo;
	}
	public void setTagsTwo(String tagsTwo) {
		this.tagsTwo = tagsTwo;
	}
	public String getTagsThree() {
		return tagsThree;
	}
	public void setTagsThree(String tagsThree) {
		this.tagsThree = tagsThree;
	}
	public String getTagsFour() {
		return tagsFour;
	}
	public void setTagsFour(String tagsFour) {
		this.tagsFour = tagsFour;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}

}
