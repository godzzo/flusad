package org.godzzo.flusad.test.common;

import java.util.Stack;

import org.junit.Test;

public class TestStack {
	
	
	@Test
	public void invoke() {
		Stack<String> stack = new Stack<String>();
		
		stack.push("1");
		stack.push("2");
		stack.push("3");
		stack.push("4");
		stack.push("5");
		stack.push("6");
		
		System.out.println("0: "+stack.get(0));
		System.out.println("SIZE: "+stack.size());
		System.out.println("?Last: "+stack.get(stack.size()-2));
	}
}
