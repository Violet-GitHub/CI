package com.robotium.solo;

import java.lang.reflect.Field;

/**
 * �������������
 * A reflection utility class.  
 * 
 * @author Per-Erik Bergman, bergman@uncle.se
 * 
 */

class Reflect {
	private Object object;

	/**
	 * ���캯������ֹ�����ֵ
	 * Constructs this object 
	 * 
	 * @param object the object to reflect on
	 */
	
	public Reflect(Object object) {
		// �����ֵ���쳣
		if (object == null)
			throw new IllegalArgumentException("Object can not be null.");
		this.object = object;
	}

	/**
	 * ��ȡ��Ӧ�����ֶ�
	 * Get a field from the object 
	 * 
	 * @param name the name of the field
	 * 
	 * @return a field reference
	 */
	
	public FieldRf field(String name) {
		return new FieldRf(object, name);
	}

	/**
	 * ����һ���ֶ�������
	 * A field reference.  
	 */
	public class FieldRf {
		// ��Ӧ�� Class
		private Class<?> clazz;
		// ��ȡ�ֶ����ԵĶ���
		private Object object;
		// ������
		private String name;

		/**���캯��
		 * 
		 * Constructs this object 
		 * 
		 * @param object the object to reflect on
		 * @param name the name of the field
		 */
		
		public FieldRf(Object object, String name) {
			this.object = object;
			this.name = name;
		}

		/**
		 * ����ָ��class���͵Ķ���
		 * Constructs this object 
		 * 
		 * @param outclazz the output type
		 *
		 * @return <T> T
		 */
		
		public <T> T out(Class<T> outclazz) {
			Field field = getField();
			Object obj = getValue(field);
			return outclazz.cast(obj);
		}

		/**
		 * �����ֶε�ֵ
		 * Set a value to a field 
		 * 
		 * @param value the value to set
		 */
		
		public void in(Object value) {
			Field field = getField();
			try {
				// ��������Ϊ�����ֵ
				field.set(object, value);
				// ��Ч�����쳣
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				// Ȩ���쳣
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		/**
		 * ����class���ͣ������ض�����
		 * Set the class type 
		 * 
		 * @param clazz the type
		 *
		 * @return a field reference
		 */
		
		public FieldRf type(Class<?> clazz) {
			this.clazz = clazz;
			return this;
		}

		// ��ȡ�ֶ�
		private Field getField() {
			//  ��δ����class���ͣ���ôʹ�ö�������class��Ϊclass����
			if (clazz == null) {
				clazz = object.getClass();
			}
			// ��ȡnameִ�е������ֶ�
			Field field = null;
			try {
				field = clazz.getDeclaredField(name);
				// �ֶ���������Ϊ���и�ֵ
				field.setAccessible(true);
			} catch (NoSuchFieldException ignored) {}
			return field;
		}
		// ��ȡ�ֶ�����ֵ
		private Object getValue(Field field) {
			// ����ֶ�Ϊnull��ô����null
			if (field == null) {
				return null;
			}
			// ��ȡ�ֶζ�Ӧ��ֵ����������
			Object obj = null;
			try {
				obj = field.get(object);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return obj;
		}
	}

}