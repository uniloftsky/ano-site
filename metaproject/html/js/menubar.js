/*
 *	Javascript Bibliothek
 *	
 *	Copyright (c) 1997-2007 face Kommunikationsentwicklung GmbH
 *	
 *	
 *	
 *	menuBar.js - Darstellung einer Menue-Struktur
 *	
 *	
 *	Autor:			Rudolf Baier
 *	Stand:			15.11.2007
 *	
 */ 




// Determine browser and version.

function Browser() {

	var ua, s, i;

	this.isIE		= false;	// Internet Explorer
	this.isNS		= false;	// Netscape
	this.version = null;

	ua = navigator.userAgent;

	s = "MSIE";
	if ((i = ua.indexOf(s)) >= 0) {
		this.isIE = true;
		this.version = parseFloat(ua.substr(i + s.length));
		return;
	}

	s = "Netscape6/";
	if ((i = ua.indexOf(s)) >= 0) {
		this.isNS = true;
		this.version = parseFloat(ua.substr(i + s.length));
		return;
	}

	// Treat any other "Gecko" browser as NS 6.1.

	s = "Gecko";
	if ((i = ua.indexOf(s)) >= 0) {
		this.isNS = true;
		this.version = 6.1;
		return;
	}
}

var browser = new Browser();

// Global variable for tracking the currently active button.

var activeButton = null;
var menuBar = { structure: new Array(), button: new Array(), menu: new Array(), cover: new Array() };

// Capture mouse clicks on the page so any active button can be
// deactivated.

if (browser.isNS)
	document.addEventListener("mousedown", pageMousedown, true);
else
	document.onmousedown = pageMousedown;

function pageMousedown(event) {
	
	var el;

	// If there is no active menu, exit.

	if (!activeButton)
		return;

	// Find the element that was clicked on.

	if (browser.isIE)
		el = window.event.srcElement;
	if (browser.isNS)
		el = (event.target.className ? event.target : event.target.parentNode);

	// If the active button was clicked on, exit.

	if (el == activeButton)
		return;
	
	
	
	// If the element clicked on was not a menu button or item, close the
	// active menu.
	
	var i		= el.parentNode;
	var item	= false;
	while ( i ) {
		if (i.tagName == 'UL' && i.className && i.className == 'menuBar') {
			item = true;
			break;
		}
		var i = i.parentNode;
	}
	if ( !item )
		resetButton(activeButton);

}

function buttonClick(button) {

	getMenuStructure(button);


	// Blur focus from the link to remove that annoying outline.

	button.blur();

	// Associate the menu to this button if not already done.

	if (!button.menu) {
		menu = getMenu (button); 
		if (menu) {
			button.menu = menu;
		}
	}

	// Reset the currently active button, if any.

	if (activeButton && activeButton != button)
		resetButton(activeButton);

	// Toggle the button's state.

	if (button.isDepressed)
		resetButton(button);
	else
		depressButton(button);

	return false;
}

function buttonMouseover(button) {
	
	getMenuStructure(button);
	
	
	// If any other button menu is active, deactivate it and activate this one.
	
	if (activeButton && activeButton != button) {
		resetButton(activeButton);
		buttonClick(button);
	}
	closeSubMenus();
	
}

function closeSubMenus(level) {
	if ( menuBar.menu.length ) {
		var i = (level) ? level : 1;
		while ( menuBar.menu[i] ) {
			menuBar.menu[i].style.visibility = 'hidden';
			menuBar.menu[i] = null;
			if (menuBar.button[i]) {
				menuBar.button[i].className = menuBar.button[i].className.replace('subMenuButtonActive', 'subMenuButton');
				menuBar.button[i] = null;
			}
			if (menuBar.cover[i])
				menuBar.cover[i].style.display = 'none';
			i++;
		}
	}
}

//	Menue holen
function getMenu (button) {
	var menu = null;
	for ( var i = 0; i < menuBar.structure.length; i++ ) {
		if ( menuBar.structure[i].button == button ) {
			menu = menuBar.structure[i].menu;
			break;
		}
	}
	return (menu);
}

function getMenuLevel(button) {
	var i = button.parentNode;
	var level = -1;
	while (i) {
		if (i.tagName == 'UL') {
			level++;
		}
		i = i.parentNode;
	}
	return level;
}


function getMenuStructure(button) {
	if (!menuBar.structure.length) {
		var i = button.parentNode;
		while (i) {
			if (i.tagName == 'UL') {
				var list = i;
				break;
			}
			i = i.parentNode;
		}
		var level	= 0;
		var menu	= 0;
		menuBar.structure[menu] = { button: null, menu: list, item: new Array(), level: new Array() };
		getMenuItems(level, menu, list);
	}
}

function getMenuItems(level, menu, list) {
	var item = 0;
	var childList = null;
	var currentMenuNumber = null;
	var menuButton = null;
	
	for (var i = 0; i < list.childNodes.length; i++ ) {
		if (list.childNodes[i].tagName == 'LI') {
			if ( list.childNodes[i].childNodes.length ) {
				for (var m = 0; m < list.childNodes[i].childNodes.length; m++ ) {
					if (list.childNodes[i].childNodes[m].tagName == 'A') {
						var menuButton = list.childNodes[i].childNodes[m];
						if (!level) {
							menuButton.onmouseover = function () { buttonMouseover(this); };
						} else {
							menuButton.onmouseover = function () { menuMouseover(this); };
						}
						item++;
					}
					if (list.childNodes[i].childNodes[m].tagName == 'UL') {
						childList = list.childNodes[i].childNodes[m];
						if (!level) {
							menuButton.onmouseover = function () { buttonMouseover(this); };
						} else {
							menuButton.onmouseover = function () { menuMouseover(this); };
							menuButton.onclick = function () { return menuClick(this) };
						}

						if (level) {
							menuButton.className += ' subMenuButton';
						}

						menuBar.structure[menuBar.structure.length] = { button: menuButton, menu: childList, item: new Array(), level: new Array() };
						getMenuItems(level+1, menuBar.structure.length, childList);
					}
				}
			}
		}
	}
}



function menuMouseover(button) {
	var level = getMenuLevel(button);
	closeSubMenus(level);
	var menu = getMenu (button); 
	if (menu) {
		button.className = button.className.replace('subMenuButton', 'subMenuButtonActive');
		showMenu ( level, menu, button.offsetWidth, button.offsetTop - 2 );
		menuBar.button[level] = button;
		menuBar.menu[level] = menu;
	}
}

function menuClick(button) {

	// Blur focus from the link to remove that annoying outline.

	button.blur();

	return false;
}

function showMenu ( level, menu, left, top ) {

	// Cover form elements with iframe.
	
	if ( !menuBar.cover[level] ) {
		menuBar.cover[level] = document.createElement('IFRAME');
		menuBar.cover[level].style.display 		= 'none';
		menuBar.cover[level].style.position		= 'absolute';
		menuBar.cover[level].style.border 		= '0';
		menuBar.cover[level].style.zIndex		= '11';
		document.body.appendChild (menuBar.cover[level]);
	}

	if (!level) {
		p = { left: 0, top: 0 };
	} else {
		p = getElementPosSize (menu.parentNode.parentNode);
	}
	
	mp = getElementPosSize (menu);
	menuBar.cover[level].style.left			= (p.left + left) + 'px';
	menuBar.cover[level].style.top			= (p.top + top) + 'px';
	menuBar.cover[level].style.width		= mp.width;
	menuBar.cover[level].style.height		= mp.height;
	menuBar.cover[level].style.display 		= 'block';
	
	// Position and show the menu.
	
	menu.style.zIndex						= '12';
	menu.style.left							= left + 'px';
	menu.style.top							= top + 'px';
	menu.style.visibility					= 'visible';
	
}

function depressButton(button) {

	var w, dw, x, y;

	// Change the button's style class to make it look like it's depressed.

	button.className = button.className.replace('menuButton', 'menuButtonActive');

	// For IE, set an explicit width on the first menu item. This will
	// cause link hovers to work on all the menu's items even when the
	// cursor is not over the link's text.
	
	if (button.menu) {
		if (browser.isIE && !button.menu.firstChild.style.width) {
			w = button.menu.firstChild.offsetWidth;
			button.menu.firstChild.style.width = w + "px";
			dw = button.menu.firstChild.offsetWidth - w;
			w -= dw;
			button.menu.firstChild.style.width = w + "px";
		}
	
		// Position the associated drop down menu under the button and
		// show it. Note that the position must be adjusted according to
		// browser, styling and positioning.
	
		x = getPageOffsetLeft(button);
		y = getPageOffsetTop(button) + button.offsetHeight;
		if (browser.isIE) {
			x += 2;
			y += 2;
		}
		if (browser.isNS && browser.version < 6.1)
			y--;
		
		showMenu ( 0, button.menu, x, y );
		
	}
	
	// Set button state and let the world know which button is
	// active.

	button.isDepressed = true;
	activeButton = button;
}

function resetButton(button) {

	// Restore the button's style class.
	
	button.className = button.className.replace('menuButtonActive', 'menuButton');
	
	// Hide the button's menu.

	if (button.menu) {
		button.menu.style.visibility = "hidden";
		if (menuBar.cover[0])
			menuBar.cover[0].style.display = 'none';
	}
	
	// Set button state and clear active menu global.
	
	button.isDepressed = false;
	activeButton = null;
	closeSubMenus();
}

function getPageOffsetLeft(el) {

	// Return the true x coordinate of an element relative to the page.

	return el.offsetLeft + (el.offsetParent ? getPageOffsetLeft(el.offsetParent) : 0);
}

function getPageOffsetTop(el) {

	// Return the true y coordinate of an element relative to the page.

	return el.offsetTop + (el.offsetParent ? getPageOffsetTop(el.offsetParent) : 0);
}

function getElementPosSize ( id ) {

	// Return the true x and y coordinate of an element relative to the page 
	// and its width and height.

	if ( !document.getElementById ) return null;
	
	var element = typeof id == 'string' ? document.getElementById (id) : id;
	
	if ( !element ) return null;
	
	var posSize = { left: 0, top: 0, width: 0, height: 0 };
	posSize.width	= element.offsetWidth;
	posSize.height	= element.offsetHeight;
	while ( element ) {
		 posSize.left	+= parseInt (element.offsetLeft);
		 posSize.top	+= parseInt (element.offsetTop);
		 element = element.offsetParent;
	}
	
	return ( posSize );
	
}


