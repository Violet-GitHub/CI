package com.robotium.solo;

/**
 * �ṩ������WebView�еĶ���ʹ��
 * 
 * Used in conjunction with the web methods. Examples are By.id(String id) and By.cssSelector(String selector).
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

public abstract class By {

	/**
	 *  ���� Id���� ���� WebView������WebElement�Ĳ���
	 * Select a WebElement by its id.
	 * 
	 * @param id the id of the web element	
	 * @return the Id object
	 */

	public static By id(final String id) {
		return new Id(id); 

	}

	/**
	 * ����Xpath��������WebView������WebElement�Ĳ���
	 * Select a WebElement by its xpath.
	 * 
	 * @param xpath the xpath of the web element
	 * @return the Xpath object
	 */

	public static By xpath(final String xpath) {
		return new Xpath(xpath); 

	}

	/**
	 * ����һ��CssSelector��������WebView������WebElement�Ĳ���
	 * Select a WebElement by its css selector.
	 * 
	 * @param selectors the css selector of the web element
	 * @return the CssSelector object
	 */

	public static By cssSelector(final String selectors) {
		return new CssSelector(selectors); 

	}

	/**
	 * ����һ�� Name��������WebView�����е�WebElement����
	 * Select a WebElement by its name.
	 * 
	 * @param name the name of the web element
	 * @return the Name object
	 */

	public static By name(final String name) {
		return new Name(name); 

	}

	/**
	 * ����һ�� ClassName��������WebView�����е�WebElement����
	 * Select a WebElement by its class name.
	 * 
	 * @param className the class name of the web element
	 * @return the ClassName object
	 */

	public static By className(final String className) {
		return new ClassName(className); 

	}

	/**
	 * ����һ�� Text��������WebView�����е�WebElement����
	 * Select a WebElement by its text content.
	 * 
	 * @param textContent the text content of the web element
	 * @return the TextContent object
	 */

	public static By textContent(final String textContent) {
		return new Text(textContent); 

	}
	
	/**
	 * ����һ�� TagName��������WebView�����е�WebElement����
	 * Select a WebElement by its tag name.
	 * 
	 * @param tagName the tag name of the web element
	 * @return the TagName object
	 */

	public static By tagName(final String tagName) {
		return new TagName(tagName); 

	}

	/**
	 * ���෽������o������ʵ��
	 * Returns the value. 
	 * 
	 * @return the value
	 */
	
	public String getValue(){
		return "";
	}

	// Id����̳�By����WebView�����еİ���id����WebElement
	static class Id extends By {
		private final String id;

		public Id(String id) {
			this.id = id;
		}

		@Override
		public String getValue(){
			return id;
		}
	}
	// Xpath����̳�By����WebView�����еİ���Xpath����WebElement
	static class Xpath extends By {
		private final String xpath;

		public Xpath(String xpath) {
			this.xpath = xpath;
		}

		@Override
		public String getValue(){
			return xpath;
		}
	}
	// CssSelector����̳�By����WebView�����еİ���CssSelector����WebElement
	static class CssSelector extends By {
		private final String selector;

		public CssSelector(String selector) {
			this.selector = selector;
		}

		@Override
		public String getValue(){
			return selector;
		}
	}
	// Name����̳�By����WebView�����еİ���Name����WebElement
	static class Name extends By {
		private final String name;

		public Name(String name) {
			this.name = name;
		}

		@Override
		public String getValue(){
			return name;
		}
	}
	// ClassName����̳�By����WebView�����еİ���ClassName����WebElement
	static class ClassName extends By {
		private final String className;

		public ClassName(String className) {
			this.className = className;
		}

		@Override
		public String getValue(){
			return className;
		}
	}
	// Text����̳�By����WebView�����еİ���Text����WebElement
	static class Text extends By {
		private final String textContent;

		public Text(String textContent) {
			this.textContent = textContent;
		}

		@Override
		public String getValue(){
			return textContent;
		}
	}
	// TagName����̳�By����WebView�����еİ���TagName����WebElement
	static class TagName extends By {
		private final String tagName;
		
		public TagName(String tagName){
			this.tagName = tagName;
		}
		
		@Override
		public String getValue(){
			return tagName;
		}
	}
}