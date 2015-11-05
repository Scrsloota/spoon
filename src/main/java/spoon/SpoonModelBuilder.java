/*
 * Spoon - http://spoon.gforge.inria.fr/
 * Copyright (C) 2006 INRIA Futurs <renaud.pawlak@inria.fr>
 *
 * This software is governed by the CeCILL-C License under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-C license as
 * circulated by CEA, CNRS and INRIA at http://www.cecill.info.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the CeCILL-C License for more details.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 */

package spoon;

import spoon.compiler.SpoonResource;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Responsible for building a spoon model from Java source code.
 *
 * <p>
 * The Spoon model (see {@link Factory} is built from input sources given as
 * files. Use {@link #build()} to create the Spoon model.
 * Once the model is built and stored in the factory, it
 * can be processed by using a {@link #instantiateAndProcess(List)}.
 * </p>
 *
 * <p>
 * Create an instance of the default implementation of the Spoon compiler by
 * using {@link spoon.Launcher#createCompiler()}. For example:
 * </p>
 */
public interface SpoonModelBuilder {

	/**
	 * Adds a file/directory to be built. By default, the files could be Java
	 * source files or Jar files. Directories are processed recursively.
	 *
	 * @param source
	 * 		file or directory to add
	 */
	void addInputSource(File source);

	/**
	 * Sets the output directory for binary generated.
	 *
	 * @param binaryOutputDirectory
	 * 		{@link File} for binary output directory.
	 */
	void setBinaryOutputDirectory(File binaryOutputDirectory);

	/**
	 * Gets the binary output directory of the compiler.
	 */
	File getBinaryOutputDirectory();

	/**
	 * Sets the output directory for source generated.
	 *
	 * @param outputDirectory
	 * 		{@link File} for output directory.
	 */
	void setSourceOutputDirectory(File outputDirectory);

	/**
	 * Gets the output directory of this compiler.
	 */
	File getSourceOutputDirectory();

	/**
	 * Adds a file/directory (as a CtResource) to be built. By default, the
	 * files could be Java source files or Jar files. Directories are processed
	 * recursively.
	 *
	 * @param source
	 * 		file or directory to add
	 */
	void addInputSource(SpoonResource source);

	/**
	 * Gets all the files/directories given as input sources to this builder
	 * (see {@link #addInputSource(File)}).
	 */
	Set<File> getInputSources();

	/**
	 * Adds a file/directory to be used to build templates. By default, the
	 * files should be Java source files or Jar files containing the sources.
	 * Directories are processed recursively. Templates are set apart from the
	 * program to be processed for logical reasons. However, if a template was
	 * needed to be processed, it could be added as an input source.
	 *
	 * @param source
	 * 		file or directory to add
	 */
	void addTemplateSource(File source);

	/**
	 * Adds a file/directory (as a CtResource) to be used to build templates. By
	 * default, the files should be Java source files or Jar files containing
	 * the sources. Directories are processed recursively. Templates are set
	 * apart from the program to be processed for logical reasons. However, if a
	 * template was needed to be processed, it could be added as an input
	 * source.
	 *
	 * @param source
	 * 		file or directory to add
	 */
	void addTemplateSource(SpoonResource source);

	/**
	 * Gets all the files/directories given as template sources to this builder
	 * (see {@link #addTemplateSource(File)}).
	 */
	Set<File> getTemplateSources();

	void addInputSources(List<SpoonResource> resources);

	void addTemplateSources(List<SpoonResource> resources);

	/**
	 * Builds the program's model with this compiler's factory and stores the
	 * result into this factory. Note that this method should only be used once
	 * on a given factory.
	 *
	 * @return true if the Java was successfully compiled with the core Java
	 * compiler, false if some errors were encountered while compiling
	 * @throws spoon.SpoonException
	 * 		when a building problem occurs
	 * @see #getSourceClasspath()
	 * @see #getTemplateClasspath()
	 */
	boolean build();

	/**
	 * Processes the Java model with the given processors.
	 * @see #instantiateAndProcess(List)
	 */
	@Deprecated
	void process(List<String> processorTypes);

	/**
	 * Takes a list of fully qualified name processors and instantiates them to process
	 * the Java model.
	 */
	void instantiateAndProcess(List<String> processors);

	/**
	 * Processes the Java model with the given processors.
	 */
	void process(Collection<Processor<? extends CtElement>> processors);

	/**
	 * Generates the source code associated to the classes stored in this
	 * compiler's factory. The source code is generated in the directory given
	 * by {@link #getSourceOutputDirectory()}.
	 *
	 * @param outputType
	 * 		the output method
	 */
	void generateProcessedSourceFiles(OutputType outputType);

	/**
	 * Generates the bytecode associated to the classes stored in this
	 * compiler's factory. The bytecode is generated in the directory given by
	 * {@link #getBinaryOutputDirectory()}.
	 *
	 * @see #getSourceClasspath()
	 */
	boolean compile();

	/**
	 * Generates the bytecode by compiling the input sources. The bytecode is
	 * generated in the directory given by {@link #getBinaryOutputDirectory()}.
	 *
	 * @see #getSourceClasspath()
	 */
	boolean compileInputSources();

	/**
	 * Gets the classpath that is used to build/compile the input sources.
	 *
	 * @see #compileInputSources()
	 * @see #build()
	 * @see #compile()
	 */
	String[] getSourceClasspath();

	/**
	 * Sets the classpath that is used to build/compile the input sources.
	 *
	 * Each element of the array is either a jar file or a folder containing bytecode files.
	 */
	void setSourceClasspath(String... classpath);

	/**
	 * Gets the classpath that is used to build the template sources.
	 *
	 * See {@link #setSourceClasspath} for the meaning of the returned string.
	 */
	String[] getTemplateClasspath();

	/**
	 * Sets the classpath that is used to build the template sources.
	 */
	void setTemplateClasspath(String... classpath);

	/**
	 * Sets this compiler to optimize the model building process by ignoring
	 * files that has not be modified since the latest source code generation.
	 */
	void setBuildOnlyOutdatedFiles(boolean buildOnlyOutdatedFiles);

	/**
	 * When {@link #setBuildOnlyOutdatedFiles(boolean)} is true, adds a resource
	 * to the forced-to-be-built list. All the files added here will be build
	 * even if no changes are detected on the file system. This list has no
	 * impacts if @link #setBuildOnlyOutdatedFiles(boolean)} is false.
	 */
	void forceBuild(SpoonResource source);

	/**
	 * Sets the encoding to use when different from the system encoding.
	 */
	void setEncoding(String encoding);

	/**
	 * Gets the encoding used by this compiler. Null means that it uses the
	 * system encoding.
	 */
	String getEncoding();

	/**
	 * Returns the working factory
	 */
	Factory getFactory();

}
