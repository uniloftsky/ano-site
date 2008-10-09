package net.anotheria.anosite.api.util.paging;

import java.util.ArrayList;
import java.util.List;

public class PagingControl {
	private int currentPage;
	private int numberOfElements;
	private int elementsPerPage;
	private int maxEntries;
	private boolean moreHitsAvailable;
	
	private List<PagingElement> elements;
	
	public PagingControl(){
		this(1,1,1);
	}

	public PagingControl(int aCurrentPage, int anElementsPerPage, int aNumberOfElements){
		this(aCurrentPage, anElementsPerPage, aNumberOfElements, false);
	}

	public PagingControl(int aCurrentPage, int anElementsPerPage, int aNumberOfElements, boolean aMoreHitsAvailable){
		currentPage = aCurrentPage;
		elementsPerPage = anElementsPerPage;
		numberOfElements = aNumberOfElements;
		elements = new ArrayList<PagingElement>();
		moreHitsAvailable = aMoreHitsAvailable;
		setup();
	}
		
	@Override
	public String toString(){
		return "currentPage:"+currentPage+", numberOfElements:"+numberOfElements+", elementsPerPage:"+elementsPerPage+ 
				", maxEntries:"+maxEntries+", moreHitsAvailable:"+moreHitsAvailable;
	}
	
	void setup(){
		int numberOfPages = getNumberOfPages();
		for (int i=1; i<=numberOfPages; i++){
			if (i==currentPage){
				elements.add(new PagingCurrentPage(""+i));
			}else{
				boolean addLink = numberOfPages <= 7 ? true : addLink(i, currentPage, numberOfPages);
				if (addLink)
					elements.add(new PagingLink(""+i, i));
				if (!addLink){
					if (!elements.get(elements.size()-1).isSeparator())
						elements.add(new Separator());
				}
			}
		}
	}
	
	private boolean addLink(int page, int selectedPage, int numberOfPages){
		if (page==1 || page==numberOfPages)
			return true;
		if (((selectedPage>page) && (selectedPage-page <= 2)) || ((page>selectedPage) && (page-selectedPage<=2)))
			return true;
			
		return false;
		
	}
	
	public int getNumberOfElements(){
		return numberOfElements;
	}
	
	public int getNumberOfPages(){
		int numberOfPages = numberOfElements / elementsPerPage;
		if (numberOfPages*elementsPerPage<numberOfElements)
			numberOfPages++;
		return numberOfPages;
	}
	
	public List<PagingElement> getElements(){
		return elements;
	}
	
	public boolean hasNext(){
		return currentPage < getNumberOfPages();
	}
	
	public boolean hasPrevious(){
		return currentPage>=2;
	}
	
	public boolean isLast(){
		return currentPage == getNumberOfPages();
	}
	
	public boolean isFirst(){
		return currentPage == 1;
	}

	public int getMaxEntries() {
		return maxEntries;
	}

	public void setMaxEntries(int aMaxEntries) {
		maxEntries = aMaxEntries;
	}

	public boolean isMoreHitsAvailable() {
		return moreHitsAvailable;
	}
	
	public int getNextPageNumber(){
		return currentPage + 1;
	}
	
	public int getPreviousPageNumber(){
		return currentPage-1;
	}
}
