/**
 * 
 */
package com.wxxr.mobile.tools.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import com.sun.tools.javac.comp.Todo;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;
import com.wxxr.mobile.core.tools.MainAnnotationProcessor;

/**
 * @author neillin
 *
 */
public class ViewModelGenerator {
	private Charset charset = Charset.defaultCharset();
	private boolean finished = false;
	private Context context = new Context() {
		
		@Override
		public void clear(){
			if(finished){
				super.clear();
			}
		}
	};
	private Writer presetWriter;
	
	public void setWriter(Writer writer) {
		this.presetWriter = writer;
	}
	
	private PrintStream feedback = System.err;
	private boolean verbose;
	private boolean noCopy;
	private String classpath, sourcepath, bootclasspath;
	private LinkedHashMap<File, File> fileToBase = new LinkedHashMap<File, File>();
	private List<File> filesToParse = new ArrayList<File>();
	
	/** If null, output to standard out. */
	private File output = null;
	

	public void setCharset(String charsetName) throws UnsupportedCharsetException {
		if (charsetName == null) {
			charset = Charset.defaultCharset();
			return;
		}
		charset = Charset.forName(charsetName);
	}
	
	public void setDiagnosticsListener(DiagnosticListener<JavaFileObject> diagnostics) {
		if (diagnostics != null) context.put(DiagnosticListener.class, diagnostics);
	}
		
	public void setFeedback(PrintStream feedback) {
		this.feedback = feedback;
	}
	
	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	
	public void setSourcepath(String sourcepath) {
		this.sourcepath = sourcepath;
	}
	
	public void setBootclasspath(String bootclasspath) {
		this.bootclasspath = bootclasspath;
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public void setNoCopy(boolean noCopy) {
		this.noCopy = noCopy;
	}
	
	public void setOutput(File dir) {
		if (dir.isFile() || (!dir.isDirectory() && dir.getName().endsWith(".java"))) 
			throw new IllegalArgumentException("output destination must be an directory");
		output = dir;
	}
	
	public void setOutputToStandardOut() {
		this.output = null;
	}
	
	public void addDirectory(File base) throws IOException {
		addDirectory0(false, base, "", 0);
	}
	
	public void addDirectory1(boolean copy, File base, String name) throws IOException {
		File f = new File(base, name);
		if (f.isFile()) {
			String extension = getExtension(f);
			if (extension.equals("java")) addFile(base, name);
//			else if (extension.equals("class")) skipClass(name);
//			else copy(copy, base, name);
		} else if (!f.exists()) {
			feedback.printf("Skipping %s because it does not exist.\n", canonical(f));
		} else if (!f.isDirectory()) {
			feedback.printf("Skipping %s because it is a special file type.\n", canonical(f));
		}
	}
	
	private void addDirectory0(boolean inHiddenDir, File base, String suffix, int loop) throws IOException {
		File dir = suffix.isEmpty() ? base : new File(base, suffix);
		
		if (dir.isDirectory()) {
			boolean thisDirIsHidden = !inHiddenDir && new File(canonical(dir)).getName().startsWith(".");
			if (loop >= 100) {
				feedback.printf("Over 100 subdirectories? I'm guessing there's a loop in your directory structure. Skipping: %s\n", suffix);
			} else {
				File[] list = dir.listFiles();
				if (list.length > 0) {
					if (thisDirIsHidden && !noCopy && output != null) {
						feedback.printf("Only processing java files (not copying non-java files) in %s because it's a hidden directory.\n", canonical(dir));
					}
					for (File f : list) {
						addDirectory0(inHiddenDir || thisDirIsHidden, base, suffix + (suffix.isEmpty() ? "" : File.separator) + f.getName(), loop + 1);
					}
				} else {
					if (!thisDirIsHidden && !noCopy && !inHiddenDir && output != null && !suffix.isEmpty()) {
						File emptyDir = new File(output, suffix);
						emptyDir.mkdirs();
						if (verbose) feedback.printf("Creating empty directory: %s\n", canonical(emptyDir));
					}
				}
			}
		} else {
			addDirectory1(!inHiddenDir && !noCopy, base, suffix);
		}
	}
	
	private void skipClass(String fileName) {
		if (verbose) feedback.printf("Skipping class file: %s\n", fileName);
	}
	
	private void copy(boolean copy, File base, String fileName) throws IOException {
		if (output == null) {
			feedback.printf("Skipping resource file: %s\n", fileName);
			return;
		}
		
		if (!copy) {
			if (verbose) feedback.printf("Skipping resource file: %s\n", fileName);
			return;
		}
		
		if (verbose) feedback.printf("Copying resource file: %s\n", fileName);
		byte[] b = new byte[65536];
		File inFile = new File(base, fileName);
		FileInputStream in = new FileInputStream(inFile);
		try {
			File outFile = new File(output, fileName);
			outFile.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(outFile);
			try {
				while (true) {
					int r = in.read(b);
					if (r == -1) break;
					out.write(b, 0, r);
				}
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}
	
	public void addFile(File base, String fileName) throws IOException {
		if (output != null && canonical(base).equals(canonical(output))) throw new IOException(
				"DELOMBOK: Output file and input file refer to the same filesystem location. Specify a separate path for output.");
		
		File f = new File(base, fileName);
		filesToParse.add(f);
		fileToBase.put(f, base);
	}
	
	private static <T> com.sun.tools.javac.util.List<T> toJavacList(List<T> list) {
		com.sun.tools.javac.util.List<T> out = com.sun.tools.javac.util.List.nil();
		ListIterator<T> li = list.listIterator(list.size());
		while (li.hasPrevious()) out = out.prepend(li.previous());
		return out;
	}
	
	public boolean process() throws Exception {
		Options options = Options.instance(context);
		options.put(OptionName.ENCODING, charset.name());
		if (classpath != null) options.put(OptionName.CLASSPATH, classpath);
		if (sourcepath != null) options.put(OptionName.SOURCEPATH, sourcepath);
		if (bootclasspath != null) options.put(OptionName.BOOTCLASSPATH, bootclasspath);
		if (output != null) options.put(OptionName.S, output.getCanonicalPath());
		options.put("compilePolicy", "check");
		
		JavaCompiler compiler = JavaCompiler.instance(context);
		
		List<JCCompilationUnit> roots = new ArrayList<JCCompilationUnit>();
		Map<JCCompilationUnit, File> baseMap = new IdentityHashMap<JCCompilationUnit, File>();
		
		compiler.initProcessAnnotations(Collections.singleton(new MainAnnotationProcessor()));
		
		for (File fileToParse : filesToParse) {
			@SuppressWarnings("deprecation") JCCompilationUnit unit = compiler.parse(fileToParse.getAbsolutePath());
			baseMap.put(unit, fileToBase.get(fileToParse));
			roots.add(unit);
		}
		if (compiler.errorCount() > 0) {
			// At least one parse error. No point continuing (a real javac run doesn't either).
			return false;
		}
		
//		for (JCCompilationUnit unit : roots) {
//			catcher.setComments(unit, new DocCommentIntegrator().integrate(catcher.getComments(unit), unit));
//		}
		
		com.sun.tools.javac.util.List<JCCompilationUnit> trees = compiler.enterTrees(toJavacList(roots));
		
		JavaCompiler delegate = compiler.processAnnotations(trees);
		
//		Object care = callAttributeMethodOnJavaCompiler(delegate, delegate.todo);
//		
//		callFlowMethodOnJavaCompiler(delegate, care);
//		for (JCCompilationUnit unit : roots) {
////			CompileResult result = new CompileResult(catcher.getComments(unit), unit, force || options.isChanged(unit));
////			if (verbose) feedback.printf("File: %s [%s]\n", unit.sourcefile.getName(), result.isChanged() ? "delomboked" : "unchanged");
//			Writer rawWriter;
//			if (presetWriter != null) rawWriter = presetWriter;
//			else if (output == null) rawWriter = createStandardOutWriter();
//			else rawWriter = createFileWriter(output, baseMap.get(unit), unit.sourcefile.toUri());
//			BufferedWriter writer = new BufferedWriter(rawWriter);
//			try {
//				unit.accept(new Pretty(writer,true));
//			} finally {
//				if (output != null) {
//					writer.close();
//				} else {
//					writer.flush();
//				}
//			}
//		}
		delegate.close();
		
		return true;
	}
	
	private static Method attributeMethod;
	/** Method is needed because the call signature has changed between javac6 and javac7; no matter what we compile against, using delombok in the other means VerifyErrors. */
	private static Object callAttributeMethodOnJavaCompiler(JavaCompiler compiler, Todo arg) throws Exception {
		if (attributeMethod == null) {
			try {
				attributeMethod = JavaCompiler.class.getDeclaredMethod("attribute", java.util.Queue.class);
			} catch (NoSuchMethodException e) {
				try {
					attributeMethod = JavaCompiler.class.getDeclaredMethod("attribute", com.sun.tools.javac.util.ListBuffer.class);
				} catch (NoSuchMethodException e2) {
					throw e2;
				}
			}
		}
		return attributeMethod.invoke(compiler, arg);
	}
	
	private static Method flowMethod;
	/** Method is needed because the call signature has changed between javac6 and javac7; no matter what we compile against, using delombok in the other means VerifyErrors. */
	private static void callFlowMethodOnJavaCompiler(JavaCompiler compiler, Object arg) throws Exception {
		if (flowMethod == null) {
			try {
				flowMethod = JavaCompiler.class.getDeclaredMethod("flow", java.util.Queue.class);
			} catch (NoSuchMethodException e) {
				try {
					flowMethod = JavaCompiler.class.getDeclaredMethod("flow", com.sun.tools.javac.util.List.class);
				} catch (NoSuchMethodException e2) {
					throw e2;
				}
			}
		}
		flowMethod.invoke(compiler, arg);
	}
	
	private static String canonical(File dir) {
		try {
			return dir.getCanonicalPath();
		} catch (Exception e) {
			return dir.getAbsolutePath();
		}
	}
	
	private static String getExtension(File dir) {
		String name = dir.getName();
		int idx = name.lastIndexOf('.');
		return idx == -1 ? "" : name.substring(idx+1);
	}
	
	private Writer createFileWriter(File outBase, File inBase, URI file) throws IOException {
		URI base = inBase.toURI();
		URI relative = base.relativize(base.resolve(file));
		File outFile;
		if (relative.isAbsolute()) {
			outFile = new File(outBase, new File(relative).getName());
		} else {
			outFile = new File(outBase, relative.getPath());
		}
		
		outFile.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(outFile);
		return createUnicodeEscapeWriter(out);
	}
	
	private Writer createStandardOutWriter() {
		return createUnicodeEscapeWriter(System.out);
	}
	
	private Writer createUnicodeEscapeWriter(OutputStream out) {
		return new UnicodeEscapeWriter(new OutputStreamWriter(out, charset), charset);
	}
	
	public void clear() {
		this.finished = true;
		this.context.clear();
	}
}
