/****************************************************/
/** Free script for any use, but please include    **/
/** a link to i-code.co.uk in any redistribution.  **/
/**                                                **/
/** Author : Stephen Griffin, www.i-code.co.uk     **/
/****************************************************/

	var list = new Array(); 
	var listIndex = 0;

function addImage(imagesrc){
	list.push(imagesrc);
}

function changeImage(){
	document.getElementById('indexdisplay').innerHTML = (listIndex+1) +" / "+list.length;
	document.getElementById('productimage').src = list[listIndex];
}

function prevImage(){
	if(listIndex == 0){
		listIndex = list.length-1;
	}else{
		listIndex--;
	}
	changeImage();
}

function nextImage(){
	if(listIndex == list.length-1){
		listIndex = 0;
	}else{
		listIndex++;
	}
	changeImage();
}
