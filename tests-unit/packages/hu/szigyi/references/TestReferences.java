package hu.szigyi.references;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class TestReferences {
	
	public class RefClass {
		
	}
	
	public List<String> occupyMemory() {
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			String string = "";
			for (int j = 0; j < 1000; j++) {
				char[] alpha = "abcdefghijklmnopqrstuvwxyz".toCharArray();
				Random r = new Random();
				int index = r.nextInt(26);
				char c = alpha[index];
				string += c;
			}
			strings.add(string);
		}
		return strings;
	}

	@Test
	public void whenStrongReference_thenGCLeaves() {
		RefClass strongRef = new RefClass();
		
		System.gc();
		
		assertThat(strongRef, notNullValue());
	}

	@Test
	public void softReference_whenThereIsEnoughMemory_thenGCLeaves() {
		SoftReference<RefClass> softRef = new SoftReference<TestReferences.RefClass>(new RefClass());
		
		System.gc();
		
		assertThat(softRef.get(), notNullValue());
	}

	@Test
	public void softReference_whenThereIsNotEnoughMemory_thenGCReclaims() {
		SoftReference<RefClass> softRef = new SoftReference<TestReferences.RefClass>(new RefClass());
		
		occupyMemory();
		System.gc();
		
		assertThat(softRef.get(), nullValue());
	}

	@Test
	public void whenWeakReference_thenGCReclaims() {
		WeakReference<RefClass> weakRef = new WeakReference<>(new RefClass());
		
		System.gc();

		// to reclaims the instance it takes some GC cycle based on the example. But why?
//		int counter = 0;
//		while (true) {
//			counter++;
//			System.gc();
//			if (null == weakRef.get()) {
//				System.out.println("GC cycle: " + counter);
//				break;
//			}
//		}
		
		assertThat(weakRef.get(), nullValue());
	}

	@Test
	public void whenPhantomReference_thenGCReclaims() {
		ReferenceQueue<RefClass> queue = new ReferenceQueue<>();
		PhantomReference<RefClass> phantomRef = new PhantomReference<>(new RefClass(), queue);
		
		System.gc();
		
		assertThat(queue.poll(), nullValue());
		assertThat(phantomRef.get(), nullValue());
	}
}
