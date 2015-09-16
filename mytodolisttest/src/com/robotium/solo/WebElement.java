package com.robotium.solo;

import java.util.Hashtable;

/**
 * ����WebView�и���Ԫ�أ�����input֮���
 * Represents an element shown in a WebView.  
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

public class WebElement {
	// ��Ӧ��Ļ�еĸÿؼ��м�λ��x����
	private int locationX = 0;
	// ��Ӧ��Ļ�еĸĿؼ��м�λ��y����
	private int locationY = 0;
	// WebԪ�� id
	private String id;
	// WebԪ��text
	private String text;
	// WebԪ��name
	private String name;
	// WebԪ��class
	private String className;
	// WebԪ��tag
	private String tagName;
	// ������������
	private Hashtable<String, String> attributes;
	

	/**
	 * ���캯��
	 * Constructs this object. 
	 * 
	 * @param webId the given web id
	 * @param textContent the given text to be set
	 * @param name the given name to be set
	 * @param className the given class name to set
	 * @param tagName the given tag name to be set
	 * @param attributes the attributes to set
	 */

	public WebElement(String webId, String textContent, String name, String className, String tagName, Hashtable<String, String> attributes) {

		this.setId(webId);
		this.setTextContent(textContent);
		this.setName(name);
		this.setClassName(className);
		this.setTagName(tagName);
		this.setAttributes(attributes);
	}

	/**
	 * ��ȡ WebElementԪ�ض�Ӧ����Ļ����
	 * Returns the WebElements location on screen.
	 */

	public void getLocationOnScreen(int[] location) {

		location[0] = locationX;
		location[1] = locationY;
	}

	/**
	 * ������Ļ���x����
	 * Sets the X location.
	 * 
	 * @param locationX the X location of the {@code WebElement}
	 */

	public void setLocationX(int locationX){
		this.locationX = locationX;
	}

	/**
	 * ������Ļ���Y����
	 * Sets the Y location.
	 * 
	 * @param locationY the Y location of the {@code WebElement}
	 */

	public void setLocationY(int locationY){
		this.locationY = locationY;
	}

	/**
	 * ��ȡ��Ļ���X����
	 * Returns the X location.
	 * 
	 * @return the X location
	 */

	public int getLocationX(){
		return this.locationX;
	}

	/**
	 * ��ȡ��Ļ���Y����
	 * Returns the Y location.
	 * 
	 * @return the Y location
	 */

	public int getLocationY(){
		return this.locationY;
	}

	/**
	 * ��ȡid
	 * Returns the id.
	 * 
	 * @return the id
	 */

	public String getId() {
		return id;
	}

	/**
	 * ����id
	 * Sets the id.
	 * 
	 * @param id the id to set
	 */

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡname
	 * Returns the name.
	 * 
	 * @return the name
	 */

	public String getName() {
		return name;
	}

	/**
	 * ����name
	 * Sets the name.
	 * 
	 * @param name the name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡclass
	 * Returns the class name.
	 * 
	 * @return the class name
	 */

	public String getClassName() {
		return className;
	}

	/**
	 * ����class
	 * Sets the class name.
	 * 
	 * @param className the class name to set
	 */

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * ��ȡtag
	 * Returns the tag name.
	 * 
	 * @return the tag name
	 */

	public String getTagName() {
		return tagName;
	}

	/**
	 * ����tag
	 * Sets the tag name.
	 * 
	 * @param tagName the tag name to set
	 */

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * ��ȡtext
	 * Returns the text content.
	 * 
	 * @return the text content
	 */

	public String getText() {
		return text;
	}

	/**
	 * ����text
	 * Sets the text content.
	 * 
	 * @param textContent the text content to set
	 */
	
	public void setTextContent(String textContent) {
		this.text = textContent;
	}

	/**
	 * ��ȡ��Ȼ��������
	 * Returns the value for the specified attribute.
	 * 
	 * @return the value for the specified attribute
	 */

	public String getAttribute(String attributeName) {
		if (attributeName != null){
			return this.attributes.get(attributeName);
		}
		
		return null;
	}

	/**
	 * ���ö�������
	 * Sets the attributes.
	 * 
	 * @param attributes the attributes to set
	 */
	
	public void setAttributes(Hashtable<String,String> attributes) {
		this.attributes = attributes;
	}

}