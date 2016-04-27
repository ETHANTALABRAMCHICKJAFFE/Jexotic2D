package editor;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.AccessControlContext;

public class JavaClassLoader extends URLClassLoader{

	public JavaClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
		// TODO Auto-generated constructor stub
	}


	
	public JavaClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		// TODO Auto-generated constructor stub
	}

	public JavaClassLoader(URL[] urls) {
		super(urls);
		// TODO Auto-generated constructor stub
	}
	

	public void addFile(URL url){
		addURL(url);
	}
}
