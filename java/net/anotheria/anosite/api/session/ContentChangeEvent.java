package net.anotheria.anosite.api.session;

public class ContentChangeEvent {
	private String documentName;
	private ContentChangeType changeType;
	private String documentId;
	
	public ContentChangeEvent(){
		
	}
	
	public ContentChangeEvent(ContentChangeType aType, String aDocumentName, String aDocumentId){
		changeType = aType;
		documentName = aDocumentName;
		documentId = aDocumentId;
	}
	
	public String toString(){
		return getChangeType()+" "+getDocumentName()+": "+getDocumentId();
	}
	
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public ContentChangeType getChangeType() {
		return changeType;
	}
	public void setChangeType(ContentChangeType changeType) {
		this.changeType = changeType;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
}
