package com.publiccms.common.ide;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.publiccms.common.ide.jobs.CovertJob;
import com.publiccms.common.ide.jobs.DeCovertJob;

public class PublicCMSNature implements IProjectNature {
    public static final String NATURE_ID = "com.publiccms.common.ide.cms.nature";
    private IProject project;

    public void configure() throws CoreException {
        new CovertJob("Convert to Public CMS project", project, new String[] { NATURE_ID, "org.eclipse.jdt.core.javanature" })
                .schedule();
    }

    public void deconfigure() throws CoreException {
        new DeCovertJob("Remove Public CMS Template project", project, NATURE_ID).schedule();
    }

    public IProject getProject() {
        return this.project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }
}