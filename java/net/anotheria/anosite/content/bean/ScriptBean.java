package net.anotheria.anosite.content.bean;


public class ScriptBean {
	
	private String id;
	private String name;
	private String link;
	private String content;
	
	public ScriptBean(){
	}
	
	public ScriptBean(String anId){
		id = anId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override public boolean equals(Object anotherObj){
		if(!(anotherObj instanceof ScriptBean))
			return false;
		return id.equals(((ScriptBean)anotherObj).getId());
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
