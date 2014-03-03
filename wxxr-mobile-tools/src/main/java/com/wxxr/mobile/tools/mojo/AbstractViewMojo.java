package com.wxxr.mobile.tools.mojo;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;



public abstract class AbstractViewMojo extends AbstractMojo {

    /**
     * Specifies whether the delombok generation should be skipped.
     */
    @Parameter(property="view.model.skip", defaultValue="false", required=true)
    protected boolean skip;

    /**
     * Encoding.
     */
    @Parameter(property="view.model.encoding", defaultValue="${project.build.sourceEncoding}", required=true)
    protected String encoding;

    /**
     * Verbose flag.  Print the name of each file as it is being delombok-ed.
     */
    @Parameter(property="view.model.verbose", defaultValue="false", required=true)
    protected boolean verbose;
 
    /**
     * Add output directory flag.  Adds the output directory to the Maven build path.
     */
    @Parameter(property="view.model.addOutputDirectory", defaultValue="true", required=true)
    protected boolean addOutputDirectory;

    /**
     * Formatting preferences.
     */
    @Parameter
    protected Map<String, String> formatPreferences;

    /**
     * The Maven project to act upon.
     */
    @Component
    protected MavenProject project;

    /**
     * The plugin dependencies.
     */
    @Parameter(property="plugin.artifacts", required=true, readonly=true)
    private List<Artifact> pluginArtifacts;

    protected abstract String getGoalDescription ();

    protected abstract File getOutputDirectory();

    protected abstract File getSourceDirectory();

    protected abstract String getSourcePath();

    protected abstract void addSourceRoot(String path);

    @Override
    public void execute() throws MojoExecutionException {
        final Log logger = getLog();
        assert null != logger;

        final String goal = getGoalDescription();
        logger.debug("Starting " + goal);
        final File outputDirectory = getOutputDirectory();
        logger.debug("outputDirectory: " + outputDirectory);
        final File sourceDirectory = getSourceDirectory();
        logger.debug("sourceDirectory: " + sourceDirectory);
        final String sourcePath = getSourcePath();
        logger.debug("sourcePath: " + sourcePath);

        if (this.skip) {
            logger.warn("Skipping " + goal);
        } else if (sourceDirectory.exists()) {
            // Build a classPath for delombok...
            final StringBuilder classPathBuilder = new StringBuilder();
            for (final Object artifact : project.getArtifacts()) {
                classPathBuilder.append(((Artifact)artifact).getFile()).append(File.pathSeparatorChar);
            }
            for (final Artifact artifact : pluginArtifacts) {
                classPathBuilder.append(artifact.getFile()).append(File.pathSeparatorChar);
            }
            final String classPath = classPathBuilder.toString();
            logger.debug("classpath: " + classPath);
            final ViewModelGenerator generator = new ViewModelGenerator();
            generator.setVerbose(this.verbose);
            generator.setClasspath(classPath);

            if (StringUtils.isNotBlank(this.encoding)) {
                try {
                    generator.setCharset(this.encoding);
                } catch (final UnsupportedCharsetException e) {
                    logger.error("The encoding parameter is invalid; Please check!", e);
                    throw new MojoExecutionException("Unknown charset: " + this.encoding, e);
                }
            } else {
                logger.warn("No encoding specified; using default: " + Charset.defaultCharset());
            }

//            if (null != formatPreferences && !formatPreferences.isEmpty()) {
//                try {
//                    // Construct a list array just like the command-line option...
//                    final List<String> formatOptions = new ArrayList(formatPreferences.size());
//                    for (final Map.Entry<String, String> entry : formatPreferences.entrySet()) {
//                        final String key = entry.getKey();
//                        // "pretty" is an exception -- it has no value...
//                        formatOptions.add( "pretty".equalsIgnoreCase(key) ? key : (key + ':' + entry.getValue()) );
//                    }
//                    generator.setFormatPreferences(generator.formatOptionsToMap(formatOptions));
//                } catch (final InvalidFormatOptionException e) {
//                    logger.error("The formatPreferences parameter is invalid; Please check!", e);
//                    throw new MojoExecutionException("Invalid formatPreferences: " + this.formatPreferences, e);
//                }
//            }

            try {
                generator.setOutput(outputDirectory);
                generator.setSourcepath(getSourcePath());
                generator.addDirectory(sourceDirectory);
                generator.process();
                logger.info(goal + " complete.");
                
                if (this.addOutputDirectory) {
                    // adding generated sources to Maven project
                    addSourceRoot(outputDirectory.getCanonicalPath());
                }
            } catch (final Exception e) {
                logger.error("Unable to delombok!", e);
                throw new MojoExecutionException("I/O problem during delombok", e);
            } finally {
            	generator.clear();
            }
        } else {
            logger.warn("Skipping " + goal + "; no source to process.");
        }
    }
}
