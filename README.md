# JavaReferenceTypes
Test how the Reference types are behave in Java.
   * StrongReference
   * SoftReference
   * WeakReference
   * PhantomReference

I created the tests based on this DZone article. [https://dzone.com/articles/java-different-types-of-references](https://dzone.com/articles/java-different-types-of-references)

## How to run it
The tests are basic jUnit tests with Hamcrest. THe important part is that you should set the heap memory very low.

Add the following agrument to the JVM when you run the tests.
> -Xms1m -Xmx2m

Not Necessary but helps if you also turn on the GC's log.
> -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps

## The test cases
```java
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
	//...
	
	assertThat(weakRef.get(), nullValue());
}
```
