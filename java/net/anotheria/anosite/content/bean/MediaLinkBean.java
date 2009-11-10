package net.anotheria.anosite.content.bean;


public class MediaLinkBean {
	
	private String id;
	private String name;
	private String href;
	private String type;
	private String media;
	private String rel;
	private String rev;
	private String charset;
	private String hreflang;
	
	public MediaLinkBean(){
	}
	
	public MediaLinkBean(String anId){
		id = anId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getHreflang() {
		return hreflang;
	}
	public void setHreflang(String hreflang) {
		this.hreflang = hreflang;
	}
	
	@Override public boolean equals(Object anotherObj){
		if(!(anotherObj instanceof MediaLinkBean))
			return false;
		return id.equals(((MediaLinkBean)anotherObj).getId());
	}
	
	@Override public int hashCode(){
		return id.hashCode();
	}
	
	@Override public String toString(){
		String ret = "";
		ret += "MediaLinkBean " + id;
		return ret;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public static void main(String[] args) {
		System.out.println(new MediaLinkBean("1").equals(new MediaLinkBean("1")));
		System.out.println(new MediaLinkBean("1").equals(new MediaLinkBean("2")));
		
	}
}
