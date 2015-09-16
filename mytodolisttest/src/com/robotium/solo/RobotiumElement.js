package com.robotium.solo;
/**
 * Used by the web methods.
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */
// ��ȡ���е�WebԪ��
function allWebElements() {
	for (var key in document.all){
		try{
		// ͨ��Robotium WebClient
			promptElement(document.all[key]);			
		}catch(ignored){}
	}
	// ֪ͨ�ű�ִ�����
	finished();
}
// �������� TEXT�ڵ�
function allTexts() {
//��ʼ��һ��range
	var range = document.createRange();
	var walk=document.createTreeWalker(document.body,NodeFilter.SHOW_TEXT,null,false); 
	while(n=walk.nextNode()){
		try{
		// ֪ͨRobotium WebClient
			promptText(n, range);
		}catch(ignored){}
	} 
	// ֪ͨ�ű�ִ�����
	finished();
}

// ���Element
function clickElement(element){
    // �����������¼�
	var e = document.createEvent('MouseEvents');
	e.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
	element.dispatchEvent(e);
}
//����ָ��id��element,clickΪtrue����,Ϊfalse�����Robotium WebClient�����Ϣ
function id(id, click) {
    // ��ȡid��Ӧ�� Element
	var element = document.getElementById(id);
	// �ҵ�Element���͵��
	if(element != null){ 
	// true����
		if(click == 'true'){
			clickElement(element);
		}
	// �������Robotium WebClient�����Ϣ
		else{
			promptElement(element);
		}
	} 
	// ����idδ�ҵ����������Ԫ�ز�ѯ
	else {
		for (var key in document.all){
			try{
				element = document.all[key];
				// �ҵ���ִ�к�������
				if(element.id == id) {
				// Ϊtrue����
					if(click == 'true'){
						clickElement(element);
						return;
					}
				// �������֪ robotiumWebClient�����Ϣ
					else{
						promptElement(element);
					}
				}
			} catch(ignored){}			
		}
	}
	// jsִ�����
	finished(); 
}
// ����xpath�������elements,clickΪtrue�������ҵ��ĵ�һ������true���ύelements�����Ϣ��RobotiumWebClient
function xpath(xpath, click) {
	// ����xpath����ָ��elements
	var elements = document.evaluate(xpath, document, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null); 

	if (elements){
	// ����elements
		var element = elements.iterateNext();
		while(element) {
			// Ϊtrue����
			if(click == 'true'){
				clickElement(element);
				return;
			}
			// �������ύ�����Ϣ��RobotiumWebClient
			else{
				promptElement(element);
				element = result.iterateNext();
			}
		}
		// �ű�ִ�н���
		finished();
	}
}
// ����css�������elements,clickΪ true�����ҵ��ĵ�һ���������ύ���elements��Ϣ��RobotiumWebClient
function cssSelector(cssSelector, click) {
// ����css�������elements
	var elements = document.querySelectorAll(cssSelector);
	// ����elements
	for (var key in elements) {
		if(elements != null){ 
			try{
			// clickΪ true���������˳�
				if(click == 'true'){
					clickElement(elements[key]);
					return;
				}
				//�ύelement�����Ϣ��RobotiumWebClient
				else{
					promptElement(elements[key]);
				}	
			}catch(ignored){}  
		}
	}
	// �ű�ִ�н���
	finished(); 
}
// ����name���Ҷ�Ӧ��element.clickΪtrue���������ĵ�һ���������ύelement��Ϣ��RobotiumWebClient
function name(name, click) {
    // ��ȡ����ʵ��
	var walk=document.createTreeWalker(document.body,NodeFilter.SHOW_ELEMENT,null,false); 
	// ����
	while(n=walk.nextNode()){
		try{
		// ����Ƿ���ָ����name
			var attributeName = n.getAttribute('name');
			if(attributeName != null && attributeName.trim().length>0 && attributeName == name){
			// clickΪ true���������˳�
				if(click == 'true'){
					clickElement(n);
					return;
				}
				//�ύelement�����Ϣ��RobotiumWebClient
				else{
					promptElement(n);
				}	
			}
		}catch(ignored){} 
	} 
	// �ű�ִ�н���
	finished();
}
// ����classname����element,clickΪtrue���������ĵ�һ���������ύelement��Ϣ��RobotiumWebClient
function className(nameOfClass, click) {
// ��ȡ����ʵ��
	var walk=document.createTreeWalker(document.body,NodeFilter.SHOW_ELEMENT,null,false); 
	// ����
	while(n=walk.nextNode()){
		try{
			var className = n.className; 
			// �ҵ���Ӧ��element
			if(className != null && className.trim().length>0 && className == nameOfClass) {
			// clickΪ true���������˳�
				if(click == 'true'){
					clickElement(n);
					return;
				}
			//�ύelement�����Ϣ��RobotiumWebClient	
				else{
					promptElement(n);
				}	
			}
		}catch(ignored){} 
	} 
	// �ű�ִ�н���
	finished(); 
}
// ����text����element,clickΪtrue���������ĵ�һ���������ύelement��Ϣ��RobotiumWebClient
function textContent(text, click) {
	// ��ȡ��Ӧ�ı���ʵ��
	var range = document.createRange();
	var walk=document.createTreeWalker(document.body,NodeFilter.SHOW_TEXT,null,false); 
	// ����
	while(n=walk.nextNode()){ 
		try{
			var textContent = n.textContent; 
			// �ҵ�ָ����element
			if(textContent.trim() == text.trim()){  
			// clickΪ true���������˳�
				if(click == 'true'){
					clickElement(n);
					return;
				}
			//�ύelement�����Ϣ��RobotiumWebClient	
				else{
					promptText(n, range);
				}
			}
		}catch(ignored){} 
	} 
	// �ű�ִ�н���
	finished();  
}
// ����tagname����element,clickΪtrue���������ĵ�һ���������ύelement��Ϣ��RobotiumWebClient
function tagName(tagName, click) {
    // ���Ҷ�Ӧ��element
	var elements = document.getElementsByTagName(tagName);
	for (var key in elements) {
		if(elements != null){ 
			try{
			// clickΪ true���������˳�
				if(click == 'true'){
					clickElement(elements[key]);
					return;
				}
			//�ύelement�����Ϣ��RobotiumWebClient	
				else{
					promptElement(elements[key]);
				}	
			}catch(ignored){}  
		}
	}
	// �ű�ִ�н���
	finished();
}
// ָ��id��element����text
function enterTextById(id, text) {
	var element = document.getElementById(id);
	if(element != null)
		element.value = text;

	finished(); 
}
// ָ��xpath��element����text
function enterTextByXpath(xpath, text) {
	// ֻ��ȡһ��
	var element = document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null ).singleNodeValue;
	if(element != null)
		element.value = text;

	finished(); 
}
// ָ��css��element����text
function enterTextByCssSelector(cssSelector, text) {
	var element = document.querySelector(cssSelector);
	if(element != null)
		element.value = text;

	finished(); 
}
// ָ��name��element����text
function enterTextByName(name, text) {
	var walk=document.createTreeWalker(document.body,NodeFilter.SHOW_ELEMENT,null,false); 
	while(n=walk.nextNode()){
		var attributeName = n.getAttribute('name');
		if(attributeName != null && attributeName.trim().length>0 && attributeName == name) 
			n.value=text;  
	} 
	finished();
}
// ָ��classname��element����text,��������д��className�Ϻ�
function enterTextByClassName(name, text) {
	var walk=document.createTreeWalker(document.body,NodeFilter.SHOW_ELEMENT,null,false); 
	while(n=walk.nextNode()){
		var className = n.className; 
		if(className != null && className.trim().length>0 && className == name) 
			n.value=text;
	}
	finished();
}
// ��������text���ݲ��Ҷ�Ӧ��element������Ϊָ����text
function enterTextByTextContent(textContent, text) {
	var walk=document.createTreeWalker(document.body,NodeFilter.SHOW_TEXT,null,false); 
	while(n=walk.nextNode()){ 
		var textValue = n.textContent; 
		if(textValue == textContent) 
			n.parentNode.value = text; 
	}
	finished();
}
// ָ��tagname��element����text
function enterTextByTagName(tagName, text) {
	var elements = document.getElementsByTagName(tagName);
	if(elements != null){
		elements[0].value = text;
	}
	finished();
}
// ��ȡElement���ԣ�������prompt��������������,Robotium�޸Ĺ���WebClientץȡ��Щ��Ϣ��������ҳ��Ԫ�� Element
function promptElement(element) {
    // ��ȡelement��id
	var id = element.id;
	// ��ȡelement�� text
	var text = element.innerText;
	// ���˵��ո�
	if(text.trim().length == 0){
		text = element.value;
	}
	// ��ȡelement��name����
	var name = element.getAttribute('name');
	// ��ȡelement��classname����
	var className = element.className;
	// ��ȡelement��tagname����
	var tagName = element.tagName;
	��ȡʣ�����������
	var attributes = "";
	var htmlAttributes = element.attributes;
	// ����ʣ�����ԣ�ͨ��#$�ָ�����
	for (var i = 0, htmlAttribute; htmlAttribute = htmlAttributes[i]; i++){
		attributes += htmlAttribute.name + "::" + htmlAttribute.value;
		if (i + 1 < htmlAttributes.length) {
			attributes += "#$";
		}
	}
	// ��ȡelement��С,
	var rect = element.getBoundingClientRect();
	// �ɼ���elementƴ���ַ��������ݸ�Robotium WebClient
	if(rect.width > 0 && rect.height > 0 && rect.left >= 0 && rect.top >= 0){
		prompt(id + ';,' + text + ';,' + name + ";," + className + ";," + tagName + ";," + rect.left + ';,' + rect.top + ';,' + rect.width + ';,' + rect.height + ';,' + attributes);
	}
}
// ����range��Ϣ�������ݷ��ظ�Robotium WebClient
function promptText(element, range) {	
    // ��ȡElemet��text����
	var text = element.textContent;
	if(text.trim().length>0) {
	    // ����range�ķ�ΧΪ��ǰElement
		range.selectNodeContents(element);
		// ��ȡ�ߴ���Ϣ
		var rect = range.getBoundingClientRect();
		// ֻ���ؿɼ��� Element
		if(rect.width > 0 && rect.height > 0 && rect.left >= 0 && rect.top >= 0){
			var id = element.parentNode.id;
			var name = element.parentNode.getAttribute('name');
			var className = element.parentNode.className;
			var tagName = element.parentNode.tagName;
			prompt(id + ';,' + text + ';,' + name + ";," + className + ";," + tagName + ";," + rect.left + ';,' + rect.top + ';,' + rect.width + ';,' + rect.height);
		}
	}
}

// jsִ����ϣ�֪ͨRobotium WebClient �����
function finished(){
	prompt('robotium-finished');
}