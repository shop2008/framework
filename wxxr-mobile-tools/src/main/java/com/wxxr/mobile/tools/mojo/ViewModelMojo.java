package com.wxxr.mobile.tools.mojo;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;


@Mojo(name="gen_viewmodel", defaultPhase=LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution=ResolutionScope.COMPILE, threadSafe=false)
public class ViewModelMojo extends AbstractViewMojo {

    /**
     * Location of the view model annotated source files.
     */
    @Parameter(property="view.model.sourceDirectory", defaultValue="${project.basedir}/src/main/java", required=true)
    private File sourceDirectory;

    /**
     * Location of the generated source files.
     * @parameter expression="${view.model.outputDirectory}" default-value="${project.build.directory}/generated-sources/viewmodel"
     * @required
     */
    @Parameter(property="view.model.outputDirectory", defaultValue="${project.build.directory}/generated-sources/viewmodel", required=true)
    private File outputDirectory;

    @Override
    protected String getGoalDescription() {
        return "gen_viewmodel";
    }

    @Override
    protected File getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    protected File getSourceDirectory() {
        return sourceDirectory;
    }

    @Override
    protected String getSourcePath() {
        return StringUtils.join(this.project.getCompileSourceRoots(), File.pathSeparatorChar);
    }

    @Override
    protected void addSourceRoot(final String path) {
        project.addCompileSourceRoot(path);
    }
}
