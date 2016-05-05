package com.publiccms.common.ide.jobs;

import static com.publiccms.common.ide.tools.NatureUtils.remove;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class DeCovertJob extends Job {
    private IProject project;
    private String natureId;

    public DeCovertJob(String name, IProject project, String natureId) {
        super(name);
        this.project = project;
        this.natureId = natureId;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            return remove(project, monitor, natureId);
        } catch (CoreException e) {
            return new Status(IStatus.ERROR, "unknown", 1, e.getMessage(), e);
        }
    }
}