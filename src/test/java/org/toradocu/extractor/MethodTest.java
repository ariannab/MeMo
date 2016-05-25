package org.toradocu.extractor;

import static org.junit.Assert.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.toradocu.extractor.Method.Builder;

import com.google.gson.Gson;

public class MethodTest {
	
	@Test
	public void testToString() {
		Method method = new Method.Builder("compute", new Parameter("java.lang.String[]", "array")).build();
		assertThat(method.toString(), is("compute(java.lang.String[] array)"));
		
		method = new Method.Builder("compute").build();
		assertThat(method.toString(), is("compute()"));
	}
	
	@Test
	public void testEquals() {
		Builder methodBuilder = new Method.Builder("compute", new Parameter("java.lang.String[]", "array"));
		methodBuilder.tag(new ThrowsTag("java.lang.NullPointerException", "if the array is empty"));
		Method method1 = methodBuilder.build();

		methodBuilder = new Method.Builder("compute", new Parameter("java.lang.String[]", "array"));
		methodBuilder.tag(new ThrowsTag("java.lang.NullPointerException", "if the array is empty"));
		Method method2 = methodBuilder.build();
		
		methodBuilder = new Method.Builder("foo", new Parameter("java.lang.String[]", "array"));
		methodBuilder.tag(new ThrowsTag("java.lang.NullPointerException", "if the array is empty"));
		Method method3 = methodBuilder.build();

		assertTrue(method1.equals(method2));
		assertFalse(method1.equals(method3));
	}

	@Test
	public void testAdd() {
		Builder methodBuilder = new Method.Builder("compute", new Parameter("java.lang.String[]", "array"));
		methodBuilder.tag(new ThrowsTag("java.lang.NullPointerException", "if the array is empty"));
		methodBuilder.tag(new ThrowsTag("java.lang.NullPointerException", "if the array is empty"));
		Method method1 = methodBuilder.build();
		
		List<ThrowsTag> tags = method1.tags();
		assertThat(tags.size(), is(1));
		assertThat(tags.get(0), is(new ThrowsTag("java.lang.NullPointerException", "if the array is empty")));
	}

	@Test
	public void testJSon() {
		Builder methodBuilder = new Method.Builder("compute", new Parameter("java.lang.String[]", "array"));
		methodBuilder.tag(new ThrowsTag("java.lang.NullPointerException", "if the array is empty"));
		Method method1 = methodBuilder.build();

		String json = new Gson().toJson(method1);
		Method method2 = new Gson().fromJson(json, Method.class);
		assertEquals(method1, method2);
	}

}
